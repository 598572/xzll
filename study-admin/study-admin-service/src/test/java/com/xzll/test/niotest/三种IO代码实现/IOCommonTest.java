package com.xzll.test.niotest.三种IO代码实现;

import org.junit.Before;
import org.junit.Test;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/6/20 08:00
 * @Description: IO性能测试
 * <p>
 * 1.测试三种方式的写性能
 * 启动 testWrite() 方法即可
 * <p>
 * 2.测试传统方式 sendfile mmap 方式的copy性能
 * <p>
 * 启动 testCopy() 方法即可
 */
public class IOCommonTest {

    public static String CONTENT = "IO测试数据";
    public static final String CHARSET = "UTF-8";

    /**
     * hook 写数据钩子 (子类实现)
     */
    public void writeTest() {
    }

    /**
     * hook 根据子类的文件名创建文件(子类实现)
     */
    public void createFileNotExist() {
    }

    /**
     * 造数据
     */
    @Before
    public void makeData() {
        StringBuilder initData = new StringBuilder();
        for (int i = 0; i < 1000000; i++) {
            initData.append("哈哈哈哈哈哈哈哈1哈哈哈哈哈哈哈1哈哈哈哈哈00111111111哈哈哈哈哈哈1哈哈哈哈哈哈哈哈1哈哈哈哈哈哈哈哈哈哈哈哈1哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈");
        }
        CONTENT = initData.toString();
    }

    /**
     * 测试三种IO的写性能
     */
    @Test
    public void testWrite() {
        System.out.println("-----------测试三种IO的写性能开始----------");
        System.out.println();

        long l = System.currentTimeMillis();
        FileChannelTest fileChannelTest = new FileChannelTest();
        fileChannelTest.createFileNotExist();
        fileChannelTest.writeTest();

        System.out.println();
        System.out.println("---------sendfile IO总用时(包含文件读取时长 不具有参考价值) " + (System.currentTimeMillis() - l) + " ms ------------");
        System.out.println();

        long l1 = System.currentTimeMillis();
        MmapTest mmapTest = new MmapTest();
        mmapTest.createFileNotExist();
        //这里不不含force刷到磁盘 因为那样的话 会和普通的IO效率差不多了 所以在使用mmap时候 force不能高频调用 只有在必须时候才调用他
        //想看force耗时 直接把writeTest方法中force的操作放开就好了
        mmapTest.writeTest();

        System.out.println();
        System.out.println("---------mmap IO总用时(包含文件读取时长 不具有参考价值) " + (System.currentTimeMillis() - l1) + " ms ------------");
        System.out.println();

        long l2 = System.currentTimeMillis();
        NormalIOTest normalIOTest = new NormalIOTest();
        normalIOTest.createFileNotExist();
        normalIOTest.writeTest();

        System.out.println();
        System.out.println("---------传统IO总用时(包含文件读取时长 不具有参考价值) " + (System.currentTimeMillis() - l2) + " ms ------------");
        System.out.println();
    }

    /**
     * 测试三种io( 传统IO , mmap ， sendfile )的拷贝性能
     *
     * @throws Exception
     */
    @Test
    public void testCopy() throws Exception {
        System.out.println("-----------测试三种IO的拷贝性能---开始----------");
        System.out.println();

        long l = System.currentTimeMillis();
        FileChannelTest fileChannelTest = new FileChannelTest();
        fileChannelTest.testCopy();

        System.out.println();
        System.out.println("---------transferTo 拷贝文件总用时(包含文件读取时长 不具有参考价值) " + (System.currentTimeMillis() - l) + " ms ------------");
        System.out.println();

        long l1 = System.currentTimeMillis();
        MmapTest mmapTest = new MmapTest();
        mmapTest.testCopy();

        System.out.println();
        System.out.println("---------mmap 拷贝文件总用时(包含文件读取时长 不具有参考价值) " + (System.currentTimeMillis() - l1) + " ms ------------");
        System.out.println();

        long l2 = System.currentTimeMillis();
        NormalIOTest normalIOTest = new NormalIOTest();
        normalIOTest.testCopy();

        System.out.println();
        System.out.println("---------传统IO 拷贝文件总用时(包含文件读取时长 不具有参考价值) " + (System.currentTimeMillis() - l2) + " ms ------------");
        System.out.println();


        /*
        我测试了N遍  发现偶尔传统IO也会快 不知道为什么(可能是进程占用?) 不过大部分情况都是mmap 和 sendfile快些

        -----------测试三种IO的拷贝性能---开始----------

        拷贝前: 文件大小 : 0
        transferTo 拷贝文件用时: 200 ms 文件大小 226000000 byte

        ---------transferTo 拷贝文件总用时(包含文件读取时长 不具有参考价值) 206 ms ------------

        拷贝前: 文件大小 : 0
        mmap 拷贝文件用时: 236 ms 文件大小 226000000 byte

        ---------mmap 拷贝文件总用时(包含文件读取时长 不具有参考价值) 240 ms ------------

        拷贝前: 文件大小 :0
        传统IO 拷贝用时: 779 ms 文件大小:226000000 byte

        ---------传统IO 拷贝文件总用时(包含文件读取时长 不具有参考价值) 808 ms ------------


         */

    }

}
