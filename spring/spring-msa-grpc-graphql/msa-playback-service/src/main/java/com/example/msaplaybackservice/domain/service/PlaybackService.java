package com.example.msaplaybackservice.domain.service;


import com.example.msaplaybackservice.domain.entity.EventLog;
import com.example.msaplaybackservice.domain.entity.PlaybackRecord;
import com.example.msaplaybackservice.domain.repository.EventLogRepository;
import com.example.msaplaybackservice.domain.repository.PlaybackRecordRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.time.LocalDateTime;

@GrpcService
public class PlaybackService extends PlaybackServiceGrpc.PlaybackServiceImplBase {

    private final PlaybackRecordRepository playbackRecordRepository;

    private final EventLogRepository eventLogRepository;

    public PlaybackService(EventLogRepository eventLogRepository, PlaybackRecordRepository playbackRecordRepository) {
        this.eventLogRepository = eventLogRepository;
        this.playbackRecordRepository = playbackRecordRepository;
    }

    @Override
    public void startRecord(PlaybackServiceOuterClass.StartRecordRequest request, StreamObserver<PlaybackServiceOuterClass.StartRecordResponse> responseObserver) {
        PlaybackRecord record = new PlaybackRecord();
        record.setUserId(request.getUserId());
        record.setFileId(request.getFileId());
        record.setStartTime(LocalDateTime.now());  // Set current time as start time
        record = playbackRecordRepository.save(record);
        PlaybackServiceOuterClass.StartRecordResponse response = PlaybackServiceOuterClass.StartRecordResponse.newBuilder()
                .setRecord(record.toProto())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void endRecord(PlaybackServiceOuterClass.EndRecordRequest request, StreamObserver<PlaybackServiceOuterClass.EndRecordResponse> responseObserver) {
        PlaybackRecord record = playbackRecordRepository.findById(request.getRecordId()).orElse(null);
        if (record != null) {
            record.setEndTime(LocalDateTime.now());
            playbackRecordRepository.save(record);
            PlaybackServiceOuterClass.EndRecordResponse response = PlaybackServiceOuterClass.EndRecordResponse.newBuilder()
                    .setRecord(record.toProto())
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(new Throwable("Record not found"));
        }
    }

    @Override
    public void logEvent(PlaybackServiceOuterClass.LogEventRequest request, StreamObserver<PlaybackServiceOuterClass.LogEventResponse> responseObserver) {
        PlaybackRecord record = playbackRecordRepository.findById(request.getEvent().getRecordId()).orElse(null);
        if (record != null) {
            EventLog event = new EventLog();
            event.setPlaybackRecord(record);
            event.setUserId(request.getEvent().getUserId());
            event.setEventType(request.getEvent().getEventType());
            event.setTimestamp(LocalDateTime.now());
            event = eventLogRepository.save(event);
            PlaybackServiceOuterClass.LogEventResponse response = PlaybackServiceOuterClass.LogEventResponse.newBuilder()
                    .setEvent(event.toProto())
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(new Throwable("Record not found"));
        }
    }
}
