package com.example.redis.Redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class RedisCacheMemberService {

    private final RedisTemplate<String,RedisMember> redisTemplate;

    @Cacheable(value = "redisMemberOne", key="#id")
    public RedisMember findOne(String id){
        log.info(" Service > findOne Method Start");
        RedisMember redisMember = redisTemplate.opsForValue().get(id);
        return redisMember;
    }

    @CacheEvict(value="redisMemberOne", key="#id")
    public void deleteMember (String id){
        log.info(" Service > delete Method");
        redisTemplate.delete(id);
    }

    @CachePut(value="redisMemberOne", key="#id")
    public RedisMember changeAge(String id, int age){
        log.info(" Service > changeAge Method");
        RedisMember redisMember = redisTemplate.opsForValue().get(id);
        redisMember.changeAge(age);
        redisTemplate.opsForValue().set(id,redisMember);
        return redisMember;
    }

}
