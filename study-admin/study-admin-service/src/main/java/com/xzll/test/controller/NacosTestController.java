package com.xzll.test.controller;

import com.xzll.common.util.SpringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/5/22 18:10
 * @Description:
 */
@RestController
@RequestMapping("/config")
//动态刷新nacos配置 实现热加载
@RefreshScope
@Api(tags = "动态刷新nacos配置 实现热加载-测试")
@EnableConfigurationProperties

public class NacosTestController {

	@Value("${useLocalCache:false}")
	private boolean useLocalCache;

	private static String host = null;

	/**
	 * 演示从配置中心(或者本地的配置文件，总之就是配置文件) 读取配置的一种方式 即使用 Environment。
	 * <p>
	 * 不使用
	 *
	 * @Value 或
	 * @Component
	 * @ConfigurationProperties(prefix = "datasource")
	 * <p>
	 * 或
	 * @PropertySource(value = { "classpath:META-INF/application.properties" })  的方式
	 */
	public static void init() {
		Environment bean = SpringUtil.getBean(Environment.class);
		String[] activeProfiles1 = bean.getActiveProfiles();
		String host = bean.getProperty("redis.host");//假如properties文件中已经配置了该key对应的值。
		host = host;
	}

	@GetMapping("/get")
	@ApiOperation(value = "动态刷新nacos配置 实现热加载-测试", notes = "动态刷新nacos配置 实现热加载-测试")
	public boolean get() {
		return useLocalCache;
	}


//	public static void main(String[] args) {
//
//
//		String str = "[{\"pushMsgId\":\"6204d279f21f553bbec81736\",\"regId\":\"OPPO_CN_c7dc26b9ba056e5ef7652af93bc888e5\",\"validTargetCount\":1,\"sendCount\":1,\"arriveCount\":1,\"showCount\":0,\"clickCount\":0},{\"pushMsgId\":\"6204d279f21f553bbec81736\",\"regId\":\"OPPO_CN_b33b3b8ebaeaaebae86674b79a8ba724\",\"validTargetCount\":1,\"sendCount\":1,\"arriveCount\":1,\"showCount\":0,\"clickCount\":0},{\"pushMsgId\":\"6204d279f21f553bbec81736\",\"regId\":\"OPPO_CN_a95f0a505ea105d5cf86b80563b24cef\",\"validTargetCount\":1,\"sendCount\":1,\"arriveCount\":0,\"showCount\":0,\"clickCount\":0},{\"pushMsgId\":\"6204cc2af21f553bbec1aeea\",\"regId\":\"OPPO_CN_81d0d99bd2f6fa4a672f1911c6df108d\",\"validTargetCount\":1,\"sendCount\":1,\"arriveCount\":0,\"showCount\":0,\"clickCount\":0},{\"pushMsgId\":\"6204cc2af21f553bbec1aeea\",\"regId\":\"OPPO_CN_39d5f4ee192b450acdee903aa73049ac\",\"validTargetCount\":1,\"sendCount\":1,\"arriveCount\":0,\"showCount\":0,\"clickCount\":0},{\"pushMsgId\":\"6204cc2af21f553bbec1aeea\",\"regId\":\"OPPO_CN_02e7a2bce264d217e27937317b471c4d\",\"validTargetCount\":1,\"sendCount\":1,\"arriveCount\":0,\"showCount\":0,\"clickCount\":0},{\"pushMsgId\":\"6204cc2af21f553bbec1aeea\",\"regId\":\"OPPO_CN_5b3213554f94ddfceb79288f0d58f2ae\",\"validTargetCount\":1,\"sendCount\":1,\"arriveCount\":0,\"showCount\":0,\"clickCount\":0},{\"pushMsgId\":\"6204d279f21f553bbec81736\",\"regId\":\"OPPO_CN_5b3213554f94ddfceb79288f0d58f2ae\",\"validTargetCount\":1,\"sendCount\":1,\"arriveCount\":0,\"showCount\":0,\"clickCount\":0},{\"pushMsgId\":\"6204cc2af21f553bbec1aeea\",\"regId\":\"OPPO_CN_c7dc26b9ba056e5ef7652af93bc888e5\",\"validTargetCount\":1,\"sendCount\":1,\"arriveCount\":1,\"showCount\":0,\"clickCount\":0},{\"pushMsgId\":\"6204d279f21f553bbec81736\",\"regId\":\"OPPO_CN_39d5f4ee192b450acdee903aa73049ac\",\"validTargetCount\":1,\"sendCount\":1,\"arriveCount\":0,\"showCount\":0,\"clickCount\":0},{\"pushMsgId\":\"6204cc2af21f553bbec1aeea\",\"regId\":\"OPPO_CN_a95f0a505ea105d5cf86b80563b24cef\",\"validTargetCount\":1,\"sendCount\":1,\"arriveCount\":0,\"showCount\":0,\"clickCount\":0},{\"pushMsgId\":\"6204d279f21f553bbec81736\",\"regId\":\"OPPO_CN_02e7a2bce264d217e27937317b471c4d\",\"validTargetCount\":1,\"sendCount\":1,\"arriveCount\":0,\"showCount\":0,\"clickCount\":0},{\"pushMsgId\":\"6204d279f21f553bbec81736\",\"regId\":\"OPPO_CN_81d0d99bd2f6fa4a672f1911c6df108d\",\"validTargetCount\":1,\"sendCount\":1,\"arriveCount\":0,\"showCount\":0,\"clickCount\":0},{\"pushMsgId\":\"psd_inner_45e22c5e261c47fca3e224588252edf6\",\"regId\":\"a9df7c446081118a0fcad697684198adf1a94a0ec806b4f5b0be4cbd3741d75f\",\"validTargetCount\":0,\"sendCount\":0,\"arriveCount\":0,\"showCount\":0,\"clickCount\":0},{\"pushMsgId\":\"psd_inner_cb4cf8b7e69e4f39a7c554ecf4977795\",\"regId\":\"0a7e5b845db8f78d28f424416f02261603e05b2b1b07b20de046d722fe397b1f\",\"validTargetCount\":0,\"sendCount\":0,\"arriveCount\":0,\"showCount\":0,\"clickCount\":0},{\"pushMsgId\":\"164448319243319069015901\",\"regId\":\"IQAAAACy0dnjAACHo2hO4bDWFpTHk6qIOdbAmFvigLd5N6wBgYbdIURPz6YNGtGzGshfOh5RG_eBkGlWxT96M0N9lMSvHF6kiLvKjzrIuaJcGTs4Iw\",\"validTargetCount\":1,\"sendCount\":1,\"arriveCount\":0,\"showCount\":0,\"clickCount\":0},{\"pushMsgId\":\"164448319243319069015901\",\"regId\":\"IQAAAACy0dnjAABBhKugKEHRk_hfwg8z2aGRA6SLGfM-dFz7mTQ1sHfoDMjkq8Gj7bQWY56xi86Ir68vOW8ns1ZhgjfWtWuRQaYTYwx3pUFMzvtu_g\",\"validTargetCount\":1,\"sendCount\":1,\"arriveCount\":0,\"showCount\":0,\"clickCount\":0},{\"pushMsgId\":\"164448157721492430001901\",\"regId\":\"IQAAAACy0dnjAABBhKugKEHRk_hfwg8z2aGRA6SLGfM-dFz7mTQ1sHfoDMjkq8Gj7bQWY56xi86Ir68vOW8ns1ZhgjfWtWuRQaYTYwx3pUFMzvtu_g\",\"validTargetCount\":1,\"sendCount\":1,\"arriveCount\":0,\"showCount\":0,\"clickCount\":0},{\"pushMsgId\":\"6204cc2af21f553bbec1aeea\",\"regId\":\"OPPO_CN_81d0d99bd2f6fa4a672f1911c6df108d\",\"validTargetCount\":1,\"sendCount\":1,\"arriveCount\":0,\"showCount\":0,\"clickCount\":0},{\"pushMsgId\":\"6204d279f21f553bbec81736\",\"regId\":\"OPPO_CN_c7dc26b9ba056e5ef7652af93bc888e5\",\"validTargetCount\":1,\"sendCount\":1,\"arriveCount\":1,\"showCount\":0,\"clickCount\":0},{\"pushMsgId\":\"psd_inner_660b7394a0cb4122815c96fa8566bbaf\",\"regId\":\"27117a54082c296adb49c18300311cdc29dc19726b49096d469a4ac1fb5e2d81\",\"validTargetCount\":0,\"sendCount\":0,\"arriveCount\":0,\"showCount\":0,\"clickCount\":0},{\"pushMsgId\":\"164448157721492430001901\",\"regId\":\"IQAAAACy0dnjAACHo2hO4bDWFpTHk6qIOdbAmFvigLd5N6wBgYbdIURPz6YNGtGzGshfOh5RG_eBkGlWxT96M0N9lMSvHF6kiLvKjzrIuaJcGTs4Iw\",\"validTargetCount\":1,\"sendCount\":1,\"arriveCount\":0,\"showCount\":0,\"clickCount\":0},{\"pushMsgId\":\"6204cc2af21f553bbec1aeea\",\"regId\":\"OPPO_CN_c7dc26b9ba056e5ef7652af93bc888e5\",\"validTargetCount\":1,\"sendCount\":1,\"arriveCount\":1,\"showCount\":0,\"clickCount\":0},{\"pushMsgId\":\"6204d279f21f553bbec81736\",\"regId\":\"OPPO_CN_81d0d99bd2f6fa4a672f1911c6df108d\",\"validTargetCount\":1,\"sendCount\":1,\"arriveCount\":0,\"showCount\":0,\"clickCount\":0},{\"pushMsgId\":\"psd_inner_01ecd10b995c4e18a52c779061d91716\",\"regId\":\"a242c6c8af37631789efa6a9c162fe8cb3a3ac881ea37f11f7e5ad82a6776e39\",\"validTargetCount\":0,\"sendCount\":0,\"arriveCount\":0,\"showCount\":0,\"clickCount\":0},{\"pushMsgId\":\"psd_inner_89ddb1d853a84bcdb1f01ad7d4d8ba41\",\"regId\":\"f2ff5a1295903d9bd31742341515a467ad32f2911d71ec530cdefdc9340d1a05\",\"validTargetCount\":0,\"sendCount\":0,\"arriveCount\":0,\"showCount\":0,\"clickCount\":0},{\"pushMsgId\":\"psd_inner_1e3ff4c17ee546d0aaeac11769bcb38f\",\"regId\":\"fe2ac51e9907b3e8aeecf5c66f500b1cdad6465c10deb2f7ddc0d7e44daa01c0\",\"validTargetCount\":0,\"sendCount\":0,\"arriveCount\":0,\"showCount\":0,\"clickCount\":0},{\"pushMsgId\":\"psd_inner_cca7f8d3656a40e1bbb9a348eeb5e1db\",\"regId\":\"36f593e146f8ca50c3c7c11f6032d8813ae56c3f867b637729be39b0f727140d\",\"validTargetCount\":0,\"sendCount\":0,\"arriveCount\":0,\"showCount\":0,\"clickCount\":0},{\"pushMsgId\":\"psd_inner_7f8cea52a7a74d2595fd40a3eaab42c5\",\"regId\":\"e80d5228d9b47fcbfe66c2368c7582c01be319e9cfcd85ba1b8c2261b38b7b03\",\"validTargetCount\":0,\"sendCount\":0,\"arriveCount\":0,\"showCount\":0,\"clickCount\":0},{\"pushMsgId\":\"164448157721492430001901\",\"regId\":\"AFb45Pw6OStTS2FYcuInxkZWibl7gBO2qSRCXFKYtsgUmYFdX9JYH4M5mSfV6SEmcUH-swt2_kbAWounQCXz6C1h5o-68oGfHyfgjuzKRp3ZGcw9isjP4TJuLTaelJ2ClA\",\"validTargetCount\":1,\"sendCount\":1,\"arriveCount\":0,\"showCount\":0,\"clickCount\":0},{\"pushMsgId\":\"psd_inner_3e42c958ed2847cc9537eab57d5b53d0\",\"regId\":\"6b0d9bf5b02d44f189abe32686ca6b77f7d6ca757fc31b787b93fd3b6150ef05\",\"validTargetCount\":0,\"sendCount\":0,\"arriveCount\":0,\"showCount\":0,\"clickCount\":0},{\"pushMsgId\":\"164448157721492430001901\",\"regId\":\"IQAAAACy0dnjAACLZVEVLFHNira5LrbILTZoX7cvCoMkOCJZ6eTwWeY1LoDbKUupcC-d4Scr_jKLnYvn79WR9Xg1vu8cBx-MYMyfhrQ42YBzj703hA\",\"validTargetCount\":1,\"sendCount\":1,\"arriveCount\":0,\"showCount\":0,\"clickCount\":0},{\"pushMsgId\":\"164448157721492430001901\",\"regId\":\"IQAAAACy0dnjAACBJ_STCPBg4sKhV56DtpO3DEQK2snMvl8VuBYDxE1ML8mQNrJDLVAWvLSsjYtokiKQHAEpvdBODhOKIvDmP4DclV51olVcq3CyXg\",\"validTargetCount\":1,\"sendCount\":1,\"arriveCount\":0,\"showCount\":0,\"clickCount\":0},{\"pushMsgId\":\"164448319243319069015901\",\"regId\":\"IQAAAACy0dnjAACLZVEVLFHNira5LrbILTZoX7cvCoMkOCJZ6eTwWeY1LoDbKUupcC-d4Scr_jKLnYvn79WR9Xg1vu8cBx-MYMyfhrQ42YBzj703hA\",\"validTargetCount\":1,\"sendCount\":1,\"arriveCount\":0,\"showCount\":0,\"clickCount\":0},{\"pushMsgId\":\"164448319243319069015901\",\"regId\":\"IQAAAACy0dnjAACBJ_STCPBg4sKhV56DtpO3DEQK2snMvl8VuBYDxE1ML8mQNrJDLVAWvLSsjYtokiKQHAEpvdBODhOKIvDmP4DclV51olVcq3CyXg\",\"validTargetCount\":1,\"sendCount\":1,\"arriveCount\":0,\"showCount\":0,\"clickCount\":0},{\"pushMsgId\":\"psd_inner_34dacd24fc1e44dba0f211f0f6ba1135\",\"regId\":\"fc51f94ff0a014d774ff58710714c451123fd939b472c305ac2b4ee52cfd7c3c\",\"validTargetCount\":0,\"sendCount\":0,\"arriveCount\":0,\"showCount\":0,\"clickCount\":0},{\"pushMsgId\":\"164448319243319069015901\",\"regId\":\"IQAAAACy0dnjAADg74jJ5tuHTR6zb7l-eeizQAmUi0VFZtcWm71GqSH1nCHsdDFPmoSdtdNKwICuV9VJzKvKhcInC8B9RMFptQQWcVfd4ynnFQZ8Pg\",\"validTargetCount\":1,\"sendCount\":1,\"arriveCount\":0,\"showCount\":0,\"clickCount\":0},{\"pushMsgId\":\"164448157721492430001901\",\"regId\":\"IQAAAACy0dnjAADg74jJ5tuHTR6zb7l-eeizQAmUi0VFZtcWm71GqSH1nCHsdDFPmoSdtdNKwICuV9VJzKvKhcInC8B9RMFptQQWcVfd4ynnFQZ8Pg\",\"validTargetCount\":1,\"sendCount\":1,\"arriveCount\":0,\"showCount\":0,\"clickCount\":0},{\"pushMsgId\":\"164448319243319069015901\",\"regId\":\"AFb45Pw6OStTS2FYcuInxkZWibl7gBO2qSRCXFKYtsgUmYFdX9JYH4M5mSfV6SEmcUH-swt2_kbAWounQCXz6C1h5o-68oGfHyfgjuzKRp3ZGcw9isjP4TJuLTaelJ2ClA\",\"validTargetCount\":1,\"sendCount\":1,\"arriveCount\":0,\"showCount\":0,\"clickCount\":0},{\"pushMsgId\":\"6204d279f21f553bbec81736\",\"regId\":\"OPPO_CN_02e7a2bce264d217e27937317b471c4d\",\"validTargetCount\":1,\"sendCount\":1,\"arriveCount\":0,\"showCount\":0,\"clickCount\":0},{\"pushMsgId\":\"164448319243319069015901\",\"regId\":\"IQAAAACy0dnjAACHo2hO4bDWFpTHk6qIOdbAmFvigLd5N6wBgYbdIURPz6YNGtGzGshfOh5RG_eBkGlWxT96M0N9lMSvHF6kiLvKjzrIuaJcGTs4Iw\",\"validTargetCount\":1,\"sendCount\":1,\"arriveCount\":0,\"showCount\":0,\"clickCount\":0},{\"pushMsgId\":\"164448319243319069015901\",\"regId\":\"IQAAAACy0dnjAACBJ_STCPBg4sKhV56DtpO3DEQK2snMvl8VuBYDxE1ML8mQNrJDLVAWvLSsjYtokiKQHAEpvdBODhOKIvDmP4DclV51olVcq3CyXg\",\"validTargetCount\":1,\"sendCount\":1,\"arriveCount\":0,\"showCount\":0,\"clickCount\":0},{\"pushMsgId\":\"164448157721492430001901\",\"regId\":\"IQAAAACy0dnjAABBhKugKEHRk_hfwg8z2aGRA6SLGfM-dFz7mTQ1sHfoDMjkq8Gj7bQWY56xi86Ir68vOW8ns1ZhgjfWtWuRQaYTYwx3pUFMzvtu_g\",\"validTargetCount\":1,\"sendCount\":1,\"arriveCount\":0,\"showCount\":0,\"clickCount\":0},{\"pushMsgId\":\"164448157721492430001901\",\"regId\":\"IQAAAACy0dnjAACBJ_STCPBg4sKhV56DtpO3DEQK2snMvl8VuBYDxE1ML8mQNrJDLVAWvLSsjYtokiKQHAEpvdBODhOKIvDmP4DclV51olVcq3CyXg\",\"validTargetCount\":1,\"sendCount\":1,\"arriveCount\":0,\"showCount\":0,\"clickCount\":0},{\"pushMsgId\":\"psd_inner_1a82236779c84d56b8b922b123123eeb\",\"regId\":\"e52105513ebb29eabe1cc23d9a16f39d97e7e01d83d097ced2f6f68e5da77b01\",\"validTargetCount\":0,\"sendCount\":0,\"arriveCount\":0,\"showCount\":0,\"clickCount\":0},{\"pushMsgId\":\"164448157721492430001901\",\"regId\":\"AFb45Pw6OStTS2FYcuInxkZWibl7gBO2qSRCXFKYtsgUmYFdX9JYH4M5mSfV6SEmcUH-swt2_kbAWounQCXz6C1h5o-68oGfHyfgjuzKRp3ZGcw9isjP4TJuLTaelJ2ClA\",\"validTargetCount\":1,\"sendCount\":1,\"arriveCount\":0,\"showCount\":0,\"clickCount\":0},{\"pushMsgId\":\"164448157721492430001901\",\"regId\":\"0864033034738134300009725100CN01\",\"validTargetCount\":1,\"sendCount\":1,\"arriveCount\":0,\"showCount\":0,\"clickCount\":0},{\"pushMsgId\":\"164448157721492430001901\",\"regId\":\"IQAAAACy0dnjAACHo2hO4bDWFpTHk6qIOdbAmFvigLd5N6wBgYbdIURPz6YNGtGzGshfOh5RG_eBkGlWxT96M0N9lMSvHF6kiLvKjzrIuaJcGTs4Iw\",\"validTargetCount\":1,\"sendCount\":1,\"arriveCount\":0,\"showCount\":0,\"clickCount\":0},{\"pushMsgId\":\"164448319243319069015901\",\"regId\":\"IQAAAACy0dnjAADg74jJ5tuHTR6zb7l-eeizQAmUi0VFZtcWm71GqSH1nCHsdDFPmoSdtdNKwICuV9VJzKvKhcInC8B9RMFptQQWcVfd4ynnFQZ8Pg\",\"validTargetCount\":1,\"sendCount\":1,\"arriveCount\":0,\"showCount\":0,\"clickCount\":0},{\"pushMsgId\":\"164448319243319069015901\",\"regId\":\"IQAAAACy0dnjAABBhKugKEHRk_hfwg8z2aGRA6SLGfM-dFz7mTQ1sHfoDMjkq8Gj7bQWY56xi86Ir68vOW8ns1ZhgjfWtWuRQaYTYwx3pUFMzvtu_g\",\"validTargetCount\":1,\"sendCount\":1,\"arriveCount\":0,\"showCount\":0,\"clickCount\":0},{\"pushMsgId\":\"164448319243319069015901\",\"regId\":\"0864033034738134300009725100CN01\",\"validTargetCount\":1,\"sendCount\":1,\"arriveCount\":0,\"showCount\":0,\"clickCount\":0},{\"pushMsgId\":\"164448157721492430001901\",\"regId\":\"IQAAAACy0dnjAADg74jJ5tuHTR6zb7l-eeizQAmUi0VFZtcWm71GqSH1nCHsdDFPmoSdtdNKwICuV9VJzKvKhcInC8B9RMFptQQWcVfd4ynnFQZ8Pg\",\"validTargetCount\":1,\"sendCount\":1,\"arriveCount\":0,\"showCount\":0,\"clickCount\":0},{\"pushMsgId\":\"164448319243319069015901\",\"regId\":\"AFb45Pw6OStTS2FYcuInxkZWibl7gBO2qSRCXFKYtsgUmYFdX9JYH4M5mSfV6SEmcUH-swt2_kbAWounQCXz6C1h5o-68oGfHyfgjuzKRp3ZGcw9isjP4TJuLTaelJ2ClA\",\"validTargetCount\":1,\"sendCount\":1,\"arriveCount\":0,\"showCount\":0,\"clickCount\":0}]";
//		List<MessageHistoryTotalStatisticsPo> messageHistoryTotalStatisticsPos = JSONUtil.toList(str, MessageHistoryTotalStatisticsPo.class);
//
//		messageHistoryTotalStatisticsPos.stream().collect(Collectors.groupingBy(x -> x.getPushMsgId() + "_" + x.getRegId())).forEach((k, v) -> {
//			if (v.size() > 1) {
//				System.out.println("wocao ");
//				System.out.println(k);
//				System.out.println(v.size());
//			}
//		});
//
//
//
//		Map<String, MessageHistoryTotalStatisticsPo> collect =
//				messageHistoryTotalStatisticsPos.stream().collect(Collectors.toMap(k -> (k.getPushMsgId() + "_" + k.getRegId()), messageHistoryTotalStatisticslPo -> messageHistoryTotalStatisticslPo));
//
//		System.out.println(collect);
//
//
//	}

	public static void main(String[] args) throws IOException {
		DatagramChannel datagramChannel = DatagramChannel.open();

		datagramChannel.configureBlocking(false);
		datagramChannel.bind(new InetSocketAddress(10001));

		Selector selector = Selector.open();

		datagramChannel.register(selector, SelectionKey.OP_READ);
		while (selector.select() > 0){
			Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
			while (iterator.hasNext()){
				SelectionKey next = iterator.next();
				if (next.isReadable()){
					ByteBuffer allocate = ByteBuffer.allocate(1024);
					datagramChannel.receive(allocate);
					allocate.flip();
					System.out.println(new String(allocate.array(),0,allocate.limit()));
					allocate.clear();
				}
			}
			iterator.remove();
		}
	}
}
