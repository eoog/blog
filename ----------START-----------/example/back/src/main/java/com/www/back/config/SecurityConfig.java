package com.www.back.config;

import com.www.back.jwt.JwtAuthenticationFilter;
import com.www.back.jwt.JwtUtil;
import com.www.back.service.CustomUserDetailsService;
import com.www.back.service.JwtBlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // security
public class SecurityConfig {

  @Autowired
  private CustomUserDetailsService userDetailsService;

  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private JwtBlacklistService jwtBlacklistService;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable) // cros-oring 미사용
        .authorizeHttpRequests((request) ->
            request.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/api/users/signUp",
                    "/api/users/login", "/api/ads", "/api/ads/**").permitAll()
                .anyRequest().authenticated()
        ).addFilterBefore(
            new JwtAuthenticationFilter(jwtUtil, userDetailsService, jwtBlacklistService),
            UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  // 비밀번호 암호화
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
    AuthenticationManagerBuilder authManagerBuilder = http.getSharedObject(
        AuthenticationManagerBuilder.class);
    authManagerBuilder
        .userDetailsService(userDetailsService)
        .passwordEncoder(passwordEncoder());

    return authManagerBuilder.build();
  }
}
