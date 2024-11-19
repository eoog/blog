package com.www.back.service;

import com.www.back.dto.AdvertisementDto;
import com.www.back.entity.Advertisement;
import com.www.back.repository.AdvertisementRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class AdvertisementService {

  private static final String REDIS_KEY = "ad:";

  AdvertisementRepository advertisementRepository;
  private final RedisTemplate<String, Object> redisTemplate;

  @Autowired
  public AdvertisementService(AdvertisementRepository advertisementRepository,
      RedisTemplate<String, Object> redisTemplate) {
    this.advertisementRepository = advertisementRepository;
    this.redisTemplate = redisTemplate;
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
  public Optional<Advertisement> getAd(Long adId) {
    Object tempObj = redisTemplate.opsForHash().get(REDIS_KEY, adId);

    // redis 에 저장되있으면 레디스에서 꺼내옴
    if (tempObj != null) {
      return Optional.ofNullable((Advertisement) redisTemplate.opsForHash().get(REDIS_KEY, adId));
    }
    // 없으면 DB조회
    return advertisementRepository.findById(adId);
  }

}
