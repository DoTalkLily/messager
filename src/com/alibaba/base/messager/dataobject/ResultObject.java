package com.alibaba.base.messager.dataobject;

/**
 * 返回给调用者的结果对象，方便封装json
 * 
 * @author li.lli
 * 
 */
public class ResultObject {

	private int code;

	private Object data;

	private String err_msg;// 为了返回的json格式，属性名不规则

	/*
	 * 有结果返回
	 */
	public ResultObject(int code, Object data, String errMsg) {
		this.code = code;
		this.data = data;
		this.err_msg = errMsg;
	}

	/*
	 * 错误情况
	 */
	public ResultObject(int code, String errMsg) {
		this.code = code;
		this.err_msg = errMsg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getErr_msg() {
		return err_msg;
	}

	public void setErr_msg(String err_msg) {
		this.err_msg = err_msg;
	}

}
