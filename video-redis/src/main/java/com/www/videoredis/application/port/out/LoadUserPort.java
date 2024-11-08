package com.www.videoredis.application.port.out;

import com.www.videoredis.domain.user.User;
import java.util.Optional;

public interface LoadUserPort {
  Optional<User> loadUser(String userId);
}
