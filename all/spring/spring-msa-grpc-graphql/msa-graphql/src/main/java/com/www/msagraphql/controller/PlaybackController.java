package com.www.msagraphql.controller;

import com.www.msagraphql.model.EventLog;
import com.www.msagraphql.model.PlaybackRecord;
import com.www.msagraphql.service.PlaybackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class PlaybackController {

  private final PlaybackService playbackService;

  @Autowired
  public PlaybackController(PlaybackService playbackService) {
    this.playbackService = playbackService;
  }

  /**
   * # 재생 기록을 생성합니다.
   */

  @MutationMapping
  public PlaybackRecord startRecord(@Argument Long userId, @Argument Long fileId) {
    return playbackService.startRecord(userId, fileId);
  }

  /**
   * # 재생 기록을 종료합니다.
   */
  @MutationMapping
  public PlaybackRecord endRecord(@Argument Long recordId) {
    return playbackService.endRecord(recordId);
  }

  /**
   * # 이벤트 로깅합니다.
   */

  @MutationMapping
  public EventLog logEvent(@Argument Long recordId, @Argument Long userId,
      @Argument String eventType, @Argument String timestamp) {
    EventLog eventLog = new EventLog(null, recordId, userId, eventType, timestamp);
    return playbackService.logEvent(eventLog);
  }
}
