package com.www.springchat.handlres;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class WebScoketChatHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

    // 서버가 연결 이후 실행
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("{}  connect" , session.getId());

        this.sessionMap.put(session.getId(),session);
    }

    // 메시지가 왔을때 로직 처리
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        log.info("{} sent {}" , session.getId() , message.getPayload());
        
        // 특정 인원이 보낸 메시지를 같은 세션의 인원에게 메시지 보내기
        this.sessionMap.values().forEach(webSocketSession -> {
            try {
                webSocketSession.sendMessage(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    // 서버 접속할때 유저가 접속끊었을때
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("{}  disconnect" , session.getId());

        this.sessionMap.remove(session.getId());
    }
}
