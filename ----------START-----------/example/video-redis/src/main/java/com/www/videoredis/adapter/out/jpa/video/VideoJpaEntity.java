package com.www.videoredis.adapter.out.jpa.video;

import com.www.videoredis.adapter.out.jpa.channel.ChannelJpaEntity;
import com.www.videoredis.domain.video.Video;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "video")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class VideoJpaEntity {
  @Id
  private String id;
  private String title;
  private String description;
  private String thumbnailUrl;
  @ManyToOne
  @JoinColumn(name = "channel_id")
  private ChannelJpaEntity channel;
  private LocalDateTime publishedAt;

  public Video toDomain() {
    return Video.builder()
        .id(this.getId())
        .title(this.getTitle())
        .description(this.getDescription())
        .thumbnail(this.getThumbnailUrl())
        .channel(this.getChannel().toDomain())
        .publishedAt(this.getPublishedAt())
        .build();
  }
}


