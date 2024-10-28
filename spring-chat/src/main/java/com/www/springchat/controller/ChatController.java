package com.www.springchat.controller;

import com.www.springchat.entity.Chatroom;
import com.www.springchat.service.ChatService;
import com.www.springchat.vo.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Block;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/chats")
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public Chatroom createChatroom(@AuthenticationPrincipal CustomOAuth2User user , @RequestParam String title) {
        return chatService.createChatroom(user.getMember() , title);
    }

    @PostMapping("/{chatroomId}")
    public Boolean joinChatroom(@PathVariable Long chatroomId, @AuthenticationPrincipal CustomOAuth2User user) {
        return chatService.joinChatroom(user.getMember(),chatroomId);
    }

    @DeleteMapping("/{chatroomId}")
    public Boolean leaveChatroom(@PathVariable Long chatroomId, @AuthenticationPrincipal CustomOAuth2User user) {
        return chatService.leaveChatroom(user.getMember(),chatroomId);
    }

    @GetMapping
    public List<Chatroom> getList(@AuthenticationPrincipal CustomOAuth2User user) {
        return chatService.getChatroomList(user.getMember());
    }
}
