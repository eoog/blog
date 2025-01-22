package com.www.userservice.controller;

import com.www.userservice.dto.UserDto;
import com.www.userservice.dto.UserDto.TokenResponse;
import com.www.userservice.entity.User;
import com.www.userservice.service.JWTService;
import com.www.userservice.service.UserService;
import io.jsonwebtoken.Claims;
import java.util.Collections;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class AuthController {

  private final JWTService jwtService;
  private final UserService userService;

  public AuthController(JWTService jwtService, UserService userService) {
    this.jwtService = jwtService;
    this.userService = userService;
  }

  /**
   * 2025.01.02 회원 로그인
   */
  @PostMapping("/login")
  public ResponseEntity<?> login(
      @RequestBody UserDto.LoginRequest request) {
    User user = userService.authenticate(request.getEmail(), request.getPassword());
    String token = jwtService.generateToken(user);
    return ResponseEntity.ok(UserDto.LoginResponse.builder()
        .token(token)
        .user(UserDto.Response.from(user))
        .build());
  }

  /**
   * 2025.01.02 회원 토큰 검증
   */
  @PostMapping("/validate-token")
  public ResponseEntity<?> validateToken(
      @RequestBody UserDto.TokenRequest request) {
    Claims claims = jwtService.validateToken(request.getToken());
    return ResponseEntity.ok(TokenResponse.builder()
        .id((claims.get("id", Integer.class)))
        .email(claims.getSubject())
        .valid(true)
        .role(claims.get("role", String.class))
        .build());
  }

  /**
   * 2025.01.02 회원 토큰 재발급
   */
  @PostMapping("/refresh-token")
  public ResponseEntity<Map<String, String>> refreshToken(
      @RequestBody UserDto.TokenRequest tokenRequest) {
    String newToken = jwtService.refreshToken(tokenRequest.getToken());
    return ResponseEntity.ok(Collections.singletonMap("token", newToken));
  }
}
