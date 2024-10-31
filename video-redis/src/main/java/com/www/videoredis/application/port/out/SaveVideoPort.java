package com.www.videoredis.application.port.out;

public interface SaveVideoPort {
  Long incrementViewCount(String videoId);
}
