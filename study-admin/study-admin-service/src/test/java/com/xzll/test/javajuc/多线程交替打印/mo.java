package com.xzll.test.javajuc.多线程交替打印;

/**
 *
 * @Author: hzz
 * @Date:  2023/2/2 10:50:39
 * @Description:
 */
public class mo {

	public static void main(String[] args) {
		for (int i = -10; i < 100; i++) {
			System.out.println("当前value: "+i);
			System.out.println("取模后value: "+i%4);
		}

		//模运算，模前边小于模后边的值 商则为前边值，可以整除的话，商则为0 ，不能整除模后的值，则商为余数
	}
}
