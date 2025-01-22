package com.www.couponservice.controller.v1;

import com.www.couponservice.domain.Coupon;
import com.www.couponservice.dto.v1.CouponDto;
import com.www.couponservice.dto.v1.CouponDto.Response;
import com.www.couponservice.service.v1.CouponService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
public class CouponController {

  private final CouponService couponService;

  /**
   * 2025.01.03 쿠폰 발행
   */
  @PostMapping("/issue")
  public ResponseEntity<Response> issueCoupon(@RequestBody CouponDto.IssueRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(CouponDto.Response.from(couponService.issueCoupon(request)));
  }

  /**
   * 2025.01.03 쿠폰 사용
   */
  @PostMapping("/{couponId}/use")
  public ResponseEntity<CouponDto.Response> useCoupon(
      @PathVariable Long couponId,
      @RequestBody CouponDto.UseRequest request) {
    return ResponseEntity.ok(
        CouponDto.Response.from(couponService.useCoupon(couponId, request.getOrderId())));
  }

  /**
   * 2025.01.03 쿠폰 사용 취소
   */
  @PostMapping("/{couponId}/cancel")
  public ResponseEntity<CouponDto.Response> cancelCoupon(@PathVariable Long couponId) {
    return ResponseEntity.ok(CouponDto.Response.from(couponService.cancelCoupon(couponId)));
  }

  /**
   * 2025.01.03 발행되 모든 쿠폰 조회
   */
  
  @GetMapping
  public ResponseEntity<List<Response>> getCoupons(
      @RequestParam(required = false) Coupon.Status status,
      @RequestParam(required = false) Integer page,
      @RequestParam(required = false) Integer size) {

    CouponDto.ListRequest request = CouponDto.ListRequest.builder()
        .status(status)
        .page(page)
        .size(size)
        .build();

    return ResponseEntity.ok(couponService.getCoupons(request));
  }
}
