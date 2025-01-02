package com.www.back.pojo;

import lombok.Data;

@Data
public class WriteComment {

  private String type = "write_comment";
  private Long commentId; // 댓글작성
}
