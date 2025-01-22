package com.www.couponservice.domain;

import com.www.couponservice.exception.CouponAlreadyUsedException;
import com.www.couponservice.exception.CouponExpiredException;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon {

  /**
   * 2025.01.03 쿠폰 상태
   */
  public enum Status {
    AVAILABLE,    // 사용 가능
    USED,         // 사용됨
    EXPIRED,      // 만료됨
    CANCELED      // 취소됨
  }

  /**
   * 2025.01.03 쿠폰 id
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * 2025.01.03 쿠폰 정책
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "coupon_policy_id")
  private CouponPolicy couponPolicy;

  /**
   * 2025.01.03 사용자 id
   */
  private Long userId;

  /**
   * 2025.01.03 쿠폰 코드
   */
  private String couponCode;

  /**
   * 2025.01.03 쿠폰 상태
   */
  @Enumerated(EnumType.STRING)
  private Status status;

  /**
   * 2025.01.03 주문 id
   */
  private Long orderId;

  /**
   * 2025.01.03 쿠폰 사용 시간
   */
  private LocalDateTime usedAt;
  private LocalDateTime createdAt;

  @Builder
  public Coupon(Long id, CouponPolicy couponPolicy, Long userId, String couponCode) {
    this.id = id;
    this.couponPolicy = couponPolicy;
    this.userId = userId;
    this.couponCode = couponCode;
    this.status = Status.AVAILABLE;
  }

  /**
   * 2025.01.03 쿠폰 사용
   */
  public void use(Long orderId) {
    if (status == Status.USED) {
      throw new CouponAlreadyUsedException("이미 사용된 쿠폰입니다.");
    }
    if (isExpired()) {
      throw new CouponExpiredException("만료된 쿠폰입니다.");
    }
    this.status = Status.USED;
    this.orderId = orderId;
    this.usedAt = LocalDateTime.now();
  }

  /**
   * 2025.01.03 쿠폰 사용 취소
   */
  public void cancel() {
    if (status != Status.USED) {
      throw new IllegalStateException("사용되지 않은 쿠폰입니다.");
    }
    this.status = Status.CANCELED;
    this.orderId = null;
    this.usedAt = null;
  }

  /**
   * 2025.01.03 쿠폰 만료 여부
   */
  public boolean isExpired() {
    LocalDateTime now = LocalDateTime.now();
    return now.isBefore(couponPolicy.getStartTime()) || now.isAfter(couponPolicy.getEndTime());
  }

  /**
   * 2025.01.03 쿠폰 사용 여부
   */

  public boolean isUsed() {
    return status == Status.USED;
  }
}
