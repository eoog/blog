package com.www.service.converter;

import com.www.client.PostClient;
import com.www.client.UserClient;
import com.www.domain.LikeNotification;
import com.www.domain.Post;
import com.www.domain.User;
import com.www.service.dto.ConvertedLikeNotification;
import org.springframework.stereotype.Service;

@Service
public class LikeUserNotificationConverter {

  private final UserClient userClient;
  private final PostClient postClient;

  public LikeUserNotificationConverter(UserClient userClient, PostClient postClient) {
    this.userClient = userClient;
    this.postClient = postClient;
  }

  public ConvertedLikeNotification convert(LikeNotification notification) {
    User user = userClient.getUser(notification.getLikerIds().getLast());
    Post post = postClient.getPost(notification.getPostId());

    return new ConvertedLikeNotification(
        notification.getId(),
        notification.getType(),
        notification.getOccurredAt(),
        notification.getLastUpdatedAt(),
        user.getName(),
        user.getProfileImageUrl(),
        notification.getLikerIds().size(),
        post.getImageUrl()
    );
  }
}
