package com.www.timesaleservice.controller.v1;

import com.www.timesaleservice.domain.TimeSale;
import com.www.timesaleservice.dto.TimeSaleDto;
import com.www.timesaleservice.dto.TimeSaleDto.Response;
import com.www.timesaleservice.service.v1.TimeSaleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/time-sales")
@RequiredArgsConstructor
public class TimeSaleController {

  private final TimeSaleService timeSaleService;

  // 타임세일 등록
  @PostMapping
  public ResponseEntity<TimeSaleDto.Response> createTimeSale(@Valid @RequestBody TimeSaleDto.CreateReqeust reqeust) {
    TimeSale timeSale = timeSaleService.crateTimeSale(reqeust);
    return ResponseEntity.status(HttpStatus.CREATED).body(TimeSaleDto.Response.from(timeSale));
  }

  // 특정 타임세일 조회
  @GetMapping("/{timeSaleId}")
  public ResponseEntity<TimeSaleDto.Response> getTimeSale(@PathVariable Long timeSaleId) {
    TimeSale timeSale = timeSaleService.getTimeSale(timeSaleId);
    return ResponseEntity.ok(TimeSaleDto.Response.from(timeSale));
  }

  // 현재 진행중인 모든 타임세일
  @GetMapping
  public ResponseEntity<Page<Response>> getOngoingTimeSales(@PageableDefault Pageable pageable) {
    Page<TimeSale> timeSales = timeSaleService.getOngoingTimeSales(pageable);
    return ResponseEntity.ok(timeSales.map(timeSale -> Response.from(timeSale)));
  }

  // 구매한 타임세일
  @PostMapping("/{timeSaleId}/purchase")
  public ResponseEntity<TimeSaleDto.PurchaseResponse> purchaseTimeSale(
      @PathVariable Long timeSaleId,
      @Valid @RequestBody TimeSaleDto.PurchaseRequest request) {
    TimeSale timeSale = timeSaleService.purchaseTimeSale(timeSaleId, request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(TimeSaleDto.PurchaseResponse.from(timeSale, request.getUserId(), request.getQuantity()));
  }
}
