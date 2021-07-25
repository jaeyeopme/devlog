package io.devlog.devlog.common.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static io.devlog.devlog.common.redis.cache.CacheRedisConfig.CACHE_REDIS_TEMPLATE_NAME;

@Slf4j
@Service
public class TokenRedisService {

    private final StringRedisTemplate cacheRedisTemplate;

    public TokenRedisService(@Qualifier(value = CACHE_REDIS_TEMPLATE_NAME)
                                     StringRedisTemplate cacheRedisTemplate) {
        this.cacheRedisTemplate = cacheRedisTemplate;
    }

    public String generateToken(String email, long timeoutMinutes) {
        String token = String.valueOf(UUID.randomUUID());
        cacheRedisTemplate.opsForValue().set(token, email, timeoutMinutes, TimeUnit.MINUTES);
        log.info("saved token email: {}", email);
        return token;
    }

    public Optional<String> getEmail(String token) {
        return Optional.ofNullable(cacheRedisTemplate.opsForValue().get(token));
    }

    public void deleteToken(String token) {
        cacheRedisTemplate.delete(token);
    }

}
