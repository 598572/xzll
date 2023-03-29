package com.xzll.test.mianshi;

/**
 * @Author: hzz
 * @Date: 2023/2/13 14:14:54
 * @Description:
 */
public class SynchronizedTest {

	public static void main(String[] args) {

	}


	private int count = 10;
	private Object obj = new Object();

	/**
	 * 使用某对象上锁
	 */
	public void m() {
		synchronized(obj) { //任何线程要执行下面的代码，必须先拿到o的锁
			//do something
		}
	}


	private int count2 = 10;

	/**
	 * 使用this（本类的对象）上的锁
	 */
	public void m2() {
		synchronized(this) { //任何线程要执行下面的代码，必须先拿到this的锁
			//do something
		}
	}




	private int count3 = 10;

	/**
	 * 修饰非静态方法
	 */
	public synchronized void m3() { //等同于 synchronized(this) 都是使用当前类对象上的锁
		//do something
	}




	private static int count4 = 10;

	/**
	 * 修饰静态方法
	 */
	public synchronized static void m4() { //这里等同于 synchronized(SynchronizedTest.class)
		//do something
	}


}
