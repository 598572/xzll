package com.xzll.test.redis;

import com.xzll.test.redis.common.RedisCommonTest;
import org.junit.Test;
import org.springframework.data.redis.core.RedisCallback;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class RedisStringTest extends RedisCommonTest {


    /**
     * redis string类型 原生命令与 redisTemplate 方法对应
     */
    @Test
    public void string() {

        clearDB();

        /**
         * Redis SET 命令用于设置给定 key 的值。如果 key 已经存储其他值， SET 就覆写旧值，且无视类型。
         *
         * redis 127.0.0.1:6379> SET KEY_NAME VALUE
         */
        string.set("set:key", "set:value");


        /**
         * Redis Setnx（SET if Not eXists） 命令在指定的 key 不存在时，为 key 设置指定的值。
         *
         * redis 127.0.0.1:6379> SETNX KEY_NAME VALUE
         *
         * redis> EXISTS job                # job 不存在
         * (integer) 0
         *
         * redis> SETNX job "programmer"    # job 设置成功
         * (integer) 1
         *
         * redis> SETNX job "code-farmer"   # 尝试覆盖 job ，失败
         * (integer) 0
         *
         * redis> GET job                   # 没有被覆盖
         * "programmer"
         */
        Boolean aBoolean = string.setIfPresent("setnx:key", "setnx:value");
        System.out.println(aBoolean);

        //setnex
        Boolean nex = string.setIfPresent("setnex:key", "setnex:value",10,TimeUnit.SECONDS);
        System.out.println(nex);

        /**
         * Redis Getrange 命令用于获取存储在指定 key 中字符串的子字符串。字符串的截取范围由 start 和 end 两个偏移量决定(包括 start 和 end 在内)。
         *
         * redis 127.0.0.1:6379> GETRANGE KEY_NAME start end
         *
         * redis 127.0.0.1:6379> SET mykey "This is my test key"
         * OK
         * redis 127.0.0.1:6379> GETRANGE mykey 0 3
         * "This"
         * redis 127.0.0.1:6379> GETRANGE mykey 0 -1
         * "This is my test key"
         *
         */
        String string2 = string.get("set:key", 0, 2);
        System.out.println(string2);


        /**
         * Redis Mset 命令用于同时设置一个或多个 key-value 对。
         *
         * redis 127.0.0.1:6379> MSET key1 value1 key2 value2 .. keyN valueN
         *
         * redis 127.0.0.1:6379> MSET key1 "Hello" key2 "World"
         * OK
         * redis 127.0.0.1:6379> GET key1
         * "Hello"
         * redis 127.0.0.1:6379> GET key2
         * 1) "World"
         *
         */
        Map<String, Object> map = new HashMap<>();
        map.put("multiSet:key1", "v1");
        map.put("multiSet:key2", "v2");
        string.multiSet(map);
        List<Object> objects1 = string.multiGet(Stream.of("multiSet:key1", "multiSet:key2").collect(Collectors.toList()));
        System.out.println(objects1);


        /**
         * Redis Setex 命令为指定的 key 设置值及其过期时间。如果 key 已经存在， SETEX 命令将会替换旧的值。
         *
         * redis 127.0.0.1:6379> SETEX KEY_NAME TIMEOUT VALUE
         *
         * redis 127.0.0.1:6379> SETEX mykey 60 redis
         * OK
         * redis 127.0.0.1:6379> TTL mykey
         * 60
         * redis 127.0.0.1:6379> GET mykey
         * "redis
         */
        string.set("string","value",1,TimeUnit.DAYS);



        /**
         * Redis Get 命令用于获取指定 key 的值。如果 key 不存在，返回 nil 。如果key 储存的值不是字符串类型，返回一个错误。
         * redis 127.0.0.1:6379> GET KEY_NAME
         */
        Object key_name = string.get("string");
        System.out.println(key_name);


        /**
         * Redis Setbit 命令用于对 key 所储存的字符串值，设置或清除指定偏移量上的位(bit)。
         * 返回  指定偏移量原来储存的位。初始都是0也就是 false
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
        Boolean setbit = string.setBit("setbit", 10, true);
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

        Boolean getbit = string.getBit("bitkey", 10);
        System.out.println(getbit);//true
        Boolean getbit2 = string.getBit("bitkey", 2);
        System.out.println(getbit2);//false


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
        string.set("decrement:key",19);
        Long decrement = string.decrement("decrement:key");
        System.out.println(decrement);//18




        /**
         * Redis Decrby 命令将 key 所储存的值减去指定的减量值。
         * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECRBY 操作。
         * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
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
        Long decrement1 = string.decrement("decrement:key",2);
        System.out.println(decrement1);


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
        string.set("size:key","hellohzzller");
        Long string1 = string.size("size:key");
        System.out.println(string1);


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
        mmap.put("multiSetIfAbsent:key1", "v1");
        mmap.put("multiSetIfAbsent:key2", "mvalue2");
        Boolean aBoolean1 = string.multiSetIfAbsent(mmap);
        System.out.println(aBoolean1);


        /**
         * Redis Incrby 命令将 key 中储存的数字加上指定的增量值。
         * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCRBY 命令。
         * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
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
         * # key 不是数字值时
         *
         * redis> SET book "long long ago..."
         * OK
         *
         * redis> INCRBY book 200
         * (error) ERR value is not an integer or out of range
         */
        Long incr = string.increment("incr");//默认是+1
        Long incrHelpYouSelf = string.increment("incr", 20);//指定+多少
        System.out.println(incr);
        System.out.println(incrHelpYouSelf);


        /**
         * Redis Incrbyfloat 命令为 key 中所储存的值加上指定的浮点数增量值。
         * 如果 key 不存在，那么 INCRBYFLOAT 会先将 key 的值设为 0 ，再执行加法操作。
         * redis 127.0.0.1:6379> INCRBYFLOAT KEY_NAME INCR_AMOUNT
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


        /**
         * Redis Setrange 命令用指定的字符串覆盖给定 key 所储存的字符串值，覆盖的位置从偏移量 offset 开始。
         * redis 127.0.0.1:6379> SETRANGE KEY_NAME OFFSET VALUE
         *
         * redis 127.0.0.1:6379> SET key1 "Hello World"
         * OK
         * redis 127.0.0.1:6379> SETRANGE key1 6 "Redis"
         * (integer) 11
         * redis 127.0.0.1:6379> GET key1
         * "Hello Redis"
         */
        string.set("key","value",2);


        /**
         * Redis Psetex 命令以毫秒为单位设置 key 的生存时间。
         * redis 127.0.0.1:6379> PSETEX key1 EXPIRY_IN_MILLISECONDS value1
         *
         * redis 127.0.0.1:6379> PSETEX mykey 1000 "Hello"
         * OK
         * redis 127.0.0.1:6379> PTTL mykey
         * 999
         * redis 127.0.0.1:6379> GET mykey
         * 1) "Hello"
         *
         *
         * //这个比较特殊 具体看源码 记得之前看过个文章讲过因为这个出现个bug 对 就是这个 https://juejin.cn/post/6890482501970558990
         *
         * if (!TimeUnit.MILLISECONDS.equals(unit) || !this.failsafeInvokePsetEx(connection)) {
         *       connection.setEx(rawKey, TimeoutUtils.toSeconds(timeout, unit), rawValue);
         * }
         *
         * public void set(K key, V value, final long timeout, final TimeUnit unit) {
         *         final byte[] rawKey = this.rawKey(key);
         *         final byte[] rawValue = this.rawValue(value);
         *         this.execute(new RedisCallback<Object>() {
         *             public Object doInRedis(RedisConnection connection) throws DataAccessException {
         *                 this.potentiallyUsePsetEx(connection);
         *                 return null;
         *             }
         *
         *             public void potentiallyUsePsetEx(RedisConnection connection) {
         *                 //可以看到 如果单位不是MILLISECONDS 那么都会转成秒 如果是MILLISECONDS 那么走failsafeInvokePsetEx方法
         *                 if (!TimeUnit.MILLISECONDS.equals(unit) || !this.failsafeInvokePsetEx(connection)) {
         *
         *                     connection.setEx(rawKey, TimeoutUtils.toSeconds(timeout, unit), rawValue);
         *                 }
         *
         *             }
         *
         *             private boolean failsafeInvokePsetEx(RedisConnection connection) {
         *                 boolean failed = false;
         *
         *                 try {
         *                     connection.pSetEx(rawKey, timeout, rawValue);//这个方法的实现逻辑 就是 https://juejin.cn/post/6890482501970558990 这个问题的原因
         *                 } catch (UnsupportedOperationException var4) {
         *                     failed = true;
         *                 }
         *
         *                 return !failed;
         *             }
         *         }, true);
         *     }
         */
        string.set("str","value",10, TimeUnit.MILLISECONDS);


        /**
         * Redis Append 命令用于为指定的 key 追加值。
         * 如果 key 已经存在并且是一个字符串， APPEND 命令将 value 追加到 key 原来的值的末尾。
         * 如果 key 不存在， APPEND 就简单地将给定 key 设为 value ，就像执行 SET key value 一样。
         *
         * redis 127.0.0.1:6379> APPEND KEY_NAME NEW_VALUE
         *
         * # 对不存在的 key 执行 APPEND
         *
         * redis> EXISTS myphone               # 确保 myphone 不存在
         * (integer) 0
         *
         * redis> APPEND myphone "nokia"       # 对不存在的 key 进行 APPEND ，等同于 SET myphone "nokia"
         * (integer) 5                         # 字符长度
         *
         *
         * # 对已存在的字符串进行 APPEND
         *
         * redis> APPEND myphone " - 1110"     # 长度从 5 个字符增加到 12 个字符
         * (integer) 12
         *
         * redis> GET myphone
         * "nokia - 1110"
         *
         */
        Integer append = string.append("append:key", "append:value");
        System.out.println(append);


        /**
         * Redis Getset 命令用于设置指定 key 的值，并返回 key 旧的值。
         * redis 127.0.0.1:6379> GETSET KEY_NAME VALUE
         *
         * redis 127.0.0.1:6379> GETSET mynewkey "This is my test key"
         * (nil)
         * redis 127.0.0.1:6379> GETSET mynewkey "This is my new value to test getset"
         * "This is my test key"
         */
        string.set("getsetkey","oldvalue");
        Object oldKey = string.getAndSet("getsetkey", "newvalue");
        System.out.println(oldKey);


        /**
         * Redis Mget 命令返回所有(一个或多个)给定 key 的值。 如果给定的 key 里面，有某个 key 不存在，那么这个 key 返回特殊值 nil 。
         *
         * redis 127.0.0.1:6379> MGET KEY1 KEY2 .. KEYN
         *
         * redis 127.0.0.1:6379> SET key1 "hello"
         * OK
         * redis 127.0.0.1:6379> SET key2 "world"
         * OK
         * redis 127.0.0.1:6379> MGET key1 key2 someOtherKey
         * 1) "Hello"
         * 2) "World"
         * 3) (nil)
         */
        List<Object> keys = new ArrayList<>();
        keys.add("key1");
        keys.add("key2");
        Map<Object,Object> mapM = new HashMap<>();
        mapM.put("key1","v1");
        mapM.put("key2","v2");
        string.multiSet(mapM);
        List<Object> objects = string.multiGet(keys);
        System.out.println(objects);


        /**
         * Redis Incr 命令将 key 中储存的数字值增一。
         * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR 操作。
         * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
         * 本操作的值限制在 64 位(bit)有符号数字表示之内。
         *
         * redis 127.0.0.1:6379> INCR KEY_NAME
         *
         * redis> SET page_view 20
         * OK
         *
         * redis> INCR page_view
         * (integer) 21
         *
         * redis> GET page_view    # 数字值在 Redis 中以字符串的形式保存
         * "21"
         */
        //与上边的increment差不多
        Long incrone = string.increment("increment:one");
        Object o = string.get("increment:one");
        System.out.println(o.toString());



        /**
         *  操作 bitmap
         *  可以使用redisTemplate的bitField 但是感觉不太方便
         *  //        BitFieldSubCommands bitFieldSubCommands = BitFieldSubCommands.create().get(BitFieldSubCommands.BitFieldType.unsigned(date.getDayOfMonth())).valueAt(0);
         * //        List<Long> list = redisTemplate.opsForValue().bitField(buildSignKey(uid, date),bitFieldSubCommands);
         *   使用下边的感觉更清晰
         *
         *
         *
         * BITCOUNT key [start] [end]
         *
         * 计算给定字符串中，被设置为 1 的比特位的数量。
         *
         * 一般情况下，给定的整个字符串都会被进行计数，通过指定额外的 start 或 end 参数，可以让计数只在特定的位上进行。
         *
         * start 和 end 参数的设置和 GETRANGE 命令类似，都可以使用负数值：比如 -1 表示最后一个位，而 -2 表示倒数第二个位，以此类推。
         *
         * 不存在的 key 被当成是空字符串来处理，因此对一个不存在的 key 进行 BITCOUNT 操作，结果为 0 。
         *
         *
         */
        Boolean aBoolean2 = string.setBit("bit:key1", 12, true);
        Boolean aBoolean3 = string.setBit("bit:key1", 14, true);
        Boolean aBoolean4 = string.setBit("bit:key1", 15, true);
        Boolean aBoolean5 = string.setBit("bit:key1", 23, true);
        Boolean bit = string.getBit("bit:key1", 0);
        System.out.println(string.getBit("bit:key1", 12));
        System.out.println(string.getBit("bit:key1", 14));
        System.out.println(string.getBit("bit:key1", 15));

        System.out.println(bit);
        Long count = redisTemplate.execute((RedisCallback<Long>) redisConnection -> redisConnection.bitCount("bit:key1".getBytes()));
        Long count2 = redisTemplate.execute((RedisCallback<Long>) redisConnection -> redisConnection.bitCount("bit:key1".getBytes(),100,500));
        Long count3 = redisTemplate.execute((RedisCallback<Long>) redisConnection -> redisConnection.bitCount("hzzbit".getBytes()));
        Long count4 = redisTemplate.execute((RedisCallback<Long>) redisConnection -> redisConnection.bitCount("hzzbit".getBytes(),0,100));
        System.out.println("bitCount  "+count);

    }

}
