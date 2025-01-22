package com.www.netplix.movie;

import java.util.List;
import lombok.Getter;

@Getter
public class NetplixPageableMovies {
  private final List<NetplixMovie> netplixMovies;

  private final int page;
  private final boolean hasNext;

  public NetplixPageableMovies(List<NetplixMovie> netplixMovies, int page, boolean hasNext) {
    this.netplixMovies = netplixMovies;
    this.page = page;
    this.hasNext = hasNext;
  }
}
