package com.www.back.entity;

import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 유저 알림 내역 개인
 */

@Document(collection = "userNotificationHistory")
@Getter
@Setter
public class UserNotificationHistory {

  @Id
  private String id;

  private String title;

  private String content;

  private Long noticeId;

  private Long userId;

  private Boolean isRead = false; // 읽음여부

  private LocalDateTime createdDate = LocalDateTime.now();

  private LocalDateTime updatedDate = LocalDateTime.now();

}
