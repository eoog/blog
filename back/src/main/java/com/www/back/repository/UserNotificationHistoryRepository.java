package com.www.back.repository;

import com.www.back.entity.UserNotificationHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserNotificationHistoryRepository extends
    MongoRepository<UserNotificationHistory, String> {

}
