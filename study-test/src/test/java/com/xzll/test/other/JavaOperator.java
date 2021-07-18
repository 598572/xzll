package com.xzll.test.other;

import org.junit.Test;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/7/18 10:01
 * @Description: 差点忘了老本行(java运算符) 哈哈
 */
public class JavaOperator {

    @Test
    public void 加减乘除模以(){

        int a = 10;
        int b = 20;
        int c = 25;
        int d = 25;
        System.out.println("a + b = " + (a + b) );
        System.out.println("a - b = " + (a - b) );
        System.out.println("a * b = " + (a * b) );
        System.out.println("b / a = " + (b / a) );
        System.out.println("b % a = " + (b % a) );
        System.out.println("c % a = " + (c % a) );
        System.out.println("a++   = " +  (a++) );
        System.out.println("a--   = " +  (a--) );
//        // 查看  d++ 与 ++d 的不同
        System.out.println("d++   = " +  (d++) );
        System.out.println("++d   = " +  (++d) );

//        for (int i = 0; i < 100; i ++) {
//            if ((i & 1) == 0) { // 偶数
//                System.out.println(i);
//            }
//        }
    }

    @Test
    public void 自增与自减(){
        int a = 5;//定义一个变量；
        int b = 5;
        int x = 2*++a;
        int y = 2*b++;
        System.out.println("自增运算符前缀运算后a="+a+",x="+x);
        System.out.println("自增运算符后缀运算后b="+b+",y="+y);
    }

    /*

    变量A的值为10，变量B的值为20：

    ＆	如果相对应位都是1，则结果为1，否则为0	（A＆B），得到12，即0000 1100
    |	如果相对应位都是 0，则结果为 0，否则为 1	（A | B）得到61，即 0011 1101
    ^	如果相对应位值相同，则结果为0，否则为1	（A ^ B）得到49，即 0011 0001
    〜	按位取反运算符翻转操作数的每一位，即0变成1，1变成0。	（〜A）得到-61，即1100 0011
    << 	按位左移运算符。左操作数按位左移右操作数指定的位数。	A << 2得到240，即 1111 0000
    >> 	按位右移运算符。左操作数按位右移右操作数指定的位数。	A >> 2得到15即 1111
    >>> 按位右移补零操作符。左操作数的值按右操作数指定的位数右移，移动得到的空位以零填充。	A>>>2得到15即0000 1111

     */
    @Test
    public void 位运算(){
        int a = 60; /* 60 = 0011 1100 */
        int b = 13; /* 13 = 0000 1101 */
        int c = 0;
        c = a & b;       /* 12 = 0000 1100 */
        System.out.println("a & b = " + c );

        c = a | b;       /* 61 = 0011 1101 */
        System.out.println("a | b = " + c );

        c = a ^ b;       /* 49 = 0011 0001 */
        System.out.println("a ^ b = " + c );

        c = ~a;          /*-61 = 1100 0011 */
        System.out.println("~a = " + c );

        c = a << 2;     /* 240 = 1111 0000 */
        System.out.println("a << 2 = " + c );

        c = a >> 2;     /* 15 = 1111 */
        System.out.println("a >> 2  = " + c );

        c = a >>> 2;     /* 15 = 0000 1111 */
        System.out.println("a >>> 2 = " + c );
    }

}
