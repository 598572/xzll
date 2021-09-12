package com.xzll.test.strategy;



import java.util.Map;

/**
 * 策略对象，针对不同的状态，有对应的实现
 *
 * @see  com.xzll.test.strategy.enums.RefundStatusEnum
 */
public interface ApproveStrategy {

    /**
     * 进入审批流程
     *
     * @param params
     * @param approvalRecoder
     * @param extra             扩展参数
     * @return
     */
    String approveByRefundStatus(Object params, Object approvalRecoder, Map<String, Object> extra);

    /**
     * hook 子类自行实现 是否有扩展参数
     *
     * @param extra
     * @return
     */
    boolean hasExtra(Map<String, Object> extra);

}
