package com.xzll.test.leetcode.suanfa.day02;

import java.util.Stack;

/**
 * @Author: hzz
 * @Date: 2023/2/1 09:04:01
 * @Description: 剑指 Offer 30. 包含min函数的栈
 *
 * 定义栈的数据结构，请在该类型中实现一个能够得到栈的最小元素的 min 函数在该栈中，调用 min、push 及 pop 的时间复杂度都是 O(1)。
 *
 *
 */
public class MinStackByDoubleStack {

	/*
	按照上面的思路，我们只需要设计一个数据结构，使得每个元素 a 与其相应的最小值 m 时刻保持一一对应。因此我们可以使用一个辅助栈，与元素栈同步插入与删除，
	用于存储与每个元素对应的最小值。

	当一个元素要入栈时，我们取当前辅助栈的栈顶存储的最小值，与当前元素比较得出最小值，将这个最小值插入辅助栈中；

	当一个元素要出栈时，我们把辅助栈的栈顶元素也一并弹出；

	在任意一个时刻，栈内元素的最小值就存储在辅助栈的栈顶元素中。

	作者：LeetCode-Solution
	链接：https://leetcode.cn/problems/bao-han-minhan-shu-de-zhan-lcof/solution/bao-han-minhan-shu-de-zhan-by-leetcode-s-i2fk/
	来源：力扣（LeetCode）
	著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。

	 */
	/**
	 * initialize your data structure here.
	 */
	private Stack<Integer> minStack;//辅助栈 存储最小值

	private Stack<Integer> stackA;


	public MinStackByDoubleStack() {
		minStack = new Stack<>();
		stackA = new Stack<>();
		minStack.push(Integer.MAX_VALUE);//务必初始化 否则会报空栈异常
	}

	public void push(int x) {
		//每次将x压栈时，都与最小栈的栈顶元素m做对比，如果比m小 那么将x压入最小栈的栈顶
		Integer peek = minStack.peek();
		if (x <= peek) {
			minStack.push(x);
		} else {
			//如果存入的值比最小栈栈顶的元素大，那么该值对应的最小栈元素任然是上一个最小元素
			minStack.push(peek);
		}
		stackA.push(x);
	}

	public void pop() {
		minStack.pop();
		stackA.pop();
	}

	public int top() {
		return stackA.peek();
	}

	public int min() {
		return minStack.peek();
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

