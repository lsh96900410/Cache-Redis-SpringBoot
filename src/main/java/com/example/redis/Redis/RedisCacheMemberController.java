package com.example.redis.Redis;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/redis")
public class RedisCacheMemberController {

    private final RedisCacheMemberService redisMemberService;

    @GetMapping("/{id}")
    public RedisMember findOne(@PathVariable String id){
        return redisMemberService.findOne(id);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable String id){
        redisMemberService.deleteMember(id);
        return "ok";
    }

    @PatchMapping("/{id}")
    public RedisMember changeAge(@PathVariable String id, @RequestParam int age){
        RedisMember redisMember = redisMemberService.changeAge(id, age);
        return redisMember;
    }
}
