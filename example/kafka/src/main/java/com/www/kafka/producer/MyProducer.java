package com.www.kafka.producer;

import com.www.kafka.model.MyMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MyProducer  {

  private final KafkaTemplate<String , MyMessage> kafkaTemplate;
  
  public void sendMessage(MyMessage message) {
     kafkaTemplate.send("my-json-topic" , String.valueOf(message.getAge()) , message);
  }

}
