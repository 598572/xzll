package com.xzll.test.leetcode.suanfa.day01;

import java.util.LinkedList;

/**
 * @Author: hzz
 * @Date: 2023/1/31 16:04:49
 * @Description: 剑指 Offer 09. 用两个栈实现队列
 * <p>
 * <p>
 * 用两个栈实现一个队列。队列的声明如下，请实现它的两个函数 appendTail 和 deleteHead ，
 * 分别完成在队列尾部插入整数和在队列头部删除整数的功能。(若队列中没有元素，deleteHead 操作返回 -1 )
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode.cn/problems/yong-liang-ge-zhan-shi-xian-dui-lie-lcof
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class CQueueByFakeList {

	//用list模拟栈 也可以用stack
	LinkedList<String> A, B;

	public CQueueByFakeList() {
		A = new LinkedList<String>();
		B = new LinkedList<String>();
	}

	//往队列尾部添加元素
	public void appendTail(String value) {
		A.addFirst(value);
	}

	//删除队列头部元素
	public void deleteHead() {

		if (!B.isEmpty()) B.removeLast();
		if (A.isEmpty()) System.out.println(-1);
		while (!A.isEmpty()) {
			//栈A不为空，将栈A的数据弹栈 压入栈B
			String s = A.removeLast();
			System.out.println("a弹栈："+s);
			B.addLast(s);
		}
		B.stream().forEach(System.out::println);
		//删除栈B的栈底元素

	}


	public static void main(String[] args) {

		String[] d = {"CQueue", "appendTail", "deleteHead", "deleteHead", "deleteHead"};
		CQueueByFakeList obj = new CQueueByFakeList();
		for (int i = 0; i < d.length; i++) {
			String s = d[i];
			obj.appendTail(s);

		}
		obj.deleteHead();

	}

}
