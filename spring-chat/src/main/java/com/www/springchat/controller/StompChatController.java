package com.www.springchat.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class StompChatController {

    @MessageMapping("/chats") // 클라이언트 > pub/chats 발행하면 여길로 전달된다. pub은 생략!!
    @SendTo("/sub/chats") // 리턴된 메시지는 구독자에게 메시지가 전달된다 , sub 은 생략 안됨
    public String handleMessage(@Payload String message) {
        log.info("{} received a message", message);

        return message;
    }
}
