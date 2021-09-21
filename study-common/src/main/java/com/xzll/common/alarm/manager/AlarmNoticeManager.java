package com.xzll.common.alarm.manager;

import com.xzll.common.alarm.config.AlarmNoticeConfig;
import com.xzll.common.alarm.config.NoticeMethodConfig;
import com.xzll.common.alarm.entity.enums.AlarmNoticeMethodEnum;
import com.xzll.common.alarm.service.AlarmNotice;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author: hzz
 * @Date: 2021/9/16 18:37:16
 * @Description: 报警中心 （该类的 sendAlarmNotice 为对外提供的发送报警信息的接口，具体的发送逻辑，由AlarmNotice接口的实现者去做）
 * 另外 : sendAlarmNotice其实只是 根据 传入的 AlarmNoticeConfig 然后找到对应的bean 并调用 sendAlarmNotice方法 而已。
 */
@Slf4j
@Service
public class AlarmNoticeManager {

	//注入AlarmNotice接口的bean
//	@Autowired
//	private Map<String, AlarmNotice> beans = new HashMap();

	/**
	 * 发送报警信息
	 *
	 * @param noticeConfig 通知配置类的载体
	 * @param tmpParams 通知的内容
	 * @param otherParam 其他的一些内容
	 */
	public void sendAlarmNotice(AlarmNoticeConfig noticeConfig, Map<String, String> tmpParams, Map<String, String> otherParam) {
		if (Objects.isNull(noticeConfig)) {
			log.warn("告警通知放弃，没有需要通知的配置信息");
		} else {
			List<NoticeMethodConfig> noticeMethodConfigList = noticeConfig.getNoticeMethodConfigList();
			if (CollectionUtils.isEmpty(noticeMethodConfigList)) {
				log.warn("没有配置接收方式和接收人，通知放弃");
			} else {
				String noticeContent = replaceTemplate(noticeConfig.getAlarmNoticeTemplate(), tmpParams);
				String title = noticeConfig.getTitle();
				noticeMethodConfigList.forEach(methodConfig->{
					//找到对应的bean
					AlarmNotice alarmNotice = findAlarmNoticeImpl(methodConfig);
					if (!Objects.isNull(alarmNotice)) {
						List<String> noticeAccountList = methodConfig.getNoticeAccountList();
						if (!CollectionUtils.isEmpty(noticeAccountList)) {
							alarmNotice.sendAlarmNotice(noticeAccountList, title, noticeContent, otherParam);
						}
					}
				});
			}
		}
	}
	//替换模板中的${}
	private static String replaceTemplate(String template, Map<String, String> argMap) {
		if (!Objects.isNull(argMap) && argMap.size() >= 1) {
			Map.Entry arg;
			String oldChar;
			for (Iterator var2 = argMap.entrySet().iterator(); var2.hasNext(); template = template.replace(oldChar, (CharSequence) arg.getValue())) {
				arg = (Map.Entry) var2.next();
				oldChar = "${" + (String) arg.getKey() + "}";
			}
			return template;
		} else {
			return template;
		}
	}

	/**
	 * 根据配置的NoticeMethodConfig 获取对应的报警具体实现类
	 *
	 * @param methodConfig
	 * @return
	 */
	private AlarmNotice findAlarmNoticeImpl(NoticeMethodConfig methodConfig) {
		String beanName = methodConfig.getAlarmNoticeBeanName();
		AlarmNoticeMethodEnum anEnum = methodConfig.getAlarmNoticeMethodEnum();
		if (StringUtils.isEmpty(beanName) && Objects.nonNull(anEnum) && !AlarmNoticeMethodEnum.NO_NOTICE.equals(anEnum)) {
			//获取对应的bean名称
			beanName = anEnum.getNoticeBeanName();
		}
		AlarmNotice alarmNotice = null;
		if (StringUtils.isEmpty(beanName)) {
			log.warn("通知放弃，没有指定通知方式");
			return null;
		} else {
//			if (Objects.isNull(beans) || beans.isEmpty()) {
//				return null;
//			}
			//根据传入的beanName找到对应的实现类
//			for (Map.Entry<String, AlarmNotice> entry : beans.entrySet()) {
//				if (Objects.equals(entry.getKey(), beanName)) {
//					alarmNotice = entry.getValue();
//				} else {
//					log.warn("通知放弃，没有找到指定通知方式 beanName ：{},beans:{}", beanName, beans);
//				}
//			}
		}
		return alarmNotice;
	}
}
