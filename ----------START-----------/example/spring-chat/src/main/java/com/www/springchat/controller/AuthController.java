package com.www.springchat.controller;

import com.www.springchat.entity.Member;
import com.www.springchat.vo.CustomOAuth2User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @GetMapping("/user")
  public ResponseEntity<MemberDto> getCurrentUser(@AuthenticationPrincipal CustomOAuth2User user) {
    if (user == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    Member member = user.getMember();
    return ResponseEntity.ok(new MemberDto(member.getEmail(), member.getNickName()));
  }
}

