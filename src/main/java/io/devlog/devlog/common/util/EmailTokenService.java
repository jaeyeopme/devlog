package io.devlog.devlog.common.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class EmailTokenService {

    private final StringRedisTemplate tokenRedisTemplate;

    public void setToken(String token, String email, long timeoutMinutes) {
        tokenRedisTemplate.opsForValue().set(token, email, timeoutMinutes, TimeUnit.MINUTES);
    }

    public Optional<String> getEmail(String token) {
        return Optional.ofNullable(tokenRedisTemplate.opsForValue().get(token));
    }

    public void deleteToken(String token) {
        tokenRedisTemplate.delete(token);
    }

    public Boolean hasToken(String token) {
        return tokenRedisTemplate.hasKey(token);
    }

}
