package com.www.pointservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "points")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Point {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id; // 트랜잭션 id

  @Column(nullable = false)
  private Long userId; // 사용자 id

  @Column(nullable = false)
  private Long amount; 	// 적립/차감 금액

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PointType type; // 적립금 타입 ( 충전 , 사용 , 취소 )

  @Column(nullable = false)
  private String description; // 트랜잭션 내용

  @Column(nullable = false)
  private Long balanceSnapshot; // 트랜잭션 시점의 잔액 , PointBalance 적립금 잔액

  @Version
  private Long version; // Optimistic Lock을 위한 버전 관리

  @ManyToOne
  @JoinColumn(name = "point_balance_id")
  private PointBalance pointBalance; // 조인 한명의 사용자는 한개의 pointBalance 가짐

  @CreatedDate
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt; // 생성 날짜

  @LastModifiedDate
  @Column(nullable = false)
  private LocalDateTime updatedAt; // 수정 날짜

  @Builder
  public Point(Long userId, Long amount, PointType type, String description, Long balanceSnapshot, PointBalance pointBalance) {
    this.userId = userId;
    this.amount = amount;
    this.type = type;
    this.description = description;
    this.balanceSnapshot = balanceSnapshot;
    this.pointBalance = pointBalance;
    this.version = 0L;
  }

}
