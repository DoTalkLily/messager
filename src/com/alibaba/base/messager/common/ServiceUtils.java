package com.alibaba.base.messager.common;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletResponse;

import com.alibaba.base.messager.dataobject.ResultObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ServiceUtils {

	public static void respondResult(ServletResponse response, Object dataValue) {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		// 结果字符串
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
				.create();

		ResultObject result = new ResultObject(Constants.SUCCESS, dataValue, "");

		PrintWriter out = null;
		try {
			// 发送结果字符串
			out = response.getWriter();
			out.write(gson.toJson(result));
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	/**
	 * 以JSON格式输出
	 * 
	 * @param response
	 */
	public static void respondError(ServletResponse response, int code) {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		Gson gson = new Gson();
		ResultObject result = new ResultObject(code,
				Constants.ERROR_MESSAGE.get(code));

		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.write(gson.toJson(result));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
}
