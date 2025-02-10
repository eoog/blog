package com.www.pointservice.config;

import com.www.pointservice.aop.PointMetricsAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class MetricsConfig {
  @Bean
  public PointMetricsAspect pointMetricsAspect(MeterRegistry registry) {
    return new PointMetricsAspect(registry);
  }
}
