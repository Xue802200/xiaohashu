package com.quanxiaoha.xiaohashu.auth;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@Slf4j
@SpringBootTest
public class RedisTests {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Test
    public void test(){
        redisTemplate.opsForValue().set("name","xzj");
    }

    @Test
    public void test1(){
        Object name = redisTemplate.opsForValue().get("name");
        System.out.println(name);
    }
}
