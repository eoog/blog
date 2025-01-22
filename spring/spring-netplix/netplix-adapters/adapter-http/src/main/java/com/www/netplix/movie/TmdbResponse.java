package com.www.netplix.movie;

import java.util.List;
import lombok.Getter;

@Getter
public class TmdbResponse {
  private TmdbDateResponse dates;
  private String page;
  private String total_pages;
  private String total_results;
  private List<TmdbMovieNowPlaying> results;
}
