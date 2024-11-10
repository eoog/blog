package com.www.videoredis.adapter.out.jpa.user;

import com.www.videoredis.domain.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserJpaEntity {

  @Id
  private String id;

  private String name;

  public static UserJpaEntity from(User user) {
    return new UserJpaEntity(user.getId(), user.getName());
  }

  public User toDomain() {
    return User.builder()
        .id(this.getId())
        .name(this.getName())
        .build();
  }
}
