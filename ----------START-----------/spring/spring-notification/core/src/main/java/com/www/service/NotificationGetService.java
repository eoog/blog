package com.www.service;

import com.www.domain.Notification;
import com.www.domain.NotificationType;
import com.www.repository.NotificationRepository;
import java.time.Instant;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationGetService {

  private final NotificationRepository repository;

  public NotificationGetService(NotificationRepository repository) {
    this.repository = repository;
  }

  public Optional<Notification> getNotificationByTypeAndCommentId(NotificationType type,
      long commentId) {
    return repository.findByTypeAndCommentId(type, commentId);
  }

  public Optional<Notification> getNotificationByTypeAndPostId(NotificationType type, long postId) {
    return repository.findByTypeAndPostId(type, postId);
  }

  public Optional<Notification> getNotificationByTypeAndUserIdAndFollowerId(NotificationType type,
      long userId, long followerId) {
    return repository.findByTypeAndUserIdAndFollowerId(type, userId, followerId);
  }

  public Instant getLatestUpdatedAt(long userId) {
    Optional<Notification> notification = repository.findFirstByUserIdOrderByLastUpdatedAtDesc(
        userId);

    if (notification.isEmpty()) {
      return null;
    }

    return notification.get().getLastUpdatedAt();
  }
}

