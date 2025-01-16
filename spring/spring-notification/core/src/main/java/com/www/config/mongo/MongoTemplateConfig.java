package com.www.config.mongo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(
    basePackages = "com.www",
    mongoTemplateRef = MongoTemplateConfig.MONGO_TEMPLATE
)
public class MongoTemplateConfig {

  // LocalMongoConfig 에서 Bean 으로 등록했던 이름 ( 연결할거임 )
  public static final String MONGO_TEMPLATE = "notificationMongoTemplate";

  @Bean(name = MONGO_TEMPLATE)
  public MongoTemplate mongoTemplate(
      MongoDatabaseFactory mongoDatabaseFactory,
      MongoConverter mongoConverter
  ) {
    return new MongoTemplate(mongoDatabaseFactory, mongoConverter);
  }
}
