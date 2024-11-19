package com.www.back.service;

import java.util.Timer;
import java.util.TimerTask;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQReceiver {

  @RabbitListener(queues = "onion-notification")
  public void receive(String message) {

    Timer timer = new Timer();

    // 10초후에 실행될 작업 Timer 등록

    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        System.out.println("Receieved Message: " + message);
      }
    }, 5000);

  }
}
