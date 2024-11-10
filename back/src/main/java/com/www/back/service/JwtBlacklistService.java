package com.www.back.service;

import com.www.back.entity.JwtBlacklist;
import com.www.back.jwt.JwtUtil;
import com.www.back.repository.JwtBlacklistRepository;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class JwtBlacklistService {

  private final JwtBlacklistRepository jwtBlacklistRepository;
  private final JwtUtil jwtUtil;

  public JwtBlacklistService(JwtBlacklistRepository jwtBlacklistRepository, JwtUtil jwtUtil) {
    this.jwtBlacklistRepository = jwtBlacklistRepository;
    this.jwtUtil = jwtUtil;
  }

  // 다른곳에서 로그인 저장
  public void blacklistToken(String token, LocalDateTime expirationTime , String username) {
    JwtBlacklist jwtBlacklist = new JwtBlacklist();
    jwtBlacklist.setToken(token);
    jwtBlacklist.setExpirationTime(expirationTime);
    jwtBlacklist.setUsername(username);
    jwtBlacklistRepository.save(jwtBlacklist);
  }

  // 토큰으로 유저 블랙리스트 추가
  public boolean isTokenBlacklisted(String currentToken) {
    // 토큰을 이용하여 유저 이름 가져오기
    String username = jwtUtil.getUserFromToken(currentToken);

    // 유저네임을 만료시간 상위 ( 값 큰거 ) 불러오기
    Optional<JwtBlacklist> blacklistedToken = jwtBlacklistRepository.findTopByUsernameOrderByExpirationTime(username);
    if (blacklistedToken.isEmpty()) {
      return false;
    }

    Instant instant = jwtUtil.getExpirationDateFromToken(currentToken).toInstant();
    LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

    return blacklistedToken.get().getExpirationTime().isAfter(localDateTime.minusMinutes(60));
  }
}
