package com.example.redis.Redis;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

/*
*   RedisMember Class
* 1. RedisHash : Redis 에 저장될 때 사용되는 prefix ex)redisMember:key 값
* 2. Id 가 Key 값으로 사용된다. 만약 Key 값을 안 넣어주면 Redis 에 입력 시 랜덤한 값으로 설정
*
*/

@Getter
@NoArgsConstructor
@RedisHash(value = "redisMember")
@ToString
public class RedisMember implements Serializable {

    @Id
    private String id;
    private String name;
    private int age;

    public RedisMember(String name,int age){
        this.id=name;
        this.name=name;
        this.age=age;
    }

    public void changeAge(int age){
        this.age= age;
    }

}

