package com.www.com.www.domain;

import java.time.Instant;


public class Notification {

  // 알림 아이디
  public Long id;
  // 유저 아이디
  public Long userId;
  // 알림 종류
  public NotificationType type;
  // 알림 대상이 실제 이벤트 발생 시간
  public Instant occurredAt;
  // 알림 생성 날짜
  public Instant createdAt;
  // 알림 업데이트 날짜
  public Instant lastUpdateAt;
  // 알림 삭제 날자
  public Instant deletedAt;

  public Notification(Long id, Long userId, NotificationType type, Instant occurredAt,
      Instant createdAt, Instant lastUpdateAt, Instant deletedAt) {
    this.id = id;
    this.userId = userId;
    this.type = type;
    this.occurredAt = occurredAt;
    this.createdAt = createdAt;
    this.lastUpdateAt = lastUpdateAt;
    this.deletedAt = deletedAt;
  }

  @Override
  public String toString() {
    return "Notification{" +
        "id=" + id +
        ", userId=" + userId +
        ", type=" + type +
        ", occurredAt=" + occurredAt +
        ", createdAt=" + createdAt +
        ", lastUpdateAt=" + lastUpdateAt +
        ", deletedAt=" + deletedAt +
        '}';
  }
}
