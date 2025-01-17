package com.www.msagateway.controller;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/fallback")
public class FallbackController {

  @GetMapping("/authFailure")
  public Mono<Map<String, Object>> authFailure(ServerWebExchange exchange) {
    return Mono.just(Map.of("status", "authFailure"));
  }
}
