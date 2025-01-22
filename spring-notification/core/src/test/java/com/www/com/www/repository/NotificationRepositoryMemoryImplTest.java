//package com.www.com.www.repository;
//
//import static com.www.domain.NotificationType.COMMENT;
//import static java.time.temporal.ChronoUnit.DAYS;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//import com.www.domain.CommentNotification;
//import com.www.domain.Notification;
//import com.www.repository.NotificationRepository;
//import java.time.Instant;
//import java.util.Optional;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//@SpringBootApplication
//class NotificationRepositoryMemoryImplTest {
//
//  @Autowired
//  NotificationRepository sut1;
//
//
//  private final Instant now = Instant.now();
//
//
//  private final long userId = 2L;
//  private final long postId = 3L;
//  private final long writerId = 4L;
//  private final long commentId = 5L;
//  private final String comment = "comment";
//  private final Instant ninetyDaysAfter = Instant.now().plus(90, DAYS);
//
//  private CommentNotification createCommentNotification(String id) {
//    return new CommentNotification(id, userId, COMMENT, now, now, now, ninetyDaysAfter, postId,
//        writerId, comment,
//        commentId);
//  }
//
//  @Test()
//  @DisplayName("Map 기반으로 저장")
//  void memory_save() {
//    String id = "1";
//    sut.save(createCommentNotification(id));
//    Optional<Notification> optionalNotification = sut.findById(id);
//
//    assertTrue(optionalNotification.isPresent());
//  }
//
//  @Test
//  @DisplayName("아이디 찾기")
//  void memory_findById() {
//    String id = "2";
//
//    sut.save(createCommentNotification(id));
//    Optional<Notification> optionalNotification = sut.findById(id);
//
//    CommentNotification notification = (CommentNotification) optionalNotification.orElseThrow();
//    assertEquals(notification.getId(), id);
//    assertEquals(notification.getUserId(), userId);
//    assertEquals(notification.getOccurredAt().getEpochSecond(), now.getEpochSecond());
//    assertEquals(notification.getCreatedAt().getEpochSecond(), now.getEpochSecond());
//    assertEquals(notification.getLastUpdatedAt().getEpochSecond(), now.getEpochSecond());
//    assertEquals(notification.getDeletedAt().getEpochSecond(), ninetyDaysAfter.getEpochSecond());
//    assertEquals(notification.getPostId(), postId);
//    assertEquals(notification.getWriterId(), writerId);
//    assertEquals(notification.getComment(), comment);
//    assertEquals(notification.getCommentId(), commentId);
//  }
//
//  @Test
//  void testDeleteById() {
//    String id = "3";
//
//    sut.save(createCommentNotification(id));
//    sut.deleteById(id);
//    Optional<Notification> optionalNotification = sut.findById(id);
//
//    assertFalse(optionalNotification.isPresent());
//  }
//}
