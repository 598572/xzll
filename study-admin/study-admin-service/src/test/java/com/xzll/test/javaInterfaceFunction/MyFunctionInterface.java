package com.xzll.test.javaInterfaceFunction;


import java.util.Objects;
import java.util.function.Function;

/**
 * 功能类型函数
 * 接受一个结果 返回一个结果
 *
 * @param <T> 传入的
 * @param <R> 返回的
 */
@FunctionalInterface
public interface MyFunctionInterface<T, R> {

	// 接受输入参数，对输入执行所需操作后  返回一个结果。
	R apply(T t);

	//先执行 before 的 apply方法，再执行当前函数对象apply方法的 函数对象。
	default <V> Function<V, R> compose(Function<? super V, ? extends T> before) {
		Objects.requireNonNull(before);
		return (V v) -> apply(before.apply(v));
	}

	// 返回一个 先执行当前函数对象apply方法， 再执行after函数对象apply方法的 函数对象。
	default <V> Function<T, V> andThen(Function<? super R, ? extends V> after) {
		Objects.requireNonNull(after);
		return (T t) -> after.apply(apply(t));
	}

	// 返回一个执行了apply()方法之后只会返回输入参数的函数对象。
	static <T> Function<T, T> identity() {
		return t -> t;
	}
}
