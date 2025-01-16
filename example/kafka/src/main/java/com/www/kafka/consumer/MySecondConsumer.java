package com.www.kafka.consumer;

import static com.www.kafka.model.Topic.MY_SECOND_TOPIC;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MySecondConsumer {

  @KafkaListener(
      topics = {MY_SECOND_TOPIC},
      groupId = "test-consumer-group",
      containerFactory = "secondKafkaListenerContainerFactory"
  )
  public void accept(ConsumerRecord<String, String> message) {
    System.out.println("[Second Consumer] Message arrived! - " + message.value());
    System.out.println("[Second Consumer] Offset - " + message.offset() + " / Partition - " + message.partition());
  }
}
