package com.xzll.test.javajuc.多线程交替打印;

/**
 * @Author: hzz
 * @Date: 2023/2/2 10:45:31
 * @Description: 这是网上给出的答案
 */
public class Wait_Notify_ABC2 {

	private int num;
	private static final Object LOCK = new Object();

	private void printABC(int targetNum) {
		for (int i = 0; i < 10; i++) {
			synchronized (LOCK) {
				while (num % 3 != targetNum) { //想想这里为什么不能用if代替，想不起来可以看公众号上一篇文章
					try {
						LOCK.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				num++;
				System.out.print(Thread.currentThread().getName());
				LOCK.notifyAll();
			}
		}
	}
	public static void main(String[] args) {
		Wait_Notify_ABC2  wait_notify_acb = new Wait_Notify_ABC2 ();
		new Thread(() -> {
			wait_notify_acb.printABC(0);
		}, "A").start();
		new Thread(() -> {
			wait_notify_acb.printABC(1);
		}, "B").start();
		new Thread(() -> {
			wait_notify_acb.printABC(2);
		}, "C").start();
	}

}
