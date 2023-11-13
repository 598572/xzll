package com.xzll.test.javajuc;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/6/26 12:01
 * @Description: CountDownLatch 简单演示
 **/
@Slf4j
public class CountDownLatchTest {

    private static BlockingQueue<Runnable> asyncSenderThreadPoolQueue;


    public static void main(String[] args) throws InterruptedException {

        int i1 = Runtime.getRuntime().availableProcessors();//获取当前机器的处理器核数
        System.out.println("Runtime.getRuntime().availableProcessors()  " + i1);
        CountDownLatch countDownLatch = new CountDownLatch(10);
        CountDownLatchTest.asyncSenderThreadPoolQueue = new LinkedBlockingQueue<Runnable>(50000);
        ThreadPoolExecutor executorService = new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors(),
                Runtime.getRuntime().availableProcessors(),
                1000 * 60,
                TimeUnit.MILLISECONDS,
                CountDownLatchTest.asyncSenderThreadPoolQueue,
                new ThreadFactory() {
                    private final AtomicInteger threadIndex = new AtomicInteger(0);
                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "CountDownLatchTest_" + this.threadIndex.incrementAndGet());
                    }
                });
        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> {
                System.out.println(Thread.currentThread().getName() + " do something..");
                countDownLatch.countDown();
            });
        }

        Thread.sleep(1000);
        countDownLatch.await();
        System.out.println("end");
        executorService.shutdown();
    }
}
