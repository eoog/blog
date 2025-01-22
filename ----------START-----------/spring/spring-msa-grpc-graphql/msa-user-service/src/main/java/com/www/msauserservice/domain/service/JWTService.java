package com.www.msauserservice.domain.service;

import com.www.msauserservice.domain.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JWTService {

  private final PasswordEncoder passwordEncoder;

  @Value("${jwt.secret}")
  private String secretKey;

  public JWTService(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  // 토큰 생성
  public String generateToken(User existingUser, String requestPassword) {

    if (!passwordEncoder.matches(requestPassword,existingUser.getPasswordHash())) {
      throw new IllegalArgumentException("비밀번호가 맞지 않습니다.");
    }

    long currentTimeMillis = System.currentTimeMillis();
    return Jwts.builder()
        .subject(existingUser.getEmail())
        .issuedAt(new Date(currentTimeMillis))
        .expiration(new Date(currentTimeMillis + 3600000)) // 1시간
        .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
        .compact();
  }

  // 토큰 검증
  public boolean validateToken(String token) {
    try {
      parseJwtClaims(token);
      return true;
    } catch (Exception e) {
      log.error("validateToken error", e);
      return  false;
    }
  }

  // 토큰 갱신
  public String refreshToken(String token) {
    Claims claims = parseJwtClaims(token);
    long currentTimeMillis = System.currentTimeMillis();
    return Jwts.builder()
        .subject(claims.getSubject())
        .issuedAt(new Date(currentTimeMillis))
        .expiration(new Date(currentTimeMillis + 3600000))
        .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
        .compact();
  }

  // 토큰
  public Claims parseJwtClaims(String token) {
    return Jwts.parser()
        .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }
}
