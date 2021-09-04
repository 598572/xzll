package com.xzll.test.config.http;

import org.springframework.util.MultiValueMap;

import java.net.URI;
import java.util.Map;

/**
 * @Author: hzz
 * @Date: 2021/9/3 12:38:12
 * @Description:
 */
public interface ResetTemplateService {
    /**
     * 发送POST请求，请求体携带参数
     * @param url
     * @param params
     */
    <T> T postWithBody(String url, Map<String,Object> params,Class<T> tClass);

    /**
     * 发送POST请求，请求体携带参数，添加请求头
     * @param url
     * @param params
     * @param headers
     */
    <T> T postWithBody(String url,Map<String,Object> params,Map<String,String> headers,Class<T> tClass);

    /**
     * 发送POST请求，FORM表单的形式
     * @param url
     * @param params
     */
    <T> T postWithForm(String url,MultiValueMap<String,String> params,Class<T> tClass);

    /**
     * 发送POST请求，FORM表单的形式,添加请求头
     * @param url
     * @param params
     * @param headers
     */
    <T> T postWithForm(String url, MultiValueMap<String, String> params, Map<String,String> headers,Class<T> tClass);

    /**
     * 无参数POST请求
     * @param url
     */
    <T> T postWithForm(String url,Class<T> tClass);

    /**
     * 发送GET请求
     * @param url
     * @param params
     */
    <T> T getWithParams(String url,Map<String,String> params,Class<T> tClass);

    /**
     * 发送GET请求
     * @param url
     * @param params
     * @param headers
     * @param tClass
     * @param <T>
     * @return
     */
    <T> T getWithParams(String url,Map<String,String> params,Map<String,String> headers,Class<T> tClass);

    /**
     * 发送GET请求
     * @param url
     */
    <T> T getWithNoParams(String url,Class<T> tClass);

    /**
     * 解决编码问题
     * @param url
     * @param tClass
     * @param <T>
     * @return
     */
    <T> T getWithNoParams(URI url, Class<T> tClass);

    /**
     * 发送GET请求
     * @param url
     * @param headers
     * @param tClass
     * @param <T>
     * @return
     */
    <T> T getWithNoParams(String url,Map<String,String> headers,Class<T> tClass);

    /**
     * 发送GET请求，RESTFUL
     * @param url
     * @param params
     * @return
     */
    <T> T getWithPathViable(String url,Map<String,String> params,Class<T> tClass);
}
