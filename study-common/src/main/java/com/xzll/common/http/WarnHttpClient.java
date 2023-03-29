package com.xzll.common.http;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


/**
 * @Author: hzz
 * @Date: 2023/2/28 11:09:52
 * @Description: 告警工具类
 */
public class WarnHttpClient {
    private static final Logger log = LoggerFactory.getLogger(WarnHttpClient.class);

    public static void warn(String url, String json) {
        log.info("发送告警信息, url={}, json={}", url, json);
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(url);
            StringEntity entity = new StringEntity(json, "UTF-8");
            httpPost.setEntity(entity);
            httpPost.setHeader("Content-Type", "application/json;charset=utf8");

            response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();

            if (responseEntity != null) {
                log.info("告警响应, url={}, 状态码:{}, 长度为:{}, 内容为:{}", url, response.getStatusLine().getStatusCode(), responseEntity.getContentLength(), EntityUtils.toString(responseEntity));
            } else {
                log.info("告警响应, url={}, 状态码:{}", url, response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            log.error("发送告警请求异常, url={}", url, e);
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
