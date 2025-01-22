package com.www.kafka.producer;

import static com.www.kafka.model.Topic.MY_SECOND_TOPIC;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MySecondProducer {

  @Qualifier("secondKafkaTemplate")
  private final KafkaTemplate<String, String> secondKafkaTemplate;

  public void sendMessageWithKey(String key, String message) {
    secondKafkaTemplate.send(MY_SECOND_TOPIC, key, message);
  }
}
