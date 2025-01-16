package com.www.task;

import com.www.com.www.client.CommentClient;
import com.www.com.www.client.PostClient;
import com.www.com.www.domain.Comment;
import com.www.com.www.domain.CommentNotification;
import com.www.com.www.domain.Notification;
import com.www.com.www.domain.NotificationType;
import com.www.com.www.domain.Post;
import com.www.com.www.service.NotificationSaveService;
import com.www.com.www.utils.NotificationIdGenerator;
import com.www.event.CommentEvent;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommentAddTask {

  @Autowired
  PostClient postClient;

  @Autowired
  CommentClient commentClient;

  @Autowired
  NotificationSaveService saveService;

  public void processEvent(CommentEvent event) {
    // 내가 작성한 댓글인 경우 무시
    Post post = postClient.getPost(event.getPostId());
    if (Objects.equals(post.getUserId(), event.getUserId())) {
      return;
    }

    // 알림생성
    Comment comment = commentClient.getComment(event.getCommentId());
    // 저장

    Notification notification = createNotification(post, comment);
    saveService.insert(notification);
  }

  // 댓글 알림 생성 이벤트
  public Notification createNotification(Post post, Comment comment) {
    Instant now = Instant.now();
    return new CommentNotification(
        NotificationIdGenerator.generate(),
        post.getUserId(),
        NotificationType.COMMENT,
        comment.getCreatedAt(),
        now,
        now,
        now.plus(90, ChronoUnit.DAYS),
        post.getId(),
        comment.getUserId(),
        comment.getContent(),
        comment.getId()
    );
  }
}
