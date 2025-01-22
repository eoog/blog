package com.www.videoredis.adapter.out.jpa.channel;

import com.www.videoredis.domain.channel.ChannelStatistics;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class ChannelStatisticsJpaEntity {
  private int viewCount;
  private int videoCount;
  private int subscriberCount;
  private int commentCount;

  public ChannelStatisticsJpaEntity(int videoCount, int subscriberCount, int commentCount) {
    this.videoCount = videoCount;
    this.subscriberCount = subscriberCount;
    this.commentCount = commentCount;
    this.viewCount = videoCount + subscriberCount + commentCount;
  }

  public static ChannelStatisticsJpaEntity from(ChannelStatistics statistics) {
    return new ChannelStatisticsJpaEntity(statistics.getVideoCount(), statistics.getSubscriberCount(), statistics.getCommentCount());
  }

  public ChannelStatistics toDomain() {
    return ChannelStatistics.builder()
        .videoCount(this.getVideoCount())
        .subscriberCount(this.getSubscriberCount())
        .commentCount(this.getCommentCount())
        .build();
  }
}
