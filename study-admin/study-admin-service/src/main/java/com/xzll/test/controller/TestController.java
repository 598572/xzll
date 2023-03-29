package com.xzll.test.controller;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xzll.common.base.Result;
import com.xzll.common.rabbitmq.producer.ProducerService;
import com.xzll.test.entity.TestEntity;
import com.xzll.test.entity.TestMqConstant;
import com.xzll.test.entity.UserMQ;
import com.xzll.test.service.TestService;
import com.xzll.test.strategy.ApproveStrategy;
import com.xzll.test.strategy.factory.StrategyFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static cn.hutool.core.date.DatePattern.NORM_DATETIME_FORMATTER;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/5/22 18:10
 * @Description:
 */
@Slf4j
@Api(tags = "测试controller")
@RestController
@RequestMapping("/admin/testController")
public class TestController {

	@Autowired
	private TestService testService;

	@Autowired
	private ProducerService producerService;

	@GetMapping("/test")
	@ApiOperation(value = "测试第一个接口查询", notes = "测试第一个接口查询")
	public Result<List<TestEntity>> test(@RequestParam(value = "param", required = true) String param) {
		log.info("测试接口");
		return Result.createOK(testService.testMybatiesPlus(param));
	}


	@GetMapping("/rabbitmqSendMsg")
	@ApiOperation(value = "测试rabbitmq发送消息", notes = "测试rabbitmq发送消息")
	public Result<List<TestEntity>> rabbitmqSendMsg(@RequestParam(value = "param", required = true) String param
			, @RequestParam(value = "age", required = true) Integer age) {

		UserMQ userMQ = new UserMQ();
		userMQ.setSex(10);
		userMQ.setAge(age);
		userMQ.setDesc(param);
		userMQ.setCtime(LocalDateTimeUtil.format(LocalDateTime.now(), NORM_DATETIME_FORMATTER));

		producerService.sendMsg(userMQ, TestMqConstant.TEST_ZHILIAN_EX, TestMqConstant.TEST_ZHILIAN_RK);

		return Result.createOK();
	}


	@Resource
	private StrategyFactory<Integer, ApproveStrategy> approveStrategyFactory;

	/**
	 * 测试策略模式的接口
	 *
	 * @param status
	 * @return
	 */
	@GetMapping("/strategy")
	@ApiOperation(value = "strategy测试策略模式", notes = "strategy测试策略模式")
	public Result<List<TestEntity>> strategy(@RequestParam(value = "status", required = true) Integer status) {
		log.info("strategy测试策略模式");
		String s = approveStrategyFactory.getStrategy(status, true).approveByRefundStatus(new Object(), new Object(), Maps.newHashMap());
		System.out.println("strategy测试策略模式返回结果" + s);
		return Result.createOK();
	}

	/**
	 * 测试 ThreadLocal在多线程情况下子线程丢失父线程信息的问题
	 *
	 * The Stack and the Map are managed per thread and are based on ThreadLocal by default. The Map can be configured to use an InheritableThreadLocal (see the Configuration section). When configured this way, the contents of the Map will be passed to child threads. However, as discussed in the Executors class and in other cases where thread pooling is utilized, the ThreadContext may not always be automatically passed to worker threads. In those cases the pooling mechanism should provide a means for doing so. The getContext() and cloneStack() methods can be used to obtain copies of the Map and Stack respectively.
	 *
	 * Note that all methods of the ThreadContext class are static.
	 *
	 *
	 *
	 * With the ThreadContext logging statements can be tagged so log entries that were related in some way can be linked via these tags. The limitation is that this only works for logging done on the same application thread (or child threads when configured).
	 *
	 * Some applications have a thread model that delegates work to other threads, and in such models, tagging attributes that are put into a thread-local map in one thread are not visible in the other threads and logging done in the other threads will not show these attributes.
	 *
	 * Log4j 2.7 adds a flexible mechanism to tag logging statements with context data coming from other sources than the ThreadContext. See the manual page on extending Log4j for details.
	 *
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		ThreadLocal<String> threadLocal = new ThreadLocal<String>();
		threadLocal.set("我是黄壮壮");
		log.info("[ThreadLocal下] 父线程的值：" + threadLocal.get());
		new Thread(new Runnable() {
			@Override
			public void run() {
				log.info("[ThreadLocal下] 子线程的值：" + threadLocal.get());
			}
		}).start();
		Thread.sleep(5000);

		System.out.println();

		ThreadLocal<String> inheritableThreadLocal = new InheritableThreadLocal<>();
		inheritableThreadLocal.set("我是黄壮壮");
		log.info("[InheritableThreadLocal下] 父线程的值：" + inheritableThreadLocal.get());

		List<Runnable> rs = Lists.newArrayList();
		for (int i = 0; i < 10; i++) {
			rs.add(new Runnable() {
				@Override
				public void run() {
					log.info("[InheritableThreadLocal下] 子线程的值：" + inheritableThreadLocal.get());
				}
			});
		}

		for (int j = 0; j < 10; j++) {
			CompletableFuture.runAsync(rs.get(j));
		}
		Thread.sleep(2000);
	}

	/**
	 * InheritableThreadLocal使用举例 注意 子线程设置的值 会覆盖父线程中的值 而 TransmittableThreadLocal不会
	 *
	 */
//	public static void main(String[] args) throws Exception{
//		final ThreadLocal<UserMQ> threadLocal=new InheritableThreadLocal<>();
//		threadLocal.set(new UserMQ("黄壮壮-父线程"));
//		System.out.println("初始值："+threadLocal.get());
//		Runnable runnable=()->{
//			System.out.println("----------start------------");
//			System.out.println("父线程的值："+threadLocal.get());
//			threadLocal.set(new UserMQ("黄壮壮-子线程"));
//			System.out.println("子线程覆盖后的值："+threadLocal.get());
//			System.out.println("------------end---------------");
//		};
//		ExecutorService executorService= Executors.newFixedThreadPool(1);
//
//		executorService.submit(runnable);
//		TimeUnit.SECONDS.sleep(1);
//
//		System.out.println();
//		executorService.submit(runnable);
//		TimeUnit.SECONDS.sleep(1);
//
//		System.out.println();
//		executorService.submit(runnable);
//	}

	/**
	 * TransmittableThreadLocalTest使用举例
	 *
	 * @param args
	 * @throws Exception
	 */
//	public static void main(String[] args) throws Exception{
//		final TransmittableThreadLocal<UserMQ> threadLocal=new TransmittableThreadLocal<>();
//		threadLocal.set(new UserMQ("黄壮壮-父线程"));
//		System.out.println("初始值："+threadLocal.get());
//		Runnable task=()->{
//			System.out.println("----------start------------");
//			System.out.println("父线程的值："+threadLocal.get());
//			threadLocal.set(new UserMQ("黄壮壮-子线程"));
//			System.out.println("子线程覆盖后的值："+threadLocal.get());
//			System.out.println("------------end---------------");
//		};
//		ExecutorService executorService= Executors.newFixedThreadPool(1);
//
//		//Runnable runnable= TtlRunnable.get(task);
//		Runnable runnable= task;
//
//		executorService.submit(runnable);
//		TimeUnit.SECONDS.sleep(1);
//
//		System.out.println();
//		executorService.submit(runnable);
//		TimeUnit.SECONDS.sleep(1);
//
//		System.out.println();
//		executorService.submit(runnable);
//	}
}
