package com.www.msagraphql.controller;

import com.www.msagraphql.model.User;
import com.www.msagraphql.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {

  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  /**
   * 새로운 사용자를 생성합니다.
   */
  @MutationMapping
  public User createUser(@Argument String name, @Argument String email, @Argument String password) {
    return userService.createUser(name, email, password);
  }

  /**
   * 특정 사용자 정보를 변경합니다.
   */
  @MutationMapping
  public User updateUser(@Argument Long userId, @Argument String name, @Argument String email) {
    return userService.updateUser(userId, name, email);
  }

  /**
   * 특정 사용자를 조회합니다.
   */
  @QueryMapping
  public User getUser(@Argument Long userId) {
    return userService.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
  }

}
