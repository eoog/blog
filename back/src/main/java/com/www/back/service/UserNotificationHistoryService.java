package com.www.back.service;

import com.www.back.entity.Article;
import com.www.back.entity.Comment;
import com.www.back.entity.UserNotificationHistory;
import com.www.back.repository.UserNotificationHistoryRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserNotificationHistoryService {

  private final UserNotificationHistoryRepository userNotificationHistoryRepository;

  @Autowired
  public UserNotificationHistoryService(
      UserNotificationHistoryRepository userNotificationHistoryRepository) {
    this.userNotificationHistoryRepository = userNotificationHistoryRepository;
  }

  // 글작성 알람
  public void insertArticleNotification(Article article, Long userId) {
    UserNotificationHistory history = new UserNotificationHistory();
    history.setTitle("글이 작성되었습니다.");
    history.setContent(article.getTitle());
    history.setUserId(userId);
    userNotificationHistoryRepository.save(history);
  }

  // 댓글 작성 알람
  public void insertCommentNotification(Comment comment, Long userId) {
    UserNotificationHistory history = new UserNotificationHistory();
    history.setTitle("댓글이 작성되었습니다.");
    history.setContent(comment.getContent());
    history.setUserId(userId);
    userNotificationHistoryRepository.save(history);
  }

  // 알람 읽음 처리
  public void readNotification(String id) {
    Optional<UserNotificationHistory> history = userNotificationHistoryRepository.findById(id);
    if (history.isEmpty()) {
      return;
    }
    history.get().setIsRead(true);
    history.get().setUpdatedDate(LocalDateTime.now());
    userNotificationHistoryRepository.save(history.get());
  }
}
