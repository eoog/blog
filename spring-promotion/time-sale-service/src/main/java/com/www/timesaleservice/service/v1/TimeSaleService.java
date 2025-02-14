package com.www.timesaleservice.service.v1;

import com.www.timesaleservice.aop.TimeSaleMetered;
import com.www.timesaleservice.domain.Product;
import com.www.timesaleservice.domain.TimeSale;
import com.www.timesaleservice.domain.TimeSaleOrder;
import com.www.timesaleservice.domain.TimeSaleStatus;
import com.www.timesaleservice.dto.TimeSaleDto;
import com.www.timesaleservice.repository.ProductRepository;
import com.www.timesaleservice.repository.TimeSaleOrderRepository;
import com.www.timesaleservice.repository.TimeSaleRepository;
import java.sql.Time;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TimeSaleService {

  private final TimeSaleRepository timeSaleRepository;
  private final ProductRepository productRepository;
  private final TimeSaleOrderRepository timeSaleOrderRepository;

  // 타임세일 생성
  @Transactional
  public TimeSale crateTimeSale(TimeSaleDto.CreateReqeust reqeust) {
    // 상품 조회
    Product product = productRepository.findById(reqeust.getProductId())
        .orElseThrow(() -> new IllegalArgumentException("상품이 없습니다."));

    // 상품 검증 , 수량 , 할인 , 시작 ,마감 시간
    validateTimeSale(reqeust.getQuantity(),reqeust.getDiscountPrice(),reqeust.getStartAt(),reqeust.getEndAt());

    TimeSale timeSale = TimeSale.builder()
        .product(product)
        .quantity(reqeust.getQuantity())
        .discountPrice(reqeust.getDiscountPrice())
        .remainingQuantity(reqeust.getQuantity())
        .startAt(reqeust.getStartAt())
        .endAt(reqeust.getEndAt())
        .status(TimeSaleStatus.ACTIVE)
        .build();

    return timeSaleRepository.save(timeSale);
  }

  // 타임세일 조회
  @Transactional(readOnly = true)
  public TimeSale getTimeSale(Long timeSaleId) {
    return timeSaleRepository.findById(timeSaleId)
        .orElseThrow(() -> new IllegalArgumentException("타임세일을 찾을수 없습니다"));
  }

  // 타임세일 현재 진행중인것 모든 거 조회
  @Transactional(readOnly = true)
  public Page<TimeSale> getOngoingTimeSales(Pageable pageable) {
    LocalDateTime now = LocalDateTime.now();
    return timeSaleRepository.findAllByStartAtBeforeAndEndAtAfterAndStatus(now,TimeSaleStatus.ACTIVE,pageable);
  }

  // 타임세일 구매
  @Transactional
  @TimeSaleMetered(version = "v1")
  public TimeSale purchaseTimeSale(Long timeSaleId , TimeSaleDto.PurchaseRequest request) {
    TimeSale timeSale = timeSaleRepository.findById(timeSaleId)
        .orElseThrow(() -> new IllegalArgumentException("타임세일을 찾을수 없습니다"));

    // 타임세일 제품 차감
    timeSale.purchase(request.getQuantity());
    timeSaleRepository.save(timeSale);

    // 주문서 작성
    TimeSaleOrder order = TimeSaleOrder.builder()
        .userId(request.getUserId())
        .timeSale(timeSale)
        .quantity(request.getQuantity())
        .discountPrice(timeSale.getDiscountPrice())
        .build();

    // 주문서 저장
    TimeSaleOrder saveOrder = timeSaleOrderRepository.save(order);

    // 주문 성공 상태 변경
    saveOrder.complete();

    return timeSale;
  }

  // 타임세일 검증
  private void validateTimeSale(Long quantity, Long discountPrice, LocalDateTime startAt, LocalDateTime endAt) {
    if (startAt.isAfter(endAt)) {
      throw new IllegalArgumentException("시작일이 마감일보다 늦을순 없습니다.");
    }
    if (quantity <= 0) {
      throw new IllegalArgumentException("수량은 0이상이여야함..양수");
    }
    if (discountPrice <= 0) {
      throw new IllegalArgumentException("할인금액은 양수 여ㅑ야함..");
    }
  }
}
