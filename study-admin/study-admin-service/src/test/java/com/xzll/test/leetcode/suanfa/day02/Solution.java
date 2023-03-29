package com.xzll.test.leetcode.suanfa.day02;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * @Author: hzz
 * @Date: 2023/2/1 14:16:24
 * @Description: 剑指 Offer 06. 从尾到头打印链表
 * <p>
 * 输入一个链表的头节点，从尾到头反过来返回每个节点的值（用数组返回）。
 *
 * 思路 ： 利用栈先进后出特性  实现列表的头尾反转
 *
 */
public class Solution {


	private static Stack<Integer> stack = new Stack<Integer>();

	public int[] reversePrint(ListNode head) {
		//递归压栈 压入栈后的栈顶就是列表的尾部元素  栈底是链表的头部元素
		digui(head);

		//遍历栈 从栈顶依次取出元素
		List<Integer> list = new ArrayList<Integer>();
		Iterator<Integer> iterator = stack.iterator();
		while (iterator.hasNext()){
			list.add(stack.pop());
		}

		//转成int数组
		int[] d = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			d[i] = list.get(i);
		}

		return d;
	}

	private void digui(ListNode head) {
		if (null != head) {
			stack.push(head.val);
			if (head.next != null) {
				digui(head.next);
			}
		}
	}


	public class ListNode {
		int val;
		ListNode next;

		ListNode(int x) {
			val = x;
		}
	}

	public static void main(String[] args) {

	}

}
