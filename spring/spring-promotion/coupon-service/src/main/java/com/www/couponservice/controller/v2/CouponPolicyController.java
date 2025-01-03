package com.www.couponservice.controller.v2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.www.couponservice.dto.v1.CouponPolicyDto;
import com.www.couponservice.dto.v1.CouponPolicyDto.Response;
import com.www.couponservice.service.v2.CouponPolicyService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("couponPolicyControllerV2")
@RequestMapping("/api/v2/coupon-policies")
@RequiredArgsConstructor
public class CouponPolicyController {

  private final CouponPolicyService couponPolicyService;

  @PostMapping
  public ResponseEntity<Response> createCouponPolicy(
      @RequestBody CouponPolicyDto.CreateRequest request) throws JsonProcessingException {
    return ResponseEntity.ok()
        .body(CouponPolicyDto.Response.from(couponPolicyService.createCouponPolicy(request)));
  }

  @GetMapping("/{id}")
  public ResponseEntity<CouponPolicyDto.Response> getCouponPolicy(@PathVariable Long id) {
    return ResponseEntity.ok(
        CouponPolicyDto.Response.from(couponPolicyService.getCouponPolicy(id)));
  }

  @GetMapping
  public ResponseEntity<List<Response>> getAllCouponPolicies() {
    return ResponseEntity.ok(couponPolicyService.getAllCouponPolicies().stream()
        .map(CouponPolicyDto.Response::from)
        .collect(Collectors.toList()));
  }
}
