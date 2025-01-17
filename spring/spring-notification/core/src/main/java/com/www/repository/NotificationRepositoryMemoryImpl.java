package com.www.repository;//package com.www.repository;
//
//import com.www.domain.Notification;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Optional;
//
//public class NotificationRepositoryMemoryImpl implements NotificationRepository {
//
//  private final Map<Long, Notification> memory = new HashMap<>();
//
//  @Override
//  public Optional<Notification> findById(Long id) {
//    return Optional.ofNullable(memory.get(id));
//  }
//
//  @Override
//  public Notification save(Notification notification) {
//    return memory.put(notification.id, notification);
//  }
//
//  @Override
//  public void delete(Long id) {
//    memory.remove(id);
//  }
//}
