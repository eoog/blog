package com.www.response;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.www.domain.NotificationType;
import com.www.service.dto.ConvertedCommentNotification;
import com.www.service.dto.ConvertedFollowNotification;
import com.www.service.dto.ConvertedLikeNotification;
import com.www.service.dto.ConvertedNotification;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "유저 알림 응답")
@JsonSubTypes({
    @JsonSubTypes.Type(value = CommentUserNotificationResponse.class),
    @JsonSubTypes.Type(value = LikeUserNotificationResponse.class),
    @JsonSubTypes.Type(value = FollowUserNotificationResponse.class)
})
public abstract class UserNotificationResponse {

  @Schema(description = "알림 ID")
  private String id;

  @Schema(description = "알림 타입")
  private NotificationType type;

  @Schema(description = "알림 이벤트 발생 시간")
  private Instant occurredAt;

  public static UserNotificationResponse of(ConvertedNotification notification) {
    switch (notification.getType()) {
      case COMMENT -> {
        return CommentUserNotificationResponse.of((ConvertedCommentNotification) notification);
      }
      case LIKE -> {
        return LikeUserNotificationResponse.of((ConvertedLikeNotification) notification);
      }
      case FOLLOW -> {
        return FollowUserNotificationResponse.of((ConvertedFollowNotification) notification);
      }
      default -> throw new IllegalArgumentException(
          "Unsupported notification type: " + notification.getType());
    }
  }
}
