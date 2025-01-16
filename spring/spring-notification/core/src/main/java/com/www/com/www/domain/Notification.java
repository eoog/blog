package com.www.com.www.domain;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Setter
@Getter
@AllArgsConstructor
@Document("notifications") // 몽고DB 어떤 컬렉션
public abstract class Notification {

  // 알림 아이디
  @Field(targetType = FieldType.STRING)
  public String id;
  // 유저 아이디
  public Long userId;
  // 알림 종류
  public NotificationType type;
  // 알림 대상이 실제 이벤트 발생 시간
  public Instant occurredAt;
  // 알림 생성 날짜
  public Instant createdAt;
  // 알림 업데이트 날짜
  public Instant lastUpdatedAt;
  // 알림 삭제 날자
  public Instant deletedAt;


}
