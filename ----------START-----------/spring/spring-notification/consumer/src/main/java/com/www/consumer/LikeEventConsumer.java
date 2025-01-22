package com.www.consumer;

import static com.www.event.LikeEventType.ADD;
import static com.www.event.LikeEventType.REMOVE;

import com.www.event.LikeEvent;
import com.www.task.LikeAddTask;
import com.www.task.LikeRemoveTask;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LikeEventConsumer {

  private final LikeAddTask likeAddTask;

  private final LikeRemoveTask likeRemoveTask;

  public LikeEventConsumer(LikeAddTask likeAddTask, LikeRemoveTask likeRemoveTask) {
    this.likeAddTask = likeAddTask;
    this.likeRemoveTask = likeRemoveTask;
  }

  @Bean("like")
  public Consumer<LikeEvent> like() {
    return event -> {
      if (event.getType() == ADD) {
        likeAddTask.processEvent(event);
      } else if (event.getType() == REMOVE) {
        likeRemoveTask.processEvent(event);
      }
    };
  }
}
