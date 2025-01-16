package com.www.consumer;

import com.www.event.FollowEvent;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FollowEventConsumer {

  @Bean("follow") //  definition: like; comment; follow; # Consumer 함수 이름
  public Consumer<FollowEvent> follow() {
    return evnet -> log.info(evnet.toString());
  }
}
