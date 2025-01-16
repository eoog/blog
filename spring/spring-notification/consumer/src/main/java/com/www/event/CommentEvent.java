package com.www.event;

import lombok.Data;

@Data
public class CommentEvent {

  public CommentEventType type;
  public Long postId;
  public Long userId;
  public Long commentId;
}
