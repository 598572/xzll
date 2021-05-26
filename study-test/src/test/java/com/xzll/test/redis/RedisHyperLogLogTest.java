package com.xzll.test.redis;

import com.xzll.test.redis.common.RedisCommonTest;
import org.junit.Test;
import org.springframework.data.redis.core.HyperLogLogOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.ValueOperations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Redis 在 2.8.9 版本添加了 HyperLogLog 结构。
 * Redis HyperLogLog 是用来做基数统计的算法，HyperLogLog 的优点是，在输入元素的数量或者体积非常非常大时，计算基数所需的空间总是固定 的、并且是很小的。
 * 在 Redis 里面，每个 HyperLogLog 键只需要花费 12 KB 内存，就可以计算接近 2^64 个不同元素的基 数。这和计算基数时，元素越多耗费内存就越多的集合形成鲜明对比。
 * 但是，因为 HyperLogLog 只会根据输入元素来计算基数，而不会储存输入元素本身，所以 HyperLogLog 不能像集合那样，返回输入的各个元素。
 *
 * 什么是基数?
 * 比如数据集 {1, 3, 5, 7, 5, 7, 8}， 那么这个数据集的基数集为 {1, 3, 5 ,7, 8}, 基数(不重复元素)为5。 基数估计就是在误差可接受的范围内，快速计算基数。
 *
 *
 */
public class RedisHyperLogLogTest extends RedisCommonTest {

    /**
     * redis HyperLogLog 使用示例
     */
    @Test
    public void hyperLogLog() {
//        HyperLogLogOperations<String, Object> hyperLogLog=redisTemplate.opsForHyperLogLog();
        /*
        Redis Pfadd 命令将所有元素参数添加到 HyperLogLog 数据结构中。
        返回 整型，如果至少有个元素被添加返回 1， 否则返回 0。

        redis 127.0.0.1:6379> PFADD key element [element ...]

        redis 127.0.0.1:6379> PFADD mykey a b c d e f g h i j
        (integer) 1
        redis 127.0.0.1:6379> PFCOUNT mykey
        (integer) 10
         */
        Long add = hyperLogLog.add("key1", "var1", "var2");
        Long add2 = hyperLogLog.add("key2", "var1", "var2");
        System.out.println(add);

        /*
            Redis Pgmerge 命令将多个 HyperLogLog 合并为一个 HyperLogLog ，合并后的 HyperLogLog 的基数估算值是通过对所有 给定 HyperLogLog 进行并集计算得出的。
            返回 OK。

            redis 127.0.0.1:6379> PFMERGE destkey sourcekey [sourcekey ...]

            redis 127.0.0.1:6379> PFADD hll1 foo bar zap a
            (integer) 1
            redis 127.0.0.1:6379> PFADD hll2 a b c foo
            (integer) 1
            redis 127.0.0.1:6379> PFMERGE hll3 hll1 hll2
            OK
            redis 127.0.0.1:6379> PFCOUNT hll3
            (integer) 6
            redis>
         */
        Long union = hyperLogLog.union("key1", "key2");
        System.out.println(union);

        /*
        Redis Pfcount 命令返回给定 HyperLogLog 的基数估算值。
        返回 整数，返回给定 HyperLogLog 的基数值，如果多个 HyperLogLog 则返回基数估值之和。

        redis 127.0.0.1:6379> PFCOUNT key [key ...]

        redis 127.0.0.1:6379> PFADD hll foo bar zap
        (integer) 1
        redis 127.0.0.1:6379> PFADD hll zap zap zap
        (integer) 0
        redis 127.0.0.1:6379> PFADD hll foo bar
        (integer) 0
        redis 127.0.0.1:6379> PFCOUNT hll
        (integer) 3
        redis 127.0.0.1:6379> PFADD some-other-hll 1 2 3
        (integer) 1
        redis 127.0.0.1:6379> PFCOUNT hll some-other-hll
        (integer) 6
        redis>
         */
        Long size = hyperLogLog.size("key1", "key2");
        System.out.println(size);

    }

}
