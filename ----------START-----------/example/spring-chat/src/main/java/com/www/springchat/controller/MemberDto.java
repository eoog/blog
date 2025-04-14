package com.www.springchat.controller;

public class MemberDto {
  private String email;
  private String nickName;

  public MemberDto(String email, String nickName) {
    this.email = email;
    this.nickName = nickName;
  }

  public String getEmail() { return email; }
  public String getNickName() { return nickName; }
}
