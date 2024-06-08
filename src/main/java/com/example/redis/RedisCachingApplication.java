package com.example.redis;

import com.example.redis.Redis.RedisMember;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootApplication
@EnableCaching
@RequiredArgsConstructor
public class RedisCachingApplication {

	private final RedisTemplate<String, RedisMember> redisTemplate;

	public static void main(String[] args) {
		SpringApplication.run(RedisCachingApplication.class, args);
	}

	/* Test Data */
	@PostConstruct
	public void init(){
		for(int i=0; i<10; i++){
			RedisMember redisMember = new RedisMember("testMember" + i, i);
			redisTemplate.opsForValue().set(redisMember.getId(),redisMember);
		}
	}

}
