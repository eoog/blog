package com.www.userservice.controller;

import com.www.userservice.dto.UserDto;
import com.www.userservice.entity.User;
import com.www.userservice.entity.UserLoginHistory;
import com.www.userservice.exception.DuplicateUserException;
import com.www.userservice.exception.UnauthorizedAccessException;
import com.www.userservice.exception.UserNotFoundException;
import com.www.userservice.service.UserService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  /**
   * 2025.01.02 회원가입
   */
  @PostMapping("/signup")
  public ResponseEntity<?> createUser(
      @RequestBody UserDto.SignupRequest request) {
    User user = userService.createUser(request.getEmail(), request.getPassword(),
        request.getName());
    return ResponseEntity.status(HttpStatus.CREATED).body(UserDto.Response.from(user));
  }

  /**
   * 2025.01.02 내정보 조회
   */
  @GetMapping("/me")
  public ResponseEntity<?> getProfile(
      @RequestHeader("X-USER-ID") Integer userId) {
    User user = userService.getUserById(userId);
    return ResponseEntity.ok(UserDto.Response.from(user));
  }

  /**
   * 2025.01.02 내정보 업데이트
   */
  @PutMapping("/me")
  public ResponseEntity<?> updateProfile(
      @RequestHeader("X-USER-ID") Integer userId,
      @RequestBody UserDto.UpdateRequest request) {
    User user = userService.updateUser(userId, request.getName());
    return ResponseEntity.ok(UserDto.Response.from(user));
  }

  /**
   * 2025.01.02 회원 비밀번호 변경
   */
  @PostMapping("/me/password")
  public ResponseEntity<?> changePassword(
      @RequestHeader("X-USER-ID") Integer userId,
      @RequestBody UserDto.PasswordChangeRequest request) {
    userService.changePassword(userId, request.getCurrentPassword(), request.getNewPassword());
    return ResponseEntity.ok().build();
  }

  /**
   * 2025.01.02 회원 로그인 이력 조회
   */
  @GetMapping("/me/login-history")
  public ResponseEntity<List<UserLoginHistory>> getLoginHistory(
      @RequestHeader("X-USER-ID") Integer userId) {
    List<UserLoginHistory> history = userService.getUserLoginHistory(userId);
    return ResponseEntity.ok(history);
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<String> handleUserNotFound(UserNotFoundException exception) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
  }

  @ExceptionHandler(DuplicateUserException.class)
  public ResponseEntity<String> handleDuplicateUser(DuplicateUserException exception) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
  }

  @ExceptionHandler(UnauthorizedAccessException.class)
  public ResponseEntity<String> handleUnauthorizedAccess(UnauthorizedAccessException exception) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.getMessage());
  }
}
