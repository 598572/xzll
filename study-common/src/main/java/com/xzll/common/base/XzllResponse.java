package com.xzll.common.base;

import org.apache.commons.lang3.StringUtils;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/8/7 17:44
 * @Description:
 */
public class XzllResponse<T> {
	public static final int CODE_SUCCESS = 1;
	public static final int CODE_FAIL = -1;
	private int status;
	private String msg;
	private T data;

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

	public XzllResponse(int status, String msg, T data) {
		this.status = status;
		this.msg = msg;
		this.data = data;
	}

	public XzllResponse(int status, String msg) {
		this.status = status;
		this.msg = msg;
	}

	public XzllResponse() {
	}

	public T getData() {
		return this.data;
	}

	public void setData(T data) {
		this.data = data;
	}


	public static <T> XzllResponse<T> createOK() {
		return new XzllResponse(CODE_SUCCESS, "success");
	}

	public static <T> XzllResponse<T> createFail() {
		return new XzllResponse(CODE_FAIL,  StringUtils.EMPTY);
	}

	public static <T> XzllResponse<T> createFail(String message) {
		return new XzllResponse(CODE_FAIL, message);
	}

	public static <T> XzllResponse<T> createFail(int status, String message) {
		return new XzllResponse(status, message);
	}

	public static <T> XzllResponse<T> createFail(int status, String message, T data) {
		return new XzllResponse(status, message, data);
	}

	public static <T> XzllResponse<T> createFail(String message, T data) {
		return new XzllResponse(CODE_FAIL, message, data);
	}

	public static <T> XzllResponse<T> createOK(T data) {
		XzllResponse<T> ok = createOK();
		ok.setMsg("success");
		ok.setData(data);
		return ok;
	}

	@Override
	public String toString() {
		return " XzllResponse {" +
				"status=" + status +
				", msg='" + msg + '\'' +
				", data=" + data +
				'}';
	}
}
