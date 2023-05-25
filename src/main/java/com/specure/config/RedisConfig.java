package com.specure.config;

import com.specure.constant.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.Set;

@Configuration
public class RedisConfig {

    @Value("${redis.port}")
    private Integer redisPort;

    @Value("${redis.host}")
    private String redisHost;

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(5))
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(JedisConnectionFactory jedisConnectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory);
        template.setStringSerializer(new StringRedisSerializer());
        return template;
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory(new RedisStandaloneConfiguration(redisHost, redisPort));
    }

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        return (builder) -> builder
                .withCacheConfiguration(Config.SPEED_DETAIL_CACHE_NAME,
                        RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(Config.SPEED_DETAIL_CACHE_EXPIRE_SECONDS)))
                .withCacheConfiguration(Config.ANONYMIZE_CACHE_NAME,
                        RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(Config.ANONYMIZE_CACHE_EXPIRE_HOURS)));
    }

    @Bean(name = "redisCacheManager")
    public RedisCacheManager cacheManager(RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizers,
                                          RedisConnectionFactory redisConnectionFactory,
                                          RedisCacheConfiguration redisCacheConfiguration) {
        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .initialCacheNames(Set.of(Config.SPEED_DETAIL_CACHE_NAME, Config.ANONYMIZE_CACHE_NAME));
        redisCacheManagerBuilderCustomizers.customize(builder);
        return builder.build();
    }
}
