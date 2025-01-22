package com.www.couponservice.exception;

public class CouponExpiredException extends RuntimeException {

  public CouponExpiredException(String message) {
    super(message);
  }
}
