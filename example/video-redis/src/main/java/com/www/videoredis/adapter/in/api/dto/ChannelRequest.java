package com.www.videoredis.adapter.in.api.dto;

import com.www.videoredis.domain.channel.ChannelSnippet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChannelRequest {

  private ChannelSnippetRequest snippet;
  private String contentOwnerId;
}
