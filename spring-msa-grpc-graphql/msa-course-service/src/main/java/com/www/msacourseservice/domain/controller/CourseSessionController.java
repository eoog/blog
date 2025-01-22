package com.www.msacourseservice.domain.controller;

import com.www.msacourseservice.domain.entity.CourseSession;
import com.www.msacourseservice.domain.service.CourseSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/courses/{courseId}/sessions")
public class CourseSessionController {


  private final CourseSessionService sessionService;

  @Autowired
  public CourseSessionController(CourseSessionService sessionService) {
    this.sessionService = sessionService;
  }

  @PostMapping
  @Operation(summary = "코스에 새로운 세션 추가", description = "지정된 코스에 새로운 세션을 추가합니다.", responses = {
      @ApiResponse(responseCode = "200", description = "성공적으로 세션이 추가됨", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CourseSession.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 입력"),
      @ApiResponse(responseCode = "500", description = "서버 내부 오류")
  })
  public ResponseEntity<CourseSession> addSessionToCourse(
      @Parameter(description = "세션을 추가할 코스의 ID", required = true)
      @PathVariable Long courseId,
      @Parameter(description = "추가할 세션 객체", required = true)
      @RequestBody CourseSession session) {
    CourseSession newSession = sessionService.addSessionToCourse(courseId, session);
    return ResponseEntity.ok(newSession);
  }

  @PutMapping("/{sessionId}")
  @Operation(summary = "기존 세션 수정", description = "기존 세션을 수정합니다.", responses = {
      @ApiResponse(responseCode = "200", description = "성공적으로 세션이 수정됨", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CourseSession.class))),
      @ApiResponse(responseCode = "404", description = "세션을 찾을 수 없음"),
      @ApiResponse(responseCode = "500", description = "서버 내부 오류")
  })
  public ResponseEntity<CourseSession> updateSession(
      @Parameter(description = "수정할 세션의 ID", required = true)
      @PathVariable Long sessionId,
      @Parameter(description = "수정된 세션 객체", required = true)
      @RequestBody CourseSession session) {
    CourseSession updatedSession = sessionService.updateSession(sessionId, session);
    return ResponseEntity.ok(updatedSession);
  }

  @GetMapping("/{sessionId}")
  @Operation(summary = "ID로 세션 조회", description = "ID로 세션을 조회합니다.", responses = {
      @ApiResponse(responseCode = "200", description = "성공적으로 세션을 조회함", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CourseSession.class))),
      @ApiResponse(responseCode = "404", description = "세션을 찾을 수 없음"),
      @ApiResponse(responseCode = "500", description = "서버 내부 오류")
  })
  public ResponseEntity<CourseSession> getSessionById(
      @Parameter(description = "조회할 세션의 ID", required = true)
      @PathVariable Long sessionId) {
    CourseSession session = sessionService.getSessionById(sessionId)
        .orElseThrow(() -> new RuntimeException("ID가 " + sessionId + "인 세션을 찾을 수 없습니다."));
    return ResponseEntity.ok(session);
  }

  @GetMapping
  @Operation(summary = "코스의 모든 세션 조회", description = "지정된 코스의 모든 세션을 조회합니다.", responses = {
      @ApiResponse(responseCode = "200", description = "성공적으로 세션 목록을 조회함", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CourseSession.class))),
      @ApiResponse(responseCode = "500", description = "서버 내부 오류")
  })
  public ResponseEntity<List<CourseSession>> getAllSessionsByCourseId(
      @Parameter(description = "세션을 조회할 코스의 ID", required = true)
      @PathVariable Long courseId) {
    List<CourseSession> sessions = sessionService.getAllSessionsByCourseId(courseId);
    return ResponseEntity.ok(sessions);
  }

}
