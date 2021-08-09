package com.xzll.common.util.log;



import com.xzll.common.util.LinkUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class LoggerDTO {

    /**
     * 请求URL
     */
    private String url;

    /**
     * 请求的Controller方法
     */
    private String httpMethod;

    /**
     * 请求的参数
     */
    private String params;

    /**
     * 方法执行结果
     */
    private String result;

    /**
     * 请求的执行时间
     */
    private Long executeTime;

    private String description;

    private LoggerDTO() {
    }

    @Override
    public String toString() {
        return LinkUtil.link(
                "study-test access log, url:", this.url,
                ", description:", this.description,
                ", method:", this.httpMethod,
                ", params:", this.params,
                ", result:", this.result,
                ", executeTime:", this.executeTime, "ms");
    }

    public static class LoggerBuilder {

        private LoggerBuilder() {

        }

        public static final LoggerBuilder BUILDER = new LoggerBuilder();

        public LoggerDTO build() {

            LoggerDTO loggerDTO = new LoggerDTO();

            loggerDTO.setExecuteTime(this.executeTime);
            loggerDTO.setHttpMethod(this.httpMethod);
            loggerDTO.setParams(this.params);
            loggerDTO.setResult(this.result);
            loggerDTO.setUrl(this.url);
            loggerDTO.setDescription(this.description);

            Map<String, Object> logMap = new ConcurrentHashMap<>();
            logMap.put("url", this.url);
            logMap.put("method", this.httpMethod);
            logMap.put("parameter", this.params);
            logMap.put("spendTime", this.executeTime);
            logMap.put("description", this.description);

//			log.info(Markers.appendEntries(logMap), JSONUtil.parse(loggerDTO).toString());

            return loggerDTO;
        }

        /**
         * 请求URL
         */
        private String url;

        /**
         * 请求的Controller方法
         */
        private String httpMethod;

        /**
         * 请求的参数
         */
        private String params;

        /**
         * 方法执行结果
         */
        private String result;

        /**
         * 请求的执行时间
         */
        private Long executeTime;
        /**
         * 方法描述
         */
        private String description;

        public LoggerBuilder setUrl(String url) {
            this.url = url;
            return this;
        }

        public LoggerBuilder setHttpMethod(String httpMethod) {
            this.httpMethod = httpMethod;
            return this;
        }

        public LoggerBuilder setParams(String params) {
            this.params = params;
            return this;
        }

        public LoggerBuilder setResult(String result) {
            this.result = result;
            return this;
        }

        public LoggerBuilder setExecuteTime(Long executeTime) {
            this.executeTime = executeTime;
            return this;
        }

        public LoggerBuilder setDescription(String description) {
            this.description = description;
            return this;
        }
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setExecuteTime(Long executeTime) {
        this.executeTime = executeTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
