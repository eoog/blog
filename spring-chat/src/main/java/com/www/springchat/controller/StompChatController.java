package com.www.springchat.controller;

import com.www.springchat.dto.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Map;

@Slf4j
@Controller
public class StompChatController {

    // 기본
    @MessageMapping("/chats") // 클라이언트 > pub/chats 발행하면 여길로 전달된다. pub은 생략!!
    @SendTo("/sub/chats") // 리턴된 메시지는 구독자에게 메시지가 전달된다 , sub 은 생략 안됨
    public ChatMessage handleMessage1(@AuthenticationPrincipal Principal principal, @Payload Map<String, String> payload) {
        log.info("{} sent {} ", principal.getName(), payload);

        return new ChatMessage(principal.getName(),payload.get("message"));
    }

   // 채팅방
    @MessageMapping("/chats/{chatroomId}") // 클라이언트 > pub/chats 발행하면 여길로 전달된다. pub은 생략!!
    @SendTo("/sub/chats/{chatroomId}") // 리턴된 메시지는 구독자에게 메시지가 전달된다 , sub 은 생략 안됨
    public ChatMessage handleMessage(@AuthenticationPrincipal Principal principal,@DestinationVariable Long chatroomId, @Payload Map<String, String> payload) {
        log.info("{} sent {} in {} ", principal.getName(), payload ,chatroomId);

        return new ChatMessage(principal.getName(),payload.get("message"));
    }
}
