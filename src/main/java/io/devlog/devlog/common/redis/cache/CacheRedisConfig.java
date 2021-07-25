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

import static io.devlog.devlog.common.redis.RedisConfigConstant.*;

@EnableCaching
@Configuration
public class CacheRedisConfig {

    public static final String CACHE_REDIS_TEMPLATE_NAME = "cacheRedisTemplate";
    public static final String CACHE_REDIS_FACTORY_NAME = "cacheRedisFactory";
    @Value("${spring.redis.cache.host}")
    private String host;

    @Value("${spring.redis.cache.port}")
    private int port;

    @Value("${spring.redis.cache.password}")
    private String password;


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
        configurations.put(POST, redisCacheConfiguration.entryTtl(Duration.ofMinutes(POST_CACHE_EXPIRE_TIME)));
        configurations.put(COMMENT, redisCacheConfiguration.entryTtl(Duration.ofMinutes(COMMENT_CACHE_EXPIRE_TIME)));

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
