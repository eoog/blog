package com.www.videoredis.application.port.in;

import com.www.videoredis.domain.channel.Channel;

public interface GetChannelUseCase {
    Channel getChannel(String id);
}
