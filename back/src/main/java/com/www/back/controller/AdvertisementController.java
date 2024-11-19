package com.www.back.controller;

import com.www.back.dto.AdvertisementDto;
import com.www.back.entity.Advertisement;
import com.www.back.service.AdvertisementService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ads")
public class AdvertisementController {

  private final AdvertisementService advertisementService;

  @Autowired
  public AdvertisementController(AdvertisementService advertisementService) {
    this.advertisementService = advertisementService;

  }

  // 광고 등록
  @PostMapping("")
  public ResponseEntity<Advertisement> writeAd(@RequestBody AdvertisementDto advertisementdto) {
    Advertisement advertisement = advertisementService.writeAd(advertisementdto);
    return ResponseEntity.ok(advertisement);

  }

  // 광고 조회
  @GetMapping("")
  public ResponseEntity<List<Advertisement>> getAdList() {
    List<Advertisement> advertisementList = advertisementService.getAdList();
    return ResponseEntity.ok(advertisementList);
  }

  // 특정 광고 조회
  @GetMapping("/{adId}")
  public Object getAdList(@PathVariable Long adId) {
    Optional<Advertisement> advertisement = advertisementService.getAd(adId);
    if (advertisement.isEmpty()) {
      return ResponseEntity.notFound();
    }

    return ResponseEntity.ok(advertisement);
  }

}
