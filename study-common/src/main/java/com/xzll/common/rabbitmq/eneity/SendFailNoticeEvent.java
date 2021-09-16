package com.xzll.common.rabbitmq.eneity;

import lombok.Data;

import java.util.Map;

/**
 * @Author: hzz
 * @Date: 2021/9/16 16:42:54
 * @Description:
 */
@Data
public class SendFailNoticeEvent {
	private static final long serialVersionUID = 260062637154297413L;
	/**紧急程度，0-必须要处理的，1-短时间内可以容忍的，2-需要关注的，3-可以忽略，但影响体验效果的*/
	private Integer level;
	private String errorMsg;
	private DingTalkTypeEnum talkTypeEnum;
	private Map<String, String> otherParam;
}
