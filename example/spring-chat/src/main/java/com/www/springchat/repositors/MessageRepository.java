package com.www.springchat.repositors;

import com.www.springchat.entity.Message;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message,Long> {

  List<Message> findAllByChatroomId(Long chatroomId);

  Boolean existsByChatroomIdAndCreatedAtAfter(Long id, LocalDateTime lastCheckedAt);
}
