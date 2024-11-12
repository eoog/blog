package com.www.springchat.repositors;

import com.www.springchat.entity.Member;
import com.www.springchat.entity.MemberCatroomMapping;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberChatRoomMappingRepository extends JpaRepository<MemberCatroomMapping, Long> {

  Boolean existsByMemberIdAndChatroomId(Long id, Long chatroomId);

  void deleteByMemberIdAndChatroomId(Long id, Long chatroomId);

  List<MemberCatroomMapping> findAllByMemberId(Long id);

  Optional<MemberCatroomMapping> findByMemberIdAndChatroomId(Long memberId, Long currentChatroomId);
}
