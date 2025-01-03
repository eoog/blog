package com.www.couponservice.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

  private static final String BOOTSTRAP_SERVERS = "localhost:9092";
  private static final String GROUP_ID = "coupon-service";


}
