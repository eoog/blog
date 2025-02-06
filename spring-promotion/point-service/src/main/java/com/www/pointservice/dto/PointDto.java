package com.www.pointservice.dto;

import com.www.pointservice.domain.Point;
import com.www.pointservice.domain.PointType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

public class PointDto {

  // 포인트 충전 DTO
  @Getter
  @Builder
  public static class Response {
    private Long id;
    private Long userId;
    private Long amount;
    private PointType type;
    private String description;
    private Long balanceSnapshot;
    private LocalDateTime createdAt;

    public static Response from(Point point) {
      return Response.builder()
          .id(point.getId())
          .userId(point.getUserId())
          .amount(point.getAmount())
          .type(point.getType())
          .description(point.getDescription())
          .balanceSnapshot(point.getBalanceSnapshot())
          .createdAt(point.getCreatedAt())
          .build();
    }
  }

  @Getter
  @Builder
  public static class BalanceResponse {
    private Long userId;
    private Long balance;

    public static BalanceResponse of(Long userId, Long balance) {
      return BalanceResponse.builder()
          .userId(userId)
          .balance(balance)
          .build();
    }
  }

  // 포인트 충전 REQEUST_BODY 값 받기
  @Getter
  @Builder
  public static class EarnRequest {
    private Long userId;

    @NotNull(message = "금액이 없습니다.")
    @Min(value = 1, message = "amount must be greater than 0")
    private Long amount;

    @NotBlank(message = "내용이 없습니다.")
    private String description;
  }


  // 포인트 사용
  @Getter
  @Builder
  public static class UseRequest {
    private Long userId;

    @NotNull(message = "금액이 없습니다.")
    @Min(value = 1, message = "amount must be greater than 0")
    private Long amount;

    @NotBlank(message = "내용이 없습니다.")
    private String description;
  }

  // 포인트 취소
  @Getter
  @Builder
  public static class CancelReqeust {

    @NotNull(message = "금액이 없습니다.")
    @Min(value = 1,message = "최소 금액은 1원입니다")
    private Long amount;

    @NotBlank(message = "내용이 없습니다")
    private String description;
  }


}
