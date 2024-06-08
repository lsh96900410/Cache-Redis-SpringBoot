package com.example.redis.Redis;

import org.springframework.data.repository.CrudRepository;

public interface MemberRedisRepository extends CrudRepository<RedisMember,String> {
}
