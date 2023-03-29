package com.xzll.test.starter;

import cn.hutool.json.JSONUtil;
import com.xzll.pay.PayStarterTest;
import com.xzll.test.StudyTestApplicationTest;
import com.xzll.util.UtilStarterTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 自己定义starter
 */
@Slf4j
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


    	log.info("占位符001:{}","哈哈hzz");



        String s = payStarterTest.testPayStarter();
        String s1 = utilStarterTest.utilStarterTest();
        System.out.println(s);
        System.out.println(s1);
    }




}
