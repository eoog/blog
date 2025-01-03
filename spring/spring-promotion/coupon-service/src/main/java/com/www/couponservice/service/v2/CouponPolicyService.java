package com.www.couponservice.service.v2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.www.couponservice.domain.CouponPolicy;
import com.www.couponservice.dto.v1.CouponPolicyDto;
import com.www.couponservice.exception.CouponPolicyNotFoundException;
import com.www.couponservice.repository.CouponPolicyRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("couponPolicyServiceV2")
@RequiredArgsConstructor
@Slf4j
public class CouponPolicyService {

  private final CouponPolicyRepository couponPolicyRepository;
  private final RedissonClient redissonClient;
  private final ObjectMapper objectMapper;

  private static final String COUPON_QUANTITY_KEY = "coupon:quantity:";
  private static final String COUPON_POLICY_KEY = "coupon:policy:";


  /**
   * 2025.01.03 쿠폰 정책 생성
   */

  @Transactional
  public CouponPolicy createCouponPolicy(CouponPolicyDto.CreateRequest request)
      throws JsonProcessingException {

    CouponPolicy couponPolicy = request.toEntity();
    CouponPolicy savedPolicy = couponPolicyRepository.save(couponPolicy);

    // Redis에 초기 수량 설정
    String quantityKey = COUPON_QUANTITY_KEY + savedPolicy.getId();
    RAtomicLong atomicQuantity = redissonClient.getAtomicLong(quantityKey);
    atomicQuantity.set(savedPolicy.getTotalQuantity());

    // Redis에 정책 정보 저장
    String policyKey = COUPON_POLICY_KEY + savedPolicy.getId();
    String policyJson = objectMapper.writeValueAsString(CouponPolicyDto.Response.from(savedPolicy));
    RBucket<String> bucket = redissonClient.getBucket(policyKey);
    bucket.set(policyJson);

    return savedPolicy;
  }

  /**
   * 2025.01.03 쿠폰 정책 조회
   */

  public CouponPolicy getCouponPolicy(Long id) {
    String policyKey = COUPON_POLICY_KEY + id;
    RBucket<String> bucket = redissonClient.getBucket(policyKey);
    String policyJson = bucket.get();
    if (policyJson != null) {
      try {
        return objectMapper.readValue(policyJson, CouponPolicy.class);
      } catch (JsonProcessingException e) {
        log.error("쿠폰 정책 정보를 JSON으로 파싱하는 중 오류가 발생했습니다.", e);
      }
    }

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
