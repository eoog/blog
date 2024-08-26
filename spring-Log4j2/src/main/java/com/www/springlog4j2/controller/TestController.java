package com.www.springlog4j2.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

  private Logger log = LoggerFactory.getLogger(this.getClass());


  @PostMapping("/result")
  public ResponseEntity<String> selectCodeList() {
    // 2. 로그를 출력합니다.
    log.debug("코드의 DEBUG");
    log.info("코드의 INFO");

    return new ResponseEntity<>("string", HttpStatus.OK);
  }
}
