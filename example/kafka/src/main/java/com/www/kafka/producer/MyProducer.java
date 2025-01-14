package com.www.kafka.producer;

import com.www.kafka.model.MyMessage;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
@Slf4j
public class MyProducer implements Supplier<Flux<Message<MyMessage>>> {

  private final Sinks.Many<Message<MyMessage>> sinks = Sinks.many().unicast().onBackpressureBuffer();

  public void sendMessage(MyMessage myMessage) {
    Message<MyMessage> message = MessageBuilder
        .withPayload(myMessage)
        .setHeader(KafkaHeaders.KEY, String.valueOf(myMessage.getAge()))
        .build();
    sinks.emitNext(message, (signalType, emitResult) -> {
      if (emitResult.isFailure()) {
        // 로그 추가
        log.error("Failed to emit message: {}", emitResult.name());
        return false; // 재시도하지 않음
      }
      return true;
    });
  }

  @Override
  public Flux<Message<MyMessage>> get() {
    return sinks.asFlux();
  }
}
