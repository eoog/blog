package com.www.springrabbitmqproducer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.www.springrabbitmqproducer.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProducerService {

  private final RabbitTemplate rabbitTemplate;

  public void sendMessage(MessageDto messageDto) {
    try {
      // 객체를 JSON으로 변환
      ObjectMapper objectMapper = new ObjectMapper();
      String objectToJSON = objectMapper.writeValueAsString(messageDto);
      rabbitTemplate.convertAndSend("hello.exchange", "hello.key", objectToJSON);
    } catch (JsonProcessingException jpe) {
      System.out.println("파싱 오류 발생");
    }
  }

}
