package com.www.pointservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "point_balances")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class PointBalance {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id; // 잔액 id

  @Column(nullable = false,unique = true)
  private Long userId; // 사용자 id

  @Column(nullable = false)
  private Long balance = 0L; // 적립금 잔액 * default = 0원

  @Version
  private Long version = 0L; // Optimistic Lock을 위한 버전 관리

  @CreatedDate
  @Column(nullable = false,updatable = false)
  private LocalDateTime createdAt; // 생성 날짜

  @LastModifiedDate // 마지막 저장
  @Column(nullable = false)
  private LocalDateTime updatedAt; // 수정 날자


  @Builder
  public PointBalance(Long userId, Long balance) {
    this.userId = userId;
    this.balance = balance != null ? balance : 0L;  // null 체크 추가
    this.version = 0L;
  }

  // 포인트 적립
  public void addBalance(Long amount) {
    // 적립금이 없을때
    if (amount <= 0) {
      throw new IllegalArgumentException("amount must be greater than 0");
    }

    // null 체크
    if (this.balance == null) {
      this.balance = 0L;
    }

    // 포인트 적립
    this.balance += amount;
  }


  // 포인트 사용
  public void subtractBalance(Long amount) {
    // 적립금이 없을때
    if (amount <= 0) {
      throw new IllegalArgumentException("amount must be greater than 0");
    }
    // null 체크
    if (this.balance == null) {
      this.balance = 0L;
    }

    // 포인트 차감
    this.balance -= amount;
  }

  // 포인트 조회 (갱신)
  public void setBalance(Long balance) {
    if (balance == null || balance < 0) {
      throw new IllegalArgumentException("balance must be greater than 0");
    }
    // 포인트 갱신
    this.balance = balance;
  }


}
