package com.www.event;

import java.time.Instant;
import lombok.Data;

@Data
public class FollowEvent {

  private FollowEventType type;
  private long userId;
  private long targetUserId;
  private Instant createdAt;
}
