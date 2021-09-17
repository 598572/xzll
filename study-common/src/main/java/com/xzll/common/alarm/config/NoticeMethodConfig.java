package com.xzll.common.alarm.config;


import com.xzll.common.alarm.entity.enums.AlarmNoticeMethodEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @Author: hzz
 * @Date: 2021/9/17 15:22:12
 * @Description: 父级是
 * @see AlarmNoticeConfig
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NoticeMethodConfig {
	//指定通知类型
	private AlarmNoticeMethodEnum alarmNoticeMethodEnum;
	//指定通知的实现类
	private String alarmNoticeBeanName;
	//指定接收人，这里我们指定为钉钉机器人
	private List<String> noticeAccountList;
}
