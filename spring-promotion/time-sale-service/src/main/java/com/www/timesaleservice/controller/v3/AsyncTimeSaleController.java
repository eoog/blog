package com.www.timesaleservice.controller.v3;

import com.www.timesaleservice.dto.TimeSaleDto;
import com.www.timesaleservice.dto.TimeSaleDto.Response;
import com.www.timesaleservice.service.v3.AsyncTimeSaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v3/time-sales")
@RequiredArgsConstructor
public class AsyncTimeSaleController {
  private final AsyncTimeSaleService asyncTimeSaleService;

  @PostMapping
  public ResponseEntity<Response> createTimeSale(@RequestBody TimeSaleDto.CreateReqeust request) {
    return ResponseEntity.ok(TimeSaleDto.Response.from(asyncTimeSaleService.createTimeSale(request)));
  }

  @GetMapping("/{id}")
  public ResponseEntity<TimeSaleDto.Response> getTimeSale(@PathVariable Long id) {
    return ResponseEntity.ok(TimeSaleDto.Response.from(asyncTimeSaleService.getTimeSale(id)));
  }

  @GetMapping
  public ResponseEntity<Page<Response>> getOngoingTimeSales(Pageable pageable) {
    return ResponseEntity.ok(asyncTimeSaleService.getOngoingTimeSales(pageable).map(TimeSaleDto.Response::from));
  }

  @PostMapping("/{id}/purchase")
  public ResponseEntity<TimeSaleDto.AsyncPurchaseResponse> purchaseTimeSale(
      @PathVariable Long id,
      @RequestBody TimeSaleDto.PurchaseRequest request) {
    String requestId = asyncTimeSaleService.purchaseTimeSale(id, request);
    return ResponseEntity.ok(TimeSaleDto.AsyncPurchaseResponse.builder()
        .requestId(requestId)
        .status("PENDING")
        .build());
  }

  @GetMapping("/purchase/result/{timeSaleId}/{requestId}")
  public ResponseEntity<TimeSaleDto.AsyncPurchaseResponse> getPurchaseResult(
      @PathVariable Long timeSaleId,
      @PathVariable String requestId) {
    return ResponseEntity.ok(asyncTimeSaleService.getPurchaseResult(timeSaleId, requestId));
  }
}
