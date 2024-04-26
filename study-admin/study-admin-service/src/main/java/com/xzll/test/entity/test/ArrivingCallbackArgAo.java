package com.xzll.test.entity.test;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 3 司机行为- 司机到达乘客上车点
 */
@Data
public class ArrivingCallbackArgAo extends BaseDriverCallBackAo {

    /**
     * 到达上车地点时间
     */
    private LocalDateTime driverArriveTime;



}
