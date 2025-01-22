package com.www.controller;

import com.www.response.SetLastReadAtResponse;
import com.www.service.LastReadAtService;
import java.time.Instant;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/user-notifications")
public class NotificationReadController implements NotificationReadControllerSpec {

  private final LastReadAtService service;

  public NotificationReadController(LastReadAtService service) {
    this.service = service;
  }

  @Override
  @PutMapping("/{userId}/read")
  public SetLastReadAtResponse setLastReadAt(
      @PathVariable(value = "userId") long userId
  ) {
    Instant lastReadAt = service.setLastReadAt(userId);
    return new SetLastReadAtResponse(lastReadAt);
  }
}
