package com.www.service.converter;

import com.www.client.PostClient;
import com.www.client.UserClient;
import com.www.domain.CommentNotification;
import com.www.domain.Post;
import com.www.domain.User;
import com.www.service.dto.ConvertedCommentNotification;
import org.springframework.stereotype.Service;

@Service
public class CommentUserNotificationConverter {

  private final UserClient userClient;
  private final PostClient postClient;

  public CommentUserNotificationConverter(UserClient userClient, PostClient postClient) {
    this.userClient = userClient;
    this.postClient = postClient;
  }

  public ConvertedCommentNotification convert(CommentNotification notification) {
    User user = userClient.getUser(notification.getWriterId());
    Post post = postClient.getPost(notification.getPostId());

    return new ConvertedCommentNotification(
        notification.getId(),
        notification.getType(),
        notification.getOccurredAt(),
        notification.getLastUpdatedAt(),
        user.getName(),
        user.getProfileImageUrl(),
        notification.getComment(),
        post.getImageUrl()
    );
  }
}
