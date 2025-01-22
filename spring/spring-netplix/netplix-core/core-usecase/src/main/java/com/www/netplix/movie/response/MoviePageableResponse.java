package com.www.netplix.movie.response;

import com.www.netplix.movie.NetplixMovie;
import java.util.List;
import lombok.Getter;

@Getter
public class MoviePageableResponse {
  private final List<NetplixMovie> movies;

  private final int page;
  private final Boolean hasNext;

  public MoviePageableResponse(List<NetplixMovie> movies, int page, Boolean hasNext) {
    this.movies = movies;
    this.page = page;
    this.hasNext = hasNext;
  }
}
