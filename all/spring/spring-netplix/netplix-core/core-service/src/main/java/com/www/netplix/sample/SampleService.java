package com.www.netplix.sample;

import com.www.netplix.sample.repository.SampleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SampleService implements SearchSampleUsecase{

  private final SamplePort samplePort;

  @Override
  public SampleResponse getSample() {
    SamplePortResponse sample = samplePort.getSample();
    return new SampleResponse(sample.getName());
  }
}
