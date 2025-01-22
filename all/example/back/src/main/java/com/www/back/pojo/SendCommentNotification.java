package com.www.back.pojo;

import lombok.Data;

@Data
public class SendCommentNotification {

  private String type = "send_comment_notification";
  private Long commentId; // 댓글
  private Long userId; // 유저
}
