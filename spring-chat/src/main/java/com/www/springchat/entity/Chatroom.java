package com.www.springchat.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
@Setter
public class Chatroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatroom_id")
    Long id;

    String title;

    @OneToMany(mappedBy = "chatroom")
    Set<MemberCatroomMapping> memberCatroomMappings;
    
    // DB 에는 적용되지 않음
    @Transient
    Boolean hasNewMessage;
    
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
