package com.www.back.controller;

import com.www.back.dto.SignUpUser;
import com.www.back.entity.User;
import com.www.back.entity.UserNotificationHistory;
import com.www.back.jwt.JwtUtil;
import com.www.back.service.CustomUserDetailsService;
import com.www.back.service.JwtBlacklistService;
import com.www.back.service.UserNotificationHistoryService;
import com.www.back.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final AuthenticationManager authenticationManager;
  private final UserService userService;
  private final JwtUtil jwtUtil;
  private final CustomUserDetailsService userDetailsService;
  private final JwtBlacklistService jwtBlacklistService;
  private final UserNotificationHistoryService userNotificationHistoryService;

  @Autowired
  public UserController(UserService userService, AuthenticationManager authenticationManager,
      JwtUtil jwtUtil, CustomUserDetailsService userDetailsService,
      JwtBlacklistService jwtBlacklistService,
      UserNotificationHistoryService userNotificationHistoryService) {
    this.userService = userService;
    this.authenticationManager = authenticationManager;
    this.jwtUtil = jwtUtil;
    this.userDetailsService = userDetailsService;
    this.jwtBlacklistService = jwtBlacklistService;
    this.userNotificationHistoryService = userNotificationHistoryService;
  }

  @PostMapping("/signUp")
  public ResponseEntity<User> createUser(@RequestBody SignUpUser signUpUser) {
    User user = userService.createUser(signUpUser);
    return ResponseEntity.ok(user);
  }

  // 2. 회원 탈퇴 기능
  @DeleteMapping("/{userId}")
  public ResponseEntity<Void> deleteUser(
      @Parameter(description = "유저 아이디 를 통한 삭제", required = true)
      @PathVariable Long userId
  ) {
    userService.deleteUser(userId);
    return ResponseEntity.noContent().build();
  }

  // 3. 모든 회원 조회
  @GetMapping("")
  public ResponseEntity<List<User>> getUserS() {
    return ResponseEntity.of(Optional.ofNullable(userService.getUserS()));
  }

  // 4. 로그인
  @PostMapping("/login")
  public String login(@RequestParam String username, @RequestParam String password,
      HttpServletResponse response) {
    // 시큐리티 인증
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

    String token = jwtUtil.generateToken(userDetails.getUsername());
    Cookie cookie = new Cookie("onion_token", token);
    cookie.setHttpOnly(true);
    cookie.setPath("/");
    cookie.setMaxAge(60 * 60);

    response.addCookie(cookie);
    return token;
  }

  // 5. 토큰 검증
  @PostMapping("/token/validation")
  @ResponseStatus(HttpStatus.OK)
  public void jwtTokenValidation(@RequestParam String token) {
    if (!jwtUtil.validateToken(token)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Token is not validation");
    }
  }

  // 6. 로그아웃 기능
  @PostMapping("/logout")
  public void logout(HttpServletResponse response) {
    Cookie cookie = new Cookie("onion_token", null);
    cookie.setHttpOnly(true);
    cookie.setPath("/");
    cookie.setMaxAge(0); // 쿠키 삭제
    response.addCookie(cookie);
  }

  // 7. 모든 계정 로그아웃
  public void logout(@RequestParam(required = false) String requestToken,
      @CookieValue(value = "onion_token", required = false) String cookieToken,
      HttpServletRequest request, HttpServletResponse response) {
    // 생성
    String token = null;
    // Authorzition 검증할때
    String bearerToken = request.getHeader("Authorization");

    // 토큰 설정
    // 내가 원하는 유저의 token 을 적용
    if (requestToken != null) {
      token = requestToken;
      // 웹브라우저상 쿠키에 등록된 토큰
    } else if (cookieToken != null) {
      token = cookieToken;
      // Header 부분에 적용한 토큰
    } else if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      token = bearerToken.substring(7);
    }

    // 날짜 비교하기 현재 시간으로
    Instant instant = new Date().toInstant();

    // 시스템 default 로 날짜 생성 ( 위에랑 비교하기 위해)
    LocalDateTime expirationTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();

    // 토큰을 사용하여 username 얻기
    String username = jwtUtil.getUserFromToken(token);

    // logout/all 은 여러대가 생겨서 모두 로그아웃
    jwtBlacklistService.blacklistToken(token, expirationTime, username);

    // 토큰 제거
    Cookie cookie = new Cookie("onion_token", null);
    cookie.setHttpOnly(true);
    cookie.setPath("/");
    cookie.setMaxAge(0); // 쿠키 삭제
    response.addCookie(cookie);
  }

  // 알람 읽기 히스토리 아이디인지,, 유저아이디인지 확인
  @PostMapping("/history")
  @ResponseStatus(HttpStatus.OK)
  public void readHistory(@RequestParam String historyId) {
    userNotificationHistoryService.readNotification(historyId);
  }

  @GetMapping("/history")
  public ResponseEntity<List<UserNotificationHistory>> getHistoryList() {
    return ResponseEntity.ok(userNotificationHistoryService.getNotificationList());
  }
}
