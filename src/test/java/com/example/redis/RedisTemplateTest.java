package com.example.redis;

import com.example.redis.Redis.RedisMember;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SpringBootTest
public class RedisTemplateTest {

    @Autowired
    RedisTemplate<String, RedisMember> redisTemplate;

    /*
    *   메소드명반환    오퍼레이션       Redis 자료구조
    * --------------------------------------------
    * opsForValue()  ValueOperations   String
    * opsForList()   ListOperations    List
    * opsForSet()    SetOperations     Set
    * opsForZSet()   ZSetOperations    Sorted Set
    * opsForHash()   HashOperations    Hash
    *
    */

    /*
     *  작업 흐름
     * 1. 연산자를 먼저 얻는다.
     * 2. set() 메소드를 통해 객체를 저장한다.
     * 3. 객체를 찾을 때 지정한 Key 값을 이용해 데이터를 찾는다.
     *  ------------------------------------------------
     * 원래 Value 는 String 타입에 대해서 연산을 진행하지만,
     * RedisConfig 설정에서 객체에 대한 직렬화 설정을 부여했기 때문에
     * 자동적으로 직렬화와 역직렬화가 수행돼, RedisMember 객체를 저장하고 데이터를 찾을 수 있다 .
     */


    @Test
    @DisplayName("Value - String 테스트")
    void redisTemplateTest(){
        ValueOperations<String, RedisMember> stringRedisMemberValueOperations=
                redisTemplate.opsForValue();

        stringRedisMemberValueOperations.set("hi",new RedisMember("hi",10));

        RedisMember redisMember = redisTemplate.opsForValue().get("hi");

        assertThat(redisMember.getAge()).isEqualTo(10);
    }

    @Test
    @DisplayName("List 테스트")
    void listTest(){
        ListOperations<String, RedisMember> stringRedisMemberListOperations=
                redisTemplate.opsForList();

        // rightPush(Key, Value) : List 에 추가
        stringRedisMemberListOperations.rightPush("redisMemberList",new RedisMember("list1",10));
        stringRedisMemberListOperations.rightPush("redisMemberList",new RedisMember("list2",10));

        // size(Key) : 크기 확인
        Long listSize = stringRedisMemberListOperations.size("redisMemberList");

        // index(Key, indexNo) : 해당 인덱스 데이터 단 건 조회
        RedisMember no1Member = stringRedisMemberListOperations.index("redisMemberList", 1);

        // range(Key , startNo , endNo) : startNo ~ endNo 까지의 다수의 데이터 조회
        List<RedisMember> results =
                stringRedisMemberListOperations.range("redisMemberList", 0, 1);

        // set(Key , indexNo, Value) : 해당 인덱스 데이터 변경
        stringRedisMemberListOperations.set("redisMemberList",0,new RedisMember("change",11));
    }

    @Test
    @DisplayName("Set 테스트")
    void setTest(){
        SetOperations<String, RedisMember> stringRedisMemberSetOperations = redisTemplate.opsForSet();
        //add(Key,Value) : set 에 추가
        stringRedisMemberSetOperations.add("memberSet",new RedisMember("set1",10));
        stringRedisMemberSetOperations.add("memberSet",new RedisMember("set2",20));
        stringRedisMemberSetOperations.add("memberSet",new RedisMember("set3",30));

        // pop(Key) : Set 에 저장된 데이터 중 랜덤하게 데이터 하나 반환
        RedisMember memberSet = stringRedisMemberSetOperations.pop("memberSet");

        // pop(Key, Count) : Set 저장된 데이터 중 랜덤하게 데이터 Count 수 만큼 반환
        List<RedisMember> memberSet1 = stringRedisMemberSetOperations.pop("memberSet",1);

        // members(Key) : set 저장된 모든 데이터 반환
        Set<RedisMember> memberSet2 = stringRedisMemberSetOperations.members("memberSet");

        // isMember(Key,Value) : set 저장된 데이터 중 Value 값 존재 확인
        Boolean member = stringRedisMemberSetOperations.isMember("memberSet", memberSet);

        // remove(Key, Value) : set 저장 데이터 중 value 값 삭제 후, 몇 건의 데이터가 삭제되었는지 반환
        Long remove = stringRedisMemberSetOperations.remove("memberSet", memberSet);

    }

    @Test
    @DisplayName("ZSet 테스트")
    void zSetTest(){
        ZSetOperations<String, RedisMember> zSet = redisTemplate.opsForZSet();

        // add(Key , Value , scoreNo) : ScoreNo 기준으로 Value 값을 정렬하며 추가
        zSet.add("memberZSet",new RedisMember("zSet1",10),3);
        zSet.add("memberZSet",new RedisMember("zSet2",20),2);
        zSet.add("memberZSet",new RedisMember("zSet3",30),1);

        // count(Key, min, max) : min ~ max 까지의 개수 반환
        Long memberZSet = zSet.count("memberZSet", 0, 2);

        // popMin(Key).getValue() : 가장 작은 가중치의 데이터를 반환
        RedisMember memberZSet1 = zSet.popMin("memberZSet").getValue();
    }

    @Test
    @DisplayName("Hash 테스트")
    void hashTest(){
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();

        hash.put("memberHashOne","name",new RedisMember("hash1",10));

        Map<String,RedisMember> map = new HashMap<>();

        map.put("hash2",new RedisMember("hash2",20));
        map.put("hash3",new RedisMember("hash3",30));

        // putAll(Key, Map) : map 의 Key 값이 hashKey 값으로 들어가며 데이터 저장
        hash.putAll("memberHashMap",map);

        // get(Key, hashKey) : Key + hashKey 값으로 데이터를 찾는다.
        RedisMember result = (RedisMember) hash.get("memberHashMap", "hash2");

        RedisMember result2 = (RedisMember) hash.get("memberHashOne", "name");

    }

}
