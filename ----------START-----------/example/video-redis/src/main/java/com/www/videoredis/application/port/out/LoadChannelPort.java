package com.www.videoredis.application.port.out;

import com.www.videoredis.domain.channel.Channel;
import java.util.Optional;

public interface LoadChannelPort {
  Optional<Channel> loadChannel(String id);
}
