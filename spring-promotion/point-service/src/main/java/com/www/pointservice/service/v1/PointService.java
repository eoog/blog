package com.www.pointservice.service.v1;

import com.www.pointservice.aop.PointMetered;
import com.www.pointservice.domain.Point;
import com.www.pointservice.domain.PointBalance;
import com.www.pointservice.domain.PointType;
import com.www.pointservice.repository.PointBalanceRepository;
import com.www.pointservice.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PointService {
    private final PointBalanceRepository pointBalanceRepository;
    private final PointRepository pointRepository;

  // 포인트 적립
  // orElse => null 이면 호출을 안함
  // 쓰기락 Lock 이 걸려있음
  @Transactional(isolation = Isolation.REPEATABLE_READ) //
  @PointMetered(version = "v1")
  public Point earnPoints(Long userId, Long amount, String description) {

    // 포인트 잔액 조회
    // orElseGet 은 해당값이 null 이면 호출된다
    PointBalance pointBalance = pointBalanceRepository.findByUserId(userId)
        .orElseGet(() -> PointBalance.builder()
            .userId(userId)
            .balance(0L)
            .build());

    // 포인트 충전
    pointBalance.addBalance(amount);

    // DB 저장
    pointBalance = pointBalanceRepository.save(pointBalance);

    // 포인트 사용 내역 생성
    Point point = Point.builder()
        .userId(userId)
        .amount(amount)
        .type(PointType.EARNED)
        .description(description)
        .balanceSnapshot(pointBalance.getBalance()) // 적립금 잔액
        .pointBalance(pointBalance)
        .build();

    return pointRepository.save(point);
  }

  // 포인트 사용
  @Transactional
  @PointMetered(version = "v1")
  public Point usePoints(Long userId, Long amount, String description) {
    // 포인트 잔액 조회
    PointBalance pointBalance = pointBalanceRepository.findByUserId(userId)
        .orElseThrow(() -> new IllegalArgumentException("적립금이 없습니다"));

    // 포인트 차감
    pointBalance.subtractBalance(amount);
    pointBalanceRepository.save(pointBalance);

    // 포인트 사용 내역
    Point point = Point.builder()
        .userId(userId)
        .amount(amount)
        .type(PointType.USED)
        .description(description)
        .balanceSnapshot(pointBalance.getBalance()) // 적릭금 잔액
        .pointBalance(pointBalance)
        .build();

    return pointRepository.save(point);
  }

  // 포인트 취소
  // 트랜잭션이 실행중에 다른 트랜잭션이 실행을 못함.
  @Transactional(isolation = Isolation.REPEATABLE_READ)
  public Point cancelPoints(Long pointId, String description) {

    // 포인트 확인
    Point orginalPoint = pointRepository.findById(pointId)
        .orElseThrow(() -> new IllegalCallerException("포인트를 찾을수 없습니다."));

    if (orginalPoint.getType() == PointType.CANCELED) {
      throw new IllegalArgumentException("이미 취소 처리가 됬습니다");
    }

    // 포인트 잔액 조회
    PointBalance pointBalance = pointBalanceRepository.findByUserId(orginalPoint.getUserId())
        .orElseThrow(() -> new IllegalArgumentException("유저를 찾을수 없습니다"));

    // 적립금 확인
    Long currentBalance = pointBalance.getBalance();
    // 새로운 적립금
    Long newBalance;

    // 포인트 타입에 따라 취소 처리
    // 충전 내역 or 사용 내역
    if (orginalPoint.getType() == PointType.EARNED) {
      // 현재 적립금보다 취소할 적립금액이 많으면 에러
      if (currentBalance < orginalPoint.getAmount()) {
        throw new IllegalArgumentException("적립된 포인트를 취소할 수 없습니다: 잔액이 부족합니다.");
      }
      // 잔액 차감
      newBalance = currentBalance - orginalPoint.getAmount();
    } else if (orginalPoint.getType() == PointType.USED) {
      // 잔액 차감
      newBalance = currentBalance - orginalPoint.getAmount();
    } else {
      throw new IllegalArgumentException("취소할 내역이 없습니다.");
    }

    // 잔액 차감후 남은 적립금 잔액 저장
    pointBalance.setBalance(newBalance);
    pointBalanceRepository.save(pointBalance);

    // 포인트 취소 내역 저장
    Point cancelPort = Point.builder()
        .userId(orginalPoint.getUserId())
        .amount(orginalPoint.getAmount())
        .type(PointType.CANCELED)
        .description(description)
        .balanceSnapshot(pointBalance.getBalance())
        .pointBalance(pointBalance)
        .build();

    return pointRepository.save(cancelPort);

  }

  // 포인트 조회
  @Transactional(readOnly = true)
  public Long getBalance(Long userId) {
    return pointBalanceRepository.findByUserId(userId)
        .map(pointBalance -> pointBalance.getBalance())
        .orElse(0L);
  }

  // 포인트 내역 조회
  public Page<Point> getPointHistory(Long userId, Pageable pageable) {
    return pointRepository.findByUserIdOrderByCreatedAtDesc(userId,pageable);
  }
}
