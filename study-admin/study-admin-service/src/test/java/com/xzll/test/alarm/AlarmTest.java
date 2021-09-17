package com.xzll.test.alarm;

import com.xzll.common.alarm.entity.enums.DingTalkTypeEnum;
import com.xzll.common.alarm.entity.dto.SendFailNoticeEventDTO;
import com.xzll.test.StudyTestApplicationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

/**
 * @Author: hzz
 * @Date: 2021/9/17 15:51:28
 * @Description:
 */
public class AlarmTest extends StudyTestApplicationTest {

	@Autowired
	private ApplicationEventPublisher publisher;


	@Test
	public void test() {
		SendFailNoticeEventDTO noticeEvent = new SendFailNoticeEventDTO();
		noticeEvent.setLevel(1);
		noticeEvent.setErrorMsg(
				System.lineSeparator() +
						"Dear 王小姐 " + "： 你是我今生的唯一 I love you !!! ");
		noticeEvent.setTalkTypeEnum(DingTalkTypeEnum.BIZ_NOTICE);
		//发送消息投递失败事件，监听器方将信息发送至钉钉机器人群里或者是某个具体的人。
		publisher.publishEvent(noticeEvent);
		try {
			Thread.sleep(5000000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
