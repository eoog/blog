package com.www.videoredis.utils;


import static com.www.videoredis.utils.CacheKeyNames.VIDEO_VIEW_COUNT;

public class RedisKeyGenerator {
    public static String getVideoViewCountKey(String videoId) {
        return VIDEO_VIEW_COUNT + ":" +  videoId;
    }
}
