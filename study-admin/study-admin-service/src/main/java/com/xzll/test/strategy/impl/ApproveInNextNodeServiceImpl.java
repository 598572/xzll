package com.xzll.test.strategy.impl;

import com.alibaba.fastjson.JSON;
import com.xzll.test.strategy.ApproveStrategy;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;


/**
 * @Author: hzz
 * @Date: 2021/9/12 19:26:30
 * @Description:
 */
@Slf4j
public class ApproveInNextNodeServiceImpl implements ApproveStrategy {



    /**
     * 非终审 && 非驳回 , 进入下一个审批节点
     *
     * @param params
     * @param approvalRecoder
     * @param extra             扩展参数
     * @return
     */
    @Override
    public String approveByRefundStatus(Object params, Object approvalRecoder, Map<String, Object> extra) {
        log.info("进入下个审批节点 ， params:{} ; approvalRecoder:{}, apNode:{}", JSON.toJSONString(params), JSON.toJSONString(approvalRecoder), JSON.toJSONString(extra));
        //TODO do something
        return "提交成功";
    }

    @Override
    public boolean hasExtra(Map<String, Object> extra) {
        return Objects.nonNull(extra) && !extra.isEmpty() && extra.containsKey("approvalNode");
    }
}
