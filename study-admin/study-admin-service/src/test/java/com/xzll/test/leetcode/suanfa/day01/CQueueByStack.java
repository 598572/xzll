package com.xzll.test.leetcode.suanfa.day01;

import java.util.Stack;

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
public class CQueueByStack {

	//用stack
	Stack<String> A, B;

	public CQueueByStack() {
		A = new Stack<String>();
		B = new Stack<String>();
	}

	//往队列尾部添加元素
	public void appendTail(String value) {
		System.out.println("向栈A压栈,元素:"+value);
		A.push(value);
	}

	//删除队列头部元素
	public void deleteHead() {

		if (A.isEmpty()) System.out.println(-1);
		while (!A.isEmpty()) {
			//栈A不为空，将栈A的数据弹栈 压入栈B
			String s = A.pop();
			System.out.println("栈A不为空，从栈A弹栈，压入栈B，元素："+s);
			B.push(s);
		}

		while (!B.isEmpty()){
			String pop = B.pop();
			System.out.println("从栈B弹出，即从队列头部删除元素:{}"+pop);
		}
	}


	public static void main(String[] args) {

		String[] d = {"CQueue", "appendTail", "deleteHead", "deleteHead", "deleteHead"};

		CQueueByStack obj = new CQueueByStack();
		for (int i = 0; i < d.length; i++) {
			String s = d[i];
			obj.appendTail(s);
		}
		obj.deleteHead();

	}

}
