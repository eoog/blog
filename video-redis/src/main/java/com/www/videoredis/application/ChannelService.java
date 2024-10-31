package com.www.videoredis.application;

import com.www.videoredis.application.port.in.GetChannelUseCase;
import com.www.videoredis.application.port.out.LoadChannelPort;
import com.www.videoredis.domain.channel.Channel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChannelService implements GetChannelUseCase {

  private LoadChannelPort loadChannelPort;

  public ChannelService(LoadChannelPort loadChannelPort) {
    this.loadChannelPort = loadChannelPort;
  }

  @Override
  public Channel getChannel(String id) {
    return loadChannelPort.loadChannel(id).get();
  }
}
