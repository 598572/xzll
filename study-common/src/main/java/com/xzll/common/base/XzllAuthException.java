package com.xzll.common.base;

/**
 * @Auther: Huangzhuangzhuang
 * @Date: 2021/8/7 11:57
 * @Description:
 */

public class XzllAuthException extends XzllBaseException {
	private String msg;
	private Object errorObj;
	protected int code = 10012;

	public XzllAuthException(String msg) {
		super(msg);
	}

	public XzllAuthException(String msg, Object errorObj, int code) {
		super(msg, errorObj, code);
	}

	public XzllAuthException(String message, String msg, Object errorObj, int code) {
		super(message, msg, errorObj, code);
	}

	public XzllAuthException(String message, Throwable cause, String msg, Object errorObj, int code) {
		super(message, cause, msg, errorObj, code);
	}

	public XzllAuthException(Throwable cause, String msg, Object errorObj, int code) {
		super(cause, msg, errorObj, code);
	}

	public XzllAuthException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String msg, Object errorObj, int code) {
		super(message, cause, enableSuppression, writableStackTrace, msg, errorObj, code);
	}
}
