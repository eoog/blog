package com.www.springchat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@EnableWebSocketMessageBroker // stomp 메시지 브로커 , 웹소켓은 웹소켓
@Configuration
public class StompConfiguration implements WebSocketMessageBrokerConfigurer {

    // 웹소켓 클라이언트가 서버로 어디주소로 오는지 지정
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/stomp/chats");
    }

    // 메시지 브로커 역할 클라이언트 > 메시지 , 브로커는 구독
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/pub"); // 발행
        registry.enableSimpleBroker("/sub"); // 구독
    }
}
