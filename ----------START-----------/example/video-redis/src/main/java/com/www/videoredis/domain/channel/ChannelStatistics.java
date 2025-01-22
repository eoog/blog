package com.www.videoredis.domain.channel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class ChannelStatistics {
    private int viewCount;
    private int videoCount;
    private int subscriberCount;
    private int commentCount;
}
