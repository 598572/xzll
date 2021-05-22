package com.xzll.test.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/5/22 17:48
 * @Description: 这个工具类 方法命名规则都以redis原生命令保持一致
 */
@Component
public class RedisClient {

    private static final long DEFAULT_SIZE = 0;
    @Resource
    private RedisTemplate<Object, Object> redisTemplate;

    /**
     * 指定缓存失效时间
     *
     * @param key
     * @param time 时间(秒)
     * @return
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key) {
        return Optional.ofNullable(redisTemplate.getExpire(key, TimeUnit.SECONDS)).orElse(DEFAULT_SIZE);
    }

    /**
     * @param key
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        try {
            return Optional.ofNullable(redisTemplate.hasKey(key)).orElse(false);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除键值
     *
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    /**
     * 获取键值
     *
     * @param key
     * @return
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 设置键值
     *
     * @param key
     * @param value
     * @return true成功 false失败
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 设置键值同时指定过期时间
     *
     * @param key
     * @param value
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 键值递增
     *
     * @param key
     * @param delta 要增加几(大于0)
     * @return
     */
    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return Optional.ofNullable(redisTemplate.opsForValue().increment(key, delta)).orElse(DEFAULT_SIZE);
    }

    /**
     * 键值递减
     *
     * @param key
     * @param delta 要减少几(小于0)
     * @return
     */
    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return Optional.ofNullable(redisTemplate.opsForValue().increment(key, -delta)).orElse(DEFAULT_SIZE);
    }

    /**
     * 获取HashGet
     *
     * @param key
     * @param item
     * @return
     */
    public Object hget(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 设置HashGet
     *
     * @param key
     * @return
     */
    public Map<Object, Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 设置多个HashGet
     *
     * @param key
     * @param map
     * @return
     */
    public boolean hmset(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 设置多个HashGet时指定过期时间
     *
     * @param key
     * @param map
     * @param time 时间(秒)
     * @return
     */
    public boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key
     * @param item
     * @param value
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key
     * @param item
     * @param value
     * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key
     * @param item
     */
    public void hdel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中是否存在item
     *
     * @param key
     * @param item
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key
     * @param item
     * @param by   要增加的值(大于0)
     * @return
     */
    public double hincr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     *
     * @param key
     * @param item
     * @param by   要减少的值(小于0)
     * @return
     */
    public double hdecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    /**
     * 获取set的内容
     *
     * @param key
     * @return
     */
    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 判断set中是否存在某个值
     *
     * @param key
     * @param value
     * @return
     */
    public boolean sHasKey(String key, Object value) {
        try {
            return Optional.ofNullable(redisTemplate.opsForSet().isMember(key, value)).orElse(false);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 设置set
     *
     * @param key
     * @param values
     * @return
     */
    public long sSet(String key, Object... values) {
        try {
            return Optional.ofNullable(redisTemplate.opsForSet().add(key, values)).orElse(DEFAULT_SIZE);
        } catch (Exception e) {
            e.printStackTrace();
            return DEFAULT_SIZE;
        }
    }

    /**
     * 设置set同时指定过期时间
     *
     * @param key
     * @param time   时间(秒)
     * @param values
     * @return
     */
    public long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0) expire(key, time);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return DEFAULT_SIZE;
        }
    }

    /**
     * 获取set的元素个数
     *
     * @param key
     * @return
     */
    public long sGetSetSize(String key) {
        try {
            return Optional.ofNullable(redisTemplate.opsForSet().size(key)).orElse(DEFAULT_SIZE);
        } catch (Exception e) {
            e.printStackTrace();
            return DEFAULT_SIZE;
        }
    }

    /**
     * 从set中移除元素
     *
     * @param key
     * @param values
     * @return
     */
    public long setRemove(String key, Object... values) {
        try {
            return Optional.ofNullable(redisTemplate.opsForSet().remove(key, values)).orElse(DEFAULT_SIZE);
        } catch (Exception e) {
            e.printStackTrace();
            return DEFAULT_SIZE;
        }
    }

    /**
     * 获取List的内容
     *
     * @param key
     * @param start 开始索引
     * @param end   结束索引 0 到 -1代表所有值
     * @return
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取list的长度
     *
     * @param key
     * @return
     */
    public long lGetListSize(String key) {
        try {
            return Optional.ofNullable(redisTemplate.opsForList().size(key)).orElse(DEFAULT_SIZE);
        } catch (Exception e) {
            e.printStackTrace();
            return DEFAULT_SIZE;
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 设置List
     *
     * @param key
     * @param value
     * @return
     */
    public boolean lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 设置List
     *
     * @param key
     * @param value
     * @param time  时间(秒)
     * @return
     */
    public boolean lSet(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0)
                expire(key, time);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 设置List
     *
     * @param key
     * @param value
     * @return
     */
    public boolean lSet(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 设置List同时指定过期时间
     *
     * @param key
     * @param value
     * @param time  时间(秒)
     * @return
     */
    public boolean lSet(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0)
                expire(key, time);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key
     * @param index
     * @param value
     * @return
     */
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key
     * @param count
     * @param value
     * @return 移除的个数
     */
    public long lRemove(String key, long count, Object value) {
        try {
            return Optional.ofNullable(redisTemplate.opsForList().remove(key, count, value)).orElse(DEFAULT_SIZE);
        } catch (Exception e) {
            e.printStackTrace();
            return DEFAULT_SIZE;
        }
    }
}
