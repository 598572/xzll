package com.xzll.common.rocketmq;

import com.xzll.common.constant.StudyConstant;
import com.xzll.common.util.TraceIdUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.hook.ConsumeMessageContext;
import org.apache.rocketmq.client.hook.ConsumeMessageHook;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.Iterator;

/**
 * @Author: hzz
 * @Date: 2023/2/28 18:04:20
 * @Description: 消费之前/之后设置的钩子，可以用来处理 执行业务前/后 时候的一些逻辑
 */
public class RocketMqConsumerMessageHook implements ConsumeMessageHook {

	@Override
	public String hookName() {
		return "RocketMqConsumerMessageHook";
	}

	@Override
	public void consumeMessageBefore(ConsumeMessageContext consumeMessageContext) {
		Object mqTraceContext = consumeMessageContext.getMqTraceContext();
		Iterator<MessageExt> iterator = consumeMessageContext.getMsgList().iterator();
		while (iterator.hasNext()) {
			MessageExt messageExt = iterator.next();
			String traceId = messageExt.getUserProperty(StudyConstant.TraceConstant.TRACE_ID);
			if (StringUtils.isNotBlank(traceId)) {
				TraceIdUtil.setTraceId(traceId);
			}
		}
	}


	public static void main(String[] args) {

		String s=null;
		String z="  ";
		String y="";
		String s2="213";
		System.out.println(org.springframework.util.StringUtils.hasText(s));
		System.out.println(org.springframework.util.StringUtils.hasText(z));
		System.out.println(org.springframework.util.StringUtils.hasText(y));
		System.out.println(org.springframework.util.StringUtils.hasText(s2));

	}
	@Override
	public void consumeMessageAfter(ConsumeMessageContext consumeMessageContext) {

	}


}
