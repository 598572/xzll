package com.xzll.test.redislock;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.xzll.test.redis.common.RedisCommonTest;
import org.junit.Test;
import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import sun.jvm.hotspot.debugger.posix.elf.ELFException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import static cn.hutool.core.date.DatePattern.NORM_DATETIME_FORMATTER;
import static cn.hutool.core.date.DatePattern.PURE_DATETIME_FORMATTER;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/7/18 07:38
 * @Description:
 *
 *
 * 注意:
 *
 * if(leaseTime = = - 1L){
 *     开启自动续期的定时任务，不断重置过期时间为lockWatchdogTimeout
 *     定时任务的频率为lockWatchdogTimeout配置时间的1/3
 * }else{
 *     不自动续期
 *     设置过期时间为leaseTime
 * }
 *
 */
public class RedisLock extends RedisCommonTest {


    @Autowired
    private RedissonClient redissonClient;


	/**
	 * 注意 此方法将不会自动续期 (当执行时间超过传入的50时候)将会自动释放锁
	 */
	@Test
    public void lock() {
        RLock lock = redissonClient.getLock(Thread.currentThread().getName() + "+" + this.getClass().getSimpleName());
        try {
        	//第一个参数 为等待时间 第二个参数为锁的持有时间，第三个参数为时间单位
            boolean b = lock.tryLock(10, 50, TimeUnit.SECONDS);
            if (b) {
                System.out.println("true 申请锁的结果: " + b);
            } else {
                System.out.println("false 申请锁的结果: " + b);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

	/**
	 *
	 *
	 * 此方法将会给锁自动续期默认应该是30秒 超时后自动续期 结论怎么得来？ 看源码即可 不传 lessTime时候 redisson给默认个-1 内部会根据这个值判断
	 *
	 *
	 * private <T> RFuture<Long> tryAcquireAsync(long waitTime, long leaseTime, TimeUnit unit, long threadId) {
	 *         RFuture ttlRemainingFuture;
	 *         if (leaseTime != -1L) {
	 *             ttlRemainingFuture = this.tryLockInnerAsync(waitTime, leaseTime, unit, threadId, RedisCommands.EVAL_LONG);
	 *         } else {
	 *             ttlRemainingFuture = this.tryLockInnerAsync(waitTime, this.internalLockLeaseTime, TimeUnit.MILLISECONDS, threadId, RedisCommands.EVAL_LONG);
	 *         }
	 *
	 *         ttlRemainingFuture.onComplete((ttlRemaining, e) -> {
	 *             if (e == null) {
	 *                 if (ttlRemaining == null) {
	 *
	 *                 //下边if else 为是否续期的代码逻辑 !!!!!!!!!!!!!1
	 *
	 *                     if (leaseTime != -1L) {
	 *                         this.internalLockLeaseTime = unit.toMillis(leaseTime);
	 *                     } else {
	 *
	 *                         this.scheduleExpirationRenewal(threadId);
	 *                     }
	 *                 }
	 *
	 *             }
	 *         });
	 *         return ttlRemainingFuture;
	 *     }
	 *
	 *
	 *
	 * 如果不等于-1那么用调用者传入的lessTime, 如果等于-1 那么将调用 scheduleExpirationRenewal() 方法
	 * 该方法内部维护一个 Timer线程（应该是netty封装jdk的），即----->  TimerTask(); netty牛逼 牛逼netty  netty yyds !!!!!
	 *
	 *  protected void scheduleExpirationRenewal(long threadId) {
	 *         RedissonBaseLock.ExpirationEntry entry = new RedissonBaseLock.ExpirationEntry();
	 *         RedissonBaseLock.ExpirationEntry oldEntry = (RedissonBaseLock.ExpirationEntry)EXPIRATION_RENEWAL_MAP.putIfAbsent(this.getEntryName(), entry);
	 *         if (oldEntry != null) {
	 *             oldEntry.addThreadId(threadId);
	 *         } else {
	 *             entry.addThreadId(threadId);
	 *             this.renewExpiration();
	 *         }
	 *
	 *     }
	 *
	 * 用与给某个key自动续期  renewExpiration()是执行续期操作的具体方法
	 *
	 *
	 * private void renewExpiration() {
	 *         RedissonBaseLock.ExpirationEntry ee = (RedissonBaseLock.ExpirationEntry)EXPIRATION_RENEWAL_MAP.get(this.getEntryName());
	 *         if (ee != null) {
	 *             Timeout task = this.commandExecutor.getConnectionManager().newTimeout(new TimerTask() {
	 *                 public void run(Timeout timeout) throws Exception {
	 *                     RedissonBaseLock.ExpirationEntry ent = (RedissonBaseLock.ExpirationEntry)RedissonBaseLock.EXPIRATION_RENEWAL_MAP.get(RedissonBaseLock.this.getEntryName());
	 *                     if (ent != null) {
	 *                         Long threadId = ent.getFirstThreadId();
	 *                         if (threadId != null) {
	 *                             RFuture<Boolean> future = RedissonBaseLock.this.renewExpirationAsync(threadId);
	 *                             future.onComplete((res, e) -> {
	 *                                 if (e != null) {
	 *                                     RedissonBaseLock.log.error("Can't update lock " + RedissonBaseLock.this.getRawName() + " expiration", e);
	 *                                     RedissonBaseLock.EXPIRATION_RENEWAL_MAP.remove(RedissonBaseLock.this.getEntryName());
	 *                                 } else {
	 *                                     if (res) {
	 *                                         RedissonBaseLock.this.renewExpiration();
	 *                                     }
	 *
	 *                                 }
	 *                             });
	 *                         }
	 *                     }
	 *                 }
	 *             }, this.internalLockLeaseTime / 3L, TimeUnit.MILLISECONDS); //可以看到 每30/3也就是10秒执行一下定时任务，检测entry,如果entry里有需要续期的 就给其执行续期操作，（其实是更新redis里的过期时间啦）
	 *             ee.setTimeout(task);
	 *         }
	 *     }
	 *
	 *
	 *
	 *
	 */
    @Test
    public void testLock() {
        String format = LocalDateTimeUtil.format(LocalDateTime.now(), PURE_DATETIME_FORMATTER);
        String key = Thread.currentThread().getName() + "_" + this.getClass().getSimpleName() + "_" + format;
        RLock lock = redissonClient.getLock(key);
        try {
//            boolean b = lock.tryLock(0, 5000, TimeUnit.HOURS);
			//参数 time为锁的等待时间，锁的租约时间没填，在内部默认是30m 超过后自动续期。牛逼！！！
            boolean b = lock.tryLock(0, TimeUnit.MILLISECONDS);
            //此处模拟业务执行，比如执行了180天 那么redis将会一直续期，180天后他还是持有该锁
            if (b){
                Thread.sleep(560000);
            }
            Iterable<String> keysByPattern = redissonClient.getKeys().getKeysByPattern(key);
            System.out.println(keysByPattern);
            RMap<Object, Object> map = redissonClient.getMap(key);
            System.out.println(map);
            System.out.println(map + "  -------- ");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }


}
