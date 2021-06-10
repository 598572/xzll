package com.xzll.test.rocketmq.delay;

import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/6/10 11:43
 * @Description:
 */
public class ScheduledMessageProducer {
    public static void main(String[] args) throws Exception {
        // 实例化一个生产者来产生延时消息
        DefaultMQProducer producer = new DefaultMQProducer("ExampleProducerGroup");

        producer.setNamesrvAddr("localhost:9876");
        producer.setSendMsgTimeout(60000);

        // 启动生产者
        producer.start();
        int totalMessagesToSend = 10;
        for (int i = 0; i < totalMessagesToSend; i++) {
            Message message = new Message("DelayMessageTopic", ("Hello scheduled message " + i).getBytes());
            // 设置延时等级3,这个消息将在10s之后发送(现在只支持固定的几个时间,详看delayTimeLevel)
            message.setDelayTimeLevel(3);
            // 发送延迟消息
            SendResult send = producer.send(message);
            if (StringUtils.isNotBlank(send.getMsgId())) {
                System.out.println("延迟消息发送成功");
                System.out.println(send.toString());
            } else
                System.out.println("延迟消息发送失败");
        }
        // 关闭生产者
//        producer.shutdown();
    }
}
