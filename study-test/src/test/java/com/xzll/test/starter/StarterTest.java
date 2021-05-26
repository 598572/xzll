package com.xzll.test.starter;

import com.xzll.pay.PayStarterTest;
import com.xzll.test.StudyTestApplicationTest;
import com.xzll.util.UtilStarterTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 自己定义starter
 */

public class StarterTest extends StudyTestApplicationTest {


    @Autowired
    private UtilStarterTest utilStarterTest;
    @Autowired
    private PayStarterTest payStarterTest;

    /**
     * starter测试
     */
    @Test
    public void testStarter() {
        String s = payStarterTest.testPayStarter();
        String s1 = utilStarterTest.utilStarterTest();
        System.out.println(s);
        System.out.println(s1);
    }

}
