package com.www.timesaleservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Table(name = "time_sale_orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class TimeSaleOrder {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long userId; // 유저 아이디

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "time_sale_id", nullable = false)
  private TimeSale timeSale; // 타임세일 상품

  @Column(nullable = false)
  private Long quantity; // 주문 수량

  @Column(nullable = false)
  private Long discountPrice; // 할인금액

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private OrderStatus status; // 주문상태

  @CreatedDate
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt; // 구매일

  @LastModifiedDate
  @Column(nullable = false)
  private LocalDateTime updatedAt; // 수정일

  @Builder
  public TimeSaleOrder(Long id, Long userId, TimeSale timeSale, Long quantity, Long discountPrice) {
    this.id = id;
    this.userId = userId;
    this.timeSale = timeSale;
    this.quantity = quantity;
    this.discountPrice = discountPrice;
    this.status = OrderStatus.PENDING;
  }

  // 주문 성공
  public void complete() {
    this.status = OrderStatus.COMPLETED;
  }

  // 주문 실패
  public void fail() {
    this.status = OrderStatus.FAILED;
  }
}
