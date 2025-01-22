package com.www.controller;

import com.www.response.UserNotificationListResponse;
import com.www.service.GetUserNotificationsService;
import java.time.Instant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/user-notifications")
public class UserNotificationListController implements UserNotificationListControllerSpec {

  private final GetUserNotificationsService getUserNotificationsService;

  public UserNotificationListController(GetUserNotificationsService getUserNotificationsService) {
    this.getUserNotificationsService = getUserNotificationsService;
  }

  @Override
  @GetMapping("/{userId}")
  public UserNotificationListResponse getNotifications(
      @PathVariable(value = "userId") long userId,
      @RequestParam(value = "pivot", required = false) Instant pivot
  ) {
    return UserNotificationListResponse.of(
        getUserNotificationsService.getUserNotificationsByPivot(userId, pivot)
    );
  }
}
