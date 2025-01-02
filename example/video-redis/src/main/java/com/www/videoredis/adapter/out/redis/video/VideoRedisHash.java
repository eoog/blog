package com.www.videoredis.adapter.out.redis.video;

import com.www.videoredis.adapter.out.jpa.video.VideoJpaEntity;
import com.www.videoredis.adapter.out.redis.channel.ChannelRedisHash;
import com.www.videoredis.domain.video.Video;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("video")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class VideoRedisHash implements Serializable {
  private String id;
  private String title;
  private String description;
  private String thumbnail;
  private ChannelRedisHash channel;
  private LocalDateTime publishedAt;

  public static VideoRedisHash fromJpaEntity(VideoJpaEntity videoJpaEntity) {
    return new VideoRedisHash(
        videoJpaEntity.getId(),
        videoJpaEntity.getTitle(),
        videoJpaEntity.getDescription(),
        videoJpaEntity.getThumbnailUrl(),
        ChannelRedisHash.fromEntity(videoJpaEntity.getChannel()),
        videoJpaEntity.getPublishedAt()
    );
  }

  public Video toDomain() {
    return Video.builder()
        .id(this.getId())
        .title(this.getTitle())
        .description(this.getDescription())
        .thumbnail(this.getThumbnail())
        .channel(this.getChannel().toDomain())
        .publishedAt(this.getPublishedAt())
        .build();
  }
}

