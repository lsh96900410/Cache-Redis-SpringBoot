package com.example.redis;

import com.example.redis.Redis.MemberRedisRepository;
import com.example.redis.Redis.RedisMember;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class RedisCrudRepositoryTest {

    @Autowired
    private MemberRedisRepository memberRedisRepository;

    @Test
    @DisplayName("redisCrudRepository 테스트")
    void repositoryTest(){
        memberRedisRepository.save(new RedisMember("Hi",10));

        RedisMember redisMember = memberRedisRepository.findById("Hi").orElseThrow();

        assertThat(redisMember.getName()).isEqualTo("Hi");
    }

    @Test
    @DisplayName(" 전체 반환")
    void repositoryFindAllTest(){
        memberRedisRepository.save(new RedisMember("Hi 1",20));

        memberRedisRepository.save(new RedisMember("Hi 2",30));

        Iterable<RedisMember> all = memberRedisRepository.findAll();

        List<RedisMember> lists = new ArrayList<>();
        all.forEach(lists::add);

        assertThat(lists.size()).isEqualTo(3);

    }




}
