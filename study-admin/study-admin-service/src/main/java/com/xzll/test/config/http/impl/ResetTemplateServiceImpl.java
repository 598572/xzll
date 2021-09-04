package com.xzll.test.config.http.impl;

import com.alibaba.fastjson.JSONObject;
import com.xzll.test.config.http.ResetTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Iterator;
import java.util.Map;

/**
 * @Author: hzz
 * @Date: 2021/9/3 12:35:12
 * @Description:
 */
@Service
public class ResetTemplateServiceImpl implements ResetTemplateService {

	@Autowired
    private RestTemplate restTemplate;

    @Override
    public <T> T postWithBody(String url, Map<String, Object> params,Class<T> tClass) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(JSONObject.toJSONString(params),headers);
        ResponseEntity<T> exchange = restTemplate.exchange(url, HttpMethod.POST,entity,tClass);
        return exchange.getBody();
    }


    @Override
    public <T> T postWithBody(String url, Map<String, Object> params, Map<String, String> newHeaders,Class<T> tClass) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        newHeaders.forEach((key,value)->{
            headers.set(key,value);
        });
        HttpEntity<String> entity = new HttpEntity<>(JSONObject.toJSONString(params),headers);
        ResponseEntity<T> exchange = restTemplate.exchange(url, HttpMethod.POST,entity,tClass);
        return exchange.getBody();
    }

    @Override
    public <T> T postWithForm(String url, MultiValueMap<String, String> params,Class<T> tClass) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params,headers);
        ResponseEntity<T> exchange = restTemplate.exchange(url, HttpMethod.POST,entity,tClass);
        return exchange.getBody();
    }

    @Override
    public <T> T postWithForm(String url, MultiValueMap<String, String> params, Map<String, String> newHeaders,Class<T> tClass) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        newHeaders.forEach((key,value)->{
            headers.set(key,value);
        });
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params,headers);
        ResponseEntity<T> exchange = restTemplate.exchange(url, HttpMethod.POST,entity,tClass);
        return exchange.getBody();
    }

    @Override
    public <T> T postWithForm(String url,Class<T> tClass) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        ResponseEntity<T> exchange = restTemplate.exchange(url, HttpMethod.POST,null,tClass);
        return exchange.getBody();
    }

    @Override
    public <T> T getWithParams(String url, Map<String, String> params,Class<T> tClass) {
        if(params==null){
            return getWithNoParams(url,tClass);
        }
        return restTemplate.getForObject(getParamUrl(url,params),tClass);
    }

    @Override
    public <T> T getWithParams(String url, Map<String, String> params, Map<String, String> newHeaders, Class<T> tClass) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        newHeaders.forEach((key,value)->{
            headers.add(key,value);
        });
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<T> exchange = restTemplate.exchange(getParamUrl(url,params), HttpMethod.GET,entity,tClass);
        return exchange.getBody();
    }

    @Override
    public <T> T getWithNoParams(String url,Class<T> tClass) {
        return restTemplate.getForObject(url,tClass);
    }

    @Override
    public <T> T getWithNoParams(URI url, Class<T> tClass) {
        return restTemplate.getForObject(url,tClass);
    }

    @Override
    public <T> T getWithNoParams(String url, Map<String, String> newHeaders, Class<T> tClass) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        newHeaders.forEach((key,value)->{
            headers.add(key,value);
        });
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<T> exchange = restTemplate.exchange(url, HttpMethod.GET,entity,tClass);
        return exchange.getBody();
    }

    @Override
    public <T> T getWithPathViable(String url, Map<String, String> params,Class<T> tClass) {
        return restTemplate.getForObject(url,tClass,params);
    }

    /**
     *
     * @param url
     * @param params
     * @return
     */
    private String getParamUrl(String url,Map<String, String> params){
        Iterator<Map.Entry<String,String>> it = params.entrySet().iterator();
        int i = 0;
        while(it.hasNext()){
            Map.Entry<String,String> entry= it.next();
            String key = entry.getKey();
            String value= entry.getValue();
            if(i==0){
                url = url+"?"+key+"="+value;
            }else{
                url = url+"&"+key+"="+value;
            }
            i++;
        }
        return url;
    }

}
