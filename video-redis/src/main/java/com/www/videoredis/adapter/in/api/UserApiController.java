package com.www.videoredis.adapter.in.api;

import com.www.videoredis.adapter.in.api.constant.HeaderAttribute;
import com.www.videoredis.application.port.in.UserUseCase;
import com.www.videoredis.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserApiController {

  private UserUseCase userUserCase;

  public UserApiController(UserUseCase userUserCase) {
    this.userUserCase = userUserCase;
  }

  @GetMapping
  public User getUSer(
      @RequestHeader(value = HeaderAttribute.X_AUTH_KEY) String authKey
  ) {
    return userUserCase.getUser(authKey);

  }

}
