package com.www.msagraphql.service;

import com.example.msaplaybackservice.domain.service.PlaybackServiceGrpc;
import com.example.msaplaybackservice.domain.service.PlaybackServiceOuterClass;
import com.www.msagraphql.model.EventLog;
import com.www.msagraphql.model.PlaybackRecord;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class PlaybackService {

  @GrpcClient("msa-playback-service")
  private PlaybackServiceGrpc.PlaybackServiceBlockingStub playbackServiceStub;

  /**
   * # 재생 기록을 생성합니다.
   */

  public PlaybackRecord startRecord(Long userId, Long fileId) {
    PlaybackRecord record = new PlaybackRecord();
    record.setUserId(userId);
    record.setFileId(fileId);

    PlaybackServiceOuterClass.StartRecordRequest request = PlaybackServiceOuterClass.StartRecordRequest.newBuilder()
        .setUserId(userId)
        .setFileId(fileId)
        .build();
    PlaybackServiceOuterClass.StartRecordResponse response = playbackServiceStub.startRecord(
        request);
    return PlaybackRecord.fromProto(response.getRecord());
  }

  /**
   * # 재생 기록을 종료합니다.
   */

  public PlaybackRecord endRecord(Long recordId) {
    PlaybackServiceOuterClass.EndRecordRequest request = PlaybackServiceOuterClass.EndRecordRequest.newBuilder()
        .setRecordId(recordId)
        .build();
    PlaybackServiceOuterClass.EndRecordResponse response = playbackServiceStub.endRecord(request);
    return PlaybackRecord.fromProto(response.getRecord());
  }


  /**
   * # 이벤트를 로깅합니다.
   */
  public EventLog logEvent(EventLog eventLog) {
    PlaybackServiceOuterClass.LogEventRequest request = PlaybackServiceOuterClass.LogEventRequest.newBuilder()
        .setEvent(EventLog.toProto(eventLog))
        .build();
    PlaybackServiceOuterClass.LogEventResponse response = playbackServiceStub.logEvent(request);
    return EventLog.fromProto(response.getEvent());
  }
}
