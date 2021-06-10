package com.xzll.test.javalock;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/6/7 19:01
 * @Description: 今天在看rocketMQ源码时候  发现其在  RouteInfoManager 类中
 * 用了 private final ReadWriteLock lock = new ReentrantReadWriteLock();
 * 实时上RocketMQ很多地方都有用到这个读写锁  不明白lockInterruptibly() 这个方法是干啥的 所有自己测试下 帮助理解
 *
 * 加锁代码
 * this.lock.readLock().lockInterruptibly();
 *
 * 解锁代码 在finally中
 * this.lock.readLock().unlock();
 *
 */
@Slf4j
public class LockDemo {

    /**
     * lock()：若lock被thread A取得，thread B会进入block状态，直到取得lock。 可能会出现死锁
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                lock.lock();
                log.info("我抢到锁了 哈哈我是 ：{}",Thread.currentThread().getName());
            }
        };
        Thread threadA = new Thread(runnable, "Thread A");
        Thread threadB = new Thread(runnable, "Thread B");

        threadA.start();
        Thread.sleep(5);
        threadB.start();
        log.info("线程A状态:{}",threadA.getState());
        log.info("线程B状态:{},线程A不释放 没办法 我只能死等了 ",threadB.getState());

        /*
        输出：

        18:36:43.997 [Thread A] INFO com.xzll.test.javalock.LockDemo - 我抢到锁了 哈哈我是 ：Thread A
        18:36:44.002 [main] INFO com.xzll.test.javalock.LockDemo - 线程A状态:TERMINATED
        18:36:44.002 [main] INFO com.xzll.test.javalock.LockDemo - 线程B状态:RUNNABLE,线程A不释放 没办法 我只能死等了

        结论 ：-------->  当使用 lock()方法时候 抢到的话执行run方法 ， 没抢到锁的线程会一直处于WAITING 状态 一直等待
         */
    }


}
