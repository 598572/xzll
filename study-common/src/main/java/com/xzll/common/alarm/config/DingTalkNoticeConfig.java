package com.xzll.common.alarm.config;

import com.xzll.common.alarm.entity.enums.DingTalkTypeEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Objects;

/**
 * @Author: hzz
 * @Date: 2021/9/17 15:20:07
 * @Description:  在配置文件中，需要以下这样配置。从而找到通知的模板，标题，通知类型，实现者的bean名称，和通知的接收人
 *
 * notice.dingtalk.dingtalkMap[biz_notice].alarmNoticeTemplate=\u7cfb\u7edf\u62a5\u8b66\uff0c\u8bf7\u68c0\u67e5\uff0c\u62a5\u8b66\u7c7b\u578b\uff1a\u0024\u007b\u0074\u0079\u0070\u0065\u007d\u002c\u7d27\u6025\u7a0b\u5ea6\u003a\u0024\u007b\u006c\u0065\u0076\u0065\u006c\u007d\u0020\u002c\u0020\u62a5\u8b66\u65f6\u95f4\u003a\u0024\u007b\u006e\u006f\u0074\u0069\u0063\u0065\u0054\u0069\u006d\u0065\u007d\u002c\u4fe1\u606f\u003a\u0024\u007b\u0065\u0072\u0072\u006f\u0072\u004d\u0073\u0067\u007d
 * notice.dingtalk.dingtalkMap[biz_notice].title=say_love_you
 * notice.dingtalk.dingtalkMap[biz_notice].noticeMethodConfigList[0].alarmNoticeMethodEnum=ding_ding
 * notice.dingtalk.dingtalkMap[biz_notice].noticeMethodConfigList[0].noticeAccountList=301f4359fc01c72877313017e476253e08034d8fe1502ad45087ebf918ef4259.SECbb39266b5798ba87038a9110c459d2074ab3c8b1ed99907a47f8890086d2b9c020a
 * notice.dingtalk.dingtalkMap[biz_notice].noticeMethodConfigList[0].alarmNoticeBeanName=dingDingAlarmNoticeImpl
 *
 *
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "notice.dingtalk")
public class DingTalkNoticeConfig {

	private Map<String, AlarmNoticeConfig> dingtalkMap;
	private boolean enabled = true;

	public AlarmNoticeConfig getNoticeConfig(DingTalkTypeEnum talkTypeEnum){
		if(this.isEnabled()){
			final Map<String, AlarmNoticeConfig> dingtalkMap = this.getDingtalkMap();
			if(Objects.nonNull(dingtalkMap)){
				return dingtalkMap.get(talkTypeEnum.getCode());
			}
		}
		return null;
	}

}
