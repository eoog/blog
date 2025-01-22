package com.www.msagateway.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 인증 관련 API GATEWAY -> USER SERVICE 위임
 */

@Component
public class AuthenticationFilter extends
    AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

  @LoadBalanced
  private final WebClient webClient;
  private final ObjectMapper objectMapper;

  public AuthenticationFilter(ReactorLoadBalancerExchangeFilterFunction lbFunction,
      ObjectMapper objectMapper) {
    super(Config.class);
    this.webClient = WebClient.builder()
        .filter(lbFunction)
        .baseUrl("http://msa-user-service")
        .build();
    this.objectMapper = objectMapper;
  }

  @Override
  public GatewayFilter apply(Config config) {
    return (exchange, chain) -> {

      String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
      if (authHeader != null && authHeader.startsWith("Bearer ")) {
        String token = authHeader.substring(7);
        return validateToken(token)
            .flatMap(userId -> proceedWithUserId(userId, exchange, chain))
            .switchIfEmpty(chain.filter(exchange)) // 빈값
            .onErrorResume(e -> handleAuthenticationError(exchange, e)); // Handle errors // 에러가 나올때
      }

      return chain.filter(exchange);
    };
  }


  /**
   * 에러
   */

  private Mono<? extends Void> handleAuthenticationError(ServerWebExchange exchange, Throwable e) {
    // 글로벌 예외 json 형태로 감싸서 보내줄수 있음
    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
    return exchange.getResponse().setComplete();
  }


  /**
   * 토큰발급 로그인
   */

  private Mono<Long> validateToken(String token) {
    return webClient.post()
        .uri("/auth/validate")
        .bodyValue("{\"token\":\"" + token + "\"}")
        .header("Content-Type", "application/json")
        .retrieve() // 요청
        .bodyToMono(Map.class)
        .map(response -> Long.valueOf(response.get("id").toString()));
  }

  /**
   * header  X-USER-ID 값을 주입
   */
  private Mono<Void> proceedWithUserId(Long userId, ServerWebExchange exchange,
      GatewayFilterChain chain) {
    exchange.getRequest().mutate().header("X-USER-ID", String.valueOf(userId));
    return chain.filter(exchange);
  }

  public static class Config {
    // 필터 구성을 위한 설정 클래스
  }

}
