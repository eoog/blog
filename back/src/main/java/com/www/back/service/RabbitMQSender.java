package com.www.back.service;


import com.www.back.entity.ArticleNotification;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQSender {

  private final RabbitTemplate rabbitTemplate;

  @Autowired
  public RabbitMQSender(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  public void send(ArticleNotification articleNotification) {
    rabbitTemplate.convertAndSend("onion-notification", articleNotification.toString());
  }
}
