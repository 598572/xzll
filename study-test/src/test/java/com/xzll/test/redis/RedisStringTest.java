package com.xzll.test.redis;

import com.xzll.test.Study01ApplicationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.HashMap;
import java.util.Map;

/**
 * redis string类型 原生命令与 redisTemplate 方法对应
 */
public class RedisStringTest extends Study01ApplicationTests {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    /**
     * redis string类型 原生命令与 redisTemplate 方法对应
     */
    @Test
    public void string() {
        //SET key value
        ValueOperations<String, Object> string = redisTemplate.opsForValue();

        /**
         * Redis SET 命令用于设置给定 key 的值。如果 key 已经存储其他值， SET 就覆写旧值，且无视类型。
         *
         * redis 127.0.0.1:6379> SET KEY_NAME VALUE
         */
        string.set("key", "value");


        /**
         * redis 127.0.0.1:6379> SETNX KEY_NAME VALUE
         */
        Boolean aBoolean = string.setIfPresent("nxkey", "nxvalue");


        /**
         * redis 127.0.0.1:6379> GETRANGE KEY_NAME start end
         */
        //redisTemplate中没找到


        /**
         * redis 127.0.0.1:6379> MSET key1 value1 key2 value2 .. keyN valueN
         */
        Map<String, Object> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        string.multiSet(map);


        /**
         * Redis Setex 命令为指定的 key 设置值及其过期时间。如果 key 已经存在， SETEX 命令将会替换旧的值。
         *
         * redis 127.0.0.1:6379> SETEX KEY_NAME TIMEOUT VALUE
         */
        //redisTemplate.expire() 不确定是不是这个


        /**
         * Redis Get 命令用于获取指定 key 的值。如果 key 不存在，返回 nil 。如果key 储存的值不是字符串类型，返回一个错误。
         * redis 127.0.0.1:6379> GET KEY_NAME
         */
        Object key_name = string.get("key_name");
        System.out.println(key_name);


        /**
         * Redis Setbit 命令用于对 key 所储存的字符串值，设置或清除指定偏移量上的位(bit)。
         * redis 127.0.0.1:6379> Setbit KEY_NAME OFFSET
         *
         * redis> SETBIT bit 10086 1
         * (integer) 0
         *
         * redis> GETBIT bit 10086
         * (integer) 1
         *
         * redis> GETBIT bit 100   # bit 默认被初始化为 0
         * (integer) 0
         */
        Boolean setbit = string.setBit("getbit", 10, true);
        System.out.println(setbit);


        /**
         * Redis Getbit 命令用于对 key 所储存的字符串值，获取指定偏移量上的位(bit)。
         * redis 127.0.0.1:6379> GETBIT KEY_NAME OFFSET
         *
         * # 对不存在的 key 或者不存在的 offset 进行 GETBIT， 返回 0
         *
         * redis> EXISTS bit
         * (integer) 0
         *
         * redis> GETBIT bit 10086
         * (integer) 0
         *
         *
         * # 对已存在的 offset 进行 GETBIT
         *
         * redis> SETBIT bit 10086 1
         * (integer) 0
         *
         * redis> GETBIT bit 10086
         * (integer) 1
         */

        Boolean getbit = string.getBit("getbit", 10);
        System.out.println(getbit);

        /**
         * Redis Decr 命令将 key 中储存的数字值减一。
         * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECR 操作。
         * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
         * 本操作的值限制在 64 位(bit)有符号数字表示之内
         *
         * redis 127.0.0.1:6379> DECR KEY_NAME
         *
         * # 对存在的数字值 key 进行 DECR
         *
         * redis> SET failure_times 10
         * OK
         *
         * redis> DECR failure_times
         * (integer) 9
         *
         *
         * # 对不存在的 key 值进行 DECR
         *
         * redis> EXISTS count
         * (integer) 0
         *
         * redis> DECR count
         * (integer) -1
         *
         *
         * # 对存在但不是数值的 key 进行 DECR
         *
         * redis> SET company YOUR_CODE_SUCKS.LLC
         * OK
         *
         * redis> DECR company
         * (error) ERR value is not an integer or out of range
         *
         */
        Long decrement = string.decrement("decrement");
        System.out.println(decrement);

        /**
         * Redis Decrby 命令将 key 所储存的值减去指定的减量值。
         *
         * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECRBY 操作。
         *
         * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
         *
         * 本操作的值限制在 64 位(bit)有符号数字表示之内。
         *
         * redis 127.0.0.1:6379> DECRBY KEY_NAME DECREMENT_AMOUNT
         *
         * # 对已存在的 key 进行 DECRBY
         *
         * redis> SET count 100
         * OK
         *
         * redis> DECRBY count 20
         * (integer) 80
         *
         *
         * # 对不存在的 key 进行DECRBY
         *
         * redis> EXISTS pages
         * (integer) 0
         *
         * redis> DECRBY pages 10
         * (integer) -10
         */
        Long decrement_num = string.decrement("decrement_num", 20);
        System.out.println(decrement_num);


        /**
         * Redis Strlen 命令用于获取指定 key 所储存的字符串值的长度。当 key 储存的不是字符串值时，返回一个错误。
         * redis 127.0.0.1:6379> STRLEN KEY_NAME
         *
         * # 获取字符串的长度
         *
         * redis> SET mykey "Hello world"
         * OK
         *
         * redis> STRLEN mykey
         * (integer) 11
         *
         *
         * # 不存在的 key 长度为 0
         *
         * redis> STRLEN nonexisting
         * (integer) 0
         *
         */
        //暂时未找到

        /**
         * Redis Msetnx 命令用于所有给定 key 都不存在时，同时设置一个或多个 key-value 对。
         * redis 127.0.0.1:6379> MSETNX key1 value1 key2 value2 .. keyN valueN
         *
         * # 对不存在的 key 进行 MSETNX
         *
         * redis> MSETNX rmdbs "MySQL" nosql "MongoDB" key-value-store "redis"
         * (integer) 1
         *
         * redis> MGET rmdbs nosql key-value-store
         * 1) "MySQL"
         * 2) "MongoDB"
         * 3) "redis"
         *
         *
         * # MSET 的给定 key 当中有已存在的 key
         *
         * redis> MSETNX rmdbs "Sqlite" language "python"  # rmdbs 键已经存在，操作失败
         * (integer) 0
         *
         * redis> EXISTS language                          # 因为 MSET 是原子性操作，language 没有被设置
         * (integer) 0
         *
         * redis> GET rmdbs                                # rmdbs 也没有被修改
         * "MySQL"
         *
         */
        Map<String, Object> mmap = new HashMap<>();
        mmap.put("mkey1", "mvalue1");
        mmap.put("mkey2", "mvalue2");
        Boolean aBoolean1 = string.multiSetIfAbsent(mmap);
        System.out.println(aBoolean1);

        /**
         * Redis Incrby 命令将 key 中储存的数字加上指定的增量值。
         *
         * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCRBY 命令。
         *
         * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
         *
         * 本操作的值限制在 64 位(bit)有符号数字表示之内。
         *
         * redis 127.0.0.1:6379> INCRBY KEY_NAME INCR_AMOUNT
         *
         * # key 存在且是数字值
         *
         * redis> SET rank 50
         * OK
         *
         * redis> INCRBY rank 20
         * (integer) 70
         *
         * redis> GET rank
         * "70"
         *
         *
         * # key 不存在时
         *
         * redis> EXISTS counter
         * (integer) 0
         *
         * redis> INCRBY counter 30
         * (integer) 30
         *
         * redis> GET counter
         * "30"
         *
         *
         * # key 不是数字值时
         *
         * redis> SET book "long long ago..."
         * OK
         *
         * redis> INCRBY book 200
         * (error) ERR value is not an integer or out of range
         */
        Long incr = string.increment("incr");
        System.out.println(incr);

        /**
         * Redis Incrbyfloat 命令为 key 中所储存的值加上指定的浮点数增量值。
         *
         * 如果 key 不存在，那么 INCRBYFLOAT 会先将 key 的值设为 0 ，再执行加法操作。
         *
         * redis 127.0.0.1:6379> INCRBYFLOAT KEY_NAME INCR_AMOUNT
         *
         * # 值和增量都不是指数符号
         *
         * redis> SET mykey 10.50
         * OK
         *
         * redis> INCRBYFLOAT mykey 0.1
         * "10.6"
         *
         *
         * # 值和增量都是指数符号
         *
         * redis> SET mykey 314e-2
         * OK
         *
         * redis> GET mykey                # 用 SET 设置的值可以是指数符号
         * "314e-2"
         *
         * redis> INCRBYFLOAT mykey 0      # 但执行 INCRBYFLOAT 之后格式会被改成非指数符号
         * "3.14"
         *
         *
         * # 可以对整数类型执行
         *
         * redis> SET mykey 3
         * OK
         *
         * redis> INCRBYFLOAT mykey 1.1
         * "4.1"
         *
         *
         * # 后跟的 0 会被移除
         *
         * redis> SET mykey 3.0
         * OK
         *
         * redis> GET mykey                                    # SET 设置的值小数部分可以是 0
         * "3.0"
         *
         * redis> INCRBYFLOAT mykey 1.000000000000000000000    # 但 INCRBYFLOAT 会将无用的 0 忽略掉，有需要的话，将浮点变为整数
         * "4"
         *
         * redis> GET mykey
         * "4"
         *
         *
         */
        Double aFloat = string.increment("float", 0.2d);
        System.out.println(aFloat);

        //todo
    }
}
