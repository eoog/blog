package com.www.msacourseservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MsaCourseServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(MsaCourseServiceApplication.class, args);
  }

}
