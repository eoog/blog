package com.www.videoredis.domain.video;

import com.www.videoredis.domain.channel.Channel;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class Video implements Serializable {
  private String id;
  private String title;
  private String description;
  private String thumbnail;
  private Channel channel;
  private Long viewCount;
  private LocalDateTime publishedAt;

  public void bindViewCount(long viewCount) {
    this.viewCount = viewCount;
  }
}
