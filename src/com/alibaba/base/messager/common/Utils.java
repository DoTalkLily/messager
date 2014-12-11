package com.alibaba.base.messager.common;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

	/*
	 * 是否是数字
	 */
	public static boolean isNumeric(String str) {
		if (str.matches("\\d*")) {
			return true;
		}
		return false;
	}

	/*
	 * 是否为空或者只包含空格
	 */
	public static boolean isEmpty(String str) {
		return (str == null || str.trim().length() == 0);
	}

	/*
	 * 是否是数字类型的字符串
	 */
	public static boolean isValidNumericStr(String str) {
		if (isEmpty(str) || !isNumeric(str)) {
			return false;
		}
		return true;
	}

	/*
	 * unix时间戳转固定日期格式 yyyy-MM-dd HH:mm:ss
	 */
	public static String timeStamp2Date(String timestampString, String formats) {
		Long timestamp = Long.parseLong(timestampString);
		String date = new SimpleDateFormat(formats).format(new Date(timestamp));
		return date;
	}

	/*
	 * 编码转换，处理get获得的参数中文乱码情况
	 */
	public static String encode(String param) {
		if (param == null) {
			return null;
		}

		try {
			return new String(param.getBytes("ISO-8859-1"), "utf8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
}
