package com.www.controller.test;

import com.www.event.CommentEvent;
import com.www.event.FollowEvent;
import com.www.event.LikeEvent;
import java.util.function.Consumer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventConsumerTestController implements EventConsumerTestControllerSpec {

  private final Consumer<CommentEvent> comment;

  private final Consumer<LikeEvent> like;

  private final Consumer<FollowEvent> follow;

  public EventConsumerTestController(Consumer<CommentEvent> comment, Consumer<LikeEvent> like,
      Consumer<FollowEvent> follow) {
    this.comment = comment;
    this.like = like;
    this.follow = follow;
  }

  @Override
  @PostMapping("/test/comment")
  public void comment(@RequestBody CommentEvent event) {
    comment.accept(event);
  }

  @Override
  @PostMapping("/test/like")
  public void like(@RequestBody LikeEvent event) {
    like.accept(event);
  }

  @Override
  @PostMapping("/test/follow")
  public void follow(@RequestBody FollowEvent event) {
    follow.accept(event);
  }
}
