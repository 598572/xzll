package com.xzll.test.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xzll.common.base.XzllBusinessException;
import com.xzll.test.ao.PrePayOrderAo;
import com.xzll.test.entity.PrePayOrderRecordDO;
import com.xzll.test.mapper.PrePayOrderRecordMapper;
import com.xzll.test.service.PrePayOrderRecordService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2023/9/8 06:03
 * @Description:
 */
@Slf4j
@Service
public class PrePayOrderRecordServiceImpl implements PrePayOrderRecordService {

	public static final String PRE_PAY_FAIL_MSG = "预支付失败，请稍后再试";
	@Autowired
	private PrePayOrderRecordMapper prePayOrderRecordMapper;

	//已经预支付成功
	private static final Integer ORDER_STATUS_SUCCESS = 1;

	//预支付锁key
	private static final String PRE_PAY_RECORD_KEY = "PRE_PAY_RECORD_KEY_";


	@Autowired
	private RedissonClient redissonClient;


	@Override
	@Transactional(rollbackFor = Exception.class)
	public List<PrePayOrderRecordDO> findForUpdate(PrePayOrderAo ao) {
		//2.2 for update 加锁查询 确保查询期间，该行数据不被修改
		LambdaQueryWrapper<PrePayOrderRecordDO> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(PrePayOrderRecordDO::getOrderId, ao.getOrderId()).last(" for update ");
		return prePayOrderRecordMapper.selectList(queryWrapper);
	}

	/**
	 * 模拟用户预支付业务逻辑
	 *
	 * @param ao
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void prePayOrder(PrePayOrderAo ao) {
		log.info("用户预支付-入参:{}", JSONUtil.toJsonStr(ao));
		RLock lock = redissonClient.getLock(PRE_PAY_RECORD_KEY + ao.getOrderId());
		try {
			//1. 预支付 加锁
			boolean result = lock.tryLock(5, 10, TimeUnit.SECONDS);
			if (!result) {
				log.info("获取预支付锁失败orderId:{}", ao.getOrderId());
				throw new XzllBusinessException(PRE_PAY_FAIL_MSG);
			}

			//2. 查询该笔订单是否有预支付记录（不加锁）
			LambdaQueryWrapper<PrePayOrderRecordDO> qwParam = new LambdaQueryWrapper<>();
			qwParam.eq(PrePayOrderRecordDO::getOrderId, ao.getOrderId());
			List<PrePayOrderRecordDO> checkExist = prePayOrderRecordMapper.selectList(qwParam);
			List<PrePayOrderRecordDO> prePayOrderRecordDOS=new ArrayList<>();
			//2.1 db有预支付记录才进行for update查询，确保for update 拿到的锁是 next-key lock+记录锁+gap锁，这样就不会被其他事务同时获取锁了（依靠next-key lock的互斥性）。
			if (!CollectionUtils.isEmpty(checkExist)){
				//2.2 for update 加锁查询 确保查询期间，该行数据不被修改
				LambdaQueryWrapper<PrePayOrderRecordDO> queryWrapper = new LambdaQueryWrapper<>();
				queryWrapper.eq(PrePayOrderRecordDO::getOrderId, ao.getOrderId()).last(" for update ");
				prePayOrderRecordDOS = prePayOrderRecordMapper.selectList(queryWrapper);
			}

			//3. 检查该订单是否有预支付过（使用状态比对）
			if (!CollectionUtils.isEmpty(prePayOrderRecordDOS) && prePayOrderRecordDOS.stream().anyMatch(item -> Objects.equals(ORDER_STATUS_SUCCESS, item.getStatus()))) {
				log.info("已预支付成功：" + ao.getOrderId() + "订单信息：" + JSONUtil.toJsonStr(prePayOrderRecordDOS));
				throw new XzllBusinessException("已预支付成功无需再次支付");
			}
			//4. 插入该订单的预支付记录，并将状态置为：ORDER_STATUS_SUCCESS
			PrePayOrderRecordDO prePayOrderRecordDO = new PrePayOrderRecordDO();
			BeanUtils.copyProperties(ao, prePayOrderRecordDO);
			Date date = new Date();
			prePayOrderRecordDO.setCreateTime(date);
			prePayOrderRecordDO.setUpdateTime(date);
			int insert = prePayOrderRecordMapper.insert(prePayOrderRecordDO);
			log.info("插入成功影响行数:{}，orderId:{}", insert,prePayOrderRecordDO.getOrderId());
		} catch (InterruptedException e) {
			log.error("获取预支付记录锁失败");
		} finally {
			lock.unlock();
		}
	}

	private List<PrePayOrderRecordDO> findListForUpdate(PrePayOrderAo ao) {
		//2. 查询是否有该笔订单是否有预支付
		LambdaQueryWrapper<PrePayOrderRecordDO> qwParam = new LambdaQueryWrapper<>();
		qwParam.eq(PrePayOrderRecordDO::getOrderId, ao.getOrderId());
		List<PrePayOrderRecordDO> checkExist = prePayOrderRecordMapper.selectList(qwParam);

		if (!CollectionUtils.isEmpty(checkExist)){
			//2. 查询是否有该笔订单是否有预支付
			LambdaQueryWrapper<PrePayOrderRecordDO> queryWrapper = new LambdaQueryWrapper<>();
			queryWrapper.eq(PrePayOrderRecordDO::getOrderId, ao.getOrderId()).last(" for update ");
			List<PrePayOrderRecordDO> prePayOrderRecordDOS = prePayOrderRecordMapper.selectList(queryWrapper);
			return prePayOrderRecordDOS;
		}
		return new ArrayList<>();
	}
}
