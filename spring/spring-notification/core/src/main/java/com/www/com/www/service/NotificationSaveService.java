package com.www.com.www.service;

import com.www.com.www.domain.Notification;
import com.www.com.www.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationSaveService {

  private final NotificationRepository repository;

  public NotificationSaveService(NotificationRepository repository) {
    this.repository = repository;
  }

  public void insert(Notification notification) {
    Notification result = repository.insert(notification);
    log.info("inserted: {}", result);
  }

  public void upsert(Notification notification) {
    Notification result = repository.save(notification);
    log.info("upserted: {}", result);
  }
}
