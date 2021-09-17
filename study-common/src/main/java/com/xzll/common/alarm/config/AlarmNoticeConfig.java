package com.xzll.common.alarm.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @Author: hzz
 * @Date: 2021/9/17 15:24:35
 * @Description:  父级是
 * @see DingTalkNoticeConfig
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AlarmNoticeConfig {
	/**
	 * 通知模板
	 */
	private String alarmNoticeTemplate = "${content}";
	/**
	 * 通知的title
	 */
	private String title;
	/**
	 * 通知方式，具体实现类，以及通知到谁的配置类
	 */
	private List<NoticeMethodConfig> noticeMethodConfigList;
}
