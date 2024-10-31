package com.www.videoredis.domain.channel;

import com.www.videoredis.adapter.out.jpa.channel.ChannelJpaEntity;
import com.www.videoredis.adapter.out.redis.channel.ChannelRedisHash;
import com.www.videoredis.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class Channel {
    private String id; // 채널 ID
    private ChannelSnippet snippet; // 채널의 영상
    private ChannelStatistics statistics; // 채널 카운트,조회수,
    private User contentOwner; // 주인

    public static Channel from(ChannelRedisHash channel) {
        return Channel.builder()
            .id(channel.getId())
            .build();
    }

    public static Channel from(ChannelJpaEntity channel) {
        return Channel.builder()
            .id(channel.getId())
            .build();
    }
}
