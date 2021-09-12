package com.xzll.common.rabbitmq.eneity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xzll.common.base.BaseBean;
import lombok.Data;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/8/7 17:13
 * @Description:
 */
@Data
@TableName("save_producer_nack_message")
public class NackProducerMessageDO extends BaseBean {

	private String correlationData;
	private Integer ack;
	private String cause;
}
