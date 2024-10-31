package com.www.videoredis.adapter.out.jpa.channel;

import com.www.videoredis.domain.channel.ChannelSnippet;
import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Data
@Getter
public class ChannelSnippetJpaEntity {
  private String title;
  private String description;
  private LocalDateTime publishedAt;
  private String thumbnailUrl;

  public ChannelSnippetJpaEntity(String title, String description, String thumbnailUrl, LocalDateTime publishedAt) {
  }


  public static ChannelSnippetJpaEntity from(ChannelSnippet channelSnippet) {
    return new ChannelSnippetJpaEntity(channelSnippet.getTitle(), channelSnippet.getDescription(), channelSnippet.getThumbnailUrl(), channelSnippet.getPublishedAt());
  }

  public ChannelSnippet toDomain() {
    return ChannelSnippet.builder()
        .title(this.getTitle())
        .description(this.getDescription())
        .thumbnailUrl(this.getThumbnailUrl())
        .publishedAt(this.getPublishedAt())
        .build();
  }
}
