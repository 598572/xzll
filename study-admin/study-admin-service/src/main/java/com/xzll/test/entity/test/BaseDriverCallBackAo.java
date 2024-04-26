package com.xzll.test.entity.test;

import lombok.Data;

import java.io.Serializable;

/**
 * 司机行为相关- 父类
 */
@Data
public class BaseDriverCallBackAo implements Serializable {

    /**
     * 接入方的回调渠道
     */
    private String channel;
    /**
     * 回调类型
     */
    private String callbackType;

    /**
     * 聚合子订单
     */
    private String subOrderId;

    /**
     * 运力商订单号
     */
    private String rideChannelOrderId;

    /**
     * 订单状态
     */
    private Integer orderStatus;

}
