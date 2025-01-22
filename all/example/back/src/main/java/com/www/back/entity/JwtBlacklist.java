package com.www.back.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class JwtBlacklist {

  // 식별 아이디
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 로그인 토큰
  @Column(unique=true , nullable = false)
  private String token;

  // 접속 시간
  @Column(nullable = false)
  private LocalDateTime expirationTime;

  // 유저 닉네임
  @Column(nullable = false)
  private String username;
}
