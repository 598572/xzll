package com.xzll.common.rabbitmq.config;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.alibaba.fastjson.JSONObject;
import com.xzll.common.rabbitmq.eneity.DingTalkTypeEnum;
import com.xzll.common.rabbitmq.eneity.SendFailNoticeEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: hzz
 * @Date: 2021/9/16 16:48:22
 * @Description: 事件监听器
 */
@Component
@Slf4j
public class NoticeEventListener {


	/**
	 * 异步监听报警事件
	 *
	 * @param event
	 */
	@Async
	@EventListener(classes = SendFailNoticeEvent.class)
	public void sendFailNotice(SendFailNoticeEvent event) {
		Integer level = event.getLevel();
		String errorMsg = event.getErrorMsg();
		final DingTalkTypeEnum talkTypeEnum = event.getTalkTypeEnum();

		Map<String, String> noticeParams = new HashMap<>(8);

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
		//TODO
	}
}
