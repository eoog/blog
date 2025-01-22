package com.www.netplix.controller.sample;

import com.www.netplix.controller.NetplixApiResponse;
import com.www.netplix.movie.FetchMovieUseCase;
import com.www.netplix.movie.response.MoviePageableResponse;
import com.www.netplix.sample.SearchSampleUsecase;
import com.www.netplix.sample.repository.SampleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
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
