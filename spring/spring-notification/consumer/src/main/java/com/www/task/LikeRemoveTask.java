package com.www.task;


import static com.www.com.www.domain.NotificationType.LIKE;

import com.www.com.www.domain.LikeNotification;
import com.www.com.www.domain.Notification;
import com.www.com.www.service.NotificationGetService;
import com.www.com.www.service.NotificationRemoveService;
import com.www.com.www.service.NotificationSaveService;
import com.www.event.LikeEvent;
import java.time.Instant;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LikeRemoveTask {

  private final NotificationGetService getService;

  private final NotificationRemoveService removeService;

  private final NotificationSaveService saveService;

  public LikeRemoveTask(NotificationGetService getService, NotificationRemoveService removeService,
      NotificationSaveService saveService) {
    this.getService = getService;
    this.removeService = removeService;
    this.saveService = saveService;
  }

  public void processEvent(LikeEvent event) {
    Optional<Notification> optionalNotification = getService.getNotificationByTypeAndPostId(LIKE,
        event.getPostId());
    if (optionalNotification.isEmpty()) {
      log.error("No notification with postId: {}", event.getPostId());
      return;
    }

    LikeNotification notification = (LikeNotification) optionalNotification.get();
    removeLikerAndUpdateNotification(notification, event);
  }

  private void removeLikerAndUpdateNotification(LikeNotification notification, LikeEvent event) {
    notification.removeLiker(event.getUserId(), Instant.now());

    if (notification.getLikerIds().isEmpty()) {
      removeService.deleteById(notification.getId());
    } else {
      saveService.upsert(notification);
    }
  }
}
