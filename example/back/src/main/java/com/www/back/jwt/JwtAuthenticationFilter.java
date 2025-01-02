package com.www.back.jwt;

import com.www.back.service.JwtBlacklistService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;
  private final UserDetailsService userDetailsService;
  private final JwtBlacklistService jwtBlacklistService;

  public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService,
      JwtBlacklistService jwtBlacklistService) {
    this.jwtUtil = jwtUtil;
    this.userDetailsService = userDetailsService;
    this.jwtBlacklistService = jwtBlacklistService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String token = resolveToken(request);
    System.out.println("token = " + token);

    // 토큰이 있으면서 블랙리스트에 등록이 안된 사람만 통과
    if (token != null && jwtUtil.validateToken(token) && !jwtBlacklistService.isTokenBlacklisted(
        token)) {
      String username = jwtUtil.getUserFromToken(token);
      UserDetails userDetails = userDetailsService.loadUserByUsername(username);
      UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
          userDetails, null, userDetails.getAuthorities());
      authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    filterChain.doFilter(request, response);
  }


  private String resolveToken(HttpServletRequest request) {
    // 인증 헤더
    String bearerToken = request.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    // 인증 쿠키
    if (bearerToken == null) {
      Cookie[] cookies = request.getCookies();
      if (cookies != null) {
        for (Cookie cookie : cookies) {
          if ("onion_token".equals(cookie.getName())) {
            return cookie.getValue();
          }
        }
      }
    }

    return null;
  }

}
