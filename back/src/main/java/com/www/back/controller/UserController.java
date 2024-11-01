package com.www.back.controller;

import com.www.back.entity.User;
import com.www.back.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  // 1. 회원 가입 기능
  @PostMapping("/signUp")
  public ResponseEntity<User> createUser(@RequestParam String username, @RequestParam String password, @RequestParam String email) {
    User user = userService.createUser(username, password, email);
    return ResponseEntity.ok(user);
  }

  // 2. 회원 탈퇴 기능
  @DeleteMapping("/{userId}")
  public ResponseEntity<Void> deleteUser(
      @Parameter(description = "유저 아이디 를 통한 삭제" , required = true)
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

}
