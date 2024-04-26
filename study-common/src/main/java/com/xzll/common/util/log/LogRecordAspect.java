package com.xzll.common.util.log;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.*;


/**
 * 类名称：LogRecordAspect
 * 类描述：
 * 创建时间：2018年10月15日
 *
 * @author hzz
 * @version 1.0.0
 */
@Aspect
@Component
@Slf4j
public class LogRecordAspect {


	@Pointcut("execution(public * com.xzll.*.controller.*.*(..))")
	public void executeController() {
	}

	@Around("executeController()")
	public Object doAround(ProceedingJoinPoint controllerJoinPoint) throws Throwable {

		HttpServletRequest request = getHttpServletRequest();

		log.info("study-test access log begin, controller execute start, url:{}", request.getRequestURL().toString());

		Object[] args = controllerJoinPoint.getArgs();

		StopWatch stopWatch = new StopWatch();

		Object result = null;

		Signature signature = controllerJoinPoint.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		Method method = methodSignature.getMethod();
		try {
			stopWatch.start();
			result = controllerJoinPoint.proceed();
			return result;
		} finally {
			stopWatch.stop();
			String description = null;
			if (method.isAnnotationPresent(ApiOperation.class)) {
				ApiOperation log = method.getAnnotation(ApiOperation.class);
				description = log.value();
			}
			LoggerDTO loggerDTO = LoggerDTO.LoggerBuilder.BUILDER
					.setUrl(request.getRequestURL().toString())
					.setDescription(description == null ? "暂无描述" : description)
					.setHttpMethod(request.getMethod())
					.setParams(buildParams(request, args))
					.setResult(buildResult(result))
					.setExecuteTime(stopWatch.getLastTaskTimeMillis())
					.build();


			log.info(loggerDTO.toString());
		}
	}

	private String buildResult(Object result) {

		if (result == null) {
			return "RESULT IS NULL";
		}

		return JSON.toJSONString(result);
	}

	private String buildParams(HttpServletRequest request, Object[] args) {
		//获取请求参数集合并进行遍历拼接
		if (args.length <= 0) {
			return "NO PARAM";
		}
		if (Objects.equals(request.getMethod(), RequestMethod.POST.toString())) {
			//当需要过滤不需要的参数对象时这么作
//            if (args.length > 1) {
//                args = Stream.of(args).filter(x -> !(x instanceof User)).toArray();
//            }
			Map<String, Object> map = getKeyAndValue(args[0]);
			return JSON.toJSONString(map);
		} else if (Objects.equals(request.getMethod(), RequestMethod.GET.toString())) {
			String encodingString = "";
			try {
				String queryString = request.getQueryString();
				//将中文转码
				encodingString = URLDecoder.decode(queryString == null ? "" : queryString, "utf-8");
			} catch (UnsupportedEncodingException e) {
				log.error("UnsupportedEncodingException , message: {}", e.getMessage());
			}
			return encodingString;
		} else {
			return "OTHER HTTP METHOD:" + request.getMethod();
		}
	}

	private HttpServletRequest getHttpServletRequest() {
		return ((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes())).getRequest();
	}

	private Map<String, Object> getKeyAndValue(Object arg0) {

		Map<String, Object> map = new HashMap<>();

		List<Field> fieldList = new ArrayList<>();
		fillFieldList(fieldList, arg0.getClass());

		for (Field field : fieldList) {

			field.setAccessible(true); // 设置些属性是可以访问的

			try {
				Object val = field.get(arg0);
				// 得到此属性的值
				map.put(field.getName(), val);// 设置键值
			} catch (IllegalArgumentException | IllegalAccessException e) {
				log.warn("field analysis error.", e);
			}
		}

		return map;
	}

	private void fillFieldList(List<Field> fieldList, Class<?> clazz) {

		if (fieldList == null) {
			fieldList = new ArrayList<>();
		}

		fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));

		if (clazz.getSuperclass() == null) {
			return;
		}

		fillFieldList(fieldList, clazz.getSuperclass());
	}
}
