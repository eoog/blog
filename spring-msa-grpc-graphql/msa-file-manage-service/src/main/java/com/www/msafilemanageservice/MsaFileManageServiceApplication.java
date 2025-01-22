package com.www.msafilemanageservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MsaFileManageServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(MsaFileManageServiceApplication.class, args);
  }

}
