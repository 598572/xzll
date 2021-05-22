package com.xzll.test;

import com.sun.org.apache.bcel.internal.generic.NEW;
import com.xzll.api.ApiTest;
import com.xzll.common.CommonTest;
import com.xzll.test.util.RedisClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@SpringBootTest(classes = Study01Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringJUnit4ClassRunner.class)
public class Study01ApplicationTests {

    @Autowired
    private RedisClient redisClient;

    @Test
    public void redisClient() {
        System.out.println(redisClient);
        System.out.println(redisClient.set("234","wyq"));
        System.out.println(redisClient.get("234"));
        CommonTest commonTest = new CommonTest();
        ApiTest apiTest = new ApiTest();
        String name = commonTest.getName();
        String name1 = apiTest.getName();
        System.out.println(name);
        System.out.println(name1);
    }
}
