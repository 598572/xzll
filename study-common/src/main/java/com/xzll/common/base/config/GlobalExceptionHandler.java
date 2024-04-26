package com.xzll.common.base.config;

import com.xzll.common.base.XzllResponse;
import com.xzll.common.base.XzllAuthException;
import com.xzll.common.base.XzllBaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/8/7 18:55
 * @Description: 这个将会在后续完善，错误码枚举出来，且将异常日志 发送到钉钉机器人群(异步-基于事件的方式)
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

	private static final String BAD_REQUEST_MSG = "参数错误";

	/**
	 * 处理业务异常
	 *
	 * @param e
	 * @return
	 */
	@ResponseBody
	@ExceptionHandler(XzllBaseException.class)
	public XzllResponse<Object> baseExceptionHandler(XzllBaseException e, HttpServletRequest request) {
		return XzllResponse.createFail(e.getCode(), e.getMessage());
	}


	/**
	 * 处理请求参数异常
	 *
	 * @param e
	 * @return
	 */
	@ResponseBody
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public XzllResponse<Object> missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException e, HttpServletRequest request) {
		return XzllResponse.createFail(e.getParameterName() + BAD_REQUEST_MSG);
	}

	@ResponseBody
	@ExceptionHandler(XzllAuthException.class)
	public XzllResponse<Object> xzllAuthExceptionHandler(XzllAuthException e, HttpServletRequest request) {
		return XzllResponse.createFail(e.getMessage());
	}

	/**
	 * 处理 json 请求体调用接口校验失败抛出的异常
	 *
	 * @param e
	 * @return
	 */
	@ResponseBody
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public XzllResponse<Object> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e, HttpServletRequest request) {
		List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
		List<String> collect = fieldErrors.stream().map(o -> o.getDefaultMessage()).collect(Collectors.toList());
		return XzllResponse.createFail("10013", collect.toString());
	}


	/**
	 * 处理 form data方式调用接口校验失败抛出的异常
	 *
	 * @param e
	 * @return
	 */
	@ResponseBody
	@ExceptionHandler(BindException.class)
	public XzllResponse<Object> bindExceptionHandler(BindException e, HttpServletRequest request) {
		List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
		List<String> collect = fieldErrors.stream().map(o -> o.getDefaultMessage()).collect(Collectors.toList());
		return XzllResponse.createFail("10014", collect.toString());
	}

	/**
	 * 处理单个参数校验失败抛出的异常
	 *
	 * @param e
	 * @return
	 */
	@ResponseBody
	@ExceptionHandler(ConstraintViolationException.class)
	public XzllResponse<Object> constraintViolationExceptionHandler(ConstraintViolationException e, HttpServletRequest request) {
		Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
		List<String> collect = constraintViolations.stream().map(o -> o.getMessage()).collect(Collectors.toList());
		return XzllResponse.createFail("10015", collect.toString());
	}

	/**
	 * 处理以上处理不了的其他异常
	 *
	 * @param e
	 * @return
	 */
	@ResponseBody
	@ExceptionHandler(Exception.class)
	public XzllResponse<Object> exceptionHandler(Exception e, HttpServletRequest request) {
		log.error("unHandle exception: ", e);
		return XzllResponse.createFail("100011", "系统忙，请稍后再试");
	}

}
