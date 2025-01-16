package com.www.task;


import static com.www.com.www.domain.NotificationType.FOLLOW;

import com.www.com.www.domain.FollowNotification;
import com.www.com.www.service.NotificationSaveService;
import com.www.com.www.utils.NotificationIdGenerator;
import com.www.event.FollowEvent;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FollowAddTask {

  private final NotificationSaveService saveService;

  public FollowAddTask(NotificationSaveService saveService) {
    this.saveService = saveService;
  }

  public void processEvent(FollowEvent event) {
    if (event.getTargetUserId() == event.getUserId()) {
      log.error("targetUserId and userId cannot be the same");
      return;
    }

    saveService.insert(createFollowNotification(event));
  }

  private FollowNotification createFollowNotification(FollowEvent event) {
    Instant now = Instant.now();

    return new FollowNotification(
        NotificationIdGenerator.generate(),
        event.getTargetUserId(),
        FOLLOW,
        event.getCreatedAt(),
        now,
        now,
        now.plus(90, ChronoUnit.DAYS),
        event.getUserId()
    );
  }
}
