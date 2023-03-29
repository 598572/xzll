package com.xzll.test.websocket.util;

import com.alibaba.fastjson.JSON;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SimpleHttpClient {
    private static final Logger log = LoggerFactory.getLogger(SimpleHttpClient.class);

	public static void main(String[] args) {

		https://id.vivo.com.cn/?_202111221131#!/access/login



		for (int i = 0; i <10; i++) {

			String  param="{\"appId\":100098090,\"keyword\":\"\",\"startTime\":\"2021-11-01\",\"endTime\":\"2021-11-22\",\"dataFrom\":\"ALL\",\"messageStatus\":\"ALL\",\"page\":1,\"rows\":20}";
			String s = postJson("https://vpush.vivo.com.cn/platform/message/notifications/list", param);

			System.out.println(JSON.toJSONString(s));

		}
	}

    public static String postJson(String url, String json) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(url);
        StringEntity entity = new StringEntity(json, "UTF-8");
        httpPost.setEntity(entity);
        httpPost.setHeader("Content-Type", "application/json;charset=utf8");
        httpPost.setHeader("Cookie", "b_account_username=Ju2eqoAExNA%3D; b_account_aid=P%2BzNV1yZ6Zw%3D; b_account_token=ea3b1a4b2f9cf8b2d7b83dbf96f30484.1637028639471; b_account_salt=Wpogo3qVfif4fbJzhXKirg%3D%3D.1637547039471; Hm_lvt_abf0cc57afab681cf24baecfb7eeef56=1636686812,1636973861,1637547040; Hm_lpvt_abf0cc57afab681cf24baecfb7eeef56=1637547862");

        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();

            if (responseEntity != null && response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                log.info("响应内容长度为:" + responseEntity.getContentLength());
                log.info("响应内容为:" + EntityUtils.toString(responseEntity));

                throw new RuntimeException();
            }
            return EntityUtils.toString(responseEntity);
        } catch (Exception e) {
            log.error("发送POST请求异常,", e);
			throw new RuntimeException();
        } finally {
            try {
                // 释放资源
                if (null == httpClient) {
                    httpClient.close();
                }
                if (null == response) {
                    response.close();
                }
            } catch (IOException e) {
                log.error("关闭HttpClient异常,", e);
            }
        }
    }

    public static String get(String url, String param) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        String fullUrl = url + "?" + param;
        HttpGet httpGet = new HttpGet(fullUrl);
        httpGet.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//        httpGet.addHeader("Accept", "application/json");

        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            HttpEntity responseEntity = response.getEntity();

            if (responseEntity != null && response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                log.info("响应内容长度为:" + responseEntity.getContentLength());
                log.info("响应内容为:" + EntityUtils.toString(responseEntity));

				throw new RuntimeException();
            }
            return EntityUtils.toString(responseEntity);
        } catch (Exception e) {
            log.error("发送Get请求异常,", e);
			throw new RuntimeException();
        } finally {
            try {
                // 释放资源
                if (null == httpClient) {
                    httpClient.close();
                }
                if (null == response) {
                    response.close();
                }
            } catch (IOException e) {
                log.error("关闭HttpClient异常,", e);
            }
        }
    }
}
