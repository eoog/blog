package com.www.back.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);// 암호화 키
    private long expirationTime = 3600000; // 권한 시간

  // 시큐리티 토큰 생성
  public String generateToken(String username) {
    Date now = new Date();
    Date expirationDate = new Date(now.getTime() + expirationTime); // 현재시간 + 권한 시간

    return Jwts.builder()
        .setSubject(username) // 제목
        .setIssuedAt(now) // 현재 시간
        .setExpiration(expirationDate) // 인증 시간
        .signWith(secretKey , SignatureAlgorithm.HS256) // 새로운 형식 으로 서명 변경
        .compact();
  }

  // 시큐리티 토큰 유효성 검사
  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder()
          .setSigningKey(secretKey) // 서명키 설정
          .build() // 빌드
          .parseClaimsJws(token); // 토큰 검증
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  // 토큰을 이용하여 username 가져오기
  public String getUserFromToken(String token) {

    Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token)
        .getBody();
    return claims.getSubject();

  }

}
