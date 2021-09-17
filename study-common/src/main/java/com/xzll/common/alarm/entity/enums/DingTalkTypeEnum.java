package com.xzll.common.alarm.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: hzz
 * @Date: 2021/9/16 16:43:27
 * @Description: 报警类型
 */
@AllArgsConstructor
@Getter
public enum DingTalkTypeEnum {

	THIRD_API_ERROR("third_api_error", "三方api异常"),
	SELF_API_ERROR("self_api_error", "自有api异常"),
	SYS_EXCEPTION("sys_exception", "系统异常"),
	BIZ_NOTICE("biz_notice", "业务报警"),
	PUBLISH_VIDEO_NOTICE("publish_video","业务通知"),
	;

	private final String code;
	private final String desc;
}
