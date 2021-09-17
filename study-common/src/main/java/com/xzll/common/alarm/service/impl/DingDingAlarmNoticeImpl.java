package com.xzll.common.alarm.service.impl;

import com.xzll.common.alarm.entity.dto.DingTalkRobotBeanDTO;
import com.xzll.common.alarm.service.AlarmNotice;
import com.xzll.common.util.DingTalkUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: hzz
 * @Date: 2021/9/16 18:39:21
 * @Description: 钉钉发送消息实现类
 */
@Slf4j
@Service
public class DingDingAlarmNoticeImpl implements AlarmNotice {


	@Override
	public void sendAlarmNotice(List<String> noticeAccountList, String title, String noticeContent, Map<String, String> otherParam) {

		for (String account : noticeAccountList) {
			int index = account.indexOf(".");
			if (index < 0) {
				log.warn("告警发送失败，请指定accessToken和报文密钥");
				return;
			}
			String token = account.substring(0, index);
			String secret = account.substring(index + 1);
			DingTalkRobotBeanDTO dingTalkRobotBean = new DingTalkRobotBeanDTO();
			dingTalkRobotBean.setContent(noticeContent);
			dingTalkRobotBean.setTitle(title);
			dingTalkRobotBean.setAccessToken(token);
			dingTalkRobotBean.setSecret(secret);
			DingTalkUtil.sendDingTalkRobot(dingTalkRobotBean);
		}
	}
}
