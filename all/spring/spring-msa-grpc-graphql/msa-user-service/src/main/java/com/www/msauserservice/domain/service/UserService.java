package com.www.msauserservice.domain.service;

import com.www.msauserservice.domain.entity.User;
import com.www.msauserservice.domain.entity.UserLoginHistory;
import com.www.msauserservice.domain.repository.UserLoginHistoryRepository;
import com.www.msauserservice.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;

  private final UserLoginHistoryRepository userLoginHistoryRepository;

  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UserService(UserRepository userRepository,
      UserLoginHistoryRepository userLoginHistoryRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.userLoginHistoryRepository = userLoginHistoryRepository;
    this.passwordEncoder = passwordEncoder;
  }

  // 유저 생성
  @Transactional
  public User createUser(String name, String email, String password) {
    User newUser = new User();
    newUser.setName(name);
    newUser.setEmail(email);
    newUser.setPasswordHash(passwordEncoder.encode(password));
    return userRepository.save(newUser);
  }

  // 아이디로 단일 유저 찾기
  public Optional<User> getUserById(Integer userId) {
    return userRepository.findById(userId);
  }

  // 이메일로 단일 유저 찾기
  public Optional<User> getUserByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  // 유저 정보 업데이트
  @Transactional
  public User updateUser(Integer userId,String name,String email) {
    User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("유저를 찾을수 없습니다."));
    user.setName(name);
    user.setEmail(email);
    return userRepository.save(user);
  }

  // 유저 로그인 이력 조회하기
  public List<UserLoginHistory> getUserLoginHistory(Integer userId) {
    return userRepository.findById(userId)
        .map(user -> user.getLoginHistories())
        .orElseThrow(() -> new RuntimeException("유저 찾을수 없음"));
  }

  // 유저 비밀번호 변경
  public void changePassword(Integer userId,String lodPassword , String newPassword) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("유저 찾을수 없습니다"));

    if (passwordEncoder.matches(lodPassword, user.getPasswordHash())) {
      user.setPasswordHash(newPassword);
      userRepository.save(user);
    } else {
      throw new IllegalArgumentException("기존 비밀번호가 틀립니다.");
    }
  }

  // 로그 이력 등록
  @Transactional
  public void logUserLogin(User user , String ipAddress) {
    UserLoginHistory loginHistory = new UserLoginHistory();
    loginHistory.setUser(user);
    loginHistory.setLoginTime(LocalDateTime.now());
    loginHistory.setIpAddress(ipAddress);
    userLoginHistoryRepository.save(loginHistory);
  }
}
