package com.xzll.common.base;

import org.apache.commons.lang3.StringUtils;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/8/7 17:44
 * @Description:
 */
public class Result<T> {
	public static final int CODE_SUCCESS = 0;
	public static final int CODE_FAIL = 1;
	private int status;
	private String msg;
	private T data;
	private Object errorData;

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMsg() {
		return this.msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Result(int status, String msg, T data) {
		this.status = status;
		this.msg = msg;
		this.data = data;
	}

	public Result(int status, String msg) {
		this.status = status;
		this.msg = msg;
	}

	public Result() {
	}

	public T getData() {
		return this.data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public Object getErrorData() {
		return this.errorData;
	}

	public void setErrorData(Object errorData) {
		this.errorData = errorData;
	}

	public static <T> Result<T> createOK() {
		return new Result(0, StringUtils.EMPTY);
	}

	public static <T> Result<T> createFail() {
		return new Result(1,  StringUtils.EMPTY);
	}

	public static <T> Result<T> createFail(String message) {
		return new Result(1, message);
	}

	public static <T> Result<T> createFail(int status, String message) {
		return new Result(status, message);
	}

	public static <T> Result<T> createFail(int status, String message, T data) {
		return new Result(status, message, data);
	}

	public static <T> Result<T> createFail(String message, T data) {
		return new Result(1, message, data);
	}

	public static <T> Result<T> createOK(T data) {
		Result<T> ok = createOK();
		ok.setMsg("数据访问成功");
		ok.setData(data);
		return ok;
	}

	@Override
	public String toString() {
		return "Result{" +
				"status=" + status +
				", msg='" + msg + '\'' +
				", data=" + data +
				", errorData=" + errorData +
				'}';
	}
}
