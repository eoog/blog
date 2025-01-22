package com.www.netplix.movie;

public interface TmdbMoviePort {
   NetplixPageableMovies fetchPageable(int page);
}
