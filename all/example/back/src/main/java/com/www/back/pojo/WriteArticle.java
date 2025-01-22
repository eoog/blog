package com.www.back.pojo;

import lombok.Data;

@Data
public class WriteArticle {

  private String type = "write_article"; // 메시지 작성
  private Long articleId; // 게시글 아이디
  private Long userId; // 유저 아이디
}
