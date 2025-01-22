package com.www.couponservice.service.v1;

import com.www.couponservice.domain.CouponPolicy;
import com.www.couponservice.dto.v1.CouponPolicyDto;
import com.www.couponservice.exception.CouponPolicyNotFoundException;
import com.www.couponservice.repository.CouponPolicyRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponPolicyService {

  private final CouponPolicyRepository couponPolicyRepository;

  /**
   * 2025.01.03 쿠폰 정책 생성
   */
  @Transactional
  public CouponPolicy createCouponPolicy(CouponPolicyDto.CreateRequest request) {
    CouponPolicy couponPolicy = request.toEntity();
    return couponPolicyRepository.save(couponPolicy);
  }

  /**
   * 2025.01.03 쿠폰 정책 조회
   */
  @Transactional(readOnly = true)
  public CouponPolicy getCouponPolicy(Long id) {
    return couponPolicyRepository.findById(id)
        .orElseThrow(() -> new CouponPolicyNotFoundException("쿠폰 정책을 찾을 수 없습니다."));
  }

  /**
   * 2025.01.03 모든 쿠폰 정책 조회
   */
  @Transactional(readOnly = true)
  public List<CouponPolicy> getAllCouponPolicies() {
    return couponPolicyRepository.findAll();
  }
}
