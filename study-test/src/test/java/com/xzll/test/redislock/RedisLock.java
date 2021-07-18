package com.xzll.test.redislock;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.xzll.test.redis.common.RedisCommonTest;
import org.junit.Test;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import sun.jvm.hotspot.debugger.posix.elf.ELFException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import static cn.hutool.core.date.DatePattern.NORM_DATETIME_FORMATTER;
import static cn.hutool.core.date.DatePattern.PURE_DATETIME_FORMATTER;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/7/18 07:38
 * @Description:
 *
 *
 * 注意:
 *
 * if(leaseTime = = - 1L){
 *     开启自动续期的定时任务，不断重置过期时间为lockWatchdogTimeout
 *     定时任务的频率为lockWatchdogTimeout配置时间的1/3
 * }else{
 *     不自动续期
 *     设置过期时间为leaseTime
 * }
 *
 */
public class RedisLock extends RedisCommonTest {


    @Autowired
    private RedissonClient redissonClient;

    @Test
    public void lock() {
        RLock lock = redissonClient.getLock(Thread.currentThread().getName() + "+" + this.getClass().getSimpleName());
        try {
            boolean b = lock.tryLock(10, 50, TimeUnit.SECONDS);
            if (b) {
                System.out.println("true 申请锁的结果: " + b);
            } else {
                System.out.println("false 申请锁的结果: " + b);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }


    @Test
    public void testLock() {
        String format = LocalDateTimeUtil.format(LocalDateTime.now(), PURE_DATETIME_FORMATTER);
        String key = Thread.currentThread().getName() + "_" + this.getClass().getSimpleName() + "_" + format;
        RLock lock = redissonClient.getLock(key);
        try {
//            boolean b = lock.tryLock(0, 5000, TimeUnit.HOURS);
            boolean b = lock.tryLock(0, TimeUnit.MILLISECONDS);
            if (b){
                Thread.sleep(560000);
            }


            Iterable<String> keysByPattern = redissonClient.getKeys().getKeysByPattern(key);
            System.out.println(keysByPattern);
            RMap<Object, Object> map = redissonClient.getMap(key);
            System.out.println(map);
            System.out.println(map + "  -------- ");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }


}
