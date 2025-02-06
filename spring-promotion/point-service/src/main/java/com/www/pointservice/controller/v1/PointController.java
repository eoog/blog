package com.www.pointservice.controller.v1;

import com.www.pointservice.config.UserIdInterceptor;
import com.www.pointservice.domain.Point;
import com.www.pointservice.dto.PointDto;
import com.www.pointservice.dto.PointDto.Response;
import com.www.pointservice.service.v1.PointService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/points")
@RequiredArgsConstructor
public class PointController {

  private final PointService pointService;

  // 포인트 충전
  @PostMapping("/earn")
  public ResponseEntity<PointDto.Response> earnPoints(@Valid @RequestBody PointDto.EarnRequest request) {
    // 사용자 ID 조회 ( Header 값에서 가져옴 )
    Long userId = UserIdInterceptor.getCurrentUserId();
    Point point = pointService.earnPoints(userId, request.getAmount(), request.getDescription());
    return ResponseEntity.status(HttpStatus.CREATED).body(PointDto.Response.from(point));
  }

  // 포인트 사용
  @PostMapping("/use")
  public ResponseEntity<PointDto.Response> usePoints(@Valid @RequestBody PointDto.UseRequest request) {
    // 사용자 ID 조회 ( Header 값에서 가져옴 )
    Long userId = UserIdInterceptor.getCurrentUserId();
    Point point = pointService.usePoints(userId, request.getAmount(), request.getDescription());
    return ResponseEntity.status(HttpStatus.CREATED).body(PointDto.Response.from(point));
  }

  // 포인트 취소
  @PostMapping("/{pointId}/cancel")
  public ResponseEntity<PointDto.Response> cancelPoint(  @PathVariable Long pointId, @Valid @RequestBody PointDto.CancelReqeust reqeust) {
    Point point = pointService.cancelPoints(pointId, reqeust.getDescription());
    return ResponseEntity.ok(PointDto.Response.from(point));
  }

  // 유저의 적립금 잔액 조회
  @GetMapping("/users/{userId}/balance")
  public ResponseEntity<PointDto.BalanceResponse> getBalance(@PathVariable Long userId) {
    Long balance = pointService.getBalance(userId);
    return ResponseEntity.ok(PointDto.BalanceResponse.of(userId,balance));
  }

  // 유저의 적립금 사용 내역
  @GetMapping("/users/{userId}/history")
  public ResponseEntity<Page<PointDto.Response>> getPointHistory(
      @PathVariable Long userId,
      Pageable pageable
  ) {
    Page<Point> points = pointService.getPointHistory(userId, pageable);
    Page<Response> responses = points.map(point -> Response.from(point));
    return ResponseEntity.ok(responses);
  }
}
