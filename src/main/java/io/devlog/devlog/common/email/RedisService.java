package io.devlog.devlog.common.email;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static io.devlog.devlog.common.redis.cache.CacheRedisConfig.CACHE_REDIS_TEMPLATE_NAME;

@Service
public class RedisService {

    private final StringRedisTemplate cacheRedisTemplate;

    public RedisService(@Qualifier(value = CACHE_REDIS_TEMPLATE_NAME)
                                StringRedisTemplate cacheRedisTemplate) {
        this.cacheRedisTemplate = cacheRedisTemplate;
    }

    public void set(String key, String value, long timeoutMinutes) {
        cacheRedisTemplate.opsForValue().set(key, value, timeoutMinutes, TimeUnit.MINUTES);
    }

    public String get(String key) {
        String value = cacheRedisTemplate.opsForValue().get(key);

        return value != null ? value : "";
    }

    public void deleteToken(String token) {
        cacheRedisTemplate.delete(token);
    }

}
