package com.www.netplix.controller.movie;

import com.www.netplix.controller.NetplixApiResponse;
import com.www.netplix.movie.FetchMovieUseCase;
import com.www.netplix.movie.response.MoviePageableResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MovieController {

  private final FetchMovieUseCase fetchMovieUseCase;

  @GetMapping("/api/v1/movie/search")
  public NetplixApiResponse<MoviePageableResponse> search(@RequestParam(name = "page" , required = false, defaultValue = "1") int page) {
    MoviePageableResponse fetch = fetchMovieUseCase.fetchFromClient(page);
    return NetplixApiResponse.ok(fetch);
  }

}
