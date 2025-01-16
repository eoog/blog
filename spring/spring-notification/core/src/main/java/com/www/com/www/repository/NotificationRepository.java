package com.www.com.www.repository;

import com.www.com.www.domain.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification, Long> {

}
