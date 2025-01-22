package com.www.springchat.config;

import com.www.springchat.handlres.WebScoketChatHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@EnableWebSocket // 웹소켓 사용
@Configuration
@RequiredArgsConstructor
public class WebSocketConfiguration implements WebSocketConfigurer {

    private final WebScoketChatHandler webScoketChatHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webScoketChatHandler, "/ws/chats"); // 어떠한 경로로 접속했을때 핸들러가 반응하게 만들꺼냐 ??
    }
}
