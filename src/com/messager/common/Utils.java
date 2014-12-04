package com.messager.common;

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
		Long timestamp = Long.parseLong(timestampString) * 1000;
		String date = new SimpleDateFormat(formats).format(new Date(timestamp));
		return date;
	}

}
