package com.www.videoredis.application;

import com.www.videoredis.adapter.out.UserPersistenceAdapter;
import com.www.videoredis.application.port.in.UserUseCase;
import com.www.videoredis.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserUseCase {
  private final UserPersistenceAdapter userPersistenceAdapter;
  
  // 유저 조회
  @Override
  public User getUser(String userId) {
    return  userPersistenceAdapter.loadUser(userId)
        .orElseThrow();
  }
}
