package com.www.springlogbatch.batch;

import com.www.springlogbatch.entity.WebLog;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.kafka.core.KafkaTemplate;

public class WebLogItemWriter implements ItemWriter<WebLog> {

  private final KafkaTemplate<String, WebLog> kafkaTemplate;

  public WebLogItemWriter(KafkaTemplate<String, WebLog> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }


  @Override
  public void write(Chunk<? extends WebLog> items) throws Exception {
    for (WebLog webLog : items) {
      kafkaTemplate.send("source_topic",webLog);
    }
  }
}
