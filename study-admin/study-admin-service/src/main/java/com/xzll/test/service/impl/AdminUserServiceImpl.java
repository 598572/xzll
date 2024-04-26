package com.xzll.test.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.http.param.MediaType;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xzll.admin.api.dto.AdminUserDTO;
import com.xzll.common.constant.StudyConstant;
import com.xzll.common.http.HttpConnectionManager;
import com.xzll.common.http.HttpConnectionPool;
import com.xzll.common.rocketmq.ClusterEvent;
import com.xzll.common.util.ThreadUtil;
import com.xzll.test.config.http.ResetTemplateService;
import com.xzll.test.config.mq.RocketMqProducerWrap;
import com.xzll.test.entity.AdminUserDO;
import com.xzll.test.entity.test.AcceptingCallbackArgAo;
import com.xzll.test.entity.test.BaseDriverCallBackAo;
import com.xzll.test.mapper.AdminUserMapper;
import com.xzll.test.service.AdminUserService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/8/7 17:32
 * @Description:
 */
@Slf4j
@Service
public class AdminUserServiceImpl implements AdminUserService {

	@Autowired
	private AdminUserMapper adminUserMapper;
	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;

	@Autowired
	private ResetTemplateService resetTemplateService;

	@Autowired
	private RocketMqProducerWrap rocketMqProducerWrap;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public List<AdminUserDTO> findByUserNameForTestTrace(String username) {
		LambdaQueryWrapper<AdminUserDO> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(AdminUserDO::getUsername, username);
		List<AdminUserDO> adminUserDOS = adminUserMapper.selectList(queryWrapper);


		//1. 演示：调用外部（第三方）接口时 如何正确 传递链路id
		try {
			JSONObject param = new JSONObject();
			param.put("key1", "value1");
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Accept", MediaType.APPLICATION_JSON.toString());
			headers.put("AppKey", "111");
			headers.put("AppSecurity", "222");
			//发送绑定请求
			HttpConnectionPool pool = HttpConnectionManager.getConnectionPool(StudyConstant.HttpPoolConstant.POOL_NAME);
			log.info("调用第三方xxx接口请求参数param:{},header:{}", JSONUtil.toJsonStr(param), JSONUtil.toJsonStr(headers));
			String result = pool.callByGet("http://www.baidu.com", null);
			//log.info("调用第三方xxx接口返回结果:{}", result);
		} catch (Exception exception) {
			//log.error("调用第三方xxx接口失败:", exception);
		}
		//2. 演示：使用org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor线程池时候 如何正确 传递链路id
		for (int i = 0; i < 5; i++) {
			int finalI = i;
			taskExecutor.execute(() -> {
				log.info("【spring的 ThreadPoolTaskExecutor】当前i值：{}", finalI);
			});
		}
		//3. 演示：使用 jdk的 ThreadPoolExecutor时候 如何正确传递 链路id
		ThreadPoolExecutor threadPool = ThreadUtil.getThreadPool(Runtime.getRuntime().availableProcessors(), Runtime.getRuntime().availableProcessors() * 2, 200, "demo-traceId");
		for (int j = 5; j < 10; j++) {
			int finalJ = j;
			threadPool.execute(() -> {
				log.info("【jdk的 ThreadPoolExecutor】当前j值：{}", finalJ);
			});
		}
		for (int j = 10; j < 15; j++) {
			int finalJ = j;
			CompletableFuture.runAsync(() -> {
				log.info("【测试-CompletableFuture】{}", finalJ);
			});

		}
		CompletableFuture.runAsync(() -> {
			log.info("【探针test-CompletableFuture】{}", "娃哈哈");
		}, threadPool);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
		}
		String retMap = resetTemplateService.getWithNoParams("http://www.sina.com.cn/", String.class);
		//log.info("restTemplate结果:{}", JSONUtil.toJsonStr(retMap));

		int totalMessagesToSend = 5;
		for (int i = 0; i < totalMessagesToSend; i++) {
			// 发送消息
			try {
				ClusterEvent clusterEvent = new ClusterEvent();
				Map<String, Object> map = new HashMap<>();
				map.put("dataKey" + i, "dataValue" + i);
				map.put("dataKey" + i, "dataValue" + i);
				clusterEvent.setData(JSONUtil.toJsonStr(map));
				boolean b = rocketMqProducerWrap.sendClusterEvent("xzll-test-topic", clusterEvent);
				log.info("发送消息结果:{}", b);
			} catch (Exception e) {
				log.error("rocketMq消息发送失败:", e);
			}
		}

		return adminUserDOS.stream().map(adminUserDO -> {
			AdminUserDTO adminUserDTO = new AdminUserDTO();
			BeanUtils.copyProperties(adminUserDO, adminUserDTO);
			return adminUserDTO;
		}).collect(Collectors.toList());
	}


	/**
	 * 用于测试 nginx
	 *
	 * @param username
	 * @return
	 */
	@Override
	public List<AdminUserDTO> findByUserNameForTestNginx(String username) {
		LambdaQueryWrapper<AdminUserDO> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.like(AdminUserDO::getUsername, username);
		List<AdminUserDO> userList = adminUserMapper.selectList(queryWrapper);
		if (CollectionUtils.isEmpty(userList)) {
			return Lists.newArrayList();
		}
		return userList.stream().map(adminUserDO -> {
			AdminUserDTO adminUserDTO = new AdminUserDTO();
			BeanUtils.copyProperties(adminUserDO, adminUserDTO);
			return adminUserDTO;
		}).collect(Collectors.toList());
	}



	@Override
	public List<AdminUserDTO> findByUserNameForTestAgent(String username) {
		LambdaQueryWrapper<AdminUserDO> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.like(AdminUserDO::getUsername, username);
		queryWrapper.orderByDesc(AdminUserDO::getId);
		List<AdminUserDO> adminUserDOS = adminUserMapper.selectList(queryWrapper);
		if (CollectionUtils.isEmpty(adminUserDOS)){
			return Lists.emptyList();
		}
		return adminUserDOS.stream().map(adminUserDO -> {
			AdminUserDTO adminUserDTO = new AdminUserDTO();
			BeanUtils.copyProperties(adminUserDO, adminUserDTO);
			return adminUserDTO;
		}).collect(Collectors.toList());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int addUser(AdminUserDTO userDTO) {
		log.info("添加人员信息入参:{}", JSONUtil.toJsonStr(userDTO));
		AdminUserDO po = new AdminUserDO();
		BeanUtils.copyProperties(userDTO, po);
		int row = adminUserMapper.insert(po);
		log.info("添加人员信息row:{}", row);
		return row;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void batchAddUser(List<AdminUserDTO> users) {
		List<AdminUserDO> dos = BeanUtil.copyToList(users, AdminUserDO.class);
		adminUserMapper.batchInsertUser(dos);
		log.info("批量添加人员信息完成");
	}

	@Override
	public AdminUserDTO findUserByUid(Long id) {
		LambdaQueryWrapper<AdminUserDO> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(AdminUserDO::getId, id);
		AdminUserDO user = adminUserMapper.selectOne(queryWrapper);
		AdminUserDTO adminUserDTO = new AdminUserDTO();
		BeanUtil.copyProperties(user,adminUserDTO);
		log.info("根据uid查询用户信息，反参：{}",JSONUtil.toJsonStr(adminUserDTO));
		return adminUserDTO;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int updateUserByUid(AdminUserDTO userDTO) {
		log.info("更新人员信息入参:{}",JSONUtil.toJsonStr(userDTO));
		AdminUserDO po = new AdminUserDO();
		po.setId(userDTO.getId());
		po.setPassword(userDTO.getPassword());
		po.setUsername(userDTO.getUsername());
		int row = adminUserMapper.updateById(po);
		log.info("更新人员信息row:{}", row);
		return row;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void batchUpdateUser(List<AdminUserDTO> userDTOs) {
		List<AdminUserDO> dos = BeanUtil.copyToList(userDTOs, AdminUserDO.class);
		adminUserMapper.batchUpdateUser(dos);
	}

	public static void main(String[] args) {

			BaseDriverCallBackAo driverCallBackAo = new AcceptingCallbackArgAo();
			driverCallBackAo.setChannel("渠道呀");
			AcceptingCallbackArgAo dispatchAo = (AcceptingCallbackArgAo) driverCallBackAo;
			dispatchAo.setRideType(1);
			dispatchAo.setGroupId(2);
			System.out.println(dispatchAo);

	}
}
