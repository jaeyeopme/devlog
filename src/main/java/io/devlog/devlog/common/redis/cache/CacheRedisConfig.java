package io.devlog.devlog.common.redis.cache;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@EnableCaching
@Configuration
public class CacheRedisConfig {

    public static final String CACHE_REDIS_TEMPLATE_NAME = "cacheRedisTemplate";
    public static final String CACHE_REDIS_FACTORY_NAME = "cacheRedisFactory";

    public static final String POST = "POST";
    public static final String COMMENT = "COMMENT";

    public static final long POST_CACHE_EXPIRATION_MINUTES = 5L;
    public static final long COMMENT_CACHE_EXPIRATION_MINUTES = 10L;

    private final String host;

    private final int port;

    private final String password;

    public CacheRedisConfig(@Value("${spring.redis.cache.host}") String host,
                            @Value("${spring.redis.cache.port}") int port,
                            @Value("${spring.redis.cache.password}") String password) {
        this.host = host;
        this.port = port;
        this.password = password;
    }

    @Bean(CACHE_REDIS_FACTORY_NAME)
    public RedisConnectionFactory cacheRedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setPassword(password);
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public RedisCacheManager redisCacheManager(
            @Qualifier(value = CACHE_REDIS_FACTORY_NAME) RedisConnectionFactory redisCacheFactory) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

        Map<String, RedisCacheConfiguration> configurations = new HashMap<>();
        configurations.put(POST, redisCacheConfiguration.entryTtl(Duration.ofMinutes(POST_CACHE_EXPIRATION_MINUTES)));
        configurations.put(COMMENT, redisCacheConfiguration.entryTtl(Duration.ofMinutes(COMMENT_CACHE_EXPIRATION_MINUTES)));

        return RedisCacheManager.builder(redisCacheFactory)
                .withInitialCacheConfigurations(configurations)
                .build();
    }

    @Bean(name = CACHE_REDIS_TEMPLATE_NAME)
    public StringRedisTemplate cacheRedisTemplate(
            @Qualifier(value = CACHE_REDIS_FACTORY_NAME) RedisConnectionFactory redisCacheFactory) {
        return new StringRedisTemplate(redisCacheFactory);
    }

}
