package com.xzll.test.redis.use;

import com.xzll.test.redis.common.RedisCommonTest;
import org.junit.Test;
import org.springframework.data.redis.core.RedisCallback;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author: hzz
 * @Date: 2021/8/26 16:56:04
 * @Description:
 */
public class RedisDataTypeUse extends RedisCommonTest {


	/**
	 * string使用经典场景 不说了这个都知道
	 */
	@Test
	public void stringUse() {
		clearDB();
	}

	/**
	 * hash使用经典场景
	 */
	@Test
	public void hashUse() {

		clearDB();

		User hzz = new User();
		hzz.setId(1L);
		hzz.setName("黄壮壮");
		hzz.setAddress("快乐星球");
		hzz.setAge(27);

		User xzll = new User();
		xzll.setId(2L);
		xzll.setName("蝎子莱莱");
		xzll.setAddress("北京");
		xzll.setAge(18);

		Arrays.asList(hzz, xzll).forEach(x -> {
			Map<String, Object> objectToMap = User.getObjectToMap(x);
			map.putAll("user", objectToMap);
		});

	}

	/**
	 * set使用经典场景
	 */
	@Test
	public void setUse() {

		set.add("黄壮壮", "孙悟空", "猪八戒", "蜻蜓队长", "骆宾王", "蟑螂恶霸");
		set.add("骆宾王", "孙悟空", "玉兔精", "蜻蜓队长", "范仲淹", "蝎子莱莱");
		set.add("蝎子莱莱", "蟑螂恶霸", "蜘蛛侦探", "蜻蜓队长", "孙悟空");

		//交集 共同关注
		Set<Object> intersectList = set.intersect("黄壮壮", Stream.of("骆宾王").collect(Collectors.toList()));
		System.out.println("黄壮壮和骆宾王的共同关注: " + intersectList);
		//差集 可能认识的人 一般都有个来自xxx
		Set<Object> differenceList = set.difference("黄壮壮", "骆宾王");
		differenceList.remove("骆宾王");
		System.out.println("可能认识的人的列表: " + differenceList + " ； 来自:" + "骆宾王");
		//我关注的某个人是否也关注了xxx  注意和共同关注的区别
		Boolean flag = set.isMember("骆宾王", "蜻蜓队长");
		System.out.println("黄壮壮关注的骆宾王是否也关注了蜻蜓队长？ " + (flag ? "关注了" : "没关注"));

	}

	/**
	 * zset使用经典场景
	 *
	 *
	 * RDM Redis Console
	 * Connecting...
	 * Connected.
	 * 本地redis:0>ZINCRBY  热搜:20210826 1  A条新闻ID
	 * "1"
	 * 本地redis:0>ZINCRBY 热搜:20210826 1 B条新闻ID
	 * "1"
	 * 本地redis:0>ZINCRBY 热搜:20210826 1 C条新闻ID
	 * "1"
	 * 本地redis:0>ZINCRBY 热搜:20210826 1 D条新闻ID
	 * "1"
	 * 本地redis:0>ZINCRBY 热搜:20210826 1 A条新闻ID
	 * "2"
	 * 本地redis:0>ZINCRBY 热搜:20210826 1 A条新闻ID
	 * "3"
	 * 本地redis:0>ZREVRANGE 热搜:20210826 0 10 WITHSCORES
	 * 1) "A条新闻ID"
	 * 2) "3"
	 * 3) "D条新闻ID"
	 * 4) "1"
	 * 5) "C条新闻ID"
	 * 6) "1"
	 * 7) "B条新闻ID"
	 * 8) "1"
	 * 本地redis:0>ZINCRBY 热搜:20210826 1 C条新闻ID
	 * "2"
	 * 本地redis:0>ZREVRANGE 热搜:20210826 0 10 WITHSCORES
	 * 1) "A条新闻ID"
	 * 2) "3"
	 * 3) "C条新闻ID"
	 * 4) "2"
	 * 5) "D条新闻ID"
	 * 6) "1"
	 * 7) "B条新闻ID"
	 * 8) "1"
	 * 本地redis:0>ZINCRBY 热搜:20210825 1 C条新闻ID
	 * "1"
	 * 本地redis:0>ZINCRBY 热搜:20210824 1 C条新闻ID
	 * "1"
	 * 本地redis:0>ZINCRBY 热搜:20210823 1 C条新闻ID
	 * "1"
	 * 本地redis:0>ZINCRBY 热搜:20210823 1 B条新闻ID
	 * "1"
	 * 本地redis:0>ZINCRBY 热搜:20210823 1 A条新闻ID
	 * "1"
	 * 本地redis:0>ZINCRBY 热搜:20210824 1 A条新闻ID
	 * "1"
	 * 本地redis:0>ZINCRBY 热搜:202108245 1 A条新闻ID
	 * "1"
	 * 本地redis:0>ZINCRBY 热搜:20210824 1 B条新闻ID
	 * "1"
	 * 本地redis:0>ZINCRBY 热搜:20210825 1 B条新闻ID
	 * "1"
	 * 本地redis:0>ZUNIONSTORE 近4天热搜计算 4 热搜:20210823 热搜:20210824 热搜:20210825 热搜:20210826
	 * "4"
	 * 本地redis:0>ZUNIONSTORE 近4天热搜计算 4 热搜:20210823 热搜:20210824 热搜:20210825 热搜:20210826
	 * "4"
	 * 本地redis:0>ZINCRBY 热搜:20210825 1 B条新闻ID
	 * "2"
	 * 本地redis:0>ZINCRBY 热搜:20210825 1 B条新闻ID
	 * "3"
	 * 本地redis:0>ZUNIONSTORE 近4天热搜计算 4 热搜:20210823 热搜:20210824 热搜:20210825 热搜:20210826
	 * "4"
	 * 本地redis:0>ZUNIONSTORE 近4天热搜计算 4  热搜:20210823 热搜:20210824 热搜:20210825 热搜:20210826
	 * "4"
	 * 本地redis:0>ZINCRBY 热搜:20210825 1 B条新闻ID
	 * "4"
	 * 本地redis:0>ZINCRBY 热搜:20210825 1 B条新闻ID
	 * "5"
	 * 本地redis:0>ZUNIONSTORE 近4天热搜计算 4 热搜:20210823 热搜:20210824 热搜:20210825 热搜:20210826
	 * "4"
	 * 本地redis:0>ZUNIONSTORE 近4天热搜计算 4 热搜:20210823 热搜:20210824 热搜:20210825 热搜:20210826 AGGREGATE SUM
	 * "4"
	 * 本地redis:0>ZUNIONSTORE 近4天热搜计算2 4 热搜:20210823 热搜:20210824 热搜:20210825 热搜:20210826
	 * "4"
	 * 本地redis:0>ZUNIONSTORE 近4天热搜计算2 4 热搜:20210823 热搜:20210824 热搜:20210825 热搜:20210826  AGGREGATE SUM
	 * "4"
	 * 本地redis:0>ZREVRANGE  近4天热搜计算2  0 10 WITHSCORES
	 * 1) "B条新闻ID"
	 * 2) "8"
	 * 3) "C条新闻ID"
	 * 4) "5"
	 * 5) "A条新闻ID"
	 * 6) "5"
	 * 7) "D条新闻ID"
	 * 8) "1"
	 * 本地redis:0>
	 *
	 *
	 *
	 */
	@Test
	public void zsetUse() {
		//不写代码了 看上边注释中的redis指令即可。
		clearDB();

	}


}
