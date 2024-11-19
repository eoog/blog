package com.www.back.service;

import com.www.back.dto.AdvertisementDto;
import com.www.back.entity.AdClickHistory;
import com.www.back.entity.AdViewHistory;
import com.www.back.entity.Advertisement;
import com.www.back.repository.AdClickHistoryRepository;
import com.www.back.repository.AdViewHistoryRepository;
import com.www.back.repository.AdvertisementRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AdvertisementService {

  private static final String REDIS_KEY = "ad:";

  private final AdvertisementRepository advertisementRepository;
  private final RedisTemplate<String, Object> redisTemplate;

  // 몽고 DB
  private final AdClickHistoryRepository adClickHistoryRepository;
  private final AdViewHistoryRepository adViewHistoryRepository;

  @Autowired
  public AdvertisementService(AdvertisementRepository advertisementRepository,
      RedisTemplate<String, Object> redisTemplate, AdViewHistoryRepository adViewHistoryRepository,
      AdClickHistoryRepository adClickHistoryRepository) {
    this.advertisementRepository = advertisementRepository;
    this.redisTemplate = redisTemplate;
    this.adClickHistoryRepository = adClickHistoryRepository;
    this.adViewHistoryRepository = adViewHistoryRepository;
  }

  // 광고 등록 redis 저장 후 반환
  @Transactional
  public Advertisement writeAd(AdvertisementDto advertisementDto) {
    Advertisement advertisement = new Advertisement();
    advertisement.setTitle(advertisementDto.getTitle());
    advertisement.setContent(advertisementDto.getContent());
    advertisement.setIsDeleted(advertisementDto.getIsDeleted());
    advertisement.setIsVisible(advertisementDto.getIsVisible());
    advertisement.setStartDate(advertisementDto.getStartDate());
    advertisement.setEndDate(advertisementDto.getEndDate());
    advertisement.setViewCount(advertisementDto.getViewCount());
    advertisement.setClickCount(advertisementDto.getClickCount());
    advertisementRepository.save(advertisement);
    // Redis 저장
    redisTemplate.opsForHash()
        .put(REDIS_KEY + advertisement.getId(), advertisement.getId(), advertisement);
    return advertisement;
  }

  // 광고 조회
  public List<Advertisement> getAdList() {
    return advertisementRepository.findAll();
  }

  // 특정 광고조회
  public Optional<Advertisement> getAd(Long adId, String clientIp, Boolean isTrueView) {
    this.insertAdViewHistory(adId, clientIp, isTrueView);
    Object tempObj = redisTemplate.opsForHash().get(REDIS_KEY, adId);

    // redis 에 저장되있으면 레디스에서 꺼내옴
    if (tempObj != null) {
      return Optional.ofNullable((Advertisement) redisTemplate.opsForHash().get(REDIS_KEY, adId));
    }
    // 없으면 DB조회
    return advertisementRepository.findById(adId);
  }

  // 광고 클릭 내역 저장 ( 몽고 DB )
  public void clickAd(Long adId, String clientIp) {
    AdClickHistory adClickHistory = new AdClickHistory();
    adClickHistory.setAdId(adId);
    adClickHistory.setClientIp(clientIp);
    adClickHistory.setCreatedDate(LocalDateTime.now());
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Object principal = authentication.getPrincipal();
    if (!principal.equals("anonymousUser")) {
      UserDetails userDetails = (UserDetails) principal;
      adClickHistory.setUsername(userDetails.getUsername());
    }
    adClickHistoryRepository.save(adClickHistory);
  }

  // 광고 본 사람들 내역 저장 ( 몽고 DB )
  private void insertAdViewHistory(Long adId, String clientIp, Boolean isTrueView) {
    AdViewHistory adViewHistory = new AdViewHistory();
    adViewHistory.setAdId(adId);
    adViewHistory.setClientIp(clientIp);
    adViewHistory.setIsTrueView(isTrueView);
    adViewHistory.setCreatedDate(LocalDateTime.now());
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Object principal = authentication.getPrincipal();
    if (!principal.equals("anonymousUser")) {
      UserDetails userDetails = (UserDetails) principal;
      adViewHistory.setUsername(userDetails.getUsername());
    }
    adViewHistoryRepository.save(adViewHistory);
  }

}
