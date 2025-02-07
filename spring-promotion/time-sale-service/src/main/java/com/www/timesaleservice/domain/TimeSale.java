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
import jakarta.persistence.Version;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "time_sales")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class TimeSale {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id" , nullable = false)
  private Product product; // 상품

  @Column(nullable = false)
  private Long quantity; //  전체 수량

  @Column(nullable = false)
  private Long remainingQuantity; // 남은 수량

  @Column(nullable = false)
  private Long discountPrice; // 할인 금액

  @Column(nullable = false)
  private LocalDateTime startAt; // 시작일시

  @Column(nullable = false)
  private LocalDateTime endAt; // 마감일시

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TimeSaleStatus status = TimeSaleStatus.ACTIVE; // 상태

  @Version
  private Long version = 0L; // 버전

  @CreatedDate
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(nullable = false)
  private LocalDateTime updatedAt;

  @Builder
  public TimeSale(Long id, Product product, Long quantity, Long remainingQuantity, Long discountPrice, LocalDateTime startAt, LocalDateTime endAt, TimeSaleStatus status) {
    this.id = id;
    this.product = product;
    this.quantity = quantity;
    this.remainingQuantity = remainingQuantity;
    this.discountPrice = discountPrice;
    this.startAt = startAt;
    this.endAt = endAt;
    this.status = status;
    this.version = 0L;
  }

  // 타임세일 실행중인지
  public boolean isActive() {
    return status == TimeSaleStatus.ACTIVE;
  }

  // 타임세일제품 구매
  // 수량 제거
  public void purchase(Long quantity) {
    validatePurchase(quantity);
    this.remainingQuantity -= quantity;
  }

  private void validatePurchase(Long quantity) {
    validateStatus();
    validateQuantity(quantity);
    validatePeriod();
  }

  // 타임세일 진행중인지 재고 있는지 확인
  private void validateStatus() {
    if (status != TimeSaleStatus.ACTIVE) {
      throw new IllegalStateException("타임세일중이 아닙니다.");
    }
  }

  // 타임세일 제품 재고 확인
  private void validateQuantity(Long quantity) {
    if (remainingQuantity < quantity) {
      throw new IllegalStateException("재고가 없습니다.");
    }
  }

  // 타임세일 기간 확인하기
  private void validatePeriod() {
    LocalDateTime now = LocalDateTime.now();
    if (now.isBefore(startAt) || now.isAfter(endAt)) {
      throw new IllegalStateException("타임세일 기간이 아닙니다");
    }
  }

  // 상품 조회
  // 지연로딩 Lazy 로딩 관련 처리
  public Product getProduct() {
    if (this.product instanceof HibernateProxy) {
      return (Product) ((HibernateProxy) this.product).getHibernateLazyInitializer().getImplementation();
    }
    return this.product;
  }



}
