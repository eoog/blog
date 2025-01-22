package com.www.msaenrollmentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MsaEnrollmentServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(MsaEnrollmentServiceApplication.class, args);
  }

}
