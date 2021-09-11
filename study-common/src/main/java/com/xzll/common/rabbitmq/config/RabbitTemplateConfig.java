package com.xzll.common.rabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.PostConstruct;

/**
 * @Author: hzz
 * @Date: 2021/9/10 13:09:58
 * @Description: 自定义确认机制
 */
@Slf4j
public class RabbitTemplateConfig implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnCallback{

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;


    @PostConstruct
    public void init(){
        rabbitTemplate.setConfirmCallback(this::confirm);
        rabbitTemplate.setReturnCallback(this::returnedMessage);
    }


	/**
	 *
	 * 生产者 确认消息的配置
	 *
	 * @param correlationData
	 * @param ack
	 * @param cause
	 */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        //发送成功
        if(ack){
            //不做处理，等待消费成功
            log.info(correlationData.getId()+"：发送成功");
        }else{
            //持久化到数据库
            log.error(correlationData.getId()+"：发送失败");
            log.info("备份内容："+redisTemplate.opsForValue().get(correlationData.getId()));
            try {

            }catch (Exception e){
                log.error("记录mq发送端错误日志失败",e);
            }
        }
        //不管成功与否读删除redis里面备份的数据
        redisTemplate.delete(correlationData.getId());
    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.error("消息主体 message : "+message);
        log.error("描述："+replyText);
        log.error("消息使用的交换器 exchange : "+exchange);
        log.error("消息使用的路由键 routing : "+routingKey);
    }

}
