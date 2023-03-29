package com.xzll.test.controller.paicha;

import lombok.*;

/**
 * @Author: hzz
 * @Date: 2021/11/26 17:11:21
 * @Description:
 */
@Setter
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageStatisticsLastResultPo {
    /**
     * 维度
     */
    private Integer hourNum;
    private Integer dateNum;
    private Integer pushPlatform;
    private Integer platform;
    private Integer pushType;
    private Integer pushChannel;
    private Integer statisticsType;
    private Long pushTime;

    //有效目标
    private Long validTargetCount;
    //送达
    private Long sendCount;
    //到达
    private Long arriveCount;
    //展示
    private Long showCount;
    //点击
    private Long clickCount;

    private String dateMonthAndDay;
}
