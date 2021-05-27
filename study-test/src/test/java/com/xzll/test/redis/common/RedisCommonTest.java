package com.xzll.test.redis.common;

import com.xzll.test.StudyTestApplicationTest;
import com.xzll.test.util.SpringUtil;
import org.junit.Test;
import org.springframework.data.redis.core.*;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * redis common 操作 redis原生命令参考  https://www.redis.net.cn/order/
 */
public class RedisCommonTest extends StudyTestApplicationTest {

    protected RedisTemplate<Object, Object> redisTemplate;

    protected ListOperations<Object, Object> list;
    protected HashOperations<Object, Object, Object> map;
    protected SetOperations<Object, Object> set;
    protected ValueOperations<Object, Object> string;
    protected ZSetOperations<Object, Object> zSet;
    protected HyperLogLogOperations<Object, Object> hyperLogLog;

    /**
     * 初始化各个数据类型对象
     */
    @PostConstruct
    public void init() {
        redisTemplate = (RedisTemplate<Object, Object>) SpringUtil.getBean("redisTemplate");
        hyperLogLog = redisTemplate.opsForHyperLogLog();
        string = redisTemplate.opsForValue();
        list = redisTemplate.opsForList();
        set = redisTemplate.opsForSet();
        map = redisTemplate.opsForHash();
        zSet = redisTemplate.opsForZSet();
    }

    @Test
    public void common() {
        Boolean mylist = redisTemplate.expire("mylist", 10, TimeUnit.DAYS);
        System.out.println(mylist);
        /**
         *
         */
    }

    public void clearDB(){
        redisTemplate.getConnectionFactory().getConnection().flushDb();
    }

}
