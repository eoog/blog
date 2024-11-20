package com.www.back.service;


import com.www.back.pojo.SendCommentNotification;
import com.www.back.pojo.WriteArticle;
import com.www.back.pojo.WriteComment;
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

  // 게시글 작성
  public void send(WriteArticle writeArticle) {
    rabbitTemplate.convertAndSend("onion-notification", writeArticle.toString());
  }

  // 댓글 작성
  public void send(WriteComment message) {
    rabbitTemplate.convertAndSend("onion-notification", message.toString());
  }

  // 댓글 작성자 모두
  public void send(SendCommentNotification message) {
    rabbitTemplate.convertAndSend("send_notification_exchange", "", message.toString());
  }
}
