package com.xzll.test.javaInterfaceFunction;

import org.junit.Test;

import java.util.function.Function;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/7/18 15:54
 * @Description: TODO 暂时没整理完。。。。。
 */
public class FunctionDemo {


	private int modifyTheValue(int valueToBeOperated, MyFunctionInterface<Integer, Integer> myFunctionInterface) {
		return myFunctionInterface.apply(valueToBeOperated);
	}

	@Test
	public void applyTest() {
		int myNumber = 10;

		// 使用lambda表达式实现函数式接口
		// (x)->(x)+20 输入一个参数x，进行加法运算，返回一个结果
		new MyFunctionInterface() {
			@Override
			public Object apply(Object o) {
				return null;
			}

			@Override
			public Function compose(Function before) {
				return null;
			}
		};


		//  使用匿名内部类实现
		int res2 = modifyTheValue(myNumber, new MyFunctionInterface<Integer, Integer>() {
			@Override
			public Integer apply(Integer t) {
				return t + 20;
			}
		});
		System.out.println(res2); // 30
	}

	@Test
	public void composeTest() {
		int myNumber = 10;

		// 使用lambda表达式实现函数式接口
		// (x)->(x)+20 输入一个参数x，进行加法运算，返回一个结果
		int res1 = modifyTheValue(myNumber, (x) -> x + 20);
		System.out.println(res1); // 30

		//  使用匿名内部类实现
		int res2 = modifyTheValue(myNumber, new MyFunctionInterface<Integer, Integer>() {
			@Override
			public Integer apply(Integer t) {
				return t + 20;
			}
		});
		System.out.println(res2); // 30
	}
}
