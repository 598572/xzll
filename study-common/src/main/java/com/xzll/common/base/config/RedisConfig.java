package com.xzll.common.base.config;


import org.apache.commons.lang3.StringUtils;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/5/22 17:05
 * @Description:
 * 引用两个大佬的
 * 一篇不错的序列化文章 https://www.cnkirito.moe/spring-data-redis-2/
 * 以及源码角度解析的 ： https://zhuanlan.zhihu.com/p/92492295
 *
 *
 */
@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.database}")
    private String database;

    public RedisConfig() {
    }

    /**
     * 使用这个序列化key value会都转成json 故不适用
     * @param redisConnectionFactory
     * @return
     */
//    @Bean
//    @ConditionalOnMissingBean(name = "redisTemplate")
//    public RedisTemplate<String, Object> redisTemplate(
//            RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
//
//        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
//        ObjectMapper om = new ObjectMapper();
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        jackson2JsonRedisSerializer.setObjectMapper(om);
//
//        RedisTemplate<String, Object> template = new RedisTemplate<>();
//        template.setConnectionFactory(redisConnectionFactory);
//        template.setKeySerializer(jackson2JsonRedisSerializer);
//        template.setValueSerializer(jackson2JsonRedisSerializer);
//        template.setHashKeySerializer(jackson2JsonRedisSerializer);
//        template.setHashValueSerializer(jackson2JsonRedisSerializer);
//        template.afterPropertiesSet();
//        return template;
//    }

    @Bean
    @Primary
    @ConditionalOnMissingBean(name = "redisTemplate")
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        //指定 key&value 序列化方式
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);

        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

	/**
	 * 目前只了解了这个框架的分布式锁相关，其他很牛逼的东西暂时没研究 挺强大的，源码也值得一看
	 * @return
	 */
	@Bean
    public RedissonClient redissonClient() {
        Config config = new Config();

        // 注意 该参数用于异步定时线程(实际是timer)隔多久进行锁的续期。默认就是30/3=10秒，
        // 在这里可以修改默认值，注意:此参数一定是小于锁的租约时长的
        config.setLockWatchdogTimeout(10000L);

        //指定redis服务器类型 比如哨兵，单点，集群，主从，复制等等，这里使用单点进行演示
        SingleServerConfig singleServerConfig = config.useSingleServer();
        singleServerConfig.setAddress("redis://"+host+":6379");
        singleServerConfig.setDatabase(Integer.valueOf(database));

        if (StringUtils.isNotBlank(password)) {
            singleServerConfig.setPassword(password);
        }
        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;
    }

}
