package com.www.videoredis.adapter.out;

import com.www.videoredis.adapter.out.jpa.user.UserJpaEntity;
import com.www.videoredis.adapter.out.jpa.user.UserJpaRepository;
import com.www.videoredis.application.port.out.LoadUserPort;
import com.www.videoredis.domain.user.User;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements LoadUserPort {

  private final UserJpaRepository userJpaRepository;

  @Override
  public Optional<User> loadUser(String userId) {
    return userJpaRepository.findById(userId)
        .map(UserJpaEntity::toDomain);
  }
}
