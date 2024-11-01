package com.www.back.service;

import com.www.back.dto.SignUpUser;
import com.www.back.entity.User;
import com.www.back.repository.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
    this.passwordEncoder = new BCryptPasswordEncoder();
  }

  // 회원 가입 기능
  public User createUser(SignUpUser add) {
    User user = new User();
    user.setUsername(add.getUsername());
    user.setPassword(passwordEncoder.encode(add.getPassword()));
    user.setEmail(add.getEmail());
    return userRepository.save(user);
  }

  // 회원 탈퇴 기능
  public void deleteUser(Long id) {
    userRepository.deleteById(id);
  }

  // 전체 회원 조회 기능
  public List<User> getUserS() {
    return userRepository.findAll();
  }
}
