package com.xzll.test.mianshi;

/**
 * @Author: hzz
 * @Date: 2023/2/13 09:16:50
 * @Description:
 */
public class StringEqualsTest {



    public static void main(String[] args) {
        String s1 = "a";
        String s2 = "b";
        String s3 = "ab";


        String s4 = "ab";
        System.out.println("s3==s4? "+ (s3==s4));

        String s5 = "a"+"b";
        System.out.println("s3==s5? "+ (s3==s5));

        String s6 = s1+s2;
        System.out.println("s3==s6? "+ (s3==s6));

        String s7 = new String("ab");
        System.out.println("s3==s7? "+ (s3==s7));

        final String s8 = "a" ;
        final String s9 = "b" ;
        String s10 = s8 + s9;
        System.out.println("s3==s10? "+ (s3==s10));



        /*
        结果:
        s3==s4? true
        s3==s5? true
        s3==s6? false
        s3==s7? false
        s3==s10? true

         */


        /*

        java中的字符串。 String类是不可变的,对String类的任何改变,都是返回一个新的String类对象。 String 对象是 System.Char 对象的有序集合，用于表示字符串。String 对象的值是该有序集合的内容，并且该值是不可变的。

        特别注意：String类是不可变(final)的,对String类的任何改变,都是返回一个新的String类对象.这样的话把String类的引用传递给一个方法,该方法对String的任何改变,对原引用指向的对象没有任何影响,这一点和基本数据类型相似.

        String池：String是不可改变的，为了提高效率Java引用了字符串池的概念，例如new String("abc");首先会在String池中创建一个对象“abc”因为有NEW的 存在所以会分配地址空间copyString池的内容。当出现的String对象在String池中不存在时即在String池中创建该对象。

        s3与s4根据String的概念他们都指向了同一个缓冲池内的地址，所以结果为true

        s3与s5因为相加的两个为常量所以编译器会把s5="a"+"b"优化为s5="ab"。所以结果也为true。

        s3与s6因为是两个变量的相加所以编译器无法优化，s1+s2即等同于(new StringBuilder(String.valueOf(s1))).append(s2).toString(); 在运行时，会有新的String地址空间的分配，而不是指向缓冲池中的“ab”。所以结果false。

        s3与s7，根据缓冲池的定义在new的时候实际会新分配地址空间，s7指向的是新分配的地址空间所以与缓冲池地址不同，所以为false

        s3与s10，类似于s3与s5，因为是final类型编译器进行了优化所以相同。

         */
    }
}
