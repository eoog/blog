package com.www.msagraphql.service;

import com.www.msagraphql.model.User;
import com.www.msagraphql.service.dto.PasswordChangeDTO;
import com.www.msagraphql.service.dto.UserDTO;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
public class UserService {

  private final RestTemplate restTemplate;
  private static final String USER_SERVICE_URL = "http://msa-user-service/users";

  @Autowired
  public UserService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  /**
   * 새로운 사용자를 생성합니다.
   */
  public User createUser(String name, String email, String password) {
    UserDTO userDTO = new UserDTO(name, email, password);
    return restTemplate.postForObject(USER_SERVICE_URL, userDTO, User.class);
  }

  /**
   * 특정 사용자를 조회합니다.
   */
  @Cacheable(value = "user", key = "#userId")
  public Optional<User> findById(Long userId) {
    String url = UriComponentsBuilder.fromHttpUrl(USER_SERVICE_URL)
        .path("/{userId}")
        .buildAndExpand(userId)
        .toUriString();
    User user = restTemplate.getForObject(url, User.class);
    log.info("user = {}", user);
    return Optional.ofNullable(user);
  }

  /**
   * 특정 사용자 정보를 변경합니다.
   */
  public User updateUser(Long userId, String name, String email) {
    UserDTO userDTO = new UserDTO(name, email, null);
    String url = UriComponentsBuilder.fromHttpUrl(USER_SERVICE_URL)
        .path("/{userId}")
        .buildAndExpand(userId)
        .toUriString();
    restTemplate.put(url, userDTO);
    return new User(userId, name, email);  // Assuming you construct a user from the updated info
  }

  /**
   * 특정 사용자 비밀번호를 변경합니다.ㅅ
   */
  public void changePassword(Integer userId, String oldPassword, String newPassword) {
    PasswordChangeDTO passwordChangeDTO = new PasswordChangeDTO(oldPassword, newPassword);
    String url = UriComponentsBuilder.fromHttpUrl(USER_SERVICE_URL)
        .path("/{userId}/password-change")
        .buildAndExpand(userId)
        .toUriString();
    restTemplate.postForLocation(url, passwordChangeDTO);
  }
}
