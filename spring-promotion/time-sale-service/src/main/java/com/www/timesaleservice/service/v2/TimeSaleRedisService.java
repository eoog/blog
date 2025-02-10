package com.www.timesaleservice.service.v2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.www.timesaleservice.domain.Product;
import com.www.timesaleservice.domain.TimeSale;
import com.www.timesaleservice.domain.TimeSaleOrder;
import com.www.timesaleservice.domain.TimeSaleStatus;
import com.www.timesaleservice.dto.TimeSaleDto;
import com.www.timesaleservice.exception.TimeSaleException;
import com.www.timesaleservice.repository.ProductRepository;
import com.www.timesaleservice.repository.TimeSaleOrderRepository;
import com.www.timesaleservice.repository.TimeSaleRepository;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TimeSaleRedisService {
    private static final String TIME_SALE_KEY = "time-sale:";
    private static final String TIME_SALE_LOCK = "time-sale-lock:";
    private static final long WAIT_TIME = 3L;
    private static final long LEASE_TIME = 3L;

    private final TimeSaleRepository timeSaleRepository;
    private final ProductRepository productRepository;
    private final TimeSaleOrderRepository timeSaleOrderRepository;
    private final RedissonClient redissonClient;
    private final ObjectMapper objectMapper;


    // 타임 세일 생성
    public TimeSale createTimeSale(TimeSaleDto.CreateReqeust reqeust) {
        // 상품 조회
        Product product = productRepository.findById(reqeust.getProductId())
            .orElseThrow(() -> new IllegalArgumentException("상품이 존재 하지 않습니다."));

        TimeSale timeSale = TimeSale.builder()
            .product(product)
            .quantity(reqeust.getQuantity())
            .remainingQuantity(reqeust.getQuantity())
            .discountPrice(reqeust.getDiscountPrice())
            .startAt(reqeust.getStartAt())
            .endAt(reqeust.getEndAt())
            .status(TimeSaleStatus.ACTIVE)
            .build();

        // 상품 등록
        TimeSale savedTimeSale = timeSaleRepository.save(timeSale);

        // redis 에 데이터 자장
        saveToRedis(savedTimeSale);
        return savedTimeSale;
    }

    // 진행중인 타임세일
    @Transactional(readOnly = true)
    public Page<TimeSale> getOngoingTimeSales(Pageable pageable) {
        LocalDateTime now = LocalDateTime.now();
        return timeSaleRepository.findAllByStartAtBeforeAndEndAtAfterAndStatus(now,TimeSaleStatus.ACTIVE,pageable);
    }

    // 지정한 타임세일 조회 단건 조회
    @Transactional(readOnly = true)
    public TimeSale getTimeSale(Long timeSaleId) {
        return getFromRedis(timeSaleId);
    }


    // 타임세일 제품 구매
    @Transactional
    public TimeSale purchaseTimeSale(Long timeSaleId , TimeSaleDto.PurchaseRequest request) {
        RLock lock = redissonClient.getLock(TIME_SALE_LOCK + timeSaleId);
        if (lock == null) {
            throw new TimeSaleException("락 생성 오류");
        }

        boolean isLocked = false;

        try {
            // 락 생성
            isLocked = lock.tryLock(WAIT_TIME, LEASE_TIME, TimeUnit.SECONDS);
            if (!isLocked) {
                throw new TimeSaleException("Failed to acquire lock");
            }

            // 타엠세일 가져오기
            TimeSale timeSale = getFromRedis(timeSaleId);

            // 수량 감소
            timeSale.purchase(request.getQuantity());

            // 감소후 저장
            timeSale = timeSaleRepository.save(timeSale);

            // 타임세일 주문서 생성
            TimeSaleOrder order = TimeSaleOrder.builder()
                .userId(request.getUserId())
                .timeSale(timeSale)
                .quantity(request.getQuantity())
                .discountPrice(timeSale.getDiscountPrice())
                .build();

            // 주문서 저장
            timeSaleOrderRepository.save(order);

            // redis 에 저장
            saveToRedis(timeSale);

            return timeSale;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new TimeSaleException("Lock interrupted");
        } finally {
            if (isLocked) {
                try {
                    lock.unlock();
                } catch (Exception e) {
                    log.error("Failed to unlock", e);
                }
            }
        }
    }


    // redis 데이터 저장
    public void saveToRedis(TimeSale timeSale) {
        try {
            String json = objectMapper.writeValueAsString(timeSale);
            RBucket<Object> bucket = redissonClient.getBucket(TIME_SALE_KEY + timeSale.getId());
            bucket.set(json);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize TimeSale: {}", timeSale.getId(), e);
        } catch (Exception e) {
            log.error("Failed to save TimeSale to Redis: {}", timeSale.getId(), e);
        }
    }

    // redis 에서 데이터 조회 없으면 DB 에서 데이터 조회
    public TimeSale getFromRedis(Long timeSaleId) {
        RBucket<String> bucket = redissonClient.getBucket(TIME_SALE_KEY + timeSaleId);
        String json = bucket.get();

        try {
            if (json != null) {
                return objectMapper.readValue(json, TimeSale.class);
            }

            // Redis에 없으면 DB에서 조회
            TimeSale timeSale = timeSaleRepository.findById(timeSaleId)
                .orElseThrow(() -> new IllegalArgumentException("TimeSale not found"));

            // Redis에 저장
            saveToRedis(timeSale);

            return timeSale;
        } catch (JsonProcessingException e) {
            throw new TimeSaleException("Failed to parse TimeSale from Redis", e);
        }
    }

}
