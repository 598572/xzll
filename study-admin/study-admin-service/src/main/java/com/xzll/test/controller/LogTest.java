package com.xzll.test.controller;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xzll.test.entity.People;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @Author: hzz
 * @Date: 2021/11/5 13:32:35
 * @Description:
 */
public class LogTest {



	private static Logger logger = LoggerFactory.getLogger(LogTest.class);

//	public static void main(String[] args) {
//		long startTimer = System.currentTimeMillis();
//		for (int i = 0; i < 2000000; i++) {
//			logger.info("i: " + i + " j:" + (i + 1));
//
//		}
//		System.out.println(System.currentTimeMillis() - startTimer + "ms");
//		//2000000次循环 44698ms
//		//200000次循环 4639ms
//		//200次循环 23ms
//	}

	public static void main(String[] args) throws ParseException, InterruptedException {






		Integer integer1 = Integer.valueOf(DateTimeFormatter.ofPattern(DatePattern.PURE_DATE_PATTERN).format(LocalDate.now()));
		Integer integer2 = Integer.valueOf(DateTimeFormatter.ofPattern(DatePattern.PURE_DATE_PATTERN).format(LocalDate.now().minusMonths(6)));

		System.out.println(integer1);

		System.out.println(integer2);


		String k="20210619";
		String k2="20211219";
		String s1 = k.toString().substring(0, 4) + "-" + k.toString().substring(4, 6) + "-" + k.toString().substring(6, 8);
		String s2 = k2.toString().substring(0, 4) + "-" + k2.toString().substring(4, 6) + "-" + k2.toString().substring(6, 8);
		System.out.println(s1);
		System.out.println(s2);


		List<String> objects3 = new ArrayList<>();
		objects3.add("2021-10-13");
		objects3.add("2021-10-12");
		objects3.add("2021-10-11");
		objects3.add("2021-10-10");
		objects3.add("2021-10-09");
		objects3.add("2021-10-08");

		Collections.sort(objects3,Collections.reverseOrder());
		System.out.println(objects3);



		int sum = 0;

		List<Integer> objects2 = new ArrayList<>();
		objects2.add(4);
		for (Integer crowdLimit : objects2) {
			double pow = Math.pow(2, crowdLimit - 1);
			sum += (int) pow;
		}

		Integer roleType = sum;
		//无人群限制,1顺风车新车主,2顺风车老车主,3顺风车新乘客,4顺风车老乘客
		List<Integer> list = new ArrayList<>();
		if (roleType == 15) {
			System.out.println(list);
		}
		for (int i = 0; i <= 3; i++) {
			if ((roleType >> i & 1) == 1) {
				list.add(i + 1);
			}
		}
		System.out.println(list);

		String str = "10110001944, 1011000522, 10110008336, 10110001744, 1021000786, 10210006232, 10110003346, 1011000393, 10210001433, 1021000918, 1011009313955, 10210006339, 1021000776, 1011005849052, 1011000789, 1011009313962, 10210000138, 10110008832, 10210003723, 10210001035, 10210005135, 10210003341, 1011009314193, 10110003325, 10210001335, 10210007540, 10210000312, 10110001330, 1021000816, 10210003641, 10210001532, 1011009314269, 1021000365, 10110003438, 10210000933, 10210000234, 10110000928, 10210005227, 10210009131, 1021009314099, 10110000932, 10110006629, 10110009637";

		String[] split = str.split(",");
		List<String> strings = Arrays.asList(split);
		StringBuffer stringBuffer = new StringBuffer();

		strings.forEach(x -> {
			String s = "\",\"" + x.trim();
			stringBuffer.append(s);
		});
		System.out.println(stringBuffer);


		for (int i = 0; i < 10; i++) {
			//System.out.println("第"+i+"此循环");
//			String s = RandomUtil.randomString(10);
//			System.out.println(s);
		}

		People people = new People();
		people.setName("王雅琴");
		people.setAddress("河北");
		people.setAge(18);


		System.out.println(people);


		List<String> objects1 = new ArrayList<>();
		objects1.add("2");
		objects1.add("3");
		objects1.add("4");
		String format4 = String.format("{\n" +
				"        '$match': {\n" +
				"            pushMsgId: { $in: [%s] }\n" +
				"        }\n" +
				"    }", objects1);//String.join(",",objects1)
		System.out.println(format4);


		int gte = 55;
		int lte = 60;
		String format3 = String.format(",{minutes:{$gte:%d,$lte:%d}}", Math.max(gte, 0), (lte >= 60 ? 59 : lte));
		System.out.println(format3);


		Integer integer = new Integer(2);
		String format2 = String.format("wwww_%s_pppp", "");
		System.out.println(format2);

		Optional.ofNullable(null).ifPresent(x -> {
			System.out.println(x);
		});


		DateTimeFormatter df333 = DateTimeFormatter.ofPattern(DatePattern.PURE_DATE_PATTERN);

		String format1 = df333.format(LocalDate.now());
		System.out.println(format1);

		ZoneId zone = ZoneId.systemDefault();
		long timestamp = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();


		Date date1 = new Date((timestamp));
		System.out.println(date1);


		SimpleDateFormat format222 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		format222.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));
		Date parse = format222.parse("2021-12-08T12:00:00Z", new ParsePosition(0));
		System.out.println(parse);


		long l2 = System.currentTimeMillis();
		LocalDateTime localDateTime2 = LocalDateTime.now().minusHours(12);
		Long milliSecond = localDateTime2.toInstant(ZoneOffset.of("+8")).toEpochMilli();
		System.out.println(l2);
		System.out.println(milliSecond);

		List<Integer> objects = new ArrayList<>();
		objects.add(1);
		objects.add(2);
		objects.add(3);
		objects.add(4);
		objects.add(5);
		objects.add(6);
		objects.add(7);
		objects.add(8);
		objects.add(9);
		objects.add(10);
		objects.add(11);


		List<List<Integer>> subList = Lists.partition(objects, 10);
		System.out.println(subList);

		List<List<Integer>> subList2 = Lists.partition(objects, 5);
		System.out.println(subList2);

		List<List<Integer>> subList3 = Lists.partition(objects, 3);
		System.out.println(subList3);

		//2021-12-01T00:00:00Z  2021-12-10T00:00:00Z
		LocalDate date = LocalDate.now();
		LocalTime hourStart = LocalTime.of(LocalDateTime.now().minusHours(1).getHour(), 0, 0);
		LocalTime hourEnd = LocalTime.of(LocalDateTime.now().getHour(), 0, 0);
		LocalDateTime startLocalDateTime = LocalDateTime.of(date, hourStart);
		LocalDateTime endLocalDateTime = LocalDateTime.of(date, hourEnd);
		DateTimeFormatter df = DateTimeFormatter.ofPattern(DatePattern.UTC_PATTERN);

		df.format(startLocalDateTime);
		String format = String.format("{$match: {\n" +
				"            pushType: 1,pushMsgId:{$ne:null}, createTime: { $gte: ISODate('%s'), $lt: ISODate('%s') }        }}", "123", "456");//df.format(endLocalDateTime)

		System.out.println(format);
//
//		LocalDate date = LocalDate.now();
//		LocalTime hourStart = LocalTime.of(LocalDateTime.now().minusHours(1).getHour(), 0, 0,0);
//		LocalTime hourEnd = LocalTime.of(LocalDateTime.now().getHour(), 0 ,0,0);
//
//		LocalDateTime startTime = LocalDateTime.of(date, hourStart);
//		LocalDateTime endTime = LocalDateTime.of(date, hourEnd);
//
//		DateTimeFormatter df = DateTimeFormatter.ofPattern(DatePattern.UTC_PATTERN);
//		String format = df.format(startTime);
//		String end = df.format(endTime);
//
//
//		System.out.println(format);
//		System.out.println(end);
		//自定义目标日期的格式化方式
		DateTimeFormatter f2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		//将格式化后的LocalDateTime转成字符串
		String transTime = f2.format(date);


		LocalDateTime localDateTime = LocalDateTime.now().minusHours(1);
		int hour = localDateTime.getHour();
		System.out.println(hour);

		LocalDateTime now = LocalDateTime.now();
		System.out.println(now);

		Map<Long, String> map = null;
		Map<Long, String> longStringMap = Optional.ofNullable(map).orElse(Maps.newConcurrentMap());
		System.out.println(longStringMap);


//		Long msgId = Long.valueOf(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.PURE_DATETIME_MS_PATTERN)));
//		System.out.println(msgId);
		long l = System.nanoTime();
		System.out.println(l);

		String wewe = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.PURE_DATETIME_MS_PATTERN)) + "_" + UUID.randomUUID();
		System.out.println(wewe);

		int i = RandomUtil.randomInt(1, Integer.MAX_VALUE);
		System.out.println();

		DjOrderServiceDrivingorderStationDaily djOrderServiceDrivingorderStationDaily = new DjOrderServiceDrivingorderStationDaily();

		String s = JSON.toJSONString(djOrderServiceDrivingorderStationDaily);
		System.out.println(s);
		System.out.println();

//		try {
//			int e=1/0;
//		}catch (Exception e){
//			logger.error("error:",new Exception());
//
//
//			logger.error("errorMsg:{} ,Exception:",e.getMessage(), e);
////			logger.error("Exception:{}",e);
//		}


//		long startTimer = System.currentTimeMillis();
//		for (int i = 0; i < 200000; i++) {
//			logger.info("i:{} j:{}" , i ,(i+1));
//		}
//		System.out.println(System.currentTimeMillis() - startTimer + "ms");
		//2000000次循环 45869ms
		//200000次循环 4556ms
		//200次循环 24ms
	}

}
