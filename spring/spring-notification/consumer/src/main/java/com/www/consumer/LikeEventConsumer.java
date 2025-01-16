package com.www.consumer;

import com.www.event.LikeEvent;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LikeEventConsumer {

  @Bean("like") //  definition: like; comment; follow; # Consumer 함수 이름
  public Consumer<LikeEvent> like() {
    return evnet -> log.info(evnet.toString());
  }
}
