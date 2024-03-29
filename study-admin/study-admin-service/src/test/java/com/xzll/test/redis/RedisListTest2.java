package com.xzll.test.redis;

import com.xzll.test.redis.common.RedisCommonTest;
import org.junit.Test;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;


public class RedisListTest2 extends RedisCommonTest {




    /**
     * redis list类型 原生命令与 redisTemplate 方法对应
     */
    @Test
    public void listOperations() {

        clearDB();

        /**
         * Redis Lindex 命令用于通过索引获取列表中的元素。你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
         * redis 127.0.0.1:6379> LINDEX KEY_NAME INDEX_POSITION
         *
         * redis 127.0.0.1:6379> LPUSH mylist "World"
         * (integer) 1
         *
         * redis 127.0.0.1:6379> LPUSH mylist "Hello"
         * (integer) 2
         *
         * redis 127.0.0.1:6379> LINDEX mylist 0
         * "Hello"
         *
         * redis 127.0.0.1:6379> LINDEX mylist -1
         * "World"
         *
         * redis 127.0.0.1:6379> LINDEX mylist 3        # index不在 mylist 的区间范围内
         * (nil)
         */
//        list.leftPush("index:key", "hzz");
//        list.leftPush("index:key", "hello"); // ["hello","hzz"]
//        Object index0 = list.index("index:key", 0);
//        Object indexdesc = list.index("index:key", -1);//负数代表倒数  倒数第一个
//        System.out.println(index0);
//        System.out.println(indexdesc);

        /**
         * Redis Rpush 命令用于将一个或多个值插入到列表的尾部(最右边)。
         *
         * 如果列表不存在，一个空列表会被创建并执行 RPUSH 操作。 当列表存在但不是列表类型时，返回一个错误。
         *
         * 注意：在 Redis 2.4 版本以前的 RPUSH 命令，都只接受单个 value 值。
         *
         * redis 127.0.0.1:6379> RPUSH KEY_NAME VALUE1..VALUEN
         *
         * redis 127.0.0.1:6379> RPUSH mylist "hello"
         * (integer) 1
         * redis 127.0.0.1:6379> RPUSH mylist "foo"
         * (integer) 2
         * redis 127.0.0.1:6379> RPUSH mylist "bar"
         * (integer) 3
         * redis 127.0.0.1:6379> LRANGE mylist 0 -1
         * 1) "hello"
         * 2) "foo"
         * 3) "bar"
         *
         */
//        Long aLong = list.rightPush("rightkey", "var1");//和leftpush基本一样 不做演示
//        System.out.println(aLong);


        /**
         * Redis Lrange 返回列表中指定区间内的元素，区间以偏移量 START 和 END 指定。 其中 0 表示列表的第一个元素， 1 表示列表的第二个元素，以此类推。 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
         * redis 127.0.0.1:6379> LRANGE KEY_NAME START END
         *
         * redis 127.0.0.1:6379> LPUSH list1 "foo"
         * (integer) 1
         * redis 127.0.0.1:6379> LPUSH list1 "bar"
         * (integer) 2
         * redis 127.0.0.1:6379> LPUSHX list1 "bar"
         * (integer) 0
         * redis 127.0.0.1:6379> LRANGE list1 0 -1
         * 1) "foo"
         * 2) "bar"
         * 3) "bar"
         *
         */
//        list.leftPush("range:key", Stream.of("var1","var2").toArray());
//        List<Object> list1 = list.range("range:key", 0, -1);//取所有
//        System.out.println("range:key  "+list1);


        /**
         * Redis Rpoplpush 命令用于移除列表的最后一个元素，并将该元素添加到另一个列表并返回。（如果不存在 那么新建个key value类型为list）
         * redis 127.0.0.1:6379> RPOPLPUSH SOURCE_KEY_NAME DESTINATION_KEY_NAME
         *
         * redis 127.0.0.1:6379> RPUSH mylist "hello"
         * (integer) 1
         * redis 127.0.0.1:6379> RPUSH mylist "foo"
         * (integer) 2
         * redis 127.0.0.1:6379> RPUSH mylist "bar"
         * (integer) 3
         * redis 127.0.0.1:6379> RPOPLPUSH mylist myotherlist
         * "bar"
         * redis 127.0.0.1:6379> LRANGE mylist 0 -1
         * 1) "hello"
         * 2) "foo"
         *
         * 可以看到 RPOPLPUSH操作将弹出的value（bar）保存在了 新的key（myotherlist）中
         * redis 127.0.0.1:6379> LRANGE myotherlist 0 -1
         * "bar"
         *
         */

        list.leftPushAll("rightPopAndLeftPush:key22",Stream.of("var1","var2","varr3").toArray());
        while (true){
			Object newlistkey = list.rightPopAndLeftPush("rightPopAndLeftPush:key22","rightPopAndLeftPush:key2233");
			if (Objects.isNull(newlistkey)){
				break;
			}
			System.out.println(newlistkey);
		}
        List<Object> newlistkey1 = list.range("rightPopAndLeftPush:new33", 0, -1);
        List<Object> newlistkey12 = list.range("rightPopAndLeftPush:key2233", 0, -1);
        System.out.println("rightPopAndLeftPush:key"+list.range("rightPopAndLeftPush:key", 0, -1));
        System.out.println("rightPopAndLeftPush:new"+newlistkey1);



        /**
         * Redis Blpop 命令移出并获取列表的第一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
         * 如果列表为空，返回一个 nil 。 否则，返回一个含有两个元素的列表，第一个元素是被弹出元素所属的 key ，第二个元素是被弹出元素的值。
         * redis 127.0.0.1:6379> BLPOP LIST1 LIST2 .. LISTN TIMEOUT
         *
         * redis 127.0.0.1:6379> BLPOP list1 100
         * 在以上实例中，操作会被阻塞，如果指定的列表 key list1 存在数据则会返回第一个元素，否则在等待100秒后会返回 nil 。
         * (nil)
         * (100.06s)
         */
        list.leftPush("leftpop:key","var1");
        list.leftPush("leftpop:key","var2");
        Object list2 = list.leftPop("leftpop:key", 20, TimeUnit.SECONDS);
        System.out.println("list.range(\"leftpop:key\",0,-1) "+list.range("leftpop:key",0,-1));
        System.out.println("leftpop:key "+list2);


        /**
         * Redis Brpop 命令移出并获取列表的最后一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
         * redis 127.0.0.1:6379> BRPOP LIST1 LIST2 .. LISTN TIMEOUT
         *
         * 假如在指定时间内没有任何元素被弹出，则返回一个 nil 和等待时长。 反之，返回一个含有两个元素的列表，第一个元素是被弹出元素所属的 key ，第二个元素是被弹出元素的值。
         *
         * redis 127.0.0.1:6379> BRPOP list1 100
         * 在以上实例中，操作会被阻塞，如果指定的列表 key list1 存在数据则会返回第一个元素，否则在等待100秒后会返回 nil 。
         * (nil)
         * (100.06s)
         */
        Object list3 = list.rightPop("list", 10, TimeUnit.SECONDS);//和leftPop基本一样 略
        System.out.println(list3);


        /**
         * Redis Brpoplpush 命令从列表中弹出最右边的一个值，将弹出的元素插入到另外一个列表中并返回它； 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
         * redis 127.0.0.1:6379> BRPOPLPUSH LIST1 ANOTHER_LIST TIMEOUT
         *
         * 假如在指定时间内没有任何元素被弹出，则返回一个 nil 和等待时长。 反之，返回一个含有两个元素的列表，第一个元素是被弹出元素的值，第二个元素是等待时长。
         *
         * # 非空列表
         *
         * redis 127.0.0.1:6379> BRPOPLPUSH msg reciver 500
         * "hello moto"                        # 弹出元素的值
         * (3.38s)                             # 等待时长
         *
         * redis 127.0.0.1:6379> LLEN reciver
         * (integer) 1
         *
         * redis 127.0.0.1:6379> LRANGE reciver 0 0
         * 1) "hello moto"
         *
         *
         * # 空列表
         *
         * redis 127.0.0.1:6379> BRPOPLPUSH msg reciver 1
         * (nil)
         * (1.34s)
         * <pre>
         * (nil)
         * (100.06s)
         *
         * 和 Rpoplpush 差不多 只不过多个阻塞这个特性
         */
        Object newkey = list.rightPopAndLeftPush("rightPopAndLeftPush:key", "newkey",10,TimeUnit.DAYS); //和 rightPopAndLeftPush基本一样 多了个阻塞的特性
        System.out.println(newkey);

        /**
         * Redis Lrem 根据参数 COUNT 的值，移除列表中与参数 VALUE 相等的元素。
         * COUNT 的值可以是以下几种：
         * count > 0 : 从表头开始向表尾搜索，移除与 VALUE 相等的元素，数量为 COUNT 。
         * count < 0 : 从表尾开始向表头搜索，移除与 VALUE 相等的元素，数量为 COUNT 的绝对值。
         * count = 0 : 移除表中所有与 VALUE 相等的值。
         *
         * redis 127.0.0.1:6379> LREM KEY_NAME COUNT VALUE
         *
         * redis 127.0.0.1:6379> RPUSH mylist "hello"
         * (integer) 1
         * redis 127.0.0.1:6379> RPUSH mylist "hello"
         * (integer) 2
         * redis 127.0.0.1:6379> RPUSH mylist "foo"
         * (integer) 3
         * redis 127.0.0.1:6379> RPUSH mylist "hello"
         * (integer) 4
         * redis 127.0.0.1:6379> LREM mylist -2 "hello"
         * (integer) 2
         */
        list.leftPushAll("remove:key",Stream.of("var1","var2","var3","var4","var3").toArray());
        Long remove = list.remove("remove:key", 0, "var3");//删除操作
        System.out.println("remove count "+remove);
        System.out.println(list.range("remove:key",0,-1));


        /**
         * Redis Llen 命令用于返回列表的长度。 如果列表 key 不存在，则 key 被解释为一个空列表，返回 0 。 如果 key 不是列表类型，返回一个错误。
         *
         * redis 127.0.0.1:6379> LLEN KEY_NAME
         *
         * redis 127.0.0.1:6379> RPUSH list1 "foo"
         * (integer) 1
         * redis 127.0.0.1:6379> RPUSH list1 "bar"
         * (integer) 2
         * redis 127.0.0.1:6379> LLEN list1
         * (integer) 2
         */
        list.rightPush("size:key","var1");
        list.rightPush("size:key","var2");
        list.rightPush("size:key","var3");
        Long mylist = list.size("size:key");
        System.out.println(mylist);

        /**
         * Redis Ltrim 对一个列表进行修剪(trim)，就是说，让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除。
         *
         * 下标 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
         *
         * redis 127.0.0.1:6379> LTRIM KEY_NAME START STOP
         *
         * redis 127.0.0.1:6379> RPUSH mylist "hello"
         * (integer) 1
         * redis 127.0.0.1:6379> RPUSH mylist "hello"
         * (integer) 2
         * redis 127.0.0.1:6379> RPUSH mylist "foo"
         * (integer) 3
         * redis 127.0.0.1:6379> RPUSH mylist "bar"
         * (integer) 4
         * redis 127.0.0.1:6379> LTRIM mylist 1 -1
         * OK
         * redis 127.0.0.1:6379> LRANGE mylist 0 -1
         * 1) "hello"
         * 2) "foo"
         * 3) "bar"
         */

        list.rightPush("trim:key","var1");
        list.rightPush("trim:key","var2");
        list.rightPush("trim:key","var3");
        list.rightPush("trim:key","var4");
        list.rightPush("trim:key","var5");
        list.rightPush("trim:key","var6");
        list.trim("trim:key", 2, 4);//void
        System.out.println("trim:key  "+list.range("trim:key",0,-1));


        /**
         * Redis Lpop 命令用于移除并返回列表的第一个元素。
         *
         * redis 127.0.0.1:6379> LLEN KEY_NAME
         *
         * redis 127.0.0.1:6379> RPUSH list1 "foo"
         * (integer) 1
         * redis 127.0.0.1:6379> RPUSH list1 "bar"
         * (integer) 2
         * redis 127.0.0.1:6379> LPOP list1
         * "foo"
         */
        Object mylist1 = list.leftPop("leftpop");//从左边移除 上边已做演示

        /**
         * Redis Lpushx 将一个或多个值插入到已存在的列表头部，列表不存在时操作无效。
         *
         * redis 127.0.0.1:6379> LPUSHX KEY_NAME VALUE1.. VALUEN
         *
         * redis 127.0.0.1:6379> LPUSH list1 "foo"
         * (integer) 1
         * redis 127.0.0.1:6379> LPUSHX list1 "bar"
         * (integer) 2
         * redis 127.0.0.1:6379> LPUSHX list2 "bar"
         * (integer) 0
         * redis 127.0.0.1:6379> LRANGE list1 0 -1
         * 1) "foo"
         * 2) "bar"
         */
        list.leftPush("leftPushIfPresent:key","var1");
        list.leftPush("leftPushIfPresent:key","var2");
        list.leftPush("leftPushIfPresent:key","var3");
        list.leftPush("leftPushIfPresent:key","var4");
        Long aLong1 = list.leftPushIfPresent("leftPushIfPresent:key", "var4");
        Long aLong2 = list.leftPushIfPresent("leftPushIfPresent:key", "var1");
        System.out.println("leftPushIfPresent:key  "+list.range("leftPushIfPresent:key",0,-1));

        /**
         * Redis Linsert 命令用于在列表的元素前或者后插入元素。 当指定元素不存在于列表中时，不执行任何操作。 当列表不存在时，被视为空列表，不执行任何操作。 如果 key 不是列表类型，返回一个错误。
         *
         * redis 127.0.0.1:6379> LINSERT KEY_NAME BEFORE EXISTING_VALUE NEW_VALUE
         *
         * redis 127.0.0.1:6379> RPUSH list1 "foo"
         * (integer) 1
         * redis 127.0.0.1:6379> RPUSH list1 "bar"
         * (integer) 2
         * redis 127.0.0.1:6379> LINSERT list1 BEFORE "bar" "Yes"
         * (integer) 3
         * redis 127.0.0.1:6379> LRANGE mylist 0 -1
         * 1) "foo"
         * 2) "Yes"
         * 3) "bar"
         */
        list.leftPush("linsert:key","var1");
        list.leftPush("linsert:key","var2");
        list.leftPush("linsert:key","var3");
        Long aLong5 = list.leftPush("linsert:key", "var2", "var100");
        Long aLong6 = list.rightPush("linsert:key", "var2", "var500");
        System.out.println("linsert:key  "+list.range("linsert:key",0,-1));


        /**
         * Redis Rpop 命令用于移除并返回列表的最后一个元素。
         *
         * redis 127.0.0.1:6379> RPOP KEY_NAME
         *
         * redis 127.0.0.1:6379> RPUSH mylist "hello"
         * (integer) 1
         * redis 127.0.0.1:6379> RPUSH mylist "hello"
         * (integer) 2
         * redis 127.0.0.1:6379> RPUSH mylist "foo"
         * (integer) 3
         * redis 127.0.0.1:6379> RPUSH mylist "bar"
         * (integer) 4
         * redis 127.0.0.1:6379> RPOP mylist
         * OK
         * redis 127.0.0.1:6379> LRANGE mylist 0 -1
         * 1) "hello"
         * 2) "hello"
         * 3) "foo"
         */
        Object mylist2 = list.rightPop("mylist");//与leftPop基本一样 略
        System.out.println(mylist2);

        /**
         * Redis Lset 通过索引来设置元素的值。
         *
         * 当索引参数超出范围，或对一个空列表进行 LSET 时，返回一个错误。
         *
         * redis 127.0.0.1:6379> LSET KEY_NAME INDEX VALUE
         *
         *redis 127.0.0.1:6379> RPUSH mylist "hello"
         * (integer) 1
         * redis 127.0.0.1:6379> RPUSH mylist "hello"
         * (integer) 2
         * redis 127.0.0.1:6379> RPUSH mylist "foo"
         * (integer) 3
         * redis 127.0.0.1:6379> RPUSH mylist "hello"
         * (integer) 4
         * redis 127.0.0.1:6379> LSET mylist 0 "bar"
         * OK
         * redis 127.0.0.1:6379> LRANGE mylist 0 -1
         * 1: "bar"
         * 2) "hello"
         * 3) "foo"
         * 4) "hello"
         */
        list.leftPush("lsetindex:key","var1");
        list.leftPush("lsetindex:key","var2");
        list.leftPush("lsetindex:key","var3");
        list.set("lsetindex:key", 1,"var4");
        System.out.println("lsetindex:key\":  "+list.range("lsetindex:key",0,-1));//lsetindex:key":  [var3, var4, var1]

        /**
         * Redis Lpush 命令将一个或多个值插入到列表头部。 如果 key 不存在，一个空列表会被创建并执行 LPUSH 操作。 当 key 存在但不是列表类型时，返回一个错误。
         *
         * redis 127.0.0.1:6379> LPUSH KEY_NAME VALUE1.. VALUEN
         *
         * redis 127.0.0.1:6379> LPUSH list1 "foo"
         * (integer) 1
         * redis 127.0.0.1:6379> LPUSH list1 "bar"
         * (integer) 2
         * redis 127.0.0.1:6379> LRANGE list1 0 -1
         * 1) "foo"
         * 2) "bar
         */
        Long aLong3 = list.leftPush("leftPush:key", "var1");//上边已有 略
        System.out.println(aLong3);

        /**
         * Redis Rpushx 命令用于将一个或多个值插入到已存在的列表尾部(最右边)。如果列表不存在，操作无效。
         *
         * redis 127.0.0.1:6379> RPUSHX KEY_NAME VALUE1..VALUEN
         *
         * redis 127.0.0.1:6379> RPUSH mylist "hello"
         * (integer) 1
         * redis 127.0.0.1:6379> RPUSH mylist "foo"
         * (integer) 2
         * redis 127.0.0.1:6379> RPUSHX mylist2 "bar"
         * (integer) 0
         * redis 127.0.0.1:6379> LRANGE mylist 0 -1
         * 1) "hello"
         * 2) "foo"
         */
        Long aLong4 = list.rightPush("rightPush:key", "var03");//略
        System.out.println(aLong4);

    }

}
