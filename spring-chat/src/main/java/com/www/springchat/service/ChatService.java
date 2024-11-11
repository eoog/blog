package com.www.springchat.service;

import com.www.springchat.entity.Chatroom;
import com.www.springchat.entity.Member;
import com.www.springchat.entity.MemberCatroomMapping;
import com.www.springchat.entity.Message;
import com.www.springchat.repositors.ChatRoomRepository;
import com.www.springchat.repositors.MemberChatRoomMappingRepository;
import com.www.springchat.repositors.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberChatRoomMappingRepository memberChatRoomMappingRepository;
    private final MessageRepository messageRepository;


    // 채팅방생성
    public Chatroom createChatroom(Member member , String title) {
        
        // 채팅방생성
        Chatroom chatroom = Chatroom.builder()
                .title(title)
                .createAt(LocalDateTime.now())
                .build();
        
        chatroom = chatRoomRepository.save(chatroom);

        // 채팅방 만든 본인 참여 테이블
        MemberCatroomMapping memberCatroomMapping = chatroom.addMember(member);

        memberCatroomMapping = memberChatRoomMappingRepository.save(memberCatroomMapping);
        
        
        return chatroom;
    }
    
    // 참여 여부 확인
    public Boolean joinChatroom(Member member, Long chatroomId) {

        // 참여 여부 확인
        if (memberChatRoomMappingRepository.existsByMemberIdAndChatroomId(member.getId(),chatroomId)) {
            log.info("이미 참여한 채팅방입니다.");
            return false;
        }

        // 채팅방 검색
        Chatroom chatroom = chatRoomRepository.findById(chatroomId).get();

        // 채팅방 참여
        MemberCatroomMapping memberCatroomMapping =  MemberCatroomMapping.builder()
                .member(member)
                .chatroom(chatroom)
                .build();

        memberCatroomMapping = memberChatRoomMappingRepository.save(memberCatroomMapping);
        
        return true;
    }


    // 채팅방 나가기
    // jakarta.persistence.TransactionRequiredException: No EntityManager with actual transaction available for current thread - cannot reliably process 'remove' call
    @Transactional
    public Boolean leaveChatroom(Member member, Long chatroomId) {

        // 기존 참여 했는지 학인한느 방
        if (!memberChatRoomMappingRepository.existsByMemberIdAndChatroomId(member.getId(),chatroomId)) {
            log.info("참여하지 않는 방입니다.");
            return false;
        }

        // 참여기록 지움
        // 삭제할땐 트랜잭션 있어야해..
        memberChatRoomMappingRepository.deleteByMemberIdAndChatroomId(member.getId(),chatroomId);

        return  true;
    }

    // 사용자가 참여한 채팅방 목록 가져오기
    public List<Chatroom> getChatroomList(Member member) {
        List<MemberCatroomMapping> memberCatroomMappings = memberChatRoomMappingRepository.findAllByMemberId(member.getId());

        return memberCatroomMappings.stream()
                .map(memberCatroomMapping -> memberCatroomMapping.getChatroom())
                .toList();
    }

    // 메시지 저장
    public Message saveMessage(Member member , Long chatroomId , String text) {
        Chatroom chatroom = chatRoomRepository.findById(chatroomId).get();

        Message message = Message.builder()
            .text(text)
            .member(member)
            .chatroom(chatroom)
            .build();

        return messageRepository.save(message);
    }

    // 특정 채팅방 작성된 메시지 가져오기
    public List<Message> getMessageList(Long chatroomId) {
        return messageRepository.findAllByChatroomId(chatroomId);
    }
}
