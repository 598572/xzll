package com.xzll.common.alarm.service.impl;

import com.xzll.common.alarm.service.AlarmNotice;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * @Author: hzz
 * @Date: 2021/9/16 18:39:21
 * @Description: 邮件发送消息实现类
 */
@Slf4j
//@Service
public class EmailAlarmNoticeImpl implements AlarmNotice {

	@Override
	public void sendAlarmNotice(List<String> noticeAccountList, String title, String noticeContent, Map<String, String> otherParam) {
		log.info("发送报警信息 (email邮件的方式) ");//TODO 因为没用到 暂时先空着
	}
}
