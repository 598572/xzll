package com.xzll.test.javalock;

import cn.hutool.core.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/6/7 19:02
 * @Description:
 */
@Slf4j
public class TryLockDemo {


    /**
     * tryLock()：若当下不能取得lock，thread就会放弃。 可以设置超时时间 示例：  lock.tryLock(2, TimeUnit.SECONDS);
     * 即超过设置时间后不在等待锁 可良好避免死锁
     *
     * @param
     * @throws InterruptedException
     */
    public static void main(String args[]) throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                if (lock.tryLock()) {

                    System.out.println(String.format("%s %s locked , 线程状态： %s", new Date(System.currentTimeMillis()),
                            Thread.currentThread().getName(),
                            Thread.currentThread().getState()
                    ));
                    while (true) {
                        //do some thing..........
                    }
                } else {
                    //获取当前锁的持有者 方法  protected Thread getOwner() {return sync.getOwner();}  受保护？必须继承你？ 不想继承咋办？ 反射，暴力获取
                    Method getOwner = ReflectUtil.getMethodByName(ReentrantLock.class, "getOwner");
                    Thread holdLockThread = (Thread) ReflectUtil.invoke(lock, getOwner);
                    log.info("被人占的呢 没抢到 不等了 我是：{} ,锁被 {} 持有的呢 ", Thread.currentThread().getName(), holdLockThread.getName());
                    System.out.println(
                            String.format("%s %s no lock , 线程状态： %s", new Date(System.currentTimeMillis()), Thread.currentThread().getName(),
                                    Thread.currentThread().getState()
                            ));
                }
            }
        };
        Thread threadA = new Thread(runnable, "Thread A");
        Thread threadB = new Thread(runnable, "Thread B");

        threadA.start();
        threadB.start();

        Thread.sleep(200);

        log.info("线程A状态 ：{}", threadA.getState());
        log.info("由于使用了 tryLock()  所以线程B状态为  ：{}", threadB.getState());

        /*

        Tue Jun 08 18:50:52 CST 2021 Thread A locked , 线程状态： RUNNABLE
        18:50:52.548 [Thread B] INFO com.xzll.test.javalock.TryLockDemo - 被人占的呢 没抢到 不等了 我是：Thread B ,锁被 Thread A 持有的呢
        Tue Jun 08 18:50:52 CST 2021 Thread B no lock , 线程状态： RUNNABLE
        18:50:52.679 [main] INFO com.xzll.test.javalock.TryLockDemo - 线程A状态 ：RUNNABLE
        18:50:52.679 [main] INFO com.xzll.test.javalock.TryLockDemo - 由于使用了 tryLock()  所以线程B状态为  ：TERMINATED


         */

    }

}
