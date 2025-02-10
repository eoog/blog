package com.www.timesaleservice.service.v3;

import com.www.timesaleservice.domain.TimeSale;
import com.www.timesaleservice.dto.TimeSaleDto;
import com.www.timesaleservice.service.v2.TimeSaleRedisService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AsyncTimeSaleService {

  private final TimeSaleRedisService timeSaleRedisService;
  private final TimeSaleProducer timeSaleProducer;
  private final RedissonClient redissonClient;
  private static final String RESULT_PREFIX = "purchase-result:";

  // 타임세일 등록
  // redis 를 사용해서 사용
  public TimeSale createTimeSale(TimeSaleDto.CreateReqeust reqeust) {
    return timeSaleRedisService.createTimeSale(reqeust);
  }

  // 단건 타임세일 아이템 조회
  public TimeSale getTimeSale(Long timeSaleId) {
    return timeSaleRedisService.getTimeSale(timeSaleId);
  }

  // 현재 진행중인 타임세일
  public Page<TimeSale> getOngoingTimeSales(Pageable pageable) {
    return timeSaleRedisService.getOngoingTimeSales(pageable);
  }

  // 타임세일 아이템 구매 // >> kafka 전송
  public String purchaseTimeSale(Long timeSaleId , TimeSaleDto.PurchaseRequest request) {
    // 구매 요청을 kafka 로 전송하고 요청 ID 반환
    return timeSaleProducer.sendPurchaseReqeust(timeSaleId,request.getUserId(),request.getQuantity());
  }

  // 타임세일 구매 결과
  public TimeSaleDto.AsyncPurchaseResponse getPurchaseResult(Long timeSaleId, String requestId) {
    RBucket<String> bucket = redissonClient.getBucket(RESULT_PREFIX + requestId);
    String result = bucket.get();
    String status = result != null ? result : "PENDING";

    // 대기 순서 조회
    Integer queuePosition = null;
    Long totalWaiting = 0L;

    if ("PENDING".equals(status)) {
      queuePosition = timeSaleProducer.getQueuePosition(timeSaleId, requestId);
      totalWaiting = timeSaleProducer.getTotalWaiting(timeSaleId);
    }

    return TimeSaleDto.AsyncPurchaseResponse.builder()
        .requestId(requestId)
        .status(status)
        .queuePosition(queuePosition)
        .totalWaiting(totalWaiting)
        .build();
  }
}
