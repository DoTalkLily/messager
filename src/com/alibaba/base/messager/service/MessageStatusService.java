package com.alibaba.base.messager.service;

import java.io.IOException;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.base.messager.common.Constants;
import com.alibaba.base.messager.common.ServiceUtils;
import com.alibaba.base.messager.common.Utils;
import com.alibaba.base.messager.processor.UpdateProcessor;

@WebServlet(urlPatterns = "/message/status", asyncSupported = true)
public class MessageStatusService extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// private static final Logger logger = LoggerFactory
	// .getLogger(MessageService.class);

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String receiverId = request.getParameter("userId");
		String[] messageIds = request.getParameterValues("messageIds");
		String toStatus = request.getParameter("toStatus");

		// 用户id不能为空
		if (!Utils.isValidNumericStr(receiverId)) {
			ServiceUtils.respondError(response, Constants.ERROR_USERID);
			return;
		}
		// 消息列表不为空
		if (messageIds == null || messageIds.length == 0) {
			ServiceUtils.respondError(response, Constants.ERROR_MESSAGE_ID);
			return;
		}

		long[] ids = new long[messageIds.length];

		for (int i = 0; i < messageIds.length; i++) {
			if (!Utils.isNumeric(messageIds[i])) {
				ServiceUtils.respondError(response, Constants.ERROR_MESSAGE_ID);
				return;
			}
			ids[i] = Long.parseLong(messageIds[i]);
		}

		if (!Utils.isValidNumericStr(toStatus)
				|| !Constants.MESSAGE_TYPE.contains(Integer.parseInt(toStatus))) {
			ServiceUtils.respondError(response, Constants.ERROR_STATUS);
			return;
		}

		AsyncContext asyncCtx = request.startAsync();
		asyncCtx.addListener(new AsyncListener());
		asyncCtx.setTimeout(Constants.TIMEOUT);

		ContextListener.executor.execute(new UpdateProcessor(asyncCtx, Long
				.parseLong(receiverId), ids, Integer.parseInt(toStatus)));
	}
}
