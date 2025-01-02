package com.www.apigateway.filter;

import java.util.Map;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter extends
    AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

  /**
   * 2025.01.02 WebClient 추가
   */
  @LoadBalanced
  private final WebClient webClient;

  public JwtAuthenticationFilter(ReactorLoadBalancerExchangeFilterFunction lbFunction) {
    super(Config.class);
    this.webClient = WebClient.builder()
        .filter(lbFunction)
        .baseUrl("http://user-service") // yml -> id 값
        .build();
  }

  @Override
  public GatewayFilter apply(Config config) {
    return ((exchange, chain) -> {
      // header 정보 획득
      String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
      if (authHeader != null && authHeader.startsWith("Bearer ")) {
        String token = authHeader.substring(7);
        return validateToken(token)
            .flatMap(userId -> proceedWithUserId(userId, exchange, chain))
            .switchIfEmpty(
                chain.filter(exchange)) // If token is invalid, continue without setting userId
            .onErrorResume(e -> handleAuthenticationError(exchange, e)); // Handle errors
      }
      return chain.filter(exchange);
    });
  }

  /**
   * 2025.01.02 인증 권한 없음
   */
  private Mono<Void> handleAuthenticationError(ServerWebExchange exchange, Throwable e) {
    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
    return exchange.getResponse().setComplete();
  }

  /**
   * 2025.01.02 Header userId 주입
   */
  private Mono<Void> proceedWithUserId(Long userId, ServerWebExchange exchange,
      GatewayFilterChain chain) {
    // 기존 exchange는 불변(immutable)이므로 mutate()를 사용해 새로운 요청을 생성해야 합니다
    ServerHttpRequest request = exchange.getRequest().mutate()
        .header("X-USER-ID", String.valueOf(userId))  // userId를 헤더에 추가
        .build();

    // 새로운 exchange 생성
    ServerWebExchange mutatedExchange = exchange.mutate()
        .request(request)
        .build();

    System.out.println("X-USER-ID 헤더 추가됨: " + userId);

    return chain.filter(mutatedExchange);  // 새로운 exchange로 필터 체인 진행
  }

  /**
   * 2025.01.02 토큰 검증
   */
  private Mono<Long> validateToken(String token) {
    System.out.println("로그=" + token);
    Mono<Long> id = webClient.post()
        .uri("/api/v1/users/validate-token")
        .bodyValue("{\"token\":\"" + token + "\"}")
        .header("Content-Type", "application/json")
        .retrieve()
        .bodyToMono(Map.class)
        .map(response -> Long.valueOf(response.get("id").toString()));
    System.out.println("id = " + id.toString());

    return id;
  }


  public static class Config {
    // 필터 구성을 위한 설정 클래스
  }

}
