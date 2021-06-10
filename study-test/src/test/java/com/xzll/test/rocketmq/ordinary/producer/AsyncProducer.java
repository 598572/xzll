package com.xzll.test.rocketmq.ordinary.producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.CountDownLatch2;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.util.concurrent.TimeUnit;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/6/10 11:31
 * @Description: 发送异步消息
 */
public class AsyncProducer {

    /**
     * 异步消息通常用在对响应时间敏感的业务场景，即发送端不能容忍长时间地等待Broker的响应。
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // 实例化消息生产者Producer
        DefaultMQProducer producer = new DefaultMQProducer("please_rename_unique_group_name");
        // 设置NameServer的地址
        producer.setNamesrvAddr("localhost:9876");

        producer.setSendMsgTimeout(60000);
        producer.setRetryTimesWhenSendAsyncFailed(0);

        // 启动Producer实例
        producer.start();

        int messageCount = 100;
        // 根据消息数量实例化倒计时计算器
        final CountDownLatch2 countDownLatch = new CountDownLatch2(messageCount);
        for (int i = 0; i < messageCount; i++) {
            final int index = i;
            // 创建消息，并指定Topic，Tag和消息体
            Message msg = new Message("TopicTest",
                    "TagA",
                    "OrderID188",
                    "Hello world".getBytes(RemotingHelper.DEFAULT_CHARSET));
            // SendCallback接收异步返回结果的回调
            producer.send(msg, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.printf("%-10d OK %s %n", index,
                            sendResult.getMsgId());
                }

                @Override
                public void onException(Throwable e) {
                    System.out.printf("%-10d Exception %s %n", index, e);
                    e.printStackTrace();
                }
            });
        }
        // 等待5s
        countDownLatch.await(5, TimeUnit.SECONDS);
        // 如果不再发送消息，关闭Producer实例。
        //producer.shutdown();

        /**
         * 官方给的示例你以为下载后就能run ？？？？ 太年轻 太天真了 记录下踩坑过程
         * 如下
         */

         /*
       坑1: 不能立马 producer.shutdown()  没前发送呢你就producer.shutdown()了 ？那我还发个毛线
        如果  producer.shutdown();  那么会报错
        org.apache.rocketmq.client.exception.MQClientException: The producer service state not OK, SHUTDOWN_ALREADY
        See http://rocketmq.apache.org/docs/faq/ for further details.
            at org.apache.rocketmq.client.impl.producer.DefaultMQProducerImpl.makeSureStateOK(DefaultMQProducerImpl.java:413)
            at org.apache.rocketmq.client.impl.producer.DefaultMQProducerImpl.sendDefaultImpl(DefaultMQProducerImpl.java:522)
            at org.apache.rocketmq.client.impl.producer.DefaultMQProducerImpl.access$300(DefaultMQProducerImpl.java:89)
            at org.apache.rocketmq.client.impl.producer.DefaultMQProducerImpl$3.run(DefaultMQProducerImpl.java:490)
            at java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:511)
            at java.util.concurrent.FutureTask.run(FutureTask.java:266)
            at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
            at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
            at java.lang.Thread.run(Thread.java:748)
       坑2：注意 producer.setSendMsgTimeout(60000); 这个东西不设置的话 会报错
        org.apache.rocketmq.remoting.exception.RemotingTooMuchRequestException: DEFAULT ASYNC send call timeout
        at org.apache.rocketmq.client.impl.producer.DefaultMQProducerImpl$3.run(DefaultMQProducerImpl.java:495)
        at java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:511)
        at java.util.concurrent.FutureTask.run(FutureTask.java:266)
        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
        at java.lang.Thread.run(Thread.java:748)

       坑3：
        版本问题
        还有个大坑 就是版本号 目前我是使用的 4.8.0服务端 ，客户端如果高于4.6.0的话 会报错 关键字如下

        RocketMQ invokeSync call timeout
        后来改成4.5.0就可以了 呵呵呵呵呵呵
        解决该问题是参考了 https://blog.csdn.net/u011442726/article/details/106859771 这个博文 在此谢谢!
         */
    }

}
