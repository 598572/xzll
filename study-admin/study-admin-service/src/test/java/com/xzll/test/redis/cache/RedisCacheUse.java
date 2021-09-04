package com.xzll.test.redis.cache;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.xzll.test.StudyTestApplicationTest;
import com.xzll.test.entity.AdminUserDO;
import com.xzll.test.mapper.AdminUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: hzz
 * @Date: 2021/8/26 16:56:04
 * @Description: 缓存穿透 缓存击穿 缓存雪崩，演示，这个例子 目前没演示出这三个问题，我准备后期使用jmter做这三个问题的演示
 */

@Slf4j
public class RedisCacheUse extends StudyTestApplicationTest {

	public static final String CACHE_KEY_PREFIX = "cacheKeyPrefix:";
	private ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(40000, 100000, 200, TimeUnit.SECONDS,
			new LinkedBlockingQueue<>(10000000), new ThreadFactoryBuilder().setNameFormat("缓存问题演示线程" + "-task-%d").build());


	@Autowired
	public AdminUserMapper adminUserMapper;

	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;

	@Test
	public void init() {

		redisTemplate.getConnectionFactory().getConnection().flushDb();

		LambdaQueryWrapper<AdminUserDO> queryWrapper = new LambdaQueryWrapper<>();

		List<AdminUserDO> adminUserDOS = adminUserMapper.selectList(queryWrapper);
		System.out.println("u_admin_user表的数据量: " + adminUserDOS.size());

		HashMap<String, String> stringHashMap = Maps.newHashMap();

		adminUserDOS.forEach(x -> stringHashMap.put(CACHE_KEY_PREFIX + x.getId().toString(), JSON.toJSONString(x)));

		redisTemplate.opsForValue().multiSet(stringHashMap);
		Set<String> keys = getKeys(CACHE_KEY_PREFIX);
		System.out.println("redis中的数据量(key前缀为cacheKeyPrefix ) " + keys.size());

	}


	/**
	 * 缓存穿透代码演示
	 */
	@Test
	public void chuantou() {

		new Thread(()->{
			for (int i = 0; i < 2000; i++) {
				poolExecutor.execute(() -> {
					Object value = redisTemplate.opsForValue().get(CACHE_KEY_PREFIX + -1);
					if (Objects.nonNull(value)){
						log.info("从 [redis] 获取 到的结果: {}",JSON.toJSONString(value));
					}
					if (Objects.isNull(value)){
						LambdaQueryWrapper<AdminUserDO> queryWrapper = new LambdaQueryWrapper<>();
						queryWrapper.eq(AdminUserDO::getId,-1);
						List<AdminUserDO> adminUserDOS = adminUserMapper.selectList(queryWrapper);
						log.info("从 [db] 获取 到的结果: {}",adminUserDOS);
					}
				});
			}
		}).start();

		new Thread(()->{
			for (int i = 0; i < 1000; i++) {
				poolExecutor.execute(() -> {
					Object value = redisTemplate.opsForValue().get(CACHE_KEY_PREFIX + -1);
					if (Objects.nonNull(value)){
						log.info("从 [redis] 获取 到的结果: {}",JSON.toJSONString(value));
					}
					if (Objects.isNull(value)){
						LambdaQueryWrapper<AdminUserDO> queryWrapper = new LambdaQueryWrapper<>();
						queryWrapper.eq(AdminUserDO::getId,-1);
						List<AdminUserDO> adminUserDOS = adminUserMapper.selectList(queryWrapper);
						log.info("从 [db] 获取 到的结果: {}",adminUserDOS);
					}
				});
			}
		}).start();



	}

	/**
	 * 缓存击穿代码演示
	 */
	@Test
	public void jichuan() {

	}

	/**
	 * 缓存雪崩代码演示
	 */
	@Test
	public void xuebeng() {

	}


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
			//log.info("getKeys 执行redis scan 命令结果，keyPrefix：{} , keyResult:{}", keyPrefix, JSON.toJSONString(keysTmp));
			return keysTmp;
		});
		return keys;
	}
}
