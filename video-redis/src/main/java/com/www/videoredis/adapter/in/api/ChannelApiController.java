package com.www.videoredis.adapter.in.api;

import com.www.videoredis.adapter.in.api.dto.ChannelRequest;
import com.www.videoredis.application.port.in.ChannelUseCase;
import lombok.RequiredArgsConstructor;
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
  public void createChannel(@RequestBody ChannelRequest channelRequest) {
    channelUseCase.createChannel(channelRequest);
  }
  
  @PutMapping("/{channelId}")
  public void updateChannel(@PathVariable String channelId, @RequestBody ChannelRequest channelRequest) {
    channelUseCase.updateChannel(channelId, channelRequest);
  }
}
