package com.www.back.service;

import com.www.back.dto.AdHistoryResult;
import com.www.back.dto.AdvertisementDto;
import com.www.back.entity.AdClickHistory;
import com.www.back.entity.AdClickStat;
import com.www.back.entity.AdViewHistory;
import com.www.back.entity.AdViewStat;
import com.www.back.entity.Advertisement;
import com.www.back.repository.AdClickHistoryRepository;
import com.www.back.repository.AdClickStatRepository;
import com.www.back.repository.AdViewHistoryRepository;
import com.www.back.repository.AdViewStatRepository;
import com.www.back.repository.AdvertisementRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
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

  // mysql 광고조회
  private final AdClickStatRepository adClickStatRepository;
  private final AdViewStatRepository adViewStatRepository;
  private final MongoTemplate mongoTemplate;

  @Autowired
  public AdvertisementService(AdvertisementRepository advertisementRepository,
      RedisTemplate<String, Object> redisTemplate, AdViewHistoryRepository adViewHistoryRepository,
      AdClickHistoryRepository adClickHistoryRepository, AdViewStatRepository adViewStatRepository,
      AdClickStatRepository adClickStatRepository, MongoTemplate mongoTemplate) {
    this.advertisementRepository = advertisementRepository;
    this.redisTemplate = redisTemplate;
    this.adClickHistoryRepository = adClickHistoryRepository;
    this.adViewHistoryRepository = adViewHistoryRepository;
    this.adClickStatRepository = adClickStatRepository;
    this.adViewStatRepository = adViewStatRepository;
    this.mongoTemplate = mongoTemplate;
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
    // 광고조회
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

  // 광고 본사람 내역 광고 id 기준으로 정렬
  public List<AdHistoryResult> getAdViewHistoryGroupByAdId() {
    List<AdHistoryResult> usernameResult = this.getAdViewHistoryGroupedByAdIdAndUsername();
    List<AdHistoryResult> clientIpResult = this.getAdViewHistoryGroupedByAdIdAndClientIp();

    HashMap<Long, Long> totalResult = new HashMap<>();

    for (AdHistoryResult item : usernameResult) {
      totalResult.put(item.getAdId(), item.getCount());
    }

    for (AdHistoryResult item : clientIpResult) {
      totalResult.merge(item.getAdId(), item.getCount(), Long::sum);
    }

    List<AdHistoryResult> resultList = new ArrayList<>();
    for (Map.Entry<Long, Long> entry : totalResult.entrySet()) {
      AdHistoryResult result = new AdHistoryResult();
      result.setAdId(entry.getKey());
      result.setCount(entry.getValue());
      resultList.add(result);
    }

    return resultList;
  }

  // 광고 본사람 광고 id 와 유저 네임 정렬
  private List<AdHistoryResult> getAdViewHistoryGroupedByAdIdAndUsername() {

    // 어제의 시작과 끝 시간 계산
    LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MIN)
        .plusHours(9);

    LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN)
        .plusHours(9);

    // Match 단계 : 어제 날짜에 해당하고 , username 이 있는 문서 필터링
    MatchOperation matchStage = Aggregation.match(
        Criteria.where("createdDate").gt(startOfDay).lt(endOfDay)
            .and("username").exists(true)
    );

    // Group 단계 : adId로 그륩화 하고 고유한 username 집합을 생성
    GroupOperation groupStage = Aggregation.group("adId").addToSet("username")
        .as("uniqueUsernames");

    // Projection 단계 : 고유한 username 집합의 크기를 count 로 계산
    ProjectionOperation projectStage = Aggregation.project().andExpression("_id").as("adId")
        .andExpression("size(uniqueUsernames)")
        .as("count");

    // Aggregation 수행
    Aggregation aggregation = Aggregation.newAggregation(matchStage, groupStage, projectStage);
    AggregationResults<AdHistoryResult> results = mongoTemplate.aggregate(aggregation,
        "adViewHistory", AdHistoryResult.class);

    return results.getMappedResults();
  }

  // 광고 본사람 광고 id 와 아이피 정렬
  private List<AdHistoryResult> getAdViewHistoryGroupedByAdIdAndClientIp() {

    // 어제의 시작과 끝 시간 계산
    LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MIN)
        .plusHours(9);

    LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN)
        .plusHours(9);

    // Match 단계 : 어제 날짜에 해당하고 , username 이 있는 문서 필터링
    MatchOperation matchStage = Aggregation.match(
        Criteria.where("createdDate").gt(startOfDay).lt(endOfDay)
            .and("username").exists(true)
    );

    // Group 단계 : adId로 그륩화 하고 고유한 clientIp 집합을 생성
    GroupOperation groupStage = Aggregation.group("adId").addToSet("clientIp")
        .as("uniqueClientIp");

    // Projection 단계 : 고유한 username 집합의 크기를 count 로 계산
    ProjectionOperation projectStage = Aggregation.project().andExpression("_id").as("adId")
        .andExpression("size(uniqueClientIp)")
        .as("count");

    // Aggregation 수행
    Aggregation aggregation = Aggregation.newAggregation(matchStage, groupStage, projectStage);
    AggregationResults<AdHistoryResult> results = mongoTemplate.aggregate(aggregation,
        "adViewHistory", AdHistoryResult.class);

    return results.getMappedResults();
  }

  // 광고 본사람 내역 저장
  public void insertAdViewStat(List<AdHistoryResult> result) {
    System.out.println("result = " + result);
    ArrayList<AdViewStat> arrayList = new ArrayList<>();
    for (AdHistoryResult item : result) {
      AdViewStat adViewStat = new AdViewStat();
      adViewStat.setAdId(item.getAdId());
      adViewStat.setCount(item.getCount());
      // yyyy-MM-dd 형식의 DateTimeFormatter 생성
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      // LocalDateTime을 yyyy-MM-dd 형식의 문자열로 변환
      String formattedDate = LocalDateTime.now().minusDays(1).format(formatter);
      adViewStat.setDt(formattedDate);
      arrayList.add(adViewStat);
    }
    adViewStatRepository.saveAll(arrayList);
  }

  public List<AdHistoryResult> getAdClickHistoryGroupedByAdId() {
    List<AdHistoryResult> usernameResult = this.getAdClickHistoryGroupedByAdIdAndUsername();
    List<AdHistoryResult> clientipResult = this.getAdClickHistoryGroupedByAdIdAndClientip();
    HashMap<Long, Long> totalResult = new HashMap<>();
    for (AdHistoryResult item : usernameResult) {
      totalResult.put(item.getAdId(), item.getCount());
    }
    for (AdHistoryResult item : clientipResult) {
      totalResult.merge(item.getAdId(), item.getCount(), Long::sum);
    }

    List<AdHistoryResult> resultList = new ArrayList<>();
    for (Map.Entry<Long, Long> entry : totalResult.entrySet()) {
      AdHistoryResult result = new AdHistoryResult();
      result.setAdId(entry.getKey());
      result.setCount(entry.getValue());
      resultList.add(result);
    }
    return resultList;
  }

  private List<AdHistoryResult> getAdClickHistoryGroupedByAdIdAndUsername() {
    // 어제의 시작과 끝 시간 계산
    LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MIN)
        .plusHours(9);
    LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN).plusHours(9);

    // Match 단계: 어제 날짜에 해당하고, username이 있는 문서 필터링
    MatchOperation matchStage = Aggregation.match(
        Criteria.where("createdDate").gte(startOfDay).lt(endOfDay)
            .and("username").exists(true)
    );

    // Group 단계: adId로 그룹화하고 고유한 username 집합을 생성
    GroupOperation groupStage = Aggregation.group("adId")
        .addToSet("username").as("uniqueUsernames");

    // Projection 단계: 고유한 username 집합의 크기를 count로 계산
    ProjectionOperation projectStage = Aggregation.project()
        .andExpression("_id").as("adId")
        .andExpression("size(uniqueUsernames)").as("count");

    // Aggregation 수행
    Aggregation aggregation = Aggregation.newAggregation(matchStage, groupStage, projectStage);
    AggregationResults<AdHistoryResult> results = mongoTemplate.aggregate(aggregation,
        "adClickHistory", AdHistoryResult.class);

    return results.getMappedResults();
  }

  private List<AdHistoryResult> getAdClickHistoryGroupedByAdIdAndClientip() {
    // 어제의 시작과 끝 시간 계산
    LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MIN)
        .plusHours(9);
    LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN).plusHours(9);

    // Match 단계: 어제 날짜에 해당하고, username이 있는 문서 필터링
    MatchOperation matchStage = Aggregation.match(
        Criteria.where("createdDate").gte(startOfDay).lt(endOfDay)
            .and("username").exists(false)
    );

    // Group 단계: adId로 그룹화하고 고유한 username 집합을 생성
    GroupOperation groupStage = Aggregation.group("adId")
        .addToSet("clientIp").as("uniqueClientIp");

    // Projection 단계: 고유한 username 집합의 크기를 count로 계산
    ProjectionOperation projectStage = Aggregation.project()
        .andExpression("_id").as("adId")
        .andExpression("size(uniqueClientIp)").as("count");

    // Aggregation 수행
    Aggregation aggregation = Aggregation.newAggregation(matchStage, groupStage, projectStage);
    AggregationResults<AdHistoryResult> results = mongoTemplate.aggregate(aggregation,
        "adClickHistory", AdHistoryResult.class);

    return results.getMappedResults();
  }

  public void insertAdClickStat(List<AdHistoryResult> result) {
    ArrayList<AdClickStat> arrayList = new ArrayList<>();
    for (AdHistoryResult item : result) {
      AdClickStat adClickStat = new AdClickStat();
      adClickStat.setAdId(item.getAdId());
      adClickStat.setCount(item.getCount());
      // yyyy-MM-dd 형식의 DateTimeFormatter 생성
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      // LocalDateTime을 yyyy-MM-dd 형식의 문자열로 변환
      String formattedDate = LocalDateTime.now().minusDays(1).format(formatter);
      adClickStat.setDt(formattedDate);
      arrayList.add(adClickStat);
    }
    adClickStatRepository.saveAll(arrayList);
  }
}
