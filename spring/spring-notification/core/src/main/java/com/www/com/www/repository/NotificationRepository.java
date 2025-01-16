package com.www.com.www.repository;

import com.www.com.www.domain.Notification;
import java.util.Optional;

public interface NotificationRepository {

  Optional<Notification> findById(Long id);

  Notification save(Notification notification);

  Notification delete(Long id);
}
