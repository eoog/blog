package com.www.videoredis.application;

import com.www.videoredis.adapter.out.VideoPersistenceAdapter;
import com.www.videoredis.application.port.in.VideoUseCase;
import com.www.videoredis.domain.video.Video;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VideoService implements VideoUseCase {

  private final VideoPersistenceAdapter videoPersistenceAdapter;

  @Override
  public Video getVideo(String videoId) {
    Video video = videoPersistenceAdapter.loadVideo(videoId);
    Long viewCount = videoPersistenceAdapter.getViewCount(videoId);
    video.bindViewCount(viewCount);

    return video;
  }

  @Override
  public List<Video> listVideos(String channelId) {
    return videoPersistenceAdapter.loadVideoByChannel(channelId).stream()
        .map(video -> {
          Long viewCount = videoPersistenceAdapter.getViewCount(video.getId());
          video.bindViewCount(viewCount);
          return video;
        })
        .toList();
  }

  @Override
  public void increaseViewCount(String videoId) {
    videoPersistenceAdapter.incrementViewCount(videoId);
  }
}
