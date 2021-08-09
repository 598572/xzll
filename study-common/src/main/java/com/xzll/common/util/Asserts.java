package com.xzll.common.util;


import com.xzll.common.base.XzllBaseException;

import java.util.Objects;

public class Asserts {

    /**
     * 断定对象不是null 若是 抛异常
     *
     * @param object
     * @param msg
     */
    public static void isNotNull(Object object, String msg) {
        if (object == null) {
            throw new XzllBaseException(msg);
        }
    }

    public static void isNotNullMany(String msg, Object... object) {
        if (object == null) {
            throw new XzllBaseException(msg);
        }
    }


    /**
     * 断定字符串不是null或者 “”  若是 抛出异常
     *
     * @param str
     * @param msg
     */
    public static void isNotNullOrEmpty(String str, String msg) {
        if (str == null || Objects.equals(str.trim(), "")) {
            throw new XzllBaseException(msg);
        }
    }

    /**
     * 断定表达式成立
     *
     * @param expression
     * @param msg
     */
    public static void isTrue(boolean expression, String msg) {
        if (!expression) {
            throw new XzllBaseException(msg);
        }
    }
}
