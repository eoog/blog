package com.www.kafka.api;

import com.www.kafka.model.MyMessage;
import com.www.kafka.producer.MyProducer;
import com.www.kafka.producer.MySecondProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MyController {

  private final MyProducer myProducer;
  private final MySecondProducer mySecondProducer;

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

  @PostMapping("/second-message/{key}")
  void message11(
      @PathVariable(value = "key") String key,
      @RequestBody String message
  ) {
    mySecondProducer.sendMessageWithKey(key, message);
  }
}