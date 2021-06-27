package com.xzll.test.javajuc;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/6/25 14:04
 * @Description:
 */
public class JucCommonTest {

    /**
     * 获取当前线程id以及名称
     *
     * @return
     */
    public static String getCurrentThreadName() {
//        try {
//            Thread.sleep(500000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return "<<<当前线程ID: " + Thread.currentThread().getId()
                + " ; 当前线程名称: " + Thread.currentThread().getName() + " >>>  输出: \n";
    }
}
