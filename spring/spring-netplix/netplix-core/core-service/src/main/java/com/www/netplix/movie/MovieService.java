package com.www.netplix.movie;

import com.www.netplix.movie.response.MoviePageableResponse;
import com.www.netplix.movie.response.MovieResponse;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieService implements FetchMovieUseCase{
  private final TmdbMoviePort tmdbMoviePort;

  @Override
  public MoviePageableResponse fetchFromClient(int page) {
    NetplixPageableMovies movies = tmdbMoviePort.fetchPageable(page);

    return new MoviePageableResponse(movies.getNetplixMovies(), page, movies.isHasNext());
  }
}
