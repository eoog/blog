package com.www.msagraphql.controller;

import com.www.msagraphql.model.ChatMessage;
import com.www.msagraphql.service.ChatService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class ChatController {

  private final ChatService chatService;

  public ChatController(ChatService chatService) {
    this.chatService = chatService;
  }

  @MutationMapping
  public boolean sendMessage(@Argument String courseId, @Argument String userId,
      @Argument String content) {
    chatService.sendMessage(courseId, userId, content);
    return true;
  }

  @SubscriptionMapping
  public Publisher<ChatMessage> messageReceived(@Argument String courseId) {
    return chatService.messageReceived(courseId);
  }

  @QueryMapping
  public List<ChatMessage> getMessages(@Argument String courseId) {
    return chatService.getMessages(courseId);
  }

}