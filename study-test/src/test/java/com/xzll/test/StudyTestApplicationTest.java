package com.xzll.test;

import com.xzll.api.ApiTest;
import com.xzll.common.CommonTest;
import com.xzll.test.util.RedisClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest(classes = StudyTestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringJUnit4ClassRunner.class)
public class StudyTestApplicationTest {

    @Autowired
    protected RedisClient redisClient;


    @Test
    public void redisClient() {
        System.out.println(redisClient);
        System.out.println(redisClient.get("234"));
        CommonTest commonTest = new CommonTest();
        ApiTest apiTest = new ApiTest();
        String name = commonTest.getName();
        String name1 = apiTest.getName();
        System.out.println(name);
        System.out.println(name1);
    }

}
