package com.xzll.test.entity.test;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 4 司机行为-开始行程
 */
@Data
public class InProgressCallbackArgAo extends BaseDriverCallBackAo {

    /**
     * 开始计费时间
     */
    private LocalDateTime startChargeTime;



}
