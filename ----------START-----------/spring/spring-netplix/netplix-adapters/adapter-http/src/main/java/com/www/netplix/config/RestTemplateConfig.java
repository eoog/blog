package com.www.netplix.config;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplateBuilder()
        .setConnectTimeout(Duration.of(1, ChronoUnit.SECONDS))
        .setReadTimeout(Duration.of(5, ChronoUnit.SECONDS))
        .build();
  }
}
