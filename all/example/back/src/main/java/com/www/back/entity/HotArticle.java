package com.www.back.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HotArticle implements Serializable {

  private Long id;
  private String title;
  private String content;
  private String authorName;
  private LocalDateTime createdDate;
  private LocalDateTime updatedDate;
  private Long viewCount = 0L;
}
