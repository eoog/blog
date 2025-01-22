package com.www.task;


import com.www.domain.NotificationType;
import com.www.event.FollowEvent;
import com.www.service.NotificationGetService;
import com.www.service.NotificationRemoveService;
import org.springframework.stereotype.Component;

@Component
public class FollowRemoveTask {

  private final NotificationGetService getService;

  private final NotificationRemoveService removeService;

  public FollowRemoveTask(NotificationGetService getService,
      NotificationRemoveService removeService) {
    this.getService = getService;
    this.removeService = removeService;
  }

  public void processEvent(FollowEvent event) {
    getService.getNotificationByTypeAndUserIdAndFollowerId(NotificationType.FOLLOW,
            event.getTargetUserId(),
            event.getUserId())
        .ifPresent(
            notification -> removeService.deleteById(notification.getId())
        );
  }
}
