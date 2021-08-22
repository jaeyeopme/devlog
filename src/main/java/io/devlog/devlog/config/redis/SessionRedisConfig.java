package io.devlog.devlog.config.redis;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableRedisHttpSession
public class SessionRedisConfig {

    private final String host;

    private final int port;

    private final String password;

    public SessionRedisConfig(@Value("${spring.redis.session.host}") String host,
                              @Value("${spring.redis.session.port}") int port,
                              @Value("${spring.redis.session.password}") String password) {
        this.host = host;
        this.port = port;
        this.password = password;
    }

    @Primary
    @Bean(name = "sessionRedisFactory")
    public RedisConnectionFactory sessionRedisFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setPassword(password);
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Primary
    @Bean(name = "sessionRedisTemplate")
    public RedisTemplate<Object, Object> sessionRedisTemplate(
            @Qualifier(value = "sessionRedisFactory") RedisConnectionFactory sessionFactory) {
        RedisTemplate<Object, Object> sessionRedisTemplate = new RedisTemplate<>();
        sessionRedisTemplate.setConnectionFactory(sessionFactory);
        return sessionRedisTemplate;
    }

}
