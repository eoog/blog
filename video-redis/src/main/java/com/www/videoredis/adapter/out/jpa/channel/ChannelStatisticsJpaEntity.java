package com.www.videoredis.adapter.out.jpa.channel;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChannelStatisticsJpaEntity {
  private int viewCount;
  private int videoCount;
  private int subscriberCount;
  private int commentCount;
}
