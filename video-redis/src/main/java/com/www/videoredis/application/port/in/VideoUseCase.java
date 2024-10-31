package com.www.videoredis.application.port.in;

import com.www.videoredis.domain.video.Video;
import java.util.List;

public interface VideoUseCase {
    Video getVideo(String videoId);
    List<Video> listVideos(String channelId);
    void increaseViewCount(String videoId);
}
