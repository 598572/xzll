package com.xzll.test.strategy.config;


import com.xzll.common.util.MapUtil;
import com.xzll.test.strategy.ApproveStrategy;
import com.xzll.test.strategy.enums.RefundStatusEnum;
import com.xzll.test.strategy.factory.StrategyFactory;
import com.xzll.test.strategy.impl.ApproveInNextNodeServiceImpl;
import com.xzll.test.strategy.impl.ApproveRefuseServiceImpl;
import com.xzll.test.strategy.impl.ApproveSuccessServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.Objects;

/**
 * @Author: hzz
 * @Date: 2021/9/12 19:22:30
 * @Description: 用于配置策略对象与类型的关系
 */
@Configuration
public class StrategyFactoryConfig {

	@Bean
	public StrategyFactory<Integer, ApproveStrategy> approveStrategyFactory() {

		MapUtil.MapBuilder<Integer, ApproveStrategy> mapBuilder = MapUtil.mapBuilder();

		Map<Integer, ApproveStrategy> approveStrategyMap = mapBuilder
				.init()
				.withEntry(RefundStatusEnum.REFUND_NO_AUDIT.getStatus(), approveInNextNodeStrategy())
				.withEntry(RefundStatusEnum.REFUND_SUCESS.getStatus(), approveSuccessServiceImplStrategy())
				.withEntry(RefundStatusEnum.REFUND_REFUSE.getStatus(), approveRefuseServiceImplStrategy())
				.toMap();

		return new StrategyFactory<Integer, ApproveStrategy>() {

			private Map<Integer, ApproveStrategy> approveStrategyMap;

			/**
			 *
			 * @param type
			 * @param lastApproveFlag 终审标志
			 * @return
			 */
			@Override
			public ApproveStrategy getStrategy(Integer type, boolean lastApproveFlag) {
				Assert.isTrue(this.approveStrategyMap.containsKey(type), "approveStrategyMap don't have type value! type is " + type);
				//只有终审且成功才会进入 ApproveInNextNodeServiceImpl
				if (lastApproveFlag && Objects.equals(RefundStatusEnum.REFUND_SUCESS.getStatus(), type)) {
					return this.approveStrategyMap.get(RefundStatusEnum.REFUND_SUCESS.getStatus());
				}
				//非终审且通过进入 approveInNextNodeStrategy
				if (!lastApproveFlag && Objects.equals(RefundStatusEnum.REFUND_SUCESS.getStatus(), type)) {
					return this.approveStrategyMap.get(RefundStatusEnum.REFUND_NO_AUDIT.getStatus());
				}
				return this.approveStrategyMap.get(type);
			}

			@Override
			public void setStrategyMap(Map<Integer, ApproveStrategy> strategyMap) {
				this.approveStrategyMap = strategyMap;
			}
		}.build(approveStrategyMap);
	}

	/**
	 * 进入下个流程
	 */
	@Bean
	public ApproveInNextNodeServiceImpl approveInNextNodeStrategy() {
		return new ApproveInNextNodeServiceImpl();
	}

	/**
	 * 退款审批被驳回
	 */
	@Bean
	public ApproveRefuseServiceImpl approveRefuseServiceImplStrategy() {
		return new ApproveRefuseServiceImpl();
	}

	/**
	 * 退款审批成功
	 */
	@Bean
	public ApproveSuccessServiceImpl approveSuccessServiceImplStrategy() {
		return new ApproveSuccessServiceImpl();
	}
}
