package com.xzll.common.alarm.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum AlarmNoticeMethodEnum {

	NO_NOTICE(0, "不通知", ""),
	SMS(1, "短信通知", "smsAlarmNoticeImpl"),
	EMAIL(2, "邮件通知", "emailAlarmNoticeImpl"),
	DING_DING(3, "钉钉通知", "dingDingAlarmNoticeImpl"),
	WEB_SOCKET(4, "websocket通知", "webSocketAlarmNoticeImpl");

	private int code;
	private String desc;
	private String noticeBeanName;

	public static AlarmNoticeMethodEnum getEnum(int code) {
		AlarmNoticeMethodEnum[] var1 = values();
		int var2 = var1.length;

		for(int var3 = 0; var3 < var2; ++var3) {
			AlarmNoticeMethodEnum noticeMethodEnum = var1[var3];
			if (noticeMethodEnum.code == code) {
				return noticeMethodEnum;
			}
		}

		return NO_NOTICE;
	}
}
