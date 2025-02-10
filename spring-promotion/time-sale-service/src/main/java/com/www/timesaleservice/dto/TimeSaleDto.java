package com.www.timesaleservice.dto;

import com.www.timesaleservice.domain.TimeSale;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TimeSaleDto {

  @Getter
  @Builder
  public static class CreateReqeust {
    @NotNull(message = "상품 아이디는 필수 입니다.")
    private Long productId;

    @NotNull(message = "수량은 필수 입니다")
    @Positive(message = "수량은 마이너스가 될수 없습니다")
    private Long quantity;

    @NotNull(message = "할인 가격 은 필수 입니다")
    @Positive(message = "할인 금액 될수 없습니다")
    private Long discountPrice;

    @NotNull(message = "시작일은 필수")
    @FutureOrPresent(message = "해당 시간이 미래 시간인지 검사한다.\n"
        + "\n"
        + "OrPresent가 붙은 것은 현재 또는 미래 시간인지 검사한다")
    private LocalDateTime startAt;

    @NotNull(message = "마감일 필수")
    @Future(message = "미래 시간이여야한다..")
    private LocalDateTime endAt;
  }

  @Getter
  @Builder
  public static class PurchaseRequest {
    @NotNull(message = "userId must not be null")
    private Long userId;

    @NotNull(message = "quantity must not be null")
    @Min(value = 1, message = "quantity must be greater than 0")
    private Long quantity;
  }

  @Getter
  @Builder
  public static class Response {
    private Long id;
    private Long productId;
    private Long quantity;
    private Long remainingQuantity;
    private Long discountPrice;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private LocalDateTime createdAt;
    private String status;

    public static Response from(TimeSale timeSale) {
      return Response.builder()
          .id(timeSale.getId())
          .productId(timeSale.getProduct().getId())
          .quantity(timeSale.getQuantity())
          .remainingQuantity(timeSale.getRemainingQuantity())
          .discountPrice(timeSale.getDiscountPrice())
          .startAt(timeSale.getStartAt())
          .endAt(timeSale.getEndAt())
          .createdAt(timeSale.getCreatedAt())
          .status(timeSale.getStatus().name())
          .build();
    }
  }

  @Getter
  @Builder
  public static class PurchaseResponse {
    private Long timeSaleId;
    private Long userId;
    private Long productId;
    private Long quantity;
    private Long discountPrice;
    private LocalDateTime purchasedAt;
    private Long totalWaiting;

    public static PurchaseResponse from(TimeSale timeSale, Long userId, Long quantity) {
      return PurchaseResponse.builder()
          .timeSaleId(timeSale.getId())
          .userId(userId)
          .productId(timeSale.getProduct().getId())
          .quantity(quantity)
          .discountPrice(timeSale.getDiscountPrice())
          .purchasedAt(LocalDateTime.now())
          .build();
    }
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class AsyncPurchaseResponse {
    private String requestId;
    private String status;
    private Integer queuePosition;
    private Long totalWaiting;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class PurchaseResponse2 {
    private String requestId;
    private String status;
  }
}
