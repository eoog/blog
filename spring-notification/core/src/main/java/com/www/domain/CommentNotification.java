package com.www.domain;

import java.time.Instant;
import lombok.Getter;
import org.springframework.data.annotation.TypeAlias;

@Getter
@TypeAlias("CommentNotification") // 역직렬화
public class CommentNotification extends Notification {

  private final long postId;
  private final long writerId;
  private final String comment;
  private final long commentId;

  public CommentNotification(String id, long userId, NotificationType type, Instant occurredAt,
      Instant createdAt,
      Instant lastUpdatedAt, Instant deletedAt, long postId, long writerId, String comment,
      long commentId) {
    super(id, userId, type, occurredAt, createdAt, lastUpdatedAt, deletedAt);
    this.postId = postId;
    this.writerId = writerId;
    this.comment = comment;
    this.commentId = commentId;
  }
}
