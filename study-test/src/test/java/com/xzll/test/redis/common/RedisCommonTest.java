package com.xzll.test.redis.common;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Sets;
import com.xzll.test.StudyTestApplicationTest;
import com.xzll.test.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.data.redis.core.*;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis common 操作 redis原生命令参考  https://www.redis.net.cn/order/
 */
@Slf4j
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

    /**
     * scan命令演示
     */
    @Test
    public void redisScanTest() {
        string.set("ke", "var123456");
        string.set("key", "var0");
        Set<String> keys = getKeys("k");
        if (CollectionUtils.isNotEmpty(keys)) {
            keys.forEach(x -> {
                boolean delete = redisTemplate.delete(x);
                if (delete) {
                    log.info("redisScanTest  delete key :{}", x);
                }
            });
            System.out.println("删除后 key："+string.get("key"));
            System.out.println("删除后 ke："+string.get("ke"));
        }
    }

    /**
     * 使用scan 扫描符合前缀的key
     *
     * @param keyPrefix
     * @return
     */
    public Set<String> getKeys(String keyPrefix) {
        if (StringUtils.isBlank(keyPrefix)) {
            log.info("getKeys keyPrefix is empty:{}", keyPrefix);
            return Sets.newHashSet();
        }
        log.info("getKeys 开始执行redis scan 命令，keyPrefix：{}", keyPrefix);
        Set<String> keys = (Set<String>) redisTemplate.execute((RedisCallback<Set<String>>) connection -> {
            Cursor<byte[]> cursor = null;
            Set<String> keysTmp = new HashSet<>();
            try {
                cursor = connection.scan(new ScanOptions.ScanOptionsBuilder().match(keyPrefix + "*").count(2000).build());
                while (cursor.hasNext()) {
                    keysTmp.add(new String(cursor.next()));
                }
            } catch (Exception e) {
                log.error("getKeys exec scan error msg:{}", e);
                e.printStackTrace();
            } finally {
                if (Objects.nonNull(cursor) && !cursor.isClosed()) {
                    try {
                        cursor.close();
                        log.info("getKeys close cursor success cursorStatus:{}", cursor.isClosed());
                    } catch (IOException e) {
                        log.error("getKeys close cursor Exception :{}", e);
                        e.printStackTrace();
                    }
                }
            }
            log.info("getKeys 执行redis scan 命令结果，keyPrefix：{} , keyResult:{}", keyPrefix, JSON.toJSONString(keysTmp));
            return keysTmp;
        });
        return keys;
    }


    public void clearDB() {
        redisTemplate.getConnectionFactory().getConnection().flushDb();
    }

}
