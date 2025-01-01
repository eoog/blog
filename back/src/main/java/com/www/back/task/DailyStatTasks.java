package com.www.back.task;


import com.www.back.dto.AdHistoryResult;
import com.www.back.service.AdvertisementService;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DailyStatTasks {

  private final AdvertisementService advertisementService;

  public DailyStatTasks(AdvertisementService advertisementService) {
    this.advertisementService = advertisementService;
  }


  @Scheduled(cron = "* * * 3 * ?")
  public void insertAdViewStatAtMidnight() {
    System.out.println("start");
    
    List<AdHistoryResult> viewResult = advertisementService.getAdViewHistoryGroupByAdId();
    advertisementService.insertAdViewStat(viewResult);
    List<AdHistoryResult> clickResult = advertisementService.getAdClickHistoryGroupedByAdId();
    advertisementService.insertAdClickStat(clickResult);
  }
}
