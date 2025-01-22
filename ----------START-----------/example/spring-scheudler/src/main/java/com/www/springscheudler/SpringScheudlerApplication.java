package com.www.springscheudler;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.www.springscheudler.mapper")  // Adjust the package name accordingly

public class SpringScheudlerApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringScheudlerApplication.class, args);
  }

}
