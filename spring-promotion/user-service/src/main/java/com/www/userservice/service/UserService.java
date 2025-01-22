package com.www.userservice.service;

import com.www.userservice.entity.User;
import com.www.userservice.entity.UserLoginHistory;
import com.www.userservice.exception.DuplicateUserException;
import com.www.userservice.exception.UnauthorizedAccessException;
import com.www.userservice.exception.UserNotFoundException;
import com.www.userservice.repository.UserLoginHistoryRepository;
import com.www.userservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final UserLoginHistoryRepository userLoginHistoryRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository,
      UserLoginHistoryRepository userLoginHistoryRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.userLoginHistoryRepository = userLoginHistoryRepository;
    this.passwordEncoder = passwordEncoder;
  }

  /**
   * 2025.01.02 회원 가입
   */

  @Transactional
  public User createUser(String email, String password, String name) {
    if (userRepository.findByEmail(email).isPresent()) {
      throw new DuplicateUserException("User 테이블에 이미 존재하는 이메일(Email):" + email);
    }

    User user = User.builder()
        .email(email)
        .passwordHash(passwordEncoder.encode(password))
        .name(name)
        .build();

    return userRepository.save(user);
  }

  /**
   * 2025.01.02 검증
   */

  public User authenticate(String email, String password) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UserNotFoundException("등록 되지 않은 이메일(Email):" + email));

    if (!passwordEncoder.matches(password, user.getPasswordHash())) {
      throw new UnauthorizedAccessException("비밀번호가 맞지 않습니다.");
    }

    return user;
  }

  /**
   * 2025.01.02 아이디 조화
   */
  public User getUserById(Integer userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException("등록 되지 않은 아이디(Email): " + userId));
  }

  /**
   * 2025.01.02 회원정보 업데이트
   */
  @Transactional
  public User updateUser(Integer userId, String name) {
    User user = getUserById(userId);
    user.setName(name);
    return userRepository.save(user);
  }

  /**
   * 2025.01.02 회원 비밀번호 변경
   */
  @Transactional
  public void changePassword(Integer userId, String currentPassword, String newPassword) {
    User user = getUserById(userId);

    if (!passwordEncoder.matches(currentPassword, user.getPasswordHash())) {
      throw new UnauthorizedAccessException("현재 비밀번호가 일치하지 않습니다.");
    }

    user.setPasswordHash(passwordEncoder.encode(newPassword));
    userRepository.save(user);
  }

  /**
   * 2025.01.02 회원 로그인 이력 조회
   */
  public List<UserLoginHistory> getUserLoginHistory(Integer userId) {
    User user = getUserById(userId);
    return userLoginHistoryRepository.findByUserOrderByLoginTimeDesc(user);
  }
}
