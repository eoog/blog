package com.www.kafka.consumer;

import static com.www.kafka.model.Topic.MY_JSON_TOPIC;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.www.kafka.model.MyMessage;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class MyThirdConsumer {

  private final ObjectMapper objectMapper = new ObjectMapper();

  private final ExecutorService executorService = Executors.newFixedThreadPool(10);

  @KafkaListener(
      topics = {MY_JSON_TOPIC},
      groupId = "batch-test-consumer-group",
      containerFactory = "batchKafkaListenerContainerFactory",
      concurrency = "1"
  )
  public void listen(List<ConsumerRecord<String, MyMessage>> messages , Acknowledgment ack) throws JsonProcessingException {
    System.out.println("[Batch Consumer] Batch message arrived! - count " + messages.size());

    messages.forEach(message -> executorService.submit(()-> {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      System.out.println("ㄴ [Batch Consumer(" + Thread.currentThread().getId() + ")] " + "[Partition - " + message.partition() + " / Offset - " + message.offset() + "] " + message);
    }));
  }
}
