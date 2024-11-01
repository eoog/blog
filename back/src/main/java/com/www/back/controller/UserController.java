package com.www.back.controller;

import com.www.back.dto.SignUpUser;
import com.www.back.entity.User;
import com.www.back.jwt.JwtUtil;
import com.www.back.service.CustomUserDetailsService;
import com.www.back.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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


  @Autowired
  public UserController(UserService userService, AuthenticationManager authenticationManager,
      JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
    this.userService = userService;
    this.authenticationManager = authenticationManager;
    this.jwtUtil = jwtUtil;
    this.userDetailsService = userDetailsService;
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
  public String login(@RequestParam String username, @RequestParam String password) {
    Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    return jwtUtil.generateToken(userDetails.getUsername());
  }

  // 5. 토큰 검증
  @PostMapping("/token/validation")
  @ResponseStatus(HttpStatus.OK)
  public void jwtTokenValidation(@RequestParam String token) {
    if (!jwtUtil.validateToken(token)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN , "Token is not validation");
    }
  }

}
