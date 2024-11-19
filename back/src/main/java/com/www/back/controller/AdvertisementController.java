package com.www.back.controller;

import com.www.back.dto.AdHistoryResult;
import com.www.back.dto.AdvertisementDto;
import com.www.back.entity.Advertisement;
import com.www.back.service.AdvertisementService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AdvertisementController {

  private final AdvertisementService advertisementService;

  @Autowired
  public AdvertisementController(AdvertisementService advertisementService) {
    this.advertisementService = advertisementService;

  }

  // 광고 등록
  @PostMapping("/admin/ads")
  public ResponseEntity<Advertisement> writeAd(@RequestBody AdvertisementDto advertisementdto) {
    Advertisement advertisement = advertisementService.writeAd(advertisementdto);
    return ResponseEntity.ok(advertisement);

  }

  // 광고 조회
  @GetMapping("/ads")
  public ResponseEntity<List<Advertisement>> getAdList() {
    List<Advertisement> advertisementList = advertisementService.getAdList();
    return ResponseEntity.ok(advertisementList);
  }

  // 특정 광고 조회 ( 광고 본사람들도 저장 )
  @GetMapping("/ads/{adId}")
  public Object getAdList(@PathVariable Long adId, HttpServletRequest request,
      @RequestParam(required = false) Boolean isTrueView) {

    // 아이피주소
    String ipAddress = request.getRemoteAddr();

    Optional<Advertisement> advertisement = advertisementService.getAd(adId, ipAddress,
        isTrueView != null && isTrueView);
    if (advertisement.isEmpty()) {
      return ResponseEntity.notFound();
    }

    return ResponseEntity.ok(advertisement);
  }

  // 특정 광고 클릭한 사람 집계 ( 광고집계 )
  @PostMapping("/ads/{adId}")
  public Object clickAd(@PathVariable Long adId, HttpServletRequest request) {
    // 아이피
    String ipAddress = request.getRemoteAddr();
    advertisementService.clickAd(adId, ipAddress);
    return ResponseEntity.ok("click");
  }

  // 광고 본사람들 내역 확인
  @GetMapping("/ads/history")
  public ResponseEntity<List<AdHistoryResult>> getAdHistory() {
    List<AdHistoryResult> result = advertisementService.getAdViewHistoryGroupByAdId();
    advertisementService.insertAdViewStat(result);
    return ResponseEntity.ok(result);
  }
}
