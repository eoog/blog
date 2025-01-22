package com.www.service.dto;

import com.www.domain.NotificationType;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class ConvertedNotification {

  protected String id;
  protected NotificationType type;
  protected Instant occurredAt;
  protected Instant lastUpdatedAt;
}
