package com.www.com.www.service;

import com.www.com.www.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationRemoveService {

  private final NotificationRepository repository;


  public void deleteById(String id) {
    log.info("deleted: {}", id);
    repository.deleteById(id);
  }
  
}
