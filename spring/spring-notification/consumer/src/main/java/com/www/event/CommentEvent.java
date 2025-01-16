package com.www.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentEvent {

  public CommentEventType type;
  public Long postId;
  public Long userId;
  public Long commentId;
}
