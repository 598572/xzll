package com.xzll.test.javalock;

import cn.hutool.core.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/6/7 19:03
 * @Description:
 */
@Slf4j
public class LockInterruptiblyDemo {

    /**
     * lockInterruptibly()：跟lock()情況差不多，但是可以通过interrupt  中断正在等待锁的线程 可避免死锁 但需要人工 interrupt
     *
     * @param args
     * @throws InterruptedException
     */

    public static void main(String[] args) throws InterruptedException {


        ReentrantLock lock = new ReentrantLock();

        Runnable runnable = () -> {
            log.info("开始抢锁：{}", Thread.currentThread().getName());
            try {
                lock.lockInterruptibly();
                log.info("抢到锁的线程:{} 抢到后我就 死循环 谁也别想得到这把锁 ", Thread.currentThread().getName());
//                    Thread.sleep(5000);
                while (true) {
                    //这里使用 while(true) 代表抢到锁的线程做一些事情 并且处于 RUNNABLE （运行状态）
                    //不用 Thread.sleep() 因为使用他会使线程出现 TIME_WAITING (限时等待  )状态 容易引起混淆

                    //do some thing ........
                }
            } catch (InterruptedException e) {
                //当某个线程 调用interrupt()方法时候 那么他将放弃等待锁 从BLOCK/WAITING 状态变为 RUNNABLE
                log.info(Thread.currentThread().getName() + " 有人打断我了 我不等了 妈的 黄花菜凉了  ");
            } finally {
            }
        };

        Thread threadA = new Thread(runnable, "thread-A");
        Thread threadB = new Thread(runnable, "thread-B");
        threadA.start();
        //主线程睡会 保证A拿到锁 否则这个示例就有点乱了
        Thread.sleep(200);
        threadB.start();
        Thread.sleep(2);
        log.info("主线程睡两秒 2000 ms ");

        log.info("interrupt前 线程A状态:{}", threadA.getState());
        Thread.State state_B = threadB.getState();
        log.info("interrupt前 线程B状态:{}", state_B);

        threadB.interrupt();

        //保证 threadB catch里的先打印 下边的interrupt后xxxxxxxx 后打印
        //加了这个sleep后 threadB.interrupt();执行完 threadB的状态是TERMINATED 不加的话是 RUNNABLE ，
        //出现这样的情况 应该是因为 interrupt后threadB还没有立马结束
        Thread.sleep(1);

        log.info("interrupt后 线程A状态:{} 我就死循环 怎么的 ", threadA.getState());
        log.info("interrupt后 (线程A还不释放锁？额不等了 等的黄花菜都凉了 ) 线程B状态:从 {} 到->>>>>>>  {}", state_B, threadB.getState());


        /*
        18:41:38.805 [thread-A] INFO com.xzll.test.javalock.LockInterruptiblyDemo - 开始抢锁：thread-A
        18:41:38.809 [thread-A] INFO com.xzll.test.javalock.LockInterruptiblyDemo - 抢到锁的线程:thread-A 抢到后我就 死循环 谁也别想得到这把锁
        18:41:39.004 [thread-B] INFO com.xzll.test.javalock.LockInterruptiblyDemo - 开始抢锁：thread-B
        18:41:39.006 [main] INFO com.xzll.test.javalock.LockInterruptiblyDemo - 主线程睡两秒 2000 ms
        18:41:39.007 [main] INFO com.xzll.test.javalock.LockInterruptiblyDemo - interrupt前 线程A状态:RUNNABLE
        18:41:39.007 [main] INFO com.xzll.test.javalock.LockInterruptiblyDemo - interrupt前 线程B状态:WAITING
        18:41:39.007 [thread-B] INFO com.xzll.test.javalock.LockInterruptiblyDemo - thread-B 有人打断我了 我不等了 妈的 黄花菜凉了
        18:41:39.009 [main] INFO com.xzll.test.javalock.LockInterruptiblyDemo - interrupt后 线程A状态:RUNNABLE 我就死循环 怎么的
        18:41:39.010 [main] INFO com.xzll.test.javalock.LockInterruptiblyDemo - interrupt后 (线程A还不释放锁？额不等了 等的黄花菜都凉了 ) 线程B状态:从 WAITING 到->>>>>>>  TERMINATED


         */
    }
}
