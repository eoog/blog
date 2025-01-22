package com.www.springfcm.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.www.springfcm.dto.FcmRequest;
import com.www.springfcm.service.FcmService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FcmController {

  private final FcmService fcmService;

  @PostMapping("/send")
  public ResponseEntity<Integer> pushM(@RequestBody FcmRequest request)
      throws IOException {
    log.debug(" == 푸시 메시지를 전송합니다. ");
    int result = fcmService.sendMessage(request);

    return new ResponseEntity<>(result,HttpStatus.OK);
  }
}
