package com.www.videoredis.application.port.in;

import com.www.videoredis.domain.user.User;

public interface UserUseCase {
  User getUser(String userId);

}
