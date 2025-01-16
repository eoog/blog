package com.www.com.www.config.mongo.local;

import com.mongodb.ConnectionString;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

@Profile("test")
@Slf4j
@Configuration
public class LocalMongoConfig {

  // 도커 이미지
  private static final String MONGODB_IMAGE_NAME = "mongo:5.0";
  // 도커 몽고DB 포트
  private static final int MONGODB_INNER_PORT = 27017;
  // 데이터베이스
  private static final String DATABASE_NAME = "notification";
  // 테스트컨테이너 생성
  private static final GenericContainer mongo = createMongoInstance();

  // 컨테이너를 생성하는 로직
  // 테스트 컨테이너 의존성
  private static GenericContainer createMongoInstance() {
    return new GenericContainer(DockerImageName.parse(MONGODB_IMAGE_NAME))
        .withExposedPorts(MONGODB_INNER_PORT)
        .withReuse(true); // 재활용 !! 매번 컨테이너 실행 안하고
  }

  // 컨테이너 만든거 실행
  @PostConstruct
  public void startMongo() {
    try {
      log.info("<<<<<< mongo container start");
      mongo.start();
    } catch (Exception e) {
      log.error("<<<< mongo start error>>>>", e);
    }
  }

  // 컨테이너 중지
  @PreDestroy
  public void stopMongo() {
    try {
      log.info("<<<<<< mongo container stop");
      if (mongo.isRunning()) {
        mongo.stop();
      }
    } catch (Exception e) {
      log.error("<<<< mongo stop error>>>>", e);
    }
  }

  // 몽고DB 팩토리
  // 팩토리로 연결해줘야함
  @Bean(name = "notificationMongoFactory")
  public MongoDatabaseFactory notificationMongoFactory() {
    return new SimpleMongoClientDatabaseFactory(connectionMongo());
  }

  // 몽고DB 주소
  private ConnectionString connectionMongo() {
    String host = mongo.getHost();
    Integer port = mongo.getMappedPort(MONGODB_INNER_PORT);

    return new ConnectionString("mongodb://" + host + ":" + port + "/" + DATABASE_NAME);
  }

}
