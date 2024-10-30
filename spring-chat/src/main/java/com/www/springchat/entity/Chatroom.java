package com.www.springchat.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
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

    public MemberCatroomMapping addMember(Member member) {
        if (this.getMemberCatroomMappings() == null) {
            this.memberCatroomMappings = new HashSet<>();
        }

        MemberCatroomMapping memberCatroomMapping = MemberCatroomMapping.builder()
                .member(member)
                .chatroom(this)
                .build();

        this.memberCatroomMappings.add(memberCatroomMapping);
        return memberCatroomMapping;
    }

}
