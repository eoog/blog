package com.www.netplix.movie;

import com.www.netplix.client.TmdbHttpClient;
import com.www.netplix.utll.ObjectMapperUtil;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.http.HttpMethod;

@Component
@RequiredArgsConstructor
public class TmdbMovieListHttpClient implements TmdbMoviePort{

  @Value("${tmdb.api.movie-lists.now-playing}")
  private String nowPlaying;

  private final TmdbHttpClient tmdbHttpClient;

  @Override
  public NetplixPageableMovies fetchPageable(int page) {
    String url = nowPlaying + "?language=ko-KR&page=" + page;
    String request = tmdbHttpClient.request(url, HttpMethod.GET, CollectionUtils.toMultiValueMap(
        Map.of()), Map.of());

    TmdbResponse object = ObjectMapperUtil.toObject(request, TmdbResponse.class);

    return new NetplixPageableMovies(
        object.getResults().stream().map(TmdbMovieNowPlaying::toDomain).toList(),
        Integer.parseInt(object.getPage()),
        (Integer.parseInt(object.getTotal_pages())) - page != 0
    );
  }
}
