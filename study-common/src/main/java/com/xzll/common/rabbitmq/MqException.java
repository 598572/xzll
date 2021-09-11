package com.xzll.common.rabbitmq;


import com.xzll.common.base.XzllBaseException;

/**
 * @Author: hzz
 * @Date: 2021/9/10 13:18:34
 * @Description:
 */
public class MqException extends XzllBaseException {

    public MqException(String message) {
        super(message);
    }

    public MqException(String message, int code) {
        super(message);
        this.code = code;
    }
}
