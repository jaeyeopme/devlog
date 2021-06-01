package io.devlog.devlog.common.token;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class TokenRedisUtils {

    private final StringRedisTemplate tokenRedisTemplate;

    public TokenRedisUtils(@Qualifier(value = "tokenTemplate") StringRedisTemplate tokenRedisTemplate) {
        this.tokenRedisTemplate = tokenRedisTemplate;
    }

    public String generateToken(String email, long timeoutMinutes) {
        String token = String.valueOf(UUID.randomUUID());
        tokenRedisTemplate.opsForValue().set(token, email, timeoutMinutes, TimeUnit.MINUTES);
        return token;
    }

    public Optional<String> getEmail(String token) {
        Optional<String> email = Optional.ofNullable(tokenRedisTemplate.opsForValue().get(token));
        tokenRedisTemplate.delete(token);
        return email;
    }

}
