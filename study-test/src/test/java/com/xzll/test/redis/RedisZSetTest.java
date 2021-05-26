package com.xzll.test.redis;

import com.xzll.test.redis.common.RedisCommonTest;
import org.junit.Test;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class RedisZSetTest extends RedisCommonTest {


    /**
     * redis zset类型 原生命令与 redisTemplate 方法对应 小部分未找到
     */
    @Test
    public void setOperations() {

        /*
        Redis Zrevrank 命令返回有序集中成员的排名。其中有序集成员按分数值递减(从大到小)排序。
        排名以 0 为底，也就是说， 分数值最大的成员排名为 0 。
        使用 ZRANK 命令可以获得成员按分数值递增(从小到大)排列的排名
        如果成员是有序集 key 的成员，返回成员的排名。 如果成员不是有序集 key 的成员，返回 nil 。

        redis 127.0.0.1:6379> ZREVRANK key member

        redis 127.0.0.1:6379> ZRANGE salary 0 -1 WITHSCORES     # 测试数据
        1) "jack"
        2) "2000"
        3) "peter"
        4) "3500"
        5) "tom"
        6) "5000"

        redis 127.0.0.1:6379> ZREVRANK salary peter     # peter 的工资排第二
        (integer) 1

        redis 127.0.0.1:6379> ZREVRANK salary tom       # tom 的工资最高
        (integer) 0
         */
        Boolean add1 = zSet.add("myzset", "var1", 10);
        System.out.println(add1);
        Set<Object> range = zSet.reverseRange("myzset", 0, 10);
        System.out.println(range);

        /*
        Redis Zlexcount 命令在计算有序集合中指定字典区间内成员数量。
        指定区间内的成员数量。

        redis 127.0.0.1:6379> ZLEXCOUNT KEY MIN MAX

        redis 127.0.0.1:6379> ZADD myzset 0 a 0 b 0 c 0 d 0 e
        (integer) 5
        redis 127.0.0.1:6379> ZADD myzset 0 f 0 g
        (integer) 2
        redis 127.0.0.1:6379> ZLEXCOUNT myzset - +
        (integer) 7
        redis 127.0.0.1:6379> ZLEXCOUNT myzset [b [f
        (integer) 5
         */
        //不确定是这个方法
        //Set<Object> objects = zSet.rangeByLex()



        /*
        Redis Zunionstore 命令计算给定的一个或多个有序集的并集，其中给定 key 的数量必须以 numkeys 参数指定，并将该并集(结果集)储存到 destination 。
        默认情况下，结果集中某个成员的分数值是所有给定集下该成员分数值之和 。

        redis 127.0.0.1:6379> ZUNIONSTORE destination numkeys key [key ...] [WEIGHTS weight [weight ...]] [AGGREGATE SUM|MIN|MAX]

        redis 127.0.0.1:6379> ZRANGE programmer 0 -1 WITHSCORES
        1) "peter"
        2) "2000"
        3) "jack"
        4) "3500"
        5) "tom"
        6) "5000"

        redis 127.0.0.1:6379> ZRANGE manager 0 -1 WITHSCORES
        1) "herry"
        2) "2000"
        3) "mary"
        4) "3500"
        5) "bob"
        6) "4000"

        redis 127.0.0.1:6379> ZUNIONSTORE salary 2 programmer manager WEIGHTS 1 3   # 公司决定加薪。。。除了程序员。。。
        (integer) 6

        redis 127.0.0.1:6379> ZRANGE salary 0 -1 WITHSCORES
        1) "peter"
        2) "2000"
        3) "jack"
        4) "3500"
        5) "tom"
        6) "5000"
        7) "herry"
        8) "6000"
        9) "mary"
        10) "10500"
        11) "bob"
        12) "12000"

         */
        Long aLong = zSet.unionAndStore("new:key", "key1", "key2");
        //该方法可以指定乘法因子WEIGHTS 默认为1 ，以及聚合方式 AGGREGATE
        Long aLong1 = zSet.unionAndStore("key", Stream.of("var1", "var2", "var3").collect(Collectors.toList()), "key1", RedisZSetCommands.Aggregate.SUM, RedisZSetCommands.Weights.of(2));
        System.out.println(aLong);
        System.out.println(aLong1);

        /*
        Redis Zremrangebyrank 命令用于移除有序集中，指定排名(rank)区间内的所有成员。
        返回被移除成员的数量。

        redis 127.0.0.1:6379> ZREMRANGEBYRANK key start stop

        redis 127.0.0.1:6379> ZADD salary 2000 jack
        (integer) 1
        redis 127.0.0.1:6379> ZADD salary 5000 tom
        (integer) 1
        redis 127.0.0.1:6379> ZADD salary 3500 peter
        (integer) 1

        redis 127.0.0.1:6379> ZREMRANGEBYRANK salary 0 1       # 移除下标 0 至 1 区间内的成员
        (integer) 2

        redis 127.0.0.1:6379> ZRANGE salary 0 -1 WITHSCORES    # 有序集只剩下一个成员
        1) "tom"
        2) "5000"
         */
        Long key = zSet.removeRange("key", 0, 2);
        System.out.println(key);

        /*
        Redis Zcard 命令用于计算集合中元素的数量。
        当 key 存在且是有序集类型时，返回有序集的基数。 当 key 不存在时，返回 0 。

        redis 127.0.0.1:6379> ZCARD KEY_NAME

        redis 127.0.0.1:6379> ZADD myset 1 "hello"
        (integer) 1
        redis 127.0.0.1:6379> ZADD myset 1 "foo"
        (integer) 1
        redis 127.0.0.1:6379> ZADD myset 2 "world" 3 "bar"
        (integer) 2
        redis 127.0.0.1:6379> ZCARD myzset
        (integer) 4
         */
        Long key1 = zSet.zCard("key");
        System.out.println(key1);

        /*
        Redis Zrem 命令用于移除有序集中的一个或多个成员，不存在的成员将被忽略。
        当 key 存在但不是有序集类型时，返回一个错误。
        注意： 在 Redis 2.4 版本以前， ZREM 每次只能删除一个元素。
        被成功移除的成员的数量，不包括被忽略的成员。

        redis 127.0.0.1:6379> ZREM key member

        # 测试数据

        redis 127.0.0.1:6379> ZRANGE page_rank 0 -1 WITHSCORES
        1) "bing.com"
        2) "8"
        3) "baidu.com"
        4) "9"
        5) "google.com"
        6) "10"

        # 移除单个元素

        redis 127.0.0.1:6379> ZREM page_rank google.com
        (integer) 1

        redis 127.0.0.1:6379> ZRANGE page_rank 0 -1 WITHSCORES
        1) "bing.com"
        2) "8"
        3) "baidu.com"
        4) "9"

        # 移除多个元素

        redis 127.0.0.1:6379> ZREM page_rank baidu.com bing.com
        (integer) 2

        redis 127.0.0.1:6379> ZRANGE page_rank 0 -1 WITHSCORES
        (empty list or set)

        # 移除不存在元素

        redis 127.0.0.1:6379> ZREM page_rank non-exists-element
        (integer) 0
         */
        Long remove = zSet.remove("key", "var", "var2");
        System.out.println(remove);

        /*
        Redis Zinterstore 命令计算给定的一个或多个有序集的交集，其中给定 key 的数量必须以 numkeys 参数指定，并将该交集(结果集)储存到 destination 。
        默认情况下，结果集中某个成员的分数值是所有给定集下该成员分数值之和。

        redis 127.0.0.1:6379> ZINTERSTORE destination numkeys key [key ...] [WEIGHTS weight [weight ...]] [AGGREGATE SUM|MIN|MAX]

        # 有序集 mid_test
        redis 127.0.0.1:6379> ZADD mid_test 70 "Li Lei"
        (integer) 1
        redis 127.0.0.1:6379> ZADD mid_test 70 "Han Meimei"
        (integer) 1
        redis 127.0.0.1:6379> ZADD mid_test 99.5 "Tom"
        (integer) 1

        # 另一个有序集 fin_test
        redis 127.0.0.1:6379> ZADD fin_test 88 "Li Lei"
        (integer) 1
        redis 127.0.0.1:6379> ZADD fin_test 75 "Han Meimei"
        (integer) 1
        redis 127.0.0.1:6379> ZADD fin_test 99.5 "Tom"
        (integer) 1

        # 交集
        redis 127.0.0.1:6379> ZINTERSTORE sum_point 2 mid_test fin_test
        (integer) 3

        # 显示有序集内所有成员及其分数值
        redis 127.0.0.1:6379> ZRANGE sum_point 0 -1 WITHSCORES
        1) "Han Meimei"
        2) "145"
        3) "Li Lei"
        4) "158"
        5) "Tom"
        6) "199"
         */
        Long aLong2 = zSet.intersectAndStore("key", "key1", "key3");
        System.out.println(aLong2);


        /*
        Redis Zrank 返回有序集中指定成员的排名。其中有序集成员按分数值递增(从小到大)顺序排列。


        redis 127.0.0.1:6379> ZRANK key member

        redis 127.0.0.1:6379> ZRANGE salary 0 -1 WITHSCORES        # 显示所有成员及其 score 值
        1) "peter"
        2) "3500"
        3) "tom"
        4) "4000"
        5) "jack"
        6) "5000"

        redis 127.0.0.1:6379> ZRANK salary tom                     # 显示 tom 的薪水排名，第二
        (integer) 1
         */
        Long rank = zSet.rank("key", "vvar2");
        System.out.println(rank);

        /*

        Redis Zincrby 命令对有序集合中指定成员的分数加上增量 increment
        可以通过传递一个负数值 increment ，让分数减去相应的值，比如 ZINCRBY key -5 member ，就是让 member 的 score 值减去 5 。
        当 key 不存在，或分数不是 key 的成员时， ZINCRBY key increment member 等同于 ZADD key increment member 。
        当 key 不是有序集类型时，返回一个错误。
        分数值可以是整数值或双精度浮点数。
        member 成员的新分数值，以字符串形式表示。

        redis 127.0.0.1:6379> ZINCRBY key increment member

        redis 127.0.0.1:6379> ZADD myzset 1 "hello"
        (integer) 1
        redis 127.0.0.1:6379> ZADD myzset 1 "foo"
        (integer) 1
        redis 127.0.0.1:6379> ZINCRBY myzset 2 "hello"
        (integer) 3
        redis 127.0.0.1:6379> ZRANGE myzset 0 -1 WITHSCORES
        1) "foo"
        2) "2"
        3) "hello"
        4) "3"
         */
        Double aDouble = zSet.incrementScore("key", "var1", 1);
        System.out.println(aDouble);

        /*
        Redis Zrangebyscore 返回有序集合中指定分数区间的成员列表。有序集成员按分数值递增(从小到大)次序排列。
        具有相同分数值的成员按字典序来排列(该属性是有序集提供的，不需要额外的计算)。
        默认情况下，区间的取值使用闭区间 (小于等于或大于等于)，你也可以通过给参数前增加 ( 符号来使用可选的开区间 (小于或大于)。
        举个例子：
        ZRANGEBYSCORE zset (1 5
        返回所有符合条件 1 < score <= 5 的成员，而
        ZRANGEBYSCORE zset (5 (10
        则返回所有符合条件 5 < score < 10 的成员。
        指定区间内，带有分数值(可选)的有序集成员的列表。

        redis 127.0.0.1:6379> ZRANGEBYSCORE key min max [WITHSCORES] [LIMIT offset count]

        redis 127.0.0.1:6379> ZADD salary 2500 jack                        # 测试数据
        (integer) 0
        redis 127.0.0.1:6379> ZADD salary 5000 tom
        (integer) 0
        redis 127.0.0.1:6379> ZADD salary 12000 peter
        (integer) 0

        redis 127.0.0.1:6379> ZRANGEBYSCORE salary -inf +inf               # 显示整个有序集
        1) "jack"
        2) "tom"
        3) "peter"

        redis 127.0.0.1:6379> ZRANGEBYSCORE salary -inf +inf WITHSCORES    # 显示整个有序集及成员的 score 值
        1) "jack"
        2) "2500"
        3) "tom"
        4) "5000"
        5) "peter"
        6) "12000"

        redis 127.0.0.1:6379> ZRANGEBYSCORE salary -inf 5000 WITHSCORES    # 显示工资 <=5000 的所有成员
        1) "jack"
        2) "2500"
        3) "tom"
        4) "5000"
        redis 127.0.0.1:6379> ZRANGEBYSCORE salary (5000 400000            # 显示工资大于 5000 小于等于 400000 的成员
        1) "peter"

         */
        Set<Object> key2 = zSet.rangeByScore("key", 2, 3);
        System.out.println(key2);


        /*
        Redis Zrangebylex 通过字典区间返回有序集合的成员。

        redis 127.0.0.1:6379> ZRANGEBYLEX key min max [LIMIT offset count]

        redis 127.0.0.1:6379> ZADD myzset 0 a 0 b 0 c 0 d 0 e 0 f 0 g
        (integer) 7
        redis 127.0.0.1:6379> ZRANGEBYLEX myzset - [c
        1) "a"
        2) "b"
        3) "c"
        redis 127.0.0.1:6379> ZRANGEBYLEX myzset - (c
        1) "a"
        2) "b"
        redis 127.0.0.1:6379> ZRANGEBYLEX myzset [aaa (g
        1) "b"
        2) "c"
        3) "d"
        4) "e"
        5) "f"
        redis>
         */
        Set<Object> key3 = zSet.rangeByLex("key", RedisZSetCommands.Range.range(), RedisZSetCommands.Limit.limit());
        System.out.println(key3);

        /*

        Redis Zscore 命令返回有序集中，成员的分数值。 如果成员元素不是有序集 key 的成员，或 key 不存在，返回 nil 。
        返回成员的分数值，以字符串形式表示。


        redis 127.0.0.1:6379> ZSCORE key member

        redis 127.0.0.1:6379> ZRANGE salary 0 -1 WITHSCORES    # 测试数据
        1) "tom"
        2) "2000"
        3) "peter"
        4) "3500"
        5) "jack"
        6) "5000"

        redis 127.0.0.1:6379> ZSCORE salary peter              # 注意返回值是字符串
        "3500"
         */
        Double score = zSet.score("key", "var2");
        System.out.println(score);

        /*
        Redis Zremrangebyscore 命令用于移除有序集中，指定分数（score）区间内的所有成员。
        被移除成员的数量。

        redis 127.0.0.1:6379> ZREMRANGEBYSCORE key min max

        redis 127.0.0.1:6379> ZRANGE salary 0 -1 WITHSCORES          # 显示有序集内所有成员及其 score 值
        1) "tom"
        2) "2000"
        3) "peter"
        4) "3500"
        5) "jack"
        6) "5000"

        redis 127.0.0.1:6379> ZREMRANGEBYSCORE salary 1500 3500      # 移除所有薪水在 1500 到 3500 内的员工
        (integer) 2

        redis> ZRANGE salary 0 -1 WITHSCORES          # 剩下的有序集成员
        1) "jack"
        2) "5000"
         */
        Long key4 = zSet.removeRangeByScore("key", 2, 3);
        System.out.println(key4);

        /*
        Redis Zscan 命令用于迭代有序集合中的元素（包括元素成员和元素分值）

        redis 127.0.0.1:6379> ZSCAN key cursor [MATCH pattern] [COUNT count]

        返回的每个元素都是一个有序集合元素，一个有序集合元素由一个成员（member）和一个分值（score）组成。
         */
        //zSet.scan()

        /*
        Redis Zrevrangebyscore 返回有序集中指定分数区间内的所有的成员。有序集成员按分数值递减(从大到小)的次序排列。
        具有相同分数值的成员按字典序的逆序(reverse lexicographical order )排列。
        除了成员按分数值递减的次序排列这一点外， ZREVRANGEBYSCORE 命令的其他方面和 ZRANGEBYSCORE 命令一样。
        指定区间内，带有分数值(可选)的有序集成员的列表。

        redis 127.0.0.1:6379> ZREVRANGEBYSCORE key max min [WITHSCORES] [LIMIT offset count]

        redis 127.0.0.1:6379> ZADD salary 10086 jack
        (integer) 1
        redis > ZADD salary 5000 tom
        (integer) 1
        redis 127.0.0.1:6379> ZADD salary 7500 peter
        (integer) 1
        redis 127.0.0.1:6379> ZADD salary 3500 joe
        (integer) 1

        redis 127.0.0.1:6379> ZREVRANGEBYSCORE salary +inf -inf   # 逆序排列所有成员
        1) "jack"
        2) "peter"
        3) "tom"
        4) "joe"

        redis 127.0.0.1:6379> ZREVRANGEBYSCORE salary 10000 2000  # 逆序排列薪水介于 10000 和 2000 之间的成员
        1) "peter"
        2) "tom"
        3) "joe"
         */
        Set<Object> key5 = zSet.reverseRangeByScore("key", 200, 300);
        System.out.println(key5);

        /*
        Redis Zremrangebylex 命令用于移除有序集合中给定的字典区间的所有成员。

        redis 127.0.0.1:6379> ZREMRANGEBYLEX key min max

        redis 127.0.0.1:6379> ZADD myzset 0 aaaa 0 b 0 c 0 d 0 e
        (integer) 5
        redis 127.0.0.1:6379> ZADD myzset 0 foo 0 zap 0 zip 0 ALPHA 0 alpha
        (integer) 5
        redis 127.0.0.1:6379> ZRANGE myzset 0 -1
        1) "ALPHA"
         2) "aaaa"
         3) "alpha"
         4) "b"
         5) "c"
         6) "d"
         7) "e"
         8) "foo"
         9) "zap"
        10) "zip"
        redis 127.0.0.1:6379> ZREMRANGEBYLEX myzset [alpha [omega
        (integer) 6
        redis 127.0.0.1:6379> ZRANGE myzset 0 -1
        1) "ALPHA"
        2) "aaaa"
        3) "zap"
        4) "zip"
         */
        Long key6 = zSet.removeRange("key", 12, 20);
        System.out.println(key6);

        /*

        Redis Zrevrange 命令返回有序集中，指定区间内的成员。
        其中成员的位置按分数值递减(从大到小)来排列。
        具有相同分数值的成员按字典序的逆序(reverse lexicographical order)排列。
        除了成员按分数值递减的次序排列这一点外， ZREVRANGE 命令的其他方面和 ZRANGE 命令一样。
        返回指定区间内，带有分数值(可选)的有序集成员的列表。

        redis 127.0.0.1:6379> ZREVRANGE key start stop [WITHSCORES]

        redis 127.0.0.1:6379> ZRANGE salary 0 -1 WITHSCORES        # 递增排列
        1) "peter"
        2) "3500"
        3) "tom"
        4) "4000"
        5) "jack"
        6) "5000"

        redis 127.0.0.1:6379> ZREVRANGE salary 0 -1 WITHSCORES     # 递减排列
        1) "jack"
        2) "5000"
        3) "tom"
        4) "4000"
        5) "peter"
        6) "3500"
         */
        Set<Object> key7 = zSet.reverseRange("key", 2, 4);
        System.out.println(key7);

        /*
        Redis Zrange 返回有序集中，指定区间内的成员。
        其中成员的位置按分数值递增(从小到大)来排序。
        具有相同分数值的成员按字典序(lexicographical order )来排列。
        如果你需要成员按
        值递减(从大到小)来排列，请使用 ZREVRANGE 命令。
        下标参数 start 和 stop 都以 0 为底，也就是说，以 0 表示有序集第一个成员，以 1 表示有序集第二个成员，以此类推。
        你也可以使用负数下标，以 -1 表示最后一个成员， -2 表示倒数第二个成员，以此类推。
        指定区间内，带有分数值(可选)的有序集成员的列表。

        redis 127.0.0.1:6379> ZRANGE key start stop [WITHSCORES]

        redis 127.0.0.1:6379> ZRANGE salary 0 -1 WITHSCORES             # 显示整个有序集成员
        1) "jack"
        2) "3500"
        3) "tom"
        4) "5000"
        5) "boss"
        6) "10086"

        redis 127.0.0.1:6379> ZRANGE salary 1 2 WITHSCORES              # 显示有序集下标区间 1 至 2 的成员
        1) "tom"
        2) "5000"
        3) "boss"
        4) "10086"

        redis 127.0.0.1:6379> ZRANGE salary 0 200000 WITHSCORES         # 测试 end 下标超出最大下标时的情况
        1) "jack"
        2) "3500"
        3) "tom"
        4) "5000"
        5) "boss"
        6) "10086"

        redis > ZRANGE salary 200000 3000000 WITHSCORES                  # 测试当给定区间不存在于有序集时的情况
        (empty list or set)
         */
        Set<Object> key8 = zSet.range("key", 0, 10);
        System.out.println(key8);

        /*
        Redis Zcount 命令用于计算有序集合中指定分数区间的成员数量。
        返回分数值在 min 和 max 之间的成员的数量。

        redis 127.0.0.1:6379> ZCOUNT key min max

        redis 127.0.0.1:6379> ZADD myzset 1 "hello"
        (integer) 1
        redis 127.0.0.1:6379> ZADD myzset 1 "foo"
        (integer) 1
        redis 127.0.0.1:6379> ZADD myzset 2 "world" 3 "bar"
        (integer) 2
        redis 127.0.0.1:6379> ZCOUNT myzset 1 3
        (integer) 4
         */
        Long key9 = zSet.count("key", 2, 10);
        System.out.println(key9);

        /*
        Redis Zadd 命令用于将一个或多个成员元素及其分数值加入到有序集当中。
        如果某个成员已经是有序集的成员，那么更新这个成员的分数值，并通过重新插入这个成员元素，来保证该成员在正确的位置上。
        分数值可以是整数值或双精度浮点数。
        如果有序集合 key 不存在，则创建一个空的有序集并执行 ZADD 操作。
        当 key 存在但不是有序集类型时，返回一个错误。
        注意： 在 Redis 2.4 版本以前， ZADD 每次只能添加一个元素。
        返回被成功添加的新成员的数量，不包括那些被更新的、已经存在的成员。


        redis 127.0.0.1:6379> ZADD KEY_NAME SCORE1 VALUE1.. SCOREN VALUEN

        redis 127.0.0.1:6379> ZADD myset 1 "hello"
        (integer) 1
        redis 127.0.0.1:6379> ZADD myset 1 "foo"
        (integer) 1
        redis 127.0.0.1:6379> ZADD myset 2 "world" 3 "bar"
        (integer) 2
        redis 127.0.0.1:6379> ZRANGE myzset 0 -1 WITHSCORES
        1) "hello"
        2) "1"
        3) "foo"
        4) "1"
        5) "world"
        6) "2"
        7) "bar"
        8) "3"
         */
        Boolean add = zSet.add("key", "var2", 2);
        System.out.println(add);


    }

}
