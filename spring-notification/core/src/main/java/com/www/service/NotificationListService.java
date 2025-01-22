package com.www.service;

import com.www.domain.Notification;
import com.www.repository.NotificationRepository;
import com.www.service.dto.GetUserNotificationsByPivotResult;
import java.time.Instant;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
public class NotificationListService {

  private final NotificationRepository repository;

  public NotificationListService(NotificationRepository repository) {
    this.repository = repository;
  }

  // 목록조회 : Pivot 방식 ( 기준점 : occurredAt,size) vs Pageing방식 (page size , page number )
  public GetUserNotificationsByPivotResult getUserNotificationsByPivot(long userId,
      Instant occurredAt) {
    Slice<Notification> result;

    if (occurredAt == null) {
      result = repository.findAllByUserIdOrderByOccurredAtDesc(userId,
          PageRequest.of(0, PAGE_SIZE));
    } else {
      result = repository.findAllByUserIdAndOccurredAtLessThanOrderByOccurredAtDesc(userId,
          occurredAt, PageRequest.of(0, PAGE_SIZE));
    }

    return new GetUserNotificationsByPivotResult(
        result.toList(),
        result.hasNext()
    );
  }

  private static final int PAGE_SIZE = 20;
}

