package com.xzll.test.leetcode.suanfa.day02;

import java.util.Stack;

/**
 * @Author: hzz
 * @Date: 2023/2/1 09:04:01
 * @Description: 剑指 Offer 30. 包含min函数的栈
 *
 * 定义栈的数据结构，请在该类型中实现一个能够得到栈的最小元素的 min 函数在该栈中，调用 min、push 及 pop 的时间复杂度都是 O(1)。
 *
 */
public class MinStack {

	/**
	 * initialize your data structure here.
	 */
	private Stack<Integer> minStack;

	private static Integer minValue = 0;

	public MinStack() {
		minStack = new Stack<>();
	}

	public void push(int x) {
		//每次push时候都与最小值对比，如果当前值小于最小值，就将当前值赋值给最小值的变量 minValue
		minValue = x < minValue ? x : minValue;
		if (x<minValue){
			//记录当前入栈的最小值的上一个最小值
			minStack.push(minValue);
		}
		minStack.push(x);
	}

	public void pop() {
		Integer pop = minStack.pop();
		//弹栈的正是最小值，那么最小值变成最小值的上一个最小值
		if (pop==minValue){
			minValue=minStack.pop();
		}
	}

	public int top() {
		return minStack.peek();
	}

	public int min() {
		return minValue;
	}

//	public static void main(String[] args) {
//		MinStack obj = new MinStack();
//		Stack<Integer> minStack = obj.minStack;
//		minStack.push(x);
//		minStack.pop();
//		int param_3 = minStack.top();
//		int param_4 = minStack.min();
//	}
}

/**
 * Your MinStack object will be instantiated and called as such:
 * MinStack obj = new MinStack();
 * obj.push(x);
 * obj.pop();
 * int param_3 = obj.top();
 * int param_4 = obj.min();
 */

