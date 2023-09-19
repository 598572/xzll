package com.xzll.test.strategy.factory;

import java.util.Map;

/**
 * @Author: hzz
 * @Date: 2021/9/12 19:22:30
 * @Description: 抽象策略工厂，根据T的类型，获取不同的Strategy实现
 */
public interface StrategyFactory<T, S> {

    S getStrategy(T type, boolean lastApproveFlag);

    void setStrategyMap(Map<T, S> strategyMap);

    default StrategyFactory<T, S> build(Map<T, S> strategyMap) {
        setStrategyMap(strategyMap);
        return this;
    }




}
