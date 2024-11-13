package com.www.back.service;

import com.www.back.entity.Article;
import com.www.back.entity.Comment;
import com.www.back.entity.Notice;
import com.www.back.entity.User;
import com.www.back.entity.UserNotificationHistory;
import com.www.back.exception.ResourceNotFoundException;
import com.www.back.repository.NoticeRepository;
import com.www.back.repository.UserNotificationHistoryRepository;
import com.www.back.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserNotificationHistoryService {

  private final NoticeRepository noticeRepository;

  private final UserRepository userRepository;

  private final UserNotificationHistoryRepository userNotificationHistoryRepository;

  @Autowired
  public UserNotificationHistoryService(
      UserNotificationHistoryRepository userNotificationHistoryRepository,
      NoticeRepository noticeRepository, UserRepository userRepository) {
    this.userNotificationHistoryRepository = userNotificationHistoryRepository;
    this.noticeRepository = noticeRepository;
    this.userRepository = userRepository;
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

  // 공통 알림 확인기능
  public List<UserNotificationHistory> getNotificationList() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
    if (user.isEmpty()) {
      throw new ResourceNotFoundException("author not found");
    }

    LocalDateTime weekDate = LocalDateTime.now().minusWeeks(7);
    // 일주일 전 알림만 노출
    List<UserNotificationHistory> userNotificationHistoryList
        = userNotificationHistoryRepository.findByUserIdAndCreatedDateAfter(
        user.get().getId(), weekDate);

    // 일주일 전 공지만 추출
    List<Notice> notices = noticeRepository.findByCreatedDate(weekDate);

    // 유저한테 나갈 history 만들기
    List<UserNotificationHistory> results = new ArrayList<>();
    HashMap<Long, UserNotificationHistory> hashMap = new HashMap<>();
    for (UserNotificationHistory history : userNotificationHistoryList) {
      if (history.getNoticeId() != null) {
        // 공지시항 히스토리만 추가(중복제거하기 위함)
        hashMap.put(history.getNoticeId(), history);
      } else {
        results.add(history);
      }
    }
    for (Notice notice : notices) {
      UserNotificationHistory history = hashMap.get(notice.getId());
      if (history != null) {
        results.add(history);
      } else {
        history = new UserNotificationHistory();
        history.setTitle("공지사항이 작성되었습니다.");
        history.setContent(notice.getTitle());
        history.setUserId(user.get().getId());
        history.setIsRead(false);
        history.setNoticeId(notice.getId());
        history.setCreatedDate(notice.getCreatedDate());
        history.setUpdatedDate(null);
        results.add(history);
      }
    }

    return results;
  }
}
