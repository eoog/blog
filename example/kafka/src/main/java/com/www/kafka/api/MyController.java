package com.www.kafka.api;

import com.www.kafka.model.MyMessage;
import com.www.kafka.producer.MyProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MyController {

  private final MyProducer myProducer;

  @RequestMapping("/hello")
  String hello() {
    return "Hello World";
  }

  @PostMapping("/message")
  void message(
      @RequestBody MyMessage message
  ) {
    myProducer.sendMessage(message);
  }
}