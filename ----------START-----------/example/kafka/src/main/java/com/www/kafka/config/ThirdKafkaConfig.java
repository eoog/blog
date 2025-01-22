package com.www.kafka.config;

import com.www.kafka.model.MyMessage;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.ContainerProperties.AckMode;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
@EnableKafka
public class ThirdKafkaConfig {

  @Bean
  @Qualifier("batchConsumerFactory")
  public ConsumerFactory<String, MyMessage> batchConsumerFactory(KafkaProperties kafkaProperties) {
    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
    props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
    props.put(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG, "false");
    props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, ConsumerConfig.DEFAULT_MAX_POLL_RECORDS);
    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG , "false");
    return new DefaultKafkaConsumerFactory<>(
        props,
        new StringDeserializer(),
        new JsonDeserializer<>(MyMessage.class)
    );
  }

  @Bean
  @Qualifier("batchKafkaListenerContainerFactory")
  public ConcurrentKafkaListenerContainerFactory<String, MyMessage> batchKafkaListenerContainerFactory(
      @Qualifier("batchConsumerFactory") ConsumerFactory<String, MyMessage> batchConsumerFactory
  ) {
    ConcurrentKafkaListenerContainerFactory<String, MyMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(batchConsumerFactory);
    factory.setBatchListener(true);
    factory.getContainerProperties().setAckMode(AckMode.MANUAL);

    return factory;
  }
}
