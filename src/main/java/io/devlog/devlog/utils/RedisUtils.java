package io.devlog.devlog.utils;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static io.devlog.devlog.config.redis.CacheRedisConfig.STRING_TEMPLATE_NAME;

@Component
public class RedisUtils {

    private final StringRedisTemplate template;

    public RedisUtils(@Qualifier(value = STRING_TEMPLATE_NAME)
                              StringRedisTemplate template) {
        this.template = template;
    }

    public void set(String key, String value, long timeoutMinutes) {
        template.opsForValue().set(key, value, timeoutMinutes, TimeUnit.MINUTES);
    }

    public String get(String key) {
        String value = template.opsForValue().get(key);

        return value != null && delete(key) ? value : "";
    }

    public boolean delete(String key) {
        return Boolean.TRUE.equals(template.delete(key));
    }

}
