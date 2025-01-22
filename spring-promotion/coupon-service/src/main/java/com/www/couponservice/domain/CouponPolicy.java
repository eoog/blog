package com.www.couponservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "coupon_policies")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponPolicy {

  /**
   * 2025.01.03 정책 ID
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * 2025.01.03 정책 제목
   */
  @Column(nullable = false)
  private String name;

  /**
   * 2025.01.03 정책 설명
   */
  @Column
  private String description;

  /**
   * 2025.01.03 할인 유형
   */
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private DiscountType discountType;

  /**
   * 2025.01.03 할인 값
   */
  @Column(nullable = false)
  private Integer discountValue;

  /**
   * 2025.01.03 최소 주문 금액
   */
  @Column(nullable = false)
  private Integer minimumOrderAmount;

  /**
   * 2025.01.03 최대 할인 금액
   */
  @Column(nullable = false)
  private Integer maximumDiscountAmount;

  /**
   * 2025.01.03 쿠폰 총 발행 수량
   */
  @Column(nullable = false)
  private Integer totalQuantity;

  /**
   * 2025.01.03 발행 시작 시간
   */
  @Column(nullable = false)
  private LocalDateTime startTime;

  /**
   * 2025.01.03 발행 종료 시간
   */
  @Column(nullable = false)
  @Setter
  private LocalDateTime endTime;

  /**
   * 2025.01.03 생성 날짜
   */
  @Column(nullable = false)
  private LocalDateTime createdAt;

  /**
   * 2025.01.03 변경 날짜
   */
  @Column(nullable = false)
  private LocalDateTime updatedAt;

  /**
   * 2025.01.03 할인유형 ENUM
   */
  public enum DiscountType {
    FIXED_AMOUNT,    // 정액 할인
    PERCENTAGE      // 정률 할인
  }

  /**
   * 2025.01.03 시작전
   */
  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
  }

  
  @PreUpdate
  protected void onUpdate() {
    updatedAt = LocalDateTime.now();
  }

  /**
   * 2025.01.03 쿠폰 만료 시간
   */
  public boolean isValidPeriod() {
    LocalDateTime now = LocalDateTime.now();
    return !now.isBefore(startTime) && !now.isAfter(endTime);
  }
}
