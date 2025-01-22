package com.www.videoredis.adapter.out;

import static com.www.videoredis.utils.CacheKeyNames.VIDEO;
import static com.www.videoredis.utils.CacheKeyNames.VIDEO_LIST;
import static com.www.videoredis.utils.RedisKeyGenerator.getVideoViewCountKey;

import com.www.videoredis.adapter.out.jpa.video.VideoJpaEntity;
import com.www.videoredis.adapter.out.jpa.video.VideoJpaRepository;
import com.www.videoredis.application.port.out.LoadVideoPort;
import com.www.videoredis.application.port.out.SaveVideoPort;
import com.www.videoredis.domain.video.Video;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VideoPersistenceAdapter implements LoadVideoPort , SaveVideoPort {

  private VideoJpaRepository videoJpaRepository;
  private RedisTemplate<String, Long> redisTemplate;


  public VideoPersistenceAdapter(VideoJpaRepository videoJpaRepository) {
    this.videoJpaRepository = videoJpaRepository;
    this.redisTemplate = redisTemplate;
  }

  // 비디오 조회
  @Override
  @Cacheable(cacheNames = VIDEO , key = "#videoId")
  public Video loadVideo(String videoId) {
    return videoJpaRepository.findById(videoId)
        .map(VideoJpaEntity::toDomain)
        .orElseThrow();
  }

  // 채널의 비디오 리스트 조회
  @Override
  @Cacheable(cacheManager = "redisListCacheManager",cacheNames = VIDEO_LIST, key = "#channelId")
  public List<Video> loadVideoByChannel(String channelId) {
    return videoJpaRepository.findByChannelId(channelId).stream()
        .map(VideoJpaEntity::toDomain)
        .toList();
  }

  // 채널 본 조회수
  @Override
  public Long getViewCount(String videoId) {
    var videoViewCountKey = getVideoViewCountKey(videoId);
    return redisTemplate.opsForValue().get(videoViewCountKey);
  }

  // 채널 증가 카운트
  @Override
  public Long incrementViewCount(String videoId) {
    var videoViewCountKey = getVideoViewCountKey(videoId);
    return redisTemplate.opsForValue().get(videoViewCountKey);

    //        // Using RedisAtomicLong
//        RedisAtomicLong redisAtomicLong = new RedisAtomicLong(videoViewCountKey, redisTemplate.getConnectionFactory());
//        redisAtomicLong.incrementAndGet()
  }

  // 뷰 증가
}
