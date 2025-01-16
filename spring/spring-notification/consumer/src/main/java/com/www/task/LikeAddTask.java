package com.www.task;


import static com.www.domain.NotificationType.LIKE;

import com.www.client.PostClient;
import com.www.domain.LikeNotification;
import com.www.domain.Notification;
import com.www.domain.Post;
import com.www.event.LikeEvent;
import com.www.service.NotificationGetService;
import com.www.service.NotificationSaveService;
import com.www.utils.NotificationIdGenerator;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LikeAddTask {

  private final PostClient postClient;

  private final NotificationGetService getService;

  private final NotificationSaveService saveService;

  public LikeAddTask(PostClient postClient, NotificationGetService getService,
      NotificationSaveService saveService) {
    this.postClient = postClient;
    this.getService = getService;
    this.saveService = saveService;
  }

  public void processEvent(LikeEvent event) {
    Post post = postClient.getPost(event.getPostId());
    if (post == null) {
      log.error("Post is null with postId:{}", event.getPostId());
      return;
    }

    if (post.getUserId() == event.getUserId()) {
      return;
    }

    saveService.upsert(createOrUpdateNotification(post, event));
  }

  private Notification createOrUpdateNotification(Post post, LikeEvent event) {
    Optional<Notification> optionalNotification = getService.getNotificationByTypeAndPostId(LIKE,
        post.getId());

    Instant now = Instant.now();
    Instant retention = now.plus(90, ChronoUnit.DAYS);

    if (optionalNotification.isPresent()) {
      return updateNotification((LikeNotification) optionalNotification.get(), event, now,
          retention);
    } else {
      return createNotification(post, event, now, retention);
    }
  }

  private Notification updateNotification(LikeNotification notification, LikeEvent event,
      Instant now, Instant retention) {
    notification.addLiker(event.getUserId(), event.getCreatedAt(), now, retention);
    return notification;
  }

  private Notification createNotification(Post post, LikeEvent event, Instant now,
      Instant retention) {
    return new LikeNotification(
        NotificationIdGenerator.generate(),
        post.getUserId(),
        LIKE,
        event.getCreatedAt(),
        now,
        now,
        retention,
        post.getId(),
        List.of(event.getUserId())
    );
  }
}
