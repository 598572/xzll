package com.xzll.common.alarm.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum AlarmNoticeTypeEnum {


	SMS("sms", "暂无"),
	EMAIL("email", "com.xzll.common.alarm.service.impl.EmailAlarmNoticeImpl"),
	DING_DING("ding_ding", "com.xzll.common.alarm.service.impl.DingDingAlarmNoticeImpl");

	private String type;
	private String classFullName;

	public static String getClassFullName(String type) {
		AlarmNoticeTypeEnum[] var1 = values();
		int var2 = var1.length;

		for (int var3 = 0; var3 < var2; ++var3) {
			AlarmNoticeTypeEnum alarmNoticeTypeEnum = var1[var3];
			if (Objects.equals(alarmNoticeTypeEnum.type, type)) {
				return alarmNoticeTypeEnum.getClassFullName();
			}
		}
		return StringUtils.EMPTY;
	}
}
