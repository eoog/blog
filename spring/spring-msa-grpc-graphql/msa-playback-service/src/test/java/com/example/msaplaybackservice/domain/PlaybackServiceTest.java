package com.example.msaplaybackservice.domain;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.msaplaybackservice.domain.entity.EventLog;
import com.example.msaplaybackservice.domain.entity.PlaybackRecord;
import com.example.msaplaybackservice.domain.repository.EventLogRepository;
import com.example.msaplaybackservice.domain.repository.PlaybackRecordRepository;
import com.example.msaplaybackservice.domain.service.PlaybackService;
import com.example.msaplaybackservice.domain.service.PlaybackServiceOuterClass;
import io.grpc.stub.StreamObserver;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PlaybackServiceTest {

  @Mock
  private PlaybackRecordRepository playbackRecordRepository;

  @Mock
  private EventLogRepository eventLogRepository;

  @Mock
  private StreamObserver<PlaybackServiceOuterClass.StartRecordResponse> startResponseObserver;

  @Mock
  private StreamObserver<PlaybackServiceOuterClass.EndRecordResponse> endResponseObserver;

  @Mock
  private StreamObserver<PlaybackServiceOuterClass.LogEventResponse> logEventResponseObserver;

  @InjectMocks
  private PlaybackService playbackService;

  @Test
  void testStartRecord() {
    when(playbackRecordRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

    PlaybackServiceOuterClass.StartRecordRequest request = PlaybackServiceOuterClass.StartRecordRequest.newBuilder()
        .setUserId(1L)
        .setFileId(1L)
        .build();

    playbackService.startRecord(request, startResponseObserver);

    verify(playbackRecordRepository).save(any(PlaybackRecord.class));
    verify(startResponseObserver).onNext(any());
    verify(startResponseObserver).onCompleted();
  }

  @Test
  void testEndRecord() {
    PlaybackRecord record = new PlaybackRecord();
    record.setRecordId(1L);
    record.setUserId(1L);
    record.setFileId(1L);
    record.setStartTime(LocalDateTime.now());

    when(playbackRecordRepository.findById(anyLong())).thenReturn(Optional.of(record));

    PlaybackServiceOuterClass.EndRecordRequest request = PlaybackServiceOuterClass.EndRecordRequest.newBuilder()
        .setRecordId(1L)
        .build();

    playbackService.endRecord(request, endResponseObserver);

    verify(playbackRecordRepository).save(record);
    verify(endResponseObserver).onNext(any());
    verify(endResponseObserver).onCompleted();
  }

  @Test
  void testLogEvent() {
    PlaybackRecord record = new PlaybackRecord();
    record.setRecordId(1L);
    record.setUserId(1L);
    record.setFileId(1L);
    record.setStartTime(LocalDateTime.now());

    when(playbackRecordRepository.findById(anyLong())).thenReturn(Optional.of(record));

    EventLog expectedEvent = new EventLog();
    expectedEvent.setPlaybackRecord(record);
    expectedEvent.setUserId(1L); // Matching the userId type
    expectedEvent.setEventType("PLAY");
    expectedEvent.setTimestamp(LocalDateTime.now());

    // Mock the save method to return the expected event
    when(eventLogRepository.save(any(EventLog.class))).thenReturn(expectedEvent);


    PlaybackServiceOuterClass.LogEventRequest request = PlaybackServiceOuterClass.LogEventRequest.newBuilder()
        .setEvent(PlaybackServiceOuterClass.EventLog.newBuilder()
            .setRecordId(1L)
            .setUserId(1L)
            .setEventType("PLAY"))
        .build();

    playbackService.logEvent(request, logEventResponseObserver);

    verify(eventLogRepository).save(any(EventLog.class));
    verify(logEventResponseObserver).onNext(any());
    verify(logEventResponseObserver).onCompleted();
  }
}
