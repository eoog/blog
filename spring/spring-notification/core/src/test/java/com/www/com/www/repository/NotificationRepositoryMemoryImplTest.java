package com.www.com.www.repository;

import com.www.com.www.domain.Notification;
import com.www.com.www.domain.NotificationType;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@SpringBootApplication
class NotificationRepositoryMemoryImplTest {

  @Autowired
  private NotificationRepository memory;

  private final Instant now = Instant.now();

  @Test()
  @DisplayName("Map 기반으로 저장")
  void memory_save() {
    // given
    Notification notification = new Notification(1L, 1L, NotificationType.LIKE, now,
        now, now, now);

    memory.save(notification);

    //when
    Optional<Notification> result = memory.findById(1L);

    System.out.println("result = " + result.get().toString());
    //then
    Assertions.assertEquals(result.get().id, 1L);
  }

  @Test
  @DisplayName("아이디 찾기")
  void memory_findById() {
    //given
    Notification notification = new Notification(2L, 2L, NotificationType.LIKE, now,
        now, now, now);
    memory.save(notification);
    //when
    Optional<Notification> result = memory.findById(2L);
    //then
    Assertions.assertEquals(result.get().id, 2L);
  }

  @Test
  @DisplayName("아이디 삭제하기")
  void memory_delete() {
    // given
    //Notification notification = new Notification(3L, 3L, NotificationType.LIKE, now, now, now, now);
    //memory.save(notification);
    memory.deleteById(2L);
    // when
    Optional<Notification> result = memory.findById(2L);
    // then
    Assertions.assertFalse(result.isPresent());
  }
}
