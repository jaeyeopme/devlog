package io.devlog.devlog.common.redis;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RedisConfigConstant {

    public static final String POST = "POST";
    public static final String COMMENT = "COMMENT";

    public static final long POST_CACHE_EXPIRE_TIME = 5L;
    public static final long COMMENT_CACHE_EXPIRE_TIME = 10L;
    public static final long EMAIL_TOKEN_EXPIRE_TIME = 180L;

}
