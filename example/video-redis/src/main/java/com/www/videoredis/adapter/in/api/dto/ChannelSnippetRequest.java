package com.www.videoredis.adapter.in.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChannelSnippetRequest {
  private String title;
  private String description;
  private String thumbnailUrl;
}
