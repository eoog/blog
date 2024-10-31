package com.www.videoredis.adapter.out.jpa.channel;

import com.www.videoredis.domain.channel.Channel;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "channel") // 테이블
@NoArgsConstructor // 기본생성자
@AllArgsConstructor // 전체생성자
@Getter
public class ChannelJpaEntity {
    @Id
  private String id;

  @Embedded
  private ChannelSnippetJpaEntity channelSnippet;

  @Embedded
  private ChannelStatisticsJpaEntity statistics;

  public Channel toDomain() {
    return Channel.builder()
        .id(this.getId())
        .build();
  }
}
