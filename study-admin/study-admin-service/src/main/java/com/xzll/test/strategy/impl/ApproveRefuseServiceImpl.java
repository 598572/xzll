package com.xzll.test.strategy.impl;

import com.alibaba.fastjson.JSON;
import com.xzll.test.strategy.ApproveStrategy;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;


/**
 * @Author: hzz
 * @Date: 2021/9/12 19:31:30
 * @Description:
 */
@Slf4j
public class ApproveRefuseServiceImpl implements ApproveStrategy {


	/**
	 * (终审 || 非终审) && 驳回
	 *
	 * @param params
	 * @param approvalRecoder
	 * @param extra           扩展参数
	 * @return
	 */
	@Override
	public String approveByRefundStatus(Object params, Object approvalRecoder, Map<String, Object> extra) {
		log.info("(终审 || 非终审) && 驳回 ，修改退款订单状态为驳回 params:{} ; approvalRecoder:{},extra:{}", JSON.toJSONString(params), JSON.toJSONString(approvalRecoder), JSON.toJSONString(extra));
		//TODO do something
		return "驳回成功";
	}

	@Override
	public boolean hasExtra(Map<String, Object> extra) {
		return false;
	}

}
