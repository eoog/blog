package com.www.kafka.consumer;

import static com.www.kafka.model.Topic.MY_JSON_TOPIC;

import com.www.kafka.model.MyMessage;
import java.util.List;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MyThirdConsumer {

  @KafkaListener(
      topics = {MY_JSON_TOPIC},
      groupId = "batch-test-consumer-group",
      containerFactory = "batchKafkaListenerContainerFactory"
  )
  public void accept(List<ConsumerRecord<String, MyMessage>> messages) {
    System.out.println("[Third Consumer] Batch message arrived! - count " + messages.size());
    messages.forEach(message -> {
          System.out.println("ㄴ [Third Consumer] Value - " + message.value() + " / Offset - " + message.offset() + " / Partition - " + message.partition());
        }
    );
  }
}
