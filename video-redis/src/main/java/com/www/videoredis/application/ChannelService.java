package com.www.videoredis.application;

import com.www.videoredis.adapter.in.api.dto.ChannelRequest;
import com.www.videoredis.application.port.in.ChannelUseCase;
import com.www.videoredis.application.port.out.LoadChannelPort;
import com.www.videoredis.application.port.out.LoadUserPort;
import com.www.videoredis.application.port.out.SaveChannelPort;
import com.www.videoredis.domain.channel.Channel;
import com.www.videoredis.domain.channel.ChannelSnippet;
import com.www.videoredis.domain.channel.ChannelStatistics;
import com.www.videoredis.domain.user.User;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChannelService implements ChannelUseCase {
  private LoadChannelPort loadChannelPort;
  private LoadUserPort loadUserPort;
  private SaveChannelPort saveChannelPort;

  // 채널 생성
  @Override
  public Channel createChannel(ChannelRequest channelRequest) {

    // 주인
    var contentOwner = loadUserPort.loadUser(channelRequest.getContentOwnerId()).get();

    // 채널 생성
    var channel = Channel.builder()
        .id(UUID.randomUUID().toString())
        .snippet(
            ChannelSnippet.builder()
                .title(channelRequest.getSnippet().getTitle())
                .description(channelRequest.getSnippet().getDescription())
                .thumbnailUrl(channelRequest.getSnippet().getThumbnailUrl())
                .publishedAt(LocalDateTime.now())
                .build()
        )
        .statistics(
            ChannelStatistics.builder()
                .subscriberCount(0)
                .videoCount(0)
                .commentCount(0)
                .build()
        )
        .contentOwner(contentOwner)
        .build();

    // 채널 저장
    saveChannelPort.saveChannel(channel);
    return channel;
  }

  // 채널 업데이트
  @Override
  public Channel updateChannel(String channelId, ChannelRequest channelRequest) {

    // 채널 조회
    var channel = loadChannelPort.loadChannel(channelId).get();

    // 채널 업데이트 ( 상태전이 )
    channel.updateSnippet(channelRequest.getSnippet());

    return channel;
  }

  // 채널 조회
  @Override
  public Channel getChannel(String id) {
    return loadChannelPort.loadChannel(id).get();
  }
}
