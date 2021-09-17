package com.xzll.common.alarm.listener;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.xzll.common.alarm.manager.AlarmNoticeManager;
import com.xzll.common.alarm.config.AlarmNoticeConfig;
import com.xzll.common.alarm.config.DingTalkNoticeConfig;
import com.xzll.common.alarm.entity.enums.DingTalkTypeEnum;
import com.xzll.common.alarm.entity.dto.SendFailNoticeEventDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: hzz
 * @Date: 2021/9/16 16:48:22
 * @Description: 事件监听器(本事件监听器只是监听发送失败的事件，一般我们在GlobalExceptionHandler 这个类中去发布(系统异常)事件)  实际中，可能会有很多个监听器，比如业务上失败的监听器，等等等等
 */
@Component
@Slf4j
public class NoticeEventListener {

	/**
	 * 注入钉钉通知的配置类  包括: (通知的模板，标题，通知类型，实现者的bean名称，和接收人)
	 */
	@Autowired
	private DingTalkNoticeConfig dingTalkNoticeConfig;
	/**
	 * 注入报警中心bean
	 */
	@Autowired
	private AlarmNoticeManager alarmNoticeManager;

	/**
	 * 异步监听报警事件
	 *
	 * @param event
	 */
	@Async
	@EventListener(classes = SendFailNoticeEventDTO.class)
	public void sendFailNotice(SendFailNoticeEventDTO event) {
		Integer level = event.getLevel();
		String errorMsg = event.getErrorMsg();
		DingTalkTypeEnum talkTypeEnum = event.getTalkTypeEnum();

		//获取钉钉提醒的配置信息 (从配置文件中)
		AlarmNoticeConfig noticeConfig = dingTalkNoticeConfig.getNoticeConfig(talkTypeEnum);
		if (Objects.isNull(noticeConfig)) {
			log.info("报警通知放弃，没有配置报警信息");
		}

		Map<String, String> noticeParams = new HashMap<>();

		String levelStr;

		switch (level) {
			case 0:
				levelStr = "【非常紧急】";
				break;
			case 1:
				levelStr = "【紧急】";
				break;
			case 2:
				levelStr = "【关注】";
				break;
			default:
				levelStr = "【一般】";
		}

		noticeParams.put("level", levelStr);
		noticeParams.put("type", talkTypeEnum.getDesc());
		noticeParams.put("noticeTime", LocalDateTimeUtil.format(LocalDateTime.now(), DatePattern.NORM_DATETIME_PATTERN));
		noticeParams.put("errorMsg", errorMsg);
		log.info("发送报警日志：{}", JSONObject.toJSONString(noticeParams));
		//使用报警中心发送信息
		alarmNoticeManager.sendAlarmNotice(noticeConfig, noticeParams, Maps.newHashMap());
	}
}
