/**
 * 延迟消息使用场景举例
 * <p>
 * 比如电商里，提交了一个订单就可以发送一个延时消息，1h后去检查这个订单的状态，如果还是未付款就取消订单释放库存
 * <p>
 * <p>
 * 延时消息的使用限制
 * // org/apache/rocketmq/store/config/MessageStoreConfig.java
 * <p>
 * private String messageDelayLevel = "1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h";
 * 现在RocketMq并不支持任意时间的延时，需要设置几个固定的延时等级，从1s到2h分别对应着等级1到18 消息消费失败会进入延时消息队列，消息发送时间与设置的延时等级和重试次数有关，详见代码SendMessageProcessor.java
 *
 * 6 消息事务样例
 * 事务消息共有三种状态，提交状态、回滚状态、中间状态：
 *
 * TransactionStatus.CommitTransaction: 提交事务，它允许消费者消费此消息。
 * TransactionStatus.RollbackTransaction: 回滚事务，它代表该消息将被删除，不允许被消费。
 * TransactionStatus.Unknown: 中间状态，它代表需要检查消息队列来确定状态。
 * 6.1 发送事务消息样例
 * 1、创建事务性生产者
 * 使用 TransactionMQProducer类创建生产者，并指定唯一的 ProducerGroup，就可以设置自定义线程池来处理这些检查请求。执行本地事务后、需要根据执行结果对消息队列进行回复。回传的事务状态在请参考前一节。
 *
 *
 */
package com.xzll.test.rocketmq.delay;