package com.www.videoredis.application.port.out;

import com.www.videoredis.domain.channel.Channel;

public interface SaveChannelPort {
  void saveChannel(Channel channel);
}
