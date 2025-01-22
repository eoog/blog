package com.www.back.exception;

public class RateLimitException extends RuntimeException {
  public RateLimitException(String message) {
    super(message);
  }
}
