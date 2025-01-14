package com.www.kafka.consumer;

import com.www.kafka.model.MyMessage;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class MyConsumer  {

  @KafkaListener(
      topics = {"my-json-topic"},
      groupId = "test-consumer-group"
  )
  public void accept(ConsumerRecord<String, MyMessage> message) {
    System.out.println("[Main Consumer] Message arrived! - " + message.value());
  }
}
