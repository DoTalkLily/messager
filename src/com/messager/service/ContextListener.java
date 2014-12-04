package com.messager.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.AsyncContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.messager.common.Constants;

@WebListener
public class ContextListener implements ServletContextListener {

	public static ThreadPoolExecutor executor;// 线程池

	public static Map<Long, AsyncContext> mapUserIdContext;// 等待新消息的用户id——AsyncContext映射

	public void contextInitialized(ServletContextEvent servletContextEvent) {
		// 创建线程池
		executor = new ThreadPoolExecutor(Constants.CORE_POOL_SIZE,
				Constants.MAX_POOL_SIZE, Constants.MAX_WAIT_TIME,
				TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(
						Constants.CORE_POOL_SIZE));

		mapUserIdContext = new HashMap<Long, AsyncContext>(
				Constants.CORE_POOL_SIZE);
	}

	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		ThreadPoolExecutor executor = (ThreadPoolExecutor) servletContextEvent
				.getServletContext().getAttribute("executor");
		executor.shutdown();

		if (mapUserIdContext != null) {
			mapUserIdContext.clear();
		}
	}
}
