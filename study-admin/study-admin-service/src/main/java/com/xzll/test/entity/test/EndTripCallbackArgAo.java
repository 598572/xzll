package com.xzll.test.entity.test;

import lombok.Data;

/**
 * 5 司机行为-结束行程
 */
@Data
public class EndTripCallbackArgAo extends BaseDriverCallBackAo {
    // 订单费用信息
    /**
     * 实际行驶时长
     */
    private Integer duration;

    /**
     * 实际行驶里程
     */
    private Integer mileage;

    /**
     * 行程费
     */
    private Integer tripPrice;

    /**
     * 附加费总金额
     */
    private Integer surchargePrice;

    /**
     * 附加费详细
     */
    private String surchargeStr;

}
