package com.xzll.test.entity.test;


import lombok.Data;

import java.time.LocalDateTime;

/**
 * 2 司机行为- 去接乘客
 */
@Data
public class SetoutCallbackArgAo extends BaseDriverCallBackAo {

    /**
     * 接乘时间
     */
    private LocalDateTime driverPickUpTime;



}
