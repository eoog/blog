package com.www.back.repository;

import com.www.back.entity.UserNotificationHistory;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserNotificationHistoryRepository extends
    MongoRepository<UserNotificationHistory, String> {

  List<UserNotificationHistory> findByUserIdAndCreatedDateAfter(Long userId, LocalDateTime date);
}
