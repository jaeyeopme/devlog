package io.devlog.devlog.common.redis.session;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableRedisHttpSession
@Configuration
public class SessionRedisConfig {

    @Value("${spring.redis.session.host}")
    private String host;

    @Value("${spring.redis.session.port}")
    private int port;

    @Value("${spring.redis.session.password}")
    private String password;

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
