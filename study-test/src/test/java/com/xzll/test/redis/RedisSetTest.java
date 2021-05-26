package com.xzll.test.redis;

import com.google.common.collect.Lists;
import com.xzll.test.redis.common.RedisCommonTest;
import org.junit.Test;
import org.springframework.data.redis.core.SetOperations;

import java.util.List;
import java.util.Set;


public class RedisSetTest extends RedisCommonTest {


    /**
     * redis set类型 原生命令与 redisTemplate 方法对应
     */
    @Test
    public void setOperations() {

        /*
        Redis Sunion 命令返回给定集合的并集。不存在的集合 key 被视为空集。

        redis 127.0.0.1:6379> SUNION KEY KEY1..KEYN

        redis 127.0.0.1:6379> SADD myset1 "hello"
        (integer) 1
        redis 127.0.0.1:6379> SADD myset1 "world"
        (integer) 1
        redis 127.0.0.1:6379> SADD myset1 "bar"
        (integer) 1
        redis 127.0.0.1:6379> SADD myset2 "hello"
        (integer) 1
        redis 127.0.0.1:6379> SADD myset2 "bar"
        (integer) 1
        redis 127.0.0.1:6379> SUNION myset1 myset2
        1) "bar"
        2) "world"
        3) "hello"
        4) "foo"
         */
        set.add("hello", "var1", "var2", "var3", "var7");
        set.add("world", "var2", "var4", "var5", "var6");
        Set<Object> union = set.union("hello", "world");
        System.out.println(union);

        /*
        Redis Scard 命令返回集合中元素的数量。
        redis 127.0.0.1:6379> SCARD KEY_NAME

        redis 127.0.0.1:6379> SADD myset "hello"
        (integer) 1
        redis 127.0.0.1:6379> SADD myset "foo"
        (integer) 1
        redis 127.0.0.1:6379> SADD myset "hello"
        (integer) 0
        redis 127.0.0.1:6379> SCARD myset
        (integer) 2
         */
        set.add("size", "var1", "var2", "var3");
        Long size = set.size("size");
        System.out.println(size);

        /*
        Redis Srandmember 命令用于返回集合中的一个随机元素。
        从 Redis 2.6 版本开始， Srandmember 命令接受可选的 count 参数：
        如果 count 为正数，且小于集合基数，那么命令返回一个包含 count 个元素的数组，数组中的元素各不相同。如果 count 大于等于集合基数，那么返回整个集合。
        如果 count 为负数，那么命令返回一个数组，数组中的元素可能会重复出现多次，而数组的长度为 count 的绝对值。
        该操作和 SPOP 相似，但 SPOP 将随机元素从集合中移除并返回，而 Srandmember 则仅仅返回随机元素，而不对集合进行任何改动。
        只提供集合 key 参数时，返回一个元素；如果集合为空，返回 nil 。 如果提供了 count 参数，那么返回一个数组；如果集合为空，返回空数组。

        redis 127.0.0.1:6379> SRANDMEMBER KEY [count]

        redis 127.0.0.1:6379> SADD myset1 "hello"
        (integer) 1
        redis 127.0.0.1:6379> SADD myset1 "world"
        (integer) 1
        redis 127.0.0.1:6379> SADD myset1 "bar"
        (integer) 1
        redis 127.0.0.1:6379> SRANDMEMBER myset1
        "bar"
        redis 127.0.0.1:6379> SRANDMEMBER myset1 2
        1) "Hello"
        2) "world"

        127.0.0.1:6379> SRANDMEMBER setkey -10
         1) "var3"
         2) "var3"
         3) "var1"
         4) "var2"
         5) "var3"
         6) "var2"
         7) "var1"
         8) "var3"
         9) "var1"
        10) "var1"

         */
        List<Object> hello = set.randomMembers("hello", 10);//该方法count最终会转换成负数 也就是以为这返回元素中可能会有重复数据 长度为count的绝对值
        System.out.println(hello);
        Set<Object> hello1 = set.distinctRandomMembers("hello", 2);//该方法不会转换成负数 不会有重复数据 因为这是set保证的
        System.out.println(hello1);

        /*
        Redis Smembers 命令返回集合中的所有的成员。 不存在的集合 key 被视为空集合。


        redis 127.0.0.1:6379> SMEMBERS KEY VALUE

        redis 127.0.0.1:6379> SADD myset1 "hello"
        (integer) 1
        redis 127.0.0.1:6379> SADD myset1 "world"
        (integer) 1
        redis 127.0.0.1:6379> SMEMBERS myset1
        1) "World"
        2) "Hello"
         */
        Set<Object> members = set.members("hello");
        System.out.println(members);

        /*
        Redis Sinter 命令返回给定所有给定集合的交集。 不存在的集合 key 被视为空集。 当给定集合当中有一个空集时，结果也为空集(根据集合运算定律)。

        redis 127.0.0.1:6379> SINTER KEY KEY1..KEYN

        redis 127.0.0.1:6379> SADD myset "hello"
        (integer) 1
        redis 127.0.0.1:6379> SADD myset "foo"
        (integer) 1
        redis 127.0.0.1:6379> SADD myset "bar"
        (integer) 1
        redis 127.0.0.1:6379> SADD myset2 "hello"
        (integer) 1
        redis 127.0.0.1:6379> SADD myset2 "world"
        (integer) 1
        redis 127.0.0.1:6379> SINTER myset myset2
        1) "hello"
         */
        set.intersect("hello", "world");
        List<String> key2 = Lists.newArrayList("var1", "var2", "var3");
        Set<Object> intersect = set.intersect("hello", key2);
        System.out.println(intersect);

        /*

        Redis Srem 命令用于移除集合中的一个或多个成员元素，不存在的成员元素会被忽略。
        当 key 不是集合类型，返回一个错误。
        在 Redis 2.4 版本以前， SREM 只接受单个成员值

        redis 127.0.0.1:6379> SREM KEY MEMBER1..MEMBERN

        redis 127.0.0.1:6379> SADD myset1 "hello"
        (integer) 1
        redis 127.0.0.1:6379> SADD myset1 "world"
        (integer) 1
        redis 127.0.0.1:6379> SADD myset1 "bar"
        (integer) 1
        redis 127.0.0.1:6379> SREM myset1 "hello"
        (integer) 1
        redis 127.0.0.1:6379> SREM myset1 "foo"
        (integer) 0
        redis 127.0.0.1:6379> SMEMBERS myset1
        1) "bar"
        2) "world"
         */
        Long remove = set.remove("hello", "var1", "var2");
        System.out.println(remove);


        /*
        Redis Smove 命令将指定成员 member 元素从 source 集合移动到 destination 集合。
        SMOVE 是原子性操作。
        如果 source 集合不存在或不包含指定的 member 元素，则 SMOVE 命令不执行任何操作，仅返回 0 。否则， member 元素从 source 集合中被移除，并添加到 destination 集合中去。
        当 destination 集合已经包含 member 元素时， SMOVE 命令只是简单地将 source 集合中的 member 元素删除。
        当 source 或 destination 不是集合类型时，返回一个错误。
        如果成员元素被成功移除，返回 1 。 如果成员元素不是 source 集合的成员，并且没有任何操作对 destination 集合执行，那么返回 0 。

        redis 127.0.0.1:6379> SMOVE SOURCE DESTINATION MEMBER

        redis 127.0.0.1:6379> SADD myset1 "hello"
        (integer) 1
        redis 127.0.0.1:6379> SADD myset1 "world"
        (integer) 1
        redis 127.0.0.1:6379> SADD myset1 "bar"
        (integer) 1
        redis 127.0.0.1:6379> SADD myset2 "foo"
        (integer) 1
        redis 127.0.0.1:6379> SMOVE myset1 myset2 "bar"
        (integer) 1
        redis 127.0.0.1:6379> SMEMBERS myset1
        1) "World"
        2) "Hello"
        redis 127.0.0.1:6379> SMEMBERS myset2
        1) "foo"
        2) "bar"
         */
        Boolean move = set.move("move", "var1", "move:new");
        System.out.println(move);

        /*
        Redis Sadd 命令将一个或多个成员元素加入到集合中，已经存在于集合的成员元素将被忽略。
        假如集合 key 不存在，则创建一个只包含添加的元素作成员的集合。
        当集合 key 不是集合类型时，返回一个错误。
        注意：在Redis2.4版本以前， SADD 只接受单个成员值。

        redis 127.0.0.1:6379> SADD KEY_NAME VALUE1..VALUEN

        redis 127.0.0.1:6379> SADD myset "hello"
        (integer) 1
        redis 127.0.0.1:6379> SADD myset "foo"
        (integer) 1
        redis 127.0.0.1:6379> SADD myset "hello"
        (integer) 0
        redis 127.0.0.1:6379> SMEMBERS myset
        1) "hello"
        2) "foo"
         */
        Long add = set.add("hello", "var1");
        System.out.println(add);

        /*
        Redis Sismember 命令判断成员元素是否是集合的成员。
        如果成员元素是集合的成员，返回 1 。 如果成员元素不是集合的成员，或 key 不存在，返回 0 。

        redis 127.0.0.1:6379> SISMEMBER KEY VALUE

        redis 127.0.0.1:6379> SADD myset1 "hello"
        (integer) 1
        redis 127.0.0.1:6379> SISMEMBER myset1 "hello"
        (integer) 1
        redis 127.0.0.1:6379> SISMEMBER myset1 "world"
        (integer) 0
         */
        Boolean member = set.isMember("hello", "var2");
        System.out.println(member);

        /*
        Redis Sdiffstore 命令将给定集合之间的差集存储在指定的集合中。如果指定的集合 key 已存在，则会被覆盖。

        redis 127.0.0.1:6379> SDIFFSTORE DESTINATION_KEY KEY1..KEYN

        redis 127.0.0.1:6379> SADD myset "hello"
        (integer) 1
        redis 127.0.0.1:6379> SADD myset "foo"
        (integer) 1
        redis 127.0.0.1:6379> SADD myset "bar"
        (integer) 1
        redis 127.0.0.1:6379> SADD myset2 "hello"
        (integer) 1
        redis 127.0.0.1:6379> SADD myset2 "world"
        (integer) 1
        redis 127.0.0.1:6379> SDIFFSTORE destset myset myset2
        (integer) 2
        redis 127.0.0.1:6379> SMEMBERS destset
        1) "foo"
        2) "bar"

        127.0.0.1:6379> SMEMBERS myset
        1) "var2"
        2) "var1"
        127.0.0.1:6379> SMEMBERS myset2
        1) "var4"
        2) "var2"
        3) "var3"
        127.0.0.1:6379> SDIFFSTORE myset:new myset2 myset
        (integer) 2
        127.0.0.1:6379> SMEMBERS myset:new
        1) "var4"
        2) "var3"
        127.0.0.1:6379>
         */

        Long aLong = set.differenceAndStore("myset:new", "myset01", "myset02");
        System.out.println(aLong);

        /*
        Redis Sdiff 命令返回给定集合之间的差集。不存在的集合 key 将视为空集。


        redis 127.0.0.1:6379> SDIFF FIRST_KEY OTHER_KEY1..OTHER_KEYN

        redis 127.0.0.1:6379> SADD myset "hello"
        (integer) 1
        redis 127.0.0.1:6379> SADD myset "foo"
        (integer) 1
        redis 127.0.0.1:6379> SADD myset "bar"
        (integer) 1
        redis 127.0.0.1:6379> SADD myset2 "hello"
        (integer) 1
        redis 127.0.0.1:6379> SADD myset2 "world"
        (integer) 1
        redis 127.0.0.1:6379> SDIFF myset myset2
        1) "foo"
        2) "bar"
         */
        Set<Object> difference = set.difference("hello", "hello2");
        System.out.println(difference);

        /*
        Redis Sscan 命令用于迭代集合键中的元素。


        redis 127.0.0.1:6379> SSCAN KEY [MATCH pattern] [COUNT count]

        redis 127.0.0.1:6379> SADD myset1 "hello"
        (integer) 1
        redis 127.0.0.1:6379> SADD myset1 "hi"
        (integer) 1
        redis 127.0.0.1:6379> SADD myset1 "bar"
        (integer) 1
        redis 127.0.0.1:6379> sscan myset1 0 match h*
        1) "0"
        2) 1) "hello"
           2) "h1"
         */

//        set.scan("hello",);

        /*
        Redis Sinterstore 命令将给定集合之间的交集存储在指定的集合中。如果指定的集合已经存在，则将其覆盖。

        redis 127.0.0.1:6379> SINTERSTORE DESTINATION_KEY KEY KEY1..KEYN

        redis 127.0.0.1:6379> SADD myset1 "hello"
        (integer) 1
        redis 127.0.0.1:6379> SADD myset1 "foo"
        (integer) 1
        redis 127.0.0.1:6379> SADD myset1 "bar"
        (integer) 1
        redis 127.0.0.1:6379> SADD myset2 "hello"
        (integer) 1
        redis 127.0.0.1:6379> SADD myset2 "world"
        (integer) 1
        redis 127.0.0.1:6379> SINTERSTORE myset myset1 myset2
        (integer) 1
        redis 127.0.0.1:6379> SMEMBERS myset
        1) "hello"
         */
        Long aLong1 = set.intersectAndStore("hello", "hello2","hello3");
        System.out.println(aLong1);

        /*
        Redis Sunionstore 命令将给定集合的并集存储在指定的集合 destination 中。


        redis 127.0.0.1:6379> SUNIONSTORE DESTINATION KEY KEY1..KEYN

        redis 127.0.0.1:6379> SADD myset1 "hello"
        (integer) 1
        redis 127.0.0.1:6379> SADD myset1 "world"
        (integer) 1
        redis 127.0.0.1:6379> SADD myset1 "bar"
        (integer) 1
        redis 127.0.0.1:6379> SADD myset2 "hello"
        (integer) 1
        redis 127.0.0.1:6379> SADD myset2 "bar"
        (integer) 1
        redis 127.0.0.1:6379> SUNIONSTORE myset myset1 myset2
        (integer) 1
        redis 127.0.0.1:6379> SMEMBERS myset
        1) "bar"
        2) "world"
        3) "hello"
        4) "foo"
         */
        Long aLong2 = set.unionAndStore("hello", "hello2", "hello3");
        System.out.println(aLong2);

        /*
        Redis Spop 命令用于移除并返回集合中的一个随机元素。
        被移除的随机元素。 当集合不存在或是空集时，返回 nil 。

        redis 127.0.0.1:6379> SPOP KEY

        redis 127.0.0.1:6379> SADD myset1 "hello"
        (integer) 1
        redis 127.0.0.1:6379> SADD myset1 "world"
        (integer) 1
        redis 127.0.0.1:6379> SADD myset1 "bar"
        (integer) 1
        redis 127.0.0.1:6379> SPOP myset1
        "bar"
        redis 127.0.0.1:6379> SMEMBERS myset1
        1) "Hello"
        2) "world"
         */
        Object hello2 = set.pop("hello");
        System.out.println(hello2);

    }

}
