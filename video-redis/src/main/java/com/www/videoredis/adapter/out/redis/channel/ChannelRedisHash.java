package com.www.videoredis.adapter.out.redis.channel;

import com.www.videoredis.adapter.out.jpa.channel.ChannelJpaEntity;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("Channel")
@AllArgsConstructor
@Getter
public class ChannelRedisHash implements Serializable {
  private String id;

  public static ChannelRedisHash fromEntity(ChannelJpaEntity jpaEntity) {
    return new ChannelRedisHash(jpaEntity.getId());
  }
}
