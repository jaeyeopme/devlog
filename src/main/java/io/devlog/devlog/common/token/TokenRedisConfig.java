package io.devlog.devlog.common.token;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class TokenRedisConfig {

    @Value("${spring.redis.token.host}")
    private String host;

    @Value("${spring.redis.token.password}")
    private String password;

    @Value("${spring.redis.token.port}")
    private int port;

    @Bean(name = "tokenRedisFactory")
    public RedisConnectionFactory tokenRedisFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPassword(password);
        redisStandaloneConfiguration.setPort(port);
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean(name = "tokenRedisTemplate")
    public StringRedisTemplate tokenRedisTemplate(
            @Qualifier(value = "tokenRedisFactory") RedisConnectionFactory tokenRedisFactory) {
        return new StringRedisTemplate(tokenRedisFactory);
    }

}
