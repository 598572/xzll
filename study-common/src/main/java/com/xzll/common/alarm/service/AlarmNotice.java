package com.xzll.common.alarm.service;


import java.util.List;
import java.util.Map;

/**
 * @Author: hzz
 * @Date: 2021/9/16 18:39:21
 * @Description: 报警接口 (实现类有 钉钉，邮件，短信，等等等等，本项目只是钉钉一种实现)
 */
public interface AlarmNotice {


	void sendAlarmNotice(List<String> noticeAccountList, String title, String noticeContent, Map<String, String> otherParam);

}
