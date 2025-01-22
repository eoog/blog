package com.www.couponservice.dto.v3;

import com.www.couponservice.domain.Coupon;
import com.www.couponservice.domain.CouponPolicy;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CouponDto {

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class IssueRequest {

    private Long couponPolicyId;
  }

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class UseRequest {

    private Long orderId;
  }

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class IssueMessage {

    private Long policyId;
    private Long userId;
  }

  @Getter
  @Builder
  public static class CouponResponse {

    private Long id;
    private Long userId;
    private String couponCode;
    private CouponPolicy.DiscountType discountType;
    private int discountValue;
    private int minimumOrderAmount;
    private int maximumDiscountAmount;
    private LocalDateTime validFrom;
    private LocalDateTime validUntil;
    private boolean used;
    private Long orderId;
    private LocalDateTime usedAt;

    public static CouponDto.CouponResponse from(Coupon coupon) {
      CouponPolicy policy = coupon.getCouponPolicy();
      return CouponDto.CouponResponse.builder()
          .id(coupon.getId())
          .userId(coupon.getUserId())
          .couponCode(coupon.getCouponCode())
          .discountType(policy.getDiscountType())
          .discountValue(policy.getDiscountValue())
          .minimumOrderAmount(policy.getMinimumOrderAmount())
          .maximumDiscountAmount(policy.getMaximumDiscountAmount())
          .validFrom(policy.getStartTime())
          .validUntil(policy.getEndTime())
          .used(coupon.isUsed())
          .orderId(coupon.getOrderId())
          .usedAt(coupon.getUsedAt())
          .build();
    }
  }
}
