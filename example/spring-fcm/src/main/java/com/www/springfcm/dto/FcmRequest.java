package com.www.springfcm.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class FcmRequest {

  private String token;

  private String title;

  private String body;

  @Builder(toBuilder = true)
  public FcmRequest(String token, String title, String body) {
    this.token = token;
    this.title = title;
    this.body = body;
  }
}
