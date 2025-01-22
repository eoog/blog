package com.www.videoredis.application.port.in;

import com.www.videoredis.adapter.in.api.dto.ChannelRequest;
import com.www.videoredis.domain.channel.Channel;

public interface ChannelUseCase {
  Channel createChannel(ChannelRequest channelRequest);
  Channel updateChannel(String channelId, ChannelRequest channelRequest);
  Channel getChannel(String id);
}
