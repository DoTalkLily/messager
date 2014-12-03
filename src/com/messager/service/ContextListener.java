package com.messager.service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.messager.common.Constants;

@WebListener
public class ContextListener implements ServletContextListener {

	public void contextInitialized(ServletContextEvent servletContextEvent) {
		// 创建线程池
		ThreadPoolExecutor executor = new ThreadPoolExecutor(
				Constants.CORE_POOL_SIZE, Constants.MAX_POOL_SIZE,
				Constants.MAX_WAIT_TIME, TimeUnit.MILLISECONDS,
				new ArrayBlockingQueue<Runnable>(Constants.CORE_POOL_SIZE));

		servletContextEvent.getServletContext().setAttribute("executor",
				executor);
	}

	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		ThreadPoolExecutor executor = (ThreadPoolExecutor) servletContextEvent
				.getServletContext().getAttribute("executor");
		executor.shutdown();
	}
}
