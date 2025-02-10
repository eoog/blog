package com.www.timesaleservice.service.v3;

import com.www.timesaleservice.dto.PurchaseReqeustMessage;
import com.www.timesaleservice.dto.TimeSaleDto.PurchaseRequest;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * 타임세일 구매 요청을 처리하는 Producer
 * - Kafka를 통해 비동기로 구매 요청을 처리
 * - Redis를 사용하여 대기열 관리
 * - Redisson을 사용하여 분산 환경에서의 동시성 제어
 */

@Service
@RequiredArgsConstructor
public class TimeSaleProducer {

  private static final String TOPIC = "time-sale-requests";

  private static final String QUEUE_KEY = "time-sale-queue:";
  private static final String TOTAL_REQUESTS_KEY = "time-sale-total-requests:";
  private static final String RESULT_PREFIX = "purchase-result:";

  private final KafkaTemplate<String, PurchaseReqeustMessage> kafkaTemplate;
  private final RedissonClient redissonClient;

  /**
   * 타임세일 구매 요청을 처리
   * 1. 요청 ID 생성
   * 2. Redis에 요청 상태 저장
   * 3. 대기열에 요청 추가
   * 4. Kafka로 메시지 전송
   */
  public String sendPurchaseReqeust(Long timeSaleId , Long userId , Long quantity) {
    // 고유한 요청 ID 생성
    String reqeustId = UUID.randomUUID().toString();

    // 구매 요청 메시지 생성
    PurchaseReqeustMessage message = PurchaseReqeustMessage.builder()
        .reqeustId(reqeustId)
        .timeSaleId(timeSaleId)
        .userId(userId)
        .quantity(quantity)
        .build();

    // redis 에서 초기 상태 저장
    RBucket<String> bucket = redissonClient.getBucket(RESULT_PREFIX + reqeustId);
    bucket.set("PENDING");

    // 대기열에 추가하고 카운터 증가
    String queueKey = QUEUE_KEY + timeSaleId;
    String totalKey = TOTAL_REQUESTS_KEY + timeSaleId;

    // 대기열 추가
    RBucket<String> queueBucket = redissonClient.getBucket(queueKey);
    queueBucket.set(reqeustId);

    // 카운터 증가
    RAtomicLong totalCounter = redissonClient.getAtomicLong(totalKey);
    totalCounter.incrementAndGet();

    // Kafka로 메시지 전송
    kafkaTemplate.send(TOPIC, reqeustId, message);
    return reqeustId;
  }

  /**
   * 대기열에서 요청의 위치를 조회
   */
  public Integer getQueuePosition(Long timeSaleId, String requestId) {
    // 대기열의 큐 조회
    String queueKey = QUEUE_KEY + timeSaleId;
    RBucket<String> queueBucket = redissonClient.getBucket(queueKey);
    String queueValue = queueBucket.get();

    if (queueValue == null || queueValue.isEmpty()) {
      return null;
    }

    String[] queueValues = queueValue.split(",");
    for (int i = 0; i < queueValues.length; i++) {
      if (requestId.equals(queueValues[i])) {
        return i + 1;
      }
    }
      return null;
  }

  /**
   * 총 대기 중인 요청 수를 조회
   */
  public Long getTotalWaiting(Long timeSaleId) {
    String totalKey = TOTAL_REQUESTS_KEY + timeSaleId;
    RAtomicLong totalCounter = redissonClient.getAtomicLong(totalKey);
    return totalCounter.get();
  }

}
