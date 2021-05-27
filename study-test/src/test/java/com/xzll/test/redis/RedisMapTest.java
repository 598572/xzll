package com.xzll.test.redis;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xzll.test.redis.common.RedisCommonTest;
import org.junit.Test;
import org.springframework.data.redis.core.HashOperations;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class RedisMapTest extends RedisCommonTest {

    /**
     * redis map类型 原生命令与 redisTemplate 方法对应
     */
    @Test
    public void map() {

        clearDB();

        /**
         *Redis Hmset 命令用于同时将多个 field-value (字段-值)对设置到哈希表中。
         *
         * 此命令会覆盖哈希表中已存在的字段。
         *
         * 如果哈希表不存在，会创建一个空哈希表，并执行 HMSET 操作。
         *
         * redis 127.0.0.1:6379> HMSET KEY_NAME FIELD1 VALUE1 ...FIELDN VALUEN
         *
         * redis 127.0.0.1:6379> HSET myhash field1 "foo" field2 "bar"
         * OK
         * redis 127.0.0.1:6379> HGET myhash field1
         * "foo"
         * redis 127.0.0.1:6379> HMGET myhash field2
         * "bar"
         */

        Map<String, Object> valueMap = Maps.newHashMap();
        valueMap.put("putAll:key1","v1");
        valueMap.put("putAll:key2","v2");
        valueMap.put("putAll:key3","v3");
        map.putAll("putAll:key",valueMap);
        List<Object> putAllKey = map.multiGet("putAll:key", Stream.of("putAll:key1", "putAll:key2", "putAll:key3").collect(Collectors.toList()));
        System.out.println("putAllKey  "+ JSON.toJSONString(putAllKey)); //putAllKey  ["v1","v2","v3"]

        /**
         * Redis Hmget 命令用于返回哈希表中，一个或多个给定字段的值。
         * 如果指定的字段不存在于哈希表，那么返回一个 nil 值。
         *
         * redis 127.0.0.1:6379> HMGET KEY_NAME FIELD1...FIELDN
         *
         * redis 127.0.0.1:6379> HSET myhash field1 "foo"
         * (integer) 1
         * redis 127.0.0.1:6379> HSET myhash field2 "bar"
         * (integer) 1
         * redis 127.0.0.1:6379> HMGET myhash field1 field2 nofield
         * 1) "foo"
         * 2) "bar"
         * 3) (nil)
         */
        List<Object> objects = Lists.newArrayList();
        objects.add("putAll:key2");
        objects.add("putAll:key3");
        List<Object> putAll = map.multiGet("putAll:key", objects);//上边已有 略
        System.out.println(putAll);//[v2, v3]


        /**
         * Redis Hset 命令用于为哈希表中的字段赋值 。
         * 如果哈希表不存在，一个新的哈希表被创建并进行 HSET 操作。
         * 如果字段已经存在于哈希表中，旧值将被覆盖。
         * 如果字段是哈希表中的一个新建字段，并且值设置成功，返回 1 。 如果哈希表中域字段已经存在且旧值已被新值覆盖，返回 0 。
         *
         * redis 127.0.0.1:6379> HSET KEY_NAME FIELD VALUE
         *
         *
         * redis 127.0.0.1:6379> HSET myhash field1 "foo"
         * OK
         * redis 127.0.0.1:6379> HGET myhash field1
         * "foo"
         *
         * redis 127.0.0.1:6379> HSET website google "www.g.cn"       # 设置一个新域
         * (integer) 1
         *
         * redis 127.0.0.1:6379>HSET website google "www.google.com" # 覆盖一个旧域
         * (integer) 0
         */
        map.put("put:key","key01","value01");//存储单个key value
        map.put("put:key","key02","value02");//存储单个key value

        /**
         * Redis Hgetall 命令用于返回哈希表中，所有的字段和值。
         * 在返回值里，紧跟每个字段名(field name)之后是字段的值(value)，所以返回值的长度是哈希表大小的两倍。
         * 以列表形式返回哈希表的字段及字段值。 若 key 不存在，返回空列表。
         *
         *
         * redis 127.0.0.1:6379> HGETALL KEY_NAME
         *
         * redis 127.0.0.1:6379> HSET myhash field1 "foo"
         * (integer) 1
         * redis 127.0.0.1:6379> HSET myhash field2 "bar"
         * (integer) 1
         * redis 127.0.0.1:6379> HGETALL myhash
         * 1) "field1"
         * 2) "Hello"
         * 3) "field2"
         * 4) "World"
         */
        Map<Object, Object> map2 = map.entries("putAll:key");
        System.out.println("entries  "+map2);//entries  {putAll:key3=v3, putAll:key2=v2, putAll:key1=v1}


        /**
         * Redis Hget 命令用于返回哈希表中指定字段的值。
         * 返回给定字段的值。如果给定的字段或 key 不存在时，返回 nil 。
         *
         * redis 127.0.0.1:6379> HGET KEY_NAME FIELD_NAME
         *
         * # 字段存在
         *
         * redis> HSET site redis redis.com
         * (integer) 1
         *
         * redis> HGET site redis
         * "redis.com"
         *
         *
         * # 字段不存在
         *
         * redis> HGET site mysql
         * (nil)
         */
        Object o = map.get("putAll:key", "putAll:key1");
        System.out.println("get"+ o);//v1

        /**
         * Redis Hexists 命令用于查看哈希表的指定字段是否存在。
         *
         * redis 127.0.0.1:6379> HEXISTS KEY_NAME FIELD_NAME
         *
         * redis 127.0.0.1:6379> HSET myhash field1 "foo"
         * (integer) 1
         * redis 127.0.0.1:6379> HEXISTS myhash field1
         * (integer) 1
         * redis 127.0.0.1:6379> HEXISTS myhash field2
         * (integer) 0
         */
        Boolean aBoolean = map.hasKey("putAll:key", "putAll:key1");
        System.out.println("hasKey "+aBoolean);//hasKey true

        /**
         * Redis Hincrby 命令用于为哈希表中的字段值加上指定增量值。
         * 增量也可以为负数，相当于对指定字段进行减法操作。
         * 如果哈希表的 key 不存在，一个新的哈希表被创建并执行 HINCRBY 命令。
         * 如果指定的字段不存在，那么在执行命令前，字段的值被初始化为 0 。
         * 对一个储存字符串值的字段执行 HINCRBY 命令将造成一个错误。
         * 本操作的值被限制在 64 位(bit)有符号数字表示之内。
         *
         * redis 127.0.0.1:6379> HINCRBY KEY_NAME FIELD_NAME INCR_BY_NUMBER
         *
         * redis 127.0.0.1:6379> HSET myhash field1 20
         * (integer) 1
         * redis 127.0.0.1:6379> HINCRBY myhash field 1
         * (integer) 21
         * redis 127.0.0.1:6379> HINCRBY myhash field -1
         * (integer) 20
         */
        map.put("incr:key","incr:key1",1);
        Long increment = map.increment("incr:key", "incr:key1", 3);
        System.out.println("incr:key "+JSON.toJSONString(map.entries("incr:key")));//incr:key {"incr:key1":4}

        /**
         * Redis Hlen 命令用于获取哈希表中字段的数量。
         *
         * redis 127.0.0.1:6379> HLEN KEY_NAME
         *
         * redis 127.0.0.1:6379> HSET myhash field1 "foo"
         * (integer) 1
         * redis 127.0.0.1:6379> HSET myhash field2 "bar"
         * (integer) 1
         * redis 127.0.0.1:6379> HLEN myhash
         * (integer) 2
         */
        Long aLong = map.size("putAll:key");
        System.out.println("lengthOfValue "+aLong);//lengthOfValue 3

        /**
         * Redis Hdel 命令用于删除哈希表 key 中的一个或多个指定字段，不存在的字段将被忽略。
         *
         * redis 127.0.0.1:6379> HDEL KEY_NAME FIELD1.. FIELDN
         *
         * redis 127.0.0.1:6379> HSET myhash field1 "foo"
         * (integer) 1
         * redis 127.0.0.1:6379> HDEL myhash field1
         * (integer) 1
         * redis 127.0.0.1:6379> HDEL myhash field2
         * (integer) 0
         */

        Long delete = map.delete("putAll:key","putAll:key1");//不传具体的hashkey时候会报错
        // org.springframework.data.redis.RedisSystemException: Unknown redis exception; nested exception is java.lang.IllegalArgumentException: Fields must not be empty
        System.out.println("delete: "+JSON.toJSONString(map.entries("putAll:key")));//delete: {"putAll:key3":"v3","putAll:key2":"v2"}


        /**
         *Redis Hvals 命令返回哈希表所有字段的值。
         *
         * redis 127.0.0.1:6379> HVALS KEY_NAME FIELD VALUE
         *
         * redis 127.0.0.1:6379> HSET myhash field1 "foo"
         * (integer) 1
         * redis 127.0.0.1:6379> HSET myhash field2 "bar"
         * (integer) 1
         * redis 127.0.0.1:6379> HVALS myhash
         * 1) "foo"
         * 2) "bar"
         *
         * # 空哈希表/不存在的key
         *
         * redis 127.0.0.1:6379> EXISTS not_exists
         * (integer) 0
         *
         * redis 127.0.0.1:6379> HVALS not_exists
         * (empty list or set)
         */
        List<Object> map1 = map.values("putAll:key");
        System.out.println("values： "+JSON.toJSONString(map1));//values： ["v3","v2"]

        /**
         * Redis Hincrbyfloat 命令用于为哈希表中的字段值加上指定浮点数增量值。
         * 如果指定的字段不存在，那么在执行命令前，字段的值被初始化为 0 。
         *
         * redis 127.0.0.1:6379> HINCRBYFLOAT KEY_NAME FIELD_NAME INCR_BY_NUMBER
         *
         * redis 127.0.0.1:6379> HSET myhash field 20.50
         * (integer) 1
         * redis 127.0.0.1:6379> HINCRBYFLOAT mykey field 0.1
         * "20.60"
         */
        Double increment1 = map.increment("increment:key", "increment:key1", 3d);//上边已经存在 这个只是类型为Double 略
        System.out.println(increment1);//3.0

        /**
         * Redis Hkeys 命令用于获取哈希表中的所有字段名。
         * 包含哈希表中所有字段的列表。 当 key 不存在时，返回一个空列表。
         *
         * redis 127.0.0.1:6379> HKEYS KEY_NAME FIELD_NAME INCR_BY_NUMBER
         *
         * redis 127.0.0.1:6379> HSET myhash field1 "foo"
         * (integer) 1
         * redis 127.0.0.1:6379> HSET myhash field2 "bar"
         * (integer) 1
         * redis 127.0.0.1:6379> HKEYS myhash
         * 1) "field1"
         * 2) "field2"
         */
        Set<Object> map3 = map.keys("putAll:key");
        System.out.println("keys  "+JSON.toJSONString(map3));// ["putAll:key3","putAll:key2"]

        /**
         * Redis Hsetnx 命令用于为哈希表中不存在的的字段赋值 。
         * 如果哈希表不存在，一个新的哈希表被创建并进行 HSET 操作。
         * 如果字段已经存在于哈希表中，操作无效。
         * 如果 key 不存在，一个新哈希表被创建并执行 HSETNX 命令。
         *
         * redis 127.0.0.1:6379> HSETNX KEY_NAME FIELD VALUE
         *
         * redis 127.0.0.1:6379> HSETNX myhash field1 "foo"
         * (integer) 1
         * redis 127.0.0.1:6379> HSETNX myhash field1 "bar"
         * (integer) 0
         * redis 127.0.0.1:6379> HGET myhash field1
         * "foo"
         *
         * redis 127.0.0.1:6379> HSETNX nosql key-value-store redis
         * (integer) 1
         *
         * redis 127.0.0.1:6379> HSETNX nosql key-value-store redis       # 操作无效， key-value-store 已存在
         * (integer) 0
         */
        Boolean aBoolean1 = map.putIfAbsent("putIfAbsent", "key1", "value1");
        Boolean aBoolean2 = map.putIfAbsent("putIfAbsent", "key1", "value1");
        Boolean aBoolean3 = map.putIfAbsent("putIfAbsent", "key2", "value1");
        System.out.println("aBoolean1 "+aBoolean1);//true
        System.out.println("aBoolean2 "+aBoolean2);//false
        System.out.println("aBoolean3 "+aBoolean3);//true

    }

}
