package com.alibaba.base.messager.service;

import java.io.IOException;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.base.messager.common.Constants;
import com.alibaba.base.messager.common.ServiceUtils;
import com.alibaba.base.messager.common.Utils;

/**
 * Servlet implementation class MessageServlet
 */
@WebServlet(urlPatterns = "/message/realtime", asyncSupported = true)
public class MessageRealtimeService extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory
			.getLogger(MessageRealtimeService.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MessageRealtimeService() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// 获取参数
		String receiverId = request.getParameter("userId");
		String callback = request.getParameter("jsonpCallback");
		// 参数校验
		// 用户id不能为空
		if (!Utils.isValidNumericStr(receiverId)) {
			ServiceUtils.respondError(response, Constants.ERROR_USERID);
			return;
		}
		// 回调函数不能为空
		if (Utils.isEmpty(callback)) {
			ServiceUtils.respondError(response, Constants.ERROR_CALLBACK);
			return;
		}

		AsyncContext asyncCtx = request.startAsync();
		asyncCtx.addListener(new AsyncListener());
		asyncCtx.setTimeout(Constants.TIMEOUT);

		logger.info("id put in map:" + receiverId + " callback:" + callback);

		ContextListener.mapUserIdContext.put(receiverId, asyncCtx);
	}
}
