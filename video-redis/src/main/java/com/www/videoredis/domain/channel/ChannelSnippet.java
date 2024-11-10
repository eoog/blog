package com.www.videoredis.domain.channel;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class ChannelSnippet {

  private String title; // 제목
  private String description; // 내용
  private LocalDateTime publishedAt;
  private String thumbnailUrl;
}
