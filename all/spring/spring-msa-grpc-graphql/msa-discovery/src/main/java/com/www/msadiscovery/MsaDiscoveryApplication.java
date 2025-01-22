package com.www.msadiscovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer // eureka server 기동
public class MsaDiscoveryApplication {

  public static void main(String[] args) {
    SpringApplication.run(MsaDiscoveryApplication.class, args);
  }

}
