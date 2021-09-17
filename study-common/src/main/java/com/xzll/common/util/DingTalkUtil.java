package com.xzll.common.util;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.google.common.base.Charsets;
import com.xzll.common.alarm.entity.dto.DingTalkRobotBeanDTO;
import com.xzll.common.alarm.entity.enums.DingTalkMsgTypeEnum;
import com.xzll.common.base.XzllBaseException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.util.Objects;

/**
 * @Author: hzz
 * @Date: 2021/9/16 18:20:29
 * @Description:
 */
public class DingTalkUtil {

	private static final Logger log = LoggerFactory.getLogger(DingTalkUtil.class);


	private static String encrypt(long timestamp, String secret) throws Exception {
		String stringToSign = timestamp + "\n" + secret;
		Mac mac = Mac.getInstance("HmacSHA256");
		mac.init(new SecretKeySpec(secret.getBytes(Charsets.UTF_8), "HmacSHA256"));
		byte[] signData = mac.doFinal(stringToSign.getBytes(Charsets.UTF_8));
		return URLEncoder.encode(new String(Base64.encodeBase64(signData)), Charsets.UTF_8.name());
	}

	public static DingTalkClient getDingTalkClient(String apiUrl, String accessToken, String secret) throws Exception {
		if (StringUtils.isBlank(apiUrl) || StringUtils.isBlank(accessToken)) {
			throw new XzllBaseException("api地址和accesToken不可为空");
		}
		StringBuilder apiUrlPath = new StringBuilder();
		apiUrlPath.append(apiUrl);
		apiUrlPath.append("?access_token=").append(accessToken);
		if (StringUtils.isNotEmpty(secret)) {
			long timestamp = System.currentTimeMillis();
			apiUrlPath.append("&timestamp=").append(timestamp);
			apiUrlPath.append("&sign=").append(encrypt(timestamp, secret));
		}

		return new DefaultDingTalkClient(apiUrlPath.toString());
	}

	public static DingTalkClient getDingTalkRobotClient(String accessToken, String secret) throws Exception {
		return getDingTalkClient("https://oapi.dingtalk.com/robot/send", accessToken, secret);
	}

	public static OapiRobotSendResponse sendDingTalkRobot(DingTalkRobotBeanDTO dingTalkRobot) {
		OapiRobotSendRequest request = new OapiRobotSendRequest();
		if (CollectionUtils.isNotEmpty(dingTalkRobot.getAtMobiles())) {
			OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
			at.setAtMobiles(dingTalkRobot.getAtMobiles());
			if (dingTalkRobot.isAtAll()) {
				at.setIsAtAll(true);
			}

			request.setAt(at);
		}

		setMsg(request, dingTalkRobot);

		try {
			OapiRobotSendResponse response = (OapiRobotSendResponse) getDingTalkRobotClient(dingTalkRobot.getAccessToken(), dingTalkRobot.getSecret()).execute(request);
			if (!response.isSuccess()) {
				log.info("钉钉消息发送失败，errorCode:{},errorMsg:{}", response.getErrcode(), response.getErrmsg());
			}

			return response;
		} catch (Exception var3) {
			log.warn("钉钉消息发送失败");
			return null;
		}
	}

	private static void setMsg(OapiRobotSendRequest request, DingTalkRobotBeanDTO dingTalkRobot) {
		DingTalkMsgTypeEnum typeEnum = chooseMsgType(dingTalkRobot);
		request.setMsgtype(typeEnum.getValue());
		switch (typeEnum) {
			case TEXT:
				OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
				text.setContent(dingTalkRobot.getContent());
				request.setText(text);
				break;
			case LINK:
				OapiRobotSendRequest.Link link = new OapiRobotSendRequest.Link();
				link.setMessageUrl(dingTalkRobot.getMessageUrl());
				link.setPicUrl(dingTalkRobot.getPicUrl());
				link.setTitle(dingTalkRobot.getTitle());
				link.setText(dingTalkRobot.getContent());
				request.setLink(link);
				break;
			default:
				OapiRobotSendRequest.Markdown markdown = new OapiRobotSendRequest.Markdown();
				markdown.setTitle(dingTalkRobot.getTitle());
				markdown.setText(dingTalkRobot.getContent());
				request.setMarkdown(markdown);
		}

	}

	private static DingTalkMsgTypeEnum chooseMsgType(DingTalkRobotBeanDTO dingTalkRobot) {
		if (!StringUtils.isNotEmpty(dingTalkRobot.getMessageUrl()) && !StringUtils.isNotEmpty(dingTalkRobot.getPicUrl())) {
			if (StringUtils.isNotEmpty(dingTalkRobot.getTitle())) {
				dingTalkRobot.setContent("【" + dingTalkRobot.getTitle() + "】\n" + dingTalkRobot.getContent());
			}

			return Objects.isNull(dingTalkRobot.getDingTalkMsgTypeEnum()) ? DingTalkMsgTypeEnum.TEXT : dingTalkRobot.getDingTalkMsgTypeEnum();
		} else {
			return DingTalkMsgTypeEnum.LINK;
		}
	}
}
