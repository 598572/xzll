package com.xzll.test.javajuc.多线程交替打印;

/**
 * @Author: hzz
 * @Date: 2023/2/2 10:45:31
 * @Description:
 */
public class Wait_Notify_ABC {

	private int num;
	private int i=0;
	private static final Object LOCK = new Object();

	private void printABC(int targetNum) {

		synchronized (LOCK) {

			while (num % 3 != targetNum) {    //想想这里为什么不能用if代替while，想不起来可以看公众号上一篇文章
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



		private  void printWhileABC(int targetNum) {
//			Object o = new Object();
			synchronized (LOCK) {
				while (i < 100) {
					System.out.println("当前线程name: " + Thread.currentThread().getName());
					System.out.println("mowai当前i值" + i);
					System.out.println("lock: " + Thread.currentThread().getName());
					if (i % 3 == Integer.valueOf(Thread.currentThread().getName())) {    //想想这里为什么不能用if代替while，想不起来可以看公众号上一篇文章
						System.out.println("进入取模代码块：" + Thread.currentThread().getName());
						System.out.println(Thread.currentThread().getName());
						i = i + 1;
						System.out.println("模中当前i值" + i);
						System.out.println("+=================");
						LOCK.notify();
					} else {
						try {
							LOCK.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

	public static void main(String[] args) {
//		Wait_Notify_ABC  wait_notify_acb = new Wait_Notify_ABC ();
//		new Thread(() -> {
//			wait_notify_acb.printABC(1);
//		}, "A").start();
//		new Thread(() -> {
//			wait_notify_acb.printABC(2);
//		}, "B").start();
//		new Thread(() -> {
//			wait_notify_acb.printABC(3);
//		}, "C").start();

//		Wait_Notify_ABC  wait_notify_acb = new Wait_Notify_ABC ();
//		new Thread(() -> {
//			wait_notify_acb.printNoSafeABC(1);
//		}, "A").start();
//		new Thread(() -> {
//			wait_notify_acb.printNoSafeABC(2);
//		}, "B").start();
//		new Thread(() -> {
//			wait_notify_acb.printNoSafeABC(3);
//		}, "C").start();


		Wait_Notify_ABC  wait_notify_acb = new Wait_Notify_ABC ();
		new Thread(() -> {
			wait_notify_acb.printDoubleWhileABC(0);
		}, "A").start();
		new Thread(() -> {
			wait_notify_acb.printDoubleWhileABC(1);
		}, "B").start();
		new Thread(() -> {
			wait_notify_acb.printDoubleWhileABC(2);
		}, "C").start();
	}


	private void printNoSafeABC(int targetNum) {


			System.out.print(Thread.currentThread().getName());


	}


	/**
	 * 自己写的答案
	 *
	 * @param targetNum
	 */
	private  void printDoubleWhileABC(int targetNum) {
//			Object o = new Object();//不能此处new 锁 否则锁不住
		synchronized (LOCK) {
			while (i < 100) {
				if (i%3 == targetNum){
					//打印当前线程
					i = i + 1;
					System.out.print(Thread.currentThread().getName());
					LOCK.notifyAll();
				}else {
					//不符合的，等待
					try {
						LOCK.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

}
