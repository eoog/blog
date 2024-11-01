package com.www.back.controller;

import com.www.back.entity.User;
import com.www.back.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
  @PostMapping("")
  public ResponseEntity<User> createUser(@RequestParam String username, @RequestParam String password, @RequestParam String email) {
    User user = userService.createUser(username, password, email);
    return ResponseEntity.ok(user);
  }

  // 2. 회원 탈퇴 기능
  public ResponseEntity<Void> deleteUser(
      @Parameter(description = "유저 아이디 를 통한 삭제" , required = true)
      @PathVariable Long userId
  ) {
    userService.deleteUser(userId);
    return ResponseEntity.noContent().build();
  }

}
