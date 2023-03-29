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
public class MessageHistoryTotalStatisticsPo {

    private String pushMsgId;
    private String regId;

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
}
