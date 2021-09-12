package com.xzll.test.strategy.impl;

import com.alibaba.fastjson.JSON;
import com.xzll.test.strategy.ApproveStrategy;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;


/**
 * @Author: hzz
 * @Date: 2021/9/12 19:38:34
 * @Description:
 */
@Slf4j
public class ApproveSuccessServiceImpl implements ApproveStrategy {


	/**
	 * 终审 && 通过
	 *
	 * @param params
	 * @param approvalRecoder
	 * @param extra           扩展参数
	 * @return
	 */
	@Override
	public String approveByRefundStatus(Object params, Object approvalRecoder, Map<String, Object> extra) {
		log.info("终审 && 通过 ,完成后，修改退款订单状态为退款完成 params:{} ; approvalRecoder:{},extra:{}", JSON.toJSONString(params), JSON.toJSONString(approvalRecoder), JSON.toJSONString(extra));
		//TODO do something
		return "提交成功";
	}

	@Override
	public boolean hasExtra(Map<String, Object> extend) {
		return Objects.nonNull(extend) && !extend.isEmpty() && extend.containsKey("apRefund");
	}
}
