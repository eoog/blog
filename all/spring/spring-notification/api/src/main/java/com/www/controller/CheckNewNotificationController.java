package com.www.controller;

import com.www.response.CheckNewNotificationResponse;
import com.www.service.CheckNewNotificationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/user-notifications")
public class CheckNewNotificationController implements CheckNewNotificationControllerSpec {

  private final CheckNewNotificationService service;

  public CheckNewNotificationController(CheckNewNotificationService service) {
    this.service = service;
  }

  @Override
  @GetMapping("/{userId}/new")
  public CheckNewNotificationResponse checkNew(
      @PathVariable(value = "userId") long userId
  ) {
    boolean hasNew = service.checkNewNotification(userId);
    return new CheckNewNotificationResponse(hasNew);
  }
}
