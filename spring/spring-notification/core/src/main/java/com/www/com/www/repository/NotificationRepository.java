package com.www.com.www.repository;

import com.www.com.www.domain.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, Long> {

  //Optional<Notification> findById(Long id);

  //Notification save(Notification notification);

  //void delete(Long id);
}
