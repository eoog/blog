package com.www.netplix.sample;

import com.www.netplix.sample.repository.SampleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SampleController {

  private final SearchSampleUsecase searchSimpleUsecase;

  @GetMapping("/api/v1/sample")
  public SampleResponse getSample() {
    return searchSimpleUsecase.getSample();
  }
}
