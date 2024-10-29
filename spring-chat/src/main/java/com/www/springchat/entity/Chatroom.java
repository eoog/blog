package com.www.springchat.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
public class Chatroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatroom_id")
    Long id;

    String title;

    @OneToMany(mappedBy = "chatroom")
    Set<MemberCatroomMapping> memberCatroomMappings;
    ;
    LocalDateTime createAt;

}
