package com.xzll.common.rabbitmq.eneity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xzll.common.base.BaseBean;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/8/7 17:13
 * @Description:
 */
@Data
@TableName("save_consumer_nack_message")
@Accessors
public class NackConsumerMessageDO extends BaseBean {

	private String message;
	private Integer type;
}
