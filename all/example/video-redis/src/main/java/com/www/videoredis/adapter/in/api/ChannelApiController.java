package com.www.videoredis.adapter.in.api;

import com.www.videoredis.adapter.in.api.dto.ChannelRequest;
import com.www.videoredis.application.port.in.ChannelUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.ReactiveRedisConnection.CommandResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/channels")
@RequiredArgsConstructor
public class ChannelApiController {

  private ChannelUseCase channelUseCase;

  public ChannelApiController(ChannelUseCase channelUseCase) {
    this.channelUseCase = channelUseCase;
  }

  @PostMapping
  public CommandResponse createChannel(@RequestBody ChannelRequest channelRequest) {
    var channel = channelUseCase.createChannel(channelRequest);

    return new CommandResponse(channel.getId(),null);
  }
  
  @PutMapping("/{channelId}")
  public void updateChannel(@PathVariable String channelId, @RequestBody ChannelRequest channelRequest) {
    channelUseCase.updateChannel(channelId, channelRequest);
  }
}
