package com.www.videoredis.adapter.in.api;

import com.www.videoredis.application.port.in.VideoUseCase;
import com.www.videoredis.domain.video.Video;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/videos")
@RequiredArgsConstructor
public class VideoApiController {

  private VideoUseCase videoUseCase;

  public VideoApiController(VideoUseCase videoUseCase) {
    this.videoUseCase = videoUseCase;
  }

  @GetMapping("{videoId}")
  public Video getVideo(@PathVariable String videoId) {
    return videoUseCase.getVideo(videoId);
  }

  @GetMapping(params = "channelId")
  public List<Video> listVideo(@RequestParam String channelId) {
    return videoUseCase.listVideos(channelId);
  }

  @PostMapping("{videoId}/view")
  public void increaseVideoViewCount(@PathVariable String videoId) {
    videoUseCase.increaseViewCount(videoId);
  }


}
