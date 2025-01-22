package com.www.back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync // 비동기
public class BackApplication {

  public static void main(String[] args) {
    SpringApplication.run(BackApplication.class, args);
  }

}
