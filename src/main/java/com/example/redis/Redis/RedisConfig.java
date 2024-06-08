package com.example.redis.Redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
public class RedisConfig {
        /* Redis 관련 설정 - application.properties*/
        @Value("${spring.data.redis.host}")
        private String host;
        @Value("${spring.data.redis.port}")
        private int port;

        /* RedisConnectionFactory 설정
        * 1. Java 에서는 RedisClient 를 Jedis, Lettuce 두 가지를 지원하지만, Spring Boot 2.0부터 Lettuce 기본 탑재
        * -> Jedis 는 멀티쓰레드 환경에서 불안정하며, Pool 등의 한계로 사용 불가,,
        * 2. LettuceConnectionFactory 에 설정 부여하여, 클러스터에 대한 설정도 가능하다.
        */
        @Bean
        public RedisConnectionFactory redisConnectionFactory() {
            return new LettuceConnectionFactory(host, port);
        }

        /*
         * Redis 에 Data 를 저장하는 방식은 크게 두 가지가 있다.
         * 1. CrudRepository 인터페이스를 상속받아 Redis 에서 사용할 Repository 인터페이스를 만드는 방법.
         *
         * 2.RedisTemplate 설정
         * - Redis 에 직렬화 설정을 부여해서 저장하는데, 자동으로 직렬화 기능을 수행해준다.
         * - 위에서 설정한 redisConnectionFactory(lettuce) 객체를 redisTemplate ConnectionFactory 객체로 주입해준다.
         * - setKeySerializer(new StringRedisSerializer()) : Key 값으로 들어오는 String 타입에 대한 직렬화 진행
         * - setValueSerializer(new GenericJackson2JsonRedisSerializer()) : Value 값으로 들어오는 Object 객체 직렬화 진행
         * -
         */

        @Bean
        public RedisTemplate<?, ?> redisTemplate() {
            RedisTemplate<?, ?> redisTemplate = new RedisTemplate<>();
            redisTemplate.setConnectionFactory(redisConnectionFactory());

            // 일반적인 key:value의 경우 시리얼라이저
            redisTemplate.setKeySerializer(new StringRedisSerializer());
            redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

            // Hash를 사용할 경우 시리얼라이저
            redisTemplate.setHashKeySerializer(new StringRedisSerializer());
            redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
            // 모든 경우
            redisTemplate.setDefaultSerializer(new StringRedisSerializer());

            return redisTemplate;
        }

        @Bean
        public CacheManager redisCacheManager(){
            RedisCacheConfiguration redisCacheConfiguration =
                    RedisCacheConfiguration.defaultCacheConfig()
                            .serializeKeysWith(RedisSerializationContext.SerializationPair
                                    .fromSerializer(new StringRedisSerializer()))   // Key 값 직렬화
                            .serializeValuesWith(RedisSerializationContext.SerializationPair
                                    .fromSerializer(new GenericJackson2JsonRedisSerializer())) // Value 값 직렬화
                            .entryTtl(Duration.ofSeconds(30));  // Cache 유지 시간

            // LettuceConnectionFactory 주입 + Cache 설정 정보 주입 후 빌드 -> RedisCacheManager 생성
            RedisCacheManager redisCacheManager =
                    RedisCacheManager.RedisCacheManagerBuilder
                            .fromConnectionFactory(redisConnectionFactory())
                            .cacheDefaults(redisCacheConfiguration)
                            .build();

            return redisCacheManager;
        }

}
