package com.www.videoredis.application.port.out;

import com.www.videoredis.domain.video.Video;
import java.util.List;

public interface LoadVideoPort {
  Video loadVideo(String videoId);
  List<Video> loadVideoByChannel(String channelId);
  Long getViewCount(String videoId);
}
