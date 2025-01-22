package com.www.springlogbatch.batch;

import com.www.springlogbatch.entity.OrderLog;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.kafka.core.KafkaTemplate;

public class OrderLogItemWriter implements ItemWriter<OrderLog> {

  private final KafkaTemplate<String, OrderLog> kafkaTemplate;

  public OrderLogItemWriter(KafkaTemplate<String, OrderLog> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  @Override
  public void write(Chunk<? extends OrderLog> chunk) throws Exception {
    for (OrderLog orderLog : chunk) {
      kafkaTemplate.send("orderLog", orderLog);
    }
  }
}
