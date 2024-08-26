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
  Logger asyncLogger = LoggerFactory.getLogger("async-logger");

  @PostMapping("/result")
  public ResponseEntity<String> selectCodeList() {
    // 2. 로그를 출력합니다.
    int answer = 0;
    for (int i = 0; i < 50; i++) {
      // 비동기 로깅 시작
      asyncLogger.info("[+] 비동기 로깅이 출력이 잘 되는지 확인합니다.");
    }

    log.debug("[+] 도중에 동기 로깅을 수행합니다.");


    return new ResponseEntity<>("string", HttpStatus.OK);
  }
}
