package com.www.msagraphql.model;

import com.example.msaplaybackservice.domain.service.PlaybackServiceOuterClass;
import java.io.Serializable;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventLog implements Serializable {

  private Long eventId;
  private Long recordId;
  private Long userId;
  private String eventType;
  private String timestamp;

  public static EventLog fromProto(PlaybackServiceOuterClass.EventLog proto) {
    EventLog log = new EventLog();
    log.setEventId(proto.getEventId());
    log.setRecordId(proto.getRecordId());
    log.setUserId(proto.getUserId());
    log.setEventType(proto.getEventType());
    log.setTimestamp(Instant.ofEpochMilli(proto.getTimestamp()).toString());
    return log;
  }

  public static PlaybackServiceOuterClass.EventLog toProto(EventLog domain) {
    return PlaybackServiceOuterClass.EventLog.newBuilder()
        .setEventId(domain.getEventId())
        .setRecordId(domain.getRecordId())
        .setUserId(domain.getUserId())
        .setEventType(domain.getEventType())
        .setTimestamp(Instant.parse(domain.getTimestamp()).toEpochMilli())
        .build();
  }
}
