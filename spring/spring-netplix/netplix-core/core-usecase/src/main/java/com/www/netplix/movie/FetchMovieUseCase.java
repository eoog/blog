package com.www.netplix.movie;

import com.www.netplix.movie.response.MoviePageableResponse;

public interface FetchMovieUseCase {
   MoviePageableResponse fetchFromClient(int page);
}
