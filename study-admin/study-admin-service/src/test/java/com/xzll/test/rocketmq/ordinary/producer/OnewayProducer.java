package com.xzll.test.rocketmq.ordinary.producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.util.concurrent.CountDownLatch;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/6/10 11:32
 * @Description:
 */
public class OnewayProducer {

    /**
     * 单向发送消息
     *
     * 这种方式主要用在不特别关心发送结果的场景，例如日志发送
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // 实例化消息生产者Producer
        DefaultMQProducer producer = new DefaultMQProducer("hzz");
        // 设置NameServer的地址
        producer.setNamesrvAddr("localhost:9876");
        producer.setSendMsgTimeout(60000);
        producer.setRetryTimesWhenSendAsyncFailed(0);

        // 启动Producer实例
        producer.start();

        for (int i = 0; i < 5; i++) {
            // 创建消息，并指定Topic，Tag和消息体
            Message msg = new Message("TopicTest" /* Topic */,
                    "TagA" /* Tag */,
                    ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
            );
            // 发送单向消息，没有任何返回结果
            producer.sendOneway(msg);
            System.out.println("发送单向消息");

        }
        // 如果不再发送消息，关闭Producer实例。
//        producer.shutdown();
    }
}
