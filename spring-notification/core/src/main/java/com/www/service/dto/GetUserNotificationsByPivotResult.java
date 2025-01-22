package com.www.service.dto;

import com.www.domain.Notification;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetUserNotificationsByPivotResult {

  private List<Notification> notifications;
  private boolean hasNext;
}

