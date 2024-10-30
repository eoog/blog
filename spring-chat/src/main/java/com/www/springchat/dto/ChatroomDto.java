package com.www.springchat.dto;

import com.www.springchat.entity.Chatroom;

import java.time.LocalDateTime;

public record ChatroomDto(
        Long id,
        String title,
        Integer memberCount,
        LocalDateTime createdAt
) {
    public static ChatroomDto from(Chatroom chatroom) {
        return new ChatroomDto(chatroom.getId(), chatroom.getTitle(), chatroom.getMemberCatroomMappings().size(), chatroom.getCreateAt());
    }
}
