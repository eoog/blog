package com.www.timesaleservice.controller.v2;

import com.www.timesaleservice.domain.TimeSale;
import com.www.timesaleservice.dto.TimeSaleDto;
import com.www.timesaleservice.dto.TimeSaleDto.Response;
import com.www.timesaleservice.service.v2.TimeSaleRedisService;
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

@RestController("TimeSaleControllerV2")
@RequestMapping("/api/v2/time-sales")
@RequiredArgsConstructor
public class TimeSaleController {
  private final TimeSaleRedisService timeSaleRedisService;

  @PostMapping
  public ResponseEntity<Response> createTimeSale(@Valid @RequestBody TimeSaleDto.CreateReqeust request) {
    TimeSale timeSale = timeSaleRedisService.createTimeSale(request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(TimeSaleDto.Response.from(timeSale));
  }

  @GetMapping("/{timeSaleId}")
  public ResponseEntity<TimeSaleDto.Response> getTimeSale(@PathVariable Long timeSaleId) {
    TimeSale timeSale = timeSaleRedisService.getTimeSale(timeSaleId);
    return ResponseEntity.ok(TimeSaleDto.Response.from(timeSale));
  }

  @GetMapping
  public ResponseEntity<Page<Response>> getOngoingTimeSales(@PageableDefault Pageable pageable) {
    Page<TimeSale> timeSales = timeSaleRedisService.getOngoingTimeSales(pageable);
    return ResponseEntity.ok(timeSales.map(TimeSaleDto.Response::from));
  }

  @PostMapping("/{timeSaleId}/purchase")
  public ResponseEntity<TimeSaleDto.PurchaseResponse> purchaseTimeSale(
      @PathVariable Long timeSaleId,
      @Valid @RequestBody TimeSaleDto.PurchaseRequest request) {
    TimeSale timeSale = timeSaleRedisService.purchaseTimeSale(timeSaleId, request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(TimeSaleDto.PurchaseResponse.from(timeSale, request.getUserId(), request.getQuantity()));
  }
}
