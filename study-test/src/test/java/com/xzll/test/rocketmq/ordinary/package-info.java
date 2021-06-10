/**
 * 普通消息， 包括 同步 ，异步， 单向 消息 目前都已经调通
 */
package com.xzll.test.rocketmq.ordinary;


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