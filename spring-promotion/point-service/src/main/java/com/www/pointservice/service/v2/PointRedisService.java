package com.www.pointservice.service.v2;

import com.www.pointservice.domain.Point;
import com.www.pointservice.domain.PointBalance;
import com.www.pointservice.domain.PointType;
import com.www.pointservice.repository.PointBalanceRepository;
import com.www.pointservice.repository.PointRepository;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Redis를 활용한 포인트 서비스 V2 구현
 * - Redisson 분산 락을 통한 동시성 제어
 * - Redis 캐시를 통한 성능 최적화
 */
@Service
@RequiredArgsConstructor
public class PointRedisService {
  // Redis key prefix 및 설정값
  private static final String POINT_BALANCE_MAP = "point:balance";
  private static final String POINT_LOCK_PREFIX = "point:lock:";
  private static final long LOCK_WAIT_TIME = 3L;
  private static final long LOCK_LEASE_TIME = 5L;

  private final PointRepository pointRepository;
  private final PointBalanceRepository pointBalanceRepository;
  private final RedissonClient redissonClient;

  /**
   * 포인트 적립 처리
   * 1. 분산 락 획득
   * 2. 캐시된 잔액 조회 (없으면 DB에서 조회)
   * 3. 포인트 잔액 증가
   * 4. DB 저장 및 캐시 업데이트
   * 5. 포인트 이력 저장
   */
  @Transactional
  public Point earnPoints(Long userId,Long amount , String description) {

    // 분산 락 획득 point:lock:1 , point:lock:2 등
    RLock lock = redissonClient.getLock(POINT_LOCK_PREFIX + userId);

    try {
      // 락 획득 시도
      boolean locked = lock.tryLock(LOCK_WAIT_TIME, LOCK_LEASE_TIME, TimeUnit.SECONDS);

      if (!locked) {
        // 락 획득 실패
        throw new IllegalStateException("Failed to acquire lock for user: \" + userId");
      }

      // 캐시된 잔액 조회
      Long currentBalance = getBalanceFromCache(userId);

      // 캐시된 잔액 없으면 DB 에서 조회
      if (currentBalance == null) {
        currentBalance = getBalanceFromDB(userId);

        // DB 에서 조회한 잔액 Redis 캐시 저장
        updateBalanceCache(userId,currentBalance);
      }

      // 포인트 잔액 조회
      // 포인트가 없으면 0 원
      PointBalance pointBalance = pointBalanceRepository.findByUserId(userId)
          .orElseGet(() -> {
            PointBalance pointBalance1 = PointBalance.builder()
                .userId(userId)
                .balance(0L)
                .build();
            return pointBalanceRepository.save(pointBalance1);
          });

      // 포인트 충전
      pointBalance.addBalance(amount);
      pointBalanceRepository.save(pointBalance);

      // 포인트 캐시 업데이트
      updateBalanceCache(userId , pointBalance.getBalance());

      // 포인트 이력 저장
      Point point = Point.builder()
          .userId(userId)
          .amount(amount)
          .type(PointType.EARNED)
          .description(description)
          .balanceSnapshot(pointBalance.getBalance())
          .pointBalance(pointBalance)
          .build();


      return pointRepository.save(point);

    } catch (InterruptedException e) {
      // 락 획득중 인터럽트 발생
      Thread.currentThread().interrupt();
      throw new IllegalStateException("Lock acquisition was interrupted", e);
    } finally {
      // 락 해제
      if (lock.isHeldByCurrentThread()) {
        lock.unlock();
      }
    }

  }


  /**
   * 포인트 사용 처리
   * 1. 분산 락 획득
   * 2. 캐시된 잔액 조회 (없으면 DB에서 조회)
   * 3. 잔액 체크
   * 4. 포인트 잔액 감소
   * 5. DB 저장 및 캐시 업데이트
   * 6. 포인트 이력 저장
   */
  @Transactional
  public Point usePoints(Long userId, Long amount , String description) {
    // 분산 락 획득
    RLock lock = redissonClient.getLock(POINT_LOCK_PREFIX + userId);

    try {
      // 락 획득 시도
      boolean locked = lock.tryLock(LOCK_WAIT_TIME, LOCK_LEASE_TIME, TimeUnit.SECONDS);

      // 락 획득 실패
      if (!locked) {
        throw new IllegalStateException("Failed to acquire lock for user: \" + userId");
      }

      // 캐시된 잔액 조회
      Long currentBalance = getBalanceFromCache(userId);

      // 캐시되 잔액이 없으면 DB 에서 조히
      if (currentBalance == null) {
        currentBalance = getBalanceFromDB(userId);
        // 캐시 업데이트 잔액
        updateBalanceCache(userId,currentBalance);
      }

      // 현재 적립된 잔액보다 사용하는 잔액이 더 큰경우
      // 잔액부족
      if (currentBalance < amount) {
        // 잔액부족
        throw new IllegalStateException("잔액이 부족합니다.");
      }

      // 포인트 조회
      PointBalance pointBalance = pointBalanceRepository.findByUserId(userId)
          .orElseThrow(() -> new IllegalArgumentException("유저가 존재 하지 않습니다."));

      // 포인트 잔액감소
      pointBalance.subtractBalance(amount);

      // DB 에 저장
      pointBalance = pointBalanceRepository.save(pointBalance);

      // 캐시에 잔액 업데이트
      // 차감된 잔액
      updateBalanceCache(userId,pointBalance.getBalance());

      // 포인트 이력 저장
      Point point = Point.builder()
          .userId(userId)
          .amount(amount)
          .type(PointType.USED)
          .description(description)
          .balanceSnapshot(pointBalance.getBalance())
          .pointBalance(pointBalance)
          .build();

      // 포인트 DB 저장
      return pointRepository.save(point);

    } catch (InterruptedException e) {
      // 락획득중 인터럽트 발생
       Thread.currentThread().interrupt();
      throw new IllegalStateException("Lock acquisition was interrupted", e);
    } finally {
      // 락해제
      if (lock.isHeldByCurrentThread()) {
        lock.unlock();
      }
    }
  }


  /**
   * 포인트 취소 처리
   * 1. 원본 포인트 이력 조회
   * 2. 분산 락 획득
   * 3. 취소 가능 여부 확인
   * 4. 포인트 잔액 원복 (적립 취소는 차감, 사용 취소는 증가)
   * 5. DB 저장 및 캐시 업데이트
   * 6. 취소 이력 저장
   */
  @Transactional
  public Point cancelPoints(Long pointId, String description) {
    // 원본 포인트 사용 이력 조회
    Point originalPoint = pointRepository.findById(pointId)
        .orElseThrow(() -> new IllegalArgumentException("포인트 이력을 찾을수 없습니다."));

    // 사용자 ID 조회
    Long userId = originalPoint.getUserId();

    // 분산 락 획득 및 생성
    RLock lock = redissonClient.getLock(POINT_LOCK_PREFIX + userId);

    try {

      // 락 획득 시도
      boolean locked = lock.tryLock(LOCK_WAIT_TIME, LOCK_LEASE_TIME, TimeUnit.SECONDS);

      // 락 획득 실패
      if (!locked) {
        throw new IllegalStateException("Failed to acquire lock for user: " + userId);
      }

      // 포인트가 취소 됬는지 확인
      if (originalPoint.getType() == PointType.CANCELED) {
        throw new IllegalArgumentException("이미 포인트가 취소되었습니다.");
      }

      // 포인트 잔액 조회
      PointBalance pointBalance = originalPoint.getPointBalance();

      if (originalPoint.getType() == PointType.EARNED) {
        // 충전을 한 상태면 이미 포인트 에  + 되어있는 상태이기에
        // 차감읋 해야함
        pointBalance.subtractBalance(originalPoint.getAmount());
      } else {
        // 사용한 상태
        // 플러스
        pointBalance.addBalance(originalPoint.getAmount());
      }

      // DB 잔액 업데이트 포인트
      pointBalance = pointBalanceRepository.save(pointBalance);

      // 캐시 잔액도 업데이트
      updateBalanceCache(userId, pointBalance.getBalance());

      // 취소 이력 저장
      Point point = Point.builder()
          .userId(userId)
          .amount(originalPoint.getAmount())
          .type(PointType.CANCELED)
          .description(description)
          .balanceSnapshot(pointBalance.getBalance())
          .pointBalance(pointBalance)
          .build();

      return pointRepository.save(point);

    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new IllegalStateException("Lock acquisition was interrupted", e);
    } finally {
      // 락 해제
      if (lock.isHeldByCurrentThread()) {
        lock.unlock();
      }
    }

  }


  /**
   * 포인트 잔액 조회
   * 1. 캐시에서 조회
   * 2. 캐시 없으면 DB에서 조회 후 캐시 업데이트
   */
  @Transactional(readOnly = true)
  public Long getBalance(Long userId) {

    // 캐쉬에서 잔액조회
    Long cacheBalance = getBalanceFromCache(userId);

    // 캐시에 잔액이 있으면 바로 전달
    if (cacheBalance != null) {
      return cacheBalance;
    }

    // DB 에서 포인트 잔액 조회
    Long dbBalance = getBalanceFromDB(userId);

    // 캐시에 잔액 업데이트
    updateBalanceCache(userId,dbBalance);
    return dbBalance;
  }

  /**
   * Redis 캐시에서 잔액 조회
   */
  @Transactional(readOnly = true)
  public Long getBalanceFromCache(Long userId) {
    RMap<String, Long> balanceMap = redissonClient.getMap(POINT_BALANCE_MAP);
    return balanceMap.get(String.valueOf(userId));
  }

  /**
   * DB 에서 잔액 조회
   */
  @Transactional(readOnly = true)
  public Long getBalanceFromDB(Long userId) {
    return pointBalanceRepository.findByUserId(userId)
        .map(pointBalance -> pointBalance.getBalance())
        .orElse(0L);
  }

  /**
   * Redis 캐시 잔액 업데이트
   */
  public void updateBalanceCache(Long userId, Long balance) {
    RMap<Object, Object> balanceMap = redissonClient.getMap(POINT_BALANCE_MAP);
    balanceMap.put(String.valueOf(userId), balance);
  }
}
