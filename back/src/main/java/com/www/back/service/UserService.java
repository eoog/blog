package com.www.back.service;

import com.www.back.entity.User;
import com.www.back.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  // 회원 가입 기능
  public User createUser(String username, String password , String email) {
    User user = new User();
    user.setUsername(username);
    user.setPassword(password);
    user.setEmail(email);
    return userRepository.save(user);
  }

  // 회원 탈퇴 기능
  public void deleteUser(Long id) {
    userRepository.deleteById(id);
  }
}
