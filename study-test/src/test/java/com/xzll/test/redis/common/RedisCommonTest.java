package com.xzll.test.redis.common;

import com.xzll.test.StudyTestApplicationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * redis common 操作 redis原生命令参考  https://www.redis.net.cn/order/
 */
public class RedisCommonTest extends StudyTestApplicationTest {

    @Autowired
    protected RedisTemplate<String, Object> redisTemplate;

    @Test
    public void common() {
        Boolean mylist = redisTemplate.expire("mylist", 10, TimeUnit.DAYS);
        System.out.println(mylist);
        /**
         *
         */
    }

}
