package com.www.service.converter;

import com.www.client.UserClient;
import com.www.domain.FollowNotification;
import com.www.domain.User;
import com.www.service.dto.ConvertedFollowNotification;
import org.springframework.stereotype.Service;

@Service
public class FollowUserNotificationConverter {

  private final UserClient userClient;

  public FollowUserNotificationConverter(UserClient userClient) {
    this.userClient = userClient;
  }

  public ConvertedFollowNotification convert(FollowNotification notification) {
    User user = userClient.getUser(notification.getFollowerId());
    boolean isFollowing = userClient.getIsFollowing(notification.getUserId(),
        notification.getFollowerId());

    return new ConvertedFollowNotification(
        notification.getId(),
        notification.getType(),
        notification.getOccurredAt(),
        notification.getLastUpdatedAt(),
        user.getName(),
        user.getProfileImageUrl(),
        isFollowing
    );
  }
}
