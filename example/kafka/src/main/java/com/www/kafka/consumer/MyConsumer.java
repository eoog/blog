package com.www.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.www.kafka.model.MyMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class MyConsumer  {

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final Map<String,Integer> idHistoryMap = new ConcurrentHashMap<>();

  @KafkaListener(
      topics = {"my-json-topic"},
      groupId = "test-consumer-group",
      concurrency = "1"
  )
  public void accept(ConsumerRecord<String, String> message , Acknowledgment ack)
      throws JsonProcessingException, InterruptedException {
    MyMessage myMessage = objectMapper.readValue(message.value(), MyMessage.class);
    this.printPayloadIfFirstMessage(myMessage);
    Thread.sleep(1000);
    ack.acknowledge(); // 수동커밋
  }

  private synchronized void printPayloadIfFirstMessage(MyMessage message) {
    if (idHistoryMap.putIfAbsent(String.valueOf(message.getId()) , 1) == null) {
      System.out.println("[Main Consumer] Message arrived! - " + message); // Exactly One 이 실행되어야 하는 로직이라고 가정
      //idHistoryMap.put(String.valueOf(message.getId()) , 1);
    } else {
      System.out.println("[Main Consumer] Duplicate ! - " + message.getId());
    }
  }
}
