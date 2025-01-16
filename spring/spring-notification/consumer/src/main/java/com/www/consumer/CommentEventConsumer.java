package com.www.consumer;

import com.www.event.CommentEvent;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CommentEventConsumer {

  @Bean("comment") //  definition: like; comment; follow; # Consumer 함수 이름
  public Consumer<CommentEvent> comment() {
    return evnet -> log.info(evnet.toString());
  }
}
