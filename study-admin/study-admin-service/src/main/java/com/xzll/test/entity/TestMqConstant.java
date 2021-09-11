package com.xzll.test.entity;


/**
 * @Author: hzz
 * @Date: 2021/9/10 12:53:33
 * @Description: 常量类 为了测试RabbitMQ
 */
public class TestMqConstant {
    /**
     * 测试直连型交换机
	 */
	public static final String TEST_ZHILIAN_EX = "xzll.admin.test.zhilian.ex";
    public static final String TEST_ZHILIAN_Q = "xzll.admin.test.zhilian.q";
    public static final String TEST_ZHILIAN_RK = "xzll.admin.test.zhilian.rk";
    //对应的死信交换机 队列 routingkey
	public static final String TEST_DEAD_LETTER_EXCHANGE = "test-dead-letter-exchange";
	public static final String TEST_DEAD_LETTER_QUEUE = "test-dead-letter-queue";
	public static final String TEST_DEAD_LETTER_ROUTING_KEY = "test-dead-letter-routing-key";



}
