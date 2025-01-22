package com.www.consumer;

import static com.www.event.CommentEventType.ADD;
import static com.www.event.CommentEventType.REMOVE;

import com.www.event.CommentEvent;
import com.www.task.CommentAddTask;
import com.www.task.CommentRemoveTask;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CommentEventConsumer {

  private final CommentAddTask commentAddTask;

  private final CommentRemoveTask commentRemoveTask;

  public CommentEventConsumer(CommentAddTask commentAddTask, CommentRemoveTask commentRemoveTask) {
    this.commentAddTask = commentAddTask;
    this.commentRemoveTask = commentRemoveTask;
  }

  @Bean("comment")
  public Consumer<CommentEvent> comment() {
    return event -> {
      if (event.getType() == ADD) {
        commentAddTask.processEvent(event);
      } else if (event.getType() == REMOVE) {
        commentRemoveTask.processEvent(event);
      }
    };
  }
}
