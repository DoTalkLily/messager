package com.alibaba.base.messager.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Constants {

	public static final int TIMEOUT = 15000;// 长轮询超时时间

	public static final int CORE_POOL_SIZE = 100; // 线程池中线程数

	public static final int MAX_POOL_SIZE = 1000;// 最大线程数

	public static final long MAX_WAIT_TIME = 50000L;// 空闲线程最长等待时长

	public static final int MESSAGE_NEW = 0;// 消息状态：新消息

	public static final int MESSAGE_UNREAD = 1;// 消息状态：未读

	public static final int MESSAGE_READEN = 2;// 消息状态：已读

	public static final int MESSAGE_DELETED = 3;// 消息状态：已删除

	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";// 日期类型

	public static final int ANNOUNCE = 1;// 消息类型：通知

	public static final int WARNING = 2;// 消息类型：警告

	public static final int ERROR = 3;// 消息类型：错误

	@SuppressWarnings("serial")
	public static List<Integer> MESSAGE_TYPE = new ArrayList<Integer>() {
		{
			add(ANNOUNCE);
			add(WARNING);
			add(ERROR);
		}
	};

	// 状态码和对应提示信息
	public static final int SUCCESS = 200;

	public static final int ERROR_TITLE = 400;

	public static final int ERROR_CONTENT = 401;

	public static final int ERROR_SENDER = 402;

	public static final int ERROR_RECEIVER = 403;

	public static final int ERROR_MESSAGE_TYPE = 404;

	public static final int ERROR_USERID = 405;

	public static final int ERROR_STATUS = 406;

	public static final int ERROR_TIME_INTERVAL = 407;

	public static final int ERROR_OFFSET_LIMIT = 408;

	public static final int ERROR_MESSAGE_ID = 409;

	public static final int ERROR_CALLBACK = 410;

	public static final int ERROR_SERVER = 500;

	@SuppressWarnings("serial")
	public static final Map<Integer, String> ERROR_MESSAGE = new HashMap<Integer, String>() {
		{
			put(ERROR_TITLE, "消息标题不能为空");
			put(ERROR_CONTENT, "消息内容不能为空");
			put(ERROR_SENDER, "发送者不能为空");
			put(ERROR_RECEIVER, "接收者不能为空");
			put(ERROR_MESSAGE_TYPE, "消息类型不正确");
			put(ERROR_USERID, "用户id不能为空");
			put(ERROR_SERVER, "服务器异常");
			put(ERROR_STATUS, "消息状态不正确");
			put(ERROR_TIME_INTERVAL, "开始时间或结束时间错误");
			put(ERROR_OFFSET_LIMIT, "offset或limit错误");
			put(ERROR_MESSAGE_ID, "消息id格式不正确");
			put(ERROR_CALLBACK, "回调函数不能为空");
		}
	};
}
