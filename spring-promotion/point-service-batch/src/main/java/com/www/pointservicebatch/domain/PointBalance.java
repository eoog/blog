package com.www.pointservicebatch.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "point_balances")
public class PointBalance {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_id", nullable = false, unique = true)
  private Long userId;

  @Column(nullable = false)
  private Long balance;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Builder
  public PointBalance(Long userId, Long balance, LocalDateTime createdAt, LocalDateTime updatedAt) {
    this.userId = userId;
    this.balance = balance;
    this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
    this.updatedAt = updatedAt != null ? updatedAt : LocalDateTime.now();
  }
}
