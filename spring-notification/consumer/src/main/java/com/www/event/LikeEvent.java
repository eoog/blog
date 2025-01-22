package com.www.event;

import java.time.Instant;
import lombok.Data;

@Data
public class LikeEvent {

  private LikeEventType type;
  private long postId;
  private long userId;
  private Instant createdAt;
}

