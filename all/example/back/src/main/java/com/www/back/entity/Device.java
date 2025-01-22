package com.www.back.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Device {

  private String deviceName; // 핸드폰 이름
  private String token; // 토큰
}
