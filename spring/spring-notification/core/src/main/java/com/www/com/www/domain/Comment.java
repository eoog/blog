package com.www.com.www.domain;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Comment {

  private long id;
  private long userId;
  private String content;
  private Instant createdAt;
}

