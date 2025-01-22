package com.www.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Post {

  private long id;
  private long userId;
  private String imageUrl;
  private String content;
}
