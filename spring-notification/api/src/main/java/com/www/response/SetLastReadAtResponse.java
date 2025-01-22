package com.www.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "사용자 알림 목록 읽은 시간 기록 응답")
public class SetLastReadAtResponse {

  @Schema(description = "기록된 읽은 시간")
  private Instant lastReadAt;
}
