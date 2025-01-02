package com.www.userservice.service;

import com.www.userservice.entity.User;
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

  /**
   * 2025.01.02 토큰 생성
   */

  public String generateToken(User user) {
    long currentTimeMillis = System.currentTimeMillis();
    return Jwts.builder()
        .subject(user.getEmail())
        .claim("role", "USER")
        .claim("id", user.getId())
        .issuedAt(new Date(currentTimeMillis)) // 발급일
        .expiration(new Date(currentTimeMillis + 3600000)) // 토큰 만료 1시간
        .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
        .compact();
  }

  /**
   * 2025.01.02 토큰 검증
   */

  public Claims validateToken(String token) {
    try {
      return parseJwtClaims(token);
    } catch (Exception e) {
      log.error("Token validation error: ", e);
      throw new IllegalArgumentException("Invalid token");
    }
  }

  /**
   * 2025.01.02 토큰 검증
   */
  public Claims parseJwtClaims(String token) {
    return Jwts.parser()
        .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  /**
   * 2025.01.02 토큰 재발급
   */
  public String refreshToken(String token) {
    Claims claims = parseJwtClaims(token);
    long currentTimeMillis = System.currentTimeMillis();
    return Jwts.builder()
        .subject(claims.getSubject())
        .claims(claims)
        .issuedAt(new Date(currentTimeMillis))
        .expiration(new Date(currentTimeMillis + 3600000))
        .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
        .compact();
  }

}
