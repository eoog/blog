package com.www.config.redis;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

@Profile("test")
@Slf4j
@Configuration
public class LocalRedisConfig {

  private static final String IMAGE = "redis:7.4.0";
  private static final int PORT = 6379;
  private GenericContainer redis;

  @PostConstruct
  public void startRedis() {
    try {
      redis = new GenericContainer(DockerImageName.parse(IMAGE)).withExposedPorts(PORT);
      redis.start();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  @PreDestroy
  public void stopRedis() {
    try {
      redis.stop();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  @Bean(name = "redisConnectionFactory")
  public RedisConnectionFactory redisConnectionFactory() {
    RedisStandaloneConfiguration redisStandaloneConfiguration =
        new RedisStandaloneConfiguration(redis.getHost(), redis.getMappedPort(PORT));
    return new LettuceConnectionFactory(redisStandaloneConfiguration);
  }
}