package com.www.pointservicebatch.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "points")
public class Point {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(nullable = false)
  private Long amount;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private PointType type;

  private String description;

  @Column(name = "balance_snapshot", nullable = false)
  private Long balanceSnapshot;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Builder
  public Point(Long userId, Long amount, PointType type, String description, Long balanceSnapshot, LocalDateTime createdAt, LocalDateTime updatedAt) {
    this.userId = userId;
    this.amount = amount;
    this.type = type;
    this.description = description;
    this.balanceSnapshot = balanceSnapshot;
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }
}
