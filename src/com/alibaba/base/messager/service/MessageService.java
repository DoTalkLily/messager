package com.alibaba.base.messager.service;

import java.io.IOException;
import java.io.PrintWriter;

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
import com.alibaba.base.messager.dao.MessageOperation;
import com.alibaba.base.messager.dataobject.Message;
import com.alibaba.base.messager.dataobject.QueryObject;
import com.alibaba.base.messager.processor.QueryProcessor;
import com.google.gson.Gson;

/**
 * Servlet implementation class MessageServlet
 */
@WebServlet(urlPatterns = "/message", asyncSupported = true)
public class MessageService extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory
			.getLogger(MessageService.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MessageService() {
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
		String end = request.getParameter("endTime");
		String begin = request.getParameter("beginTime");
		String senderId = request.getParameter("senderId");
		String status = request.getParameter("status");
		String offset = request.getParameter("offset");
		String limit = request.getParameter("limit");
		String messageId = request.getParameter("messageId");
		String senderNick = Utils.encode(request.getParameter("senderNick"));
		String messageType = Utils.encode(request.getParameter("messageType"));

		QueryObject params = new QueryObject();
		AsyncContext asyncCtx = request.startAsync();
		asyncCtx.addListener(new AsyncListener());
		asyncCtx.setTimeout(Constants.TIMEOUT);

		// 参数校验
		// 用户id不能为空
		if (!Utils.isValidNumericStr(receiverId)) {
			ServiceUtils.respondError(response, Constants.ERROR_USERID);
			return;
		}

		params.setReceiverId(Long.parseLong(receiverId));

		// 如果消息id不为空,查询并返回消息体
		if (!Utils.isEmpty(messageId)) {
			if (!Utils.isNumeric(messageId)) {
				ServiceUtils.respondError(response, Constants.ERROR_MESSAGE_ID);
				return;
			}

			params.setMessageId(Long.parseLong(messageId));
			ContextListener.executor.execute(new QueryProcessor(asyncCtx,
					params));
			return;
		}

		// 如果发送者id不为空，判断是否为数字
		if (!Utils.isEmpty(senderId)) {
			if (!Utils.isNumeric(senderId)) {
				ServiceUtils.respondError(response, Constants.ERROR_SENDER);
				return;
			}

			params.setSenderId(Long.parseLong(senderId));
		}
		// 开始时间和结束时间必须同时指定
		if (!Utils.isEmpty(begin) && !Utils.isEmpty(end)) {

			if (!Utils.isNumeric(begin) || !Utils.isNumeric(end)
					|| begin.compareTo(end) > 0) {
				ServiceUtils.respondError(response,
						Constants.ERROR_TIME_INTERVAL);
				return;
			}

			end = Utils.timeStamp2Date(end, Constants.DATE_FORMAT);
			begin = Utils.timeStamp2Date(begin, Constants.DATE_FORMAT);
			params.setBeginTime(begin);
			params.setEndTime(end);
		}
		// 状态如果指定，判断是否在有效状态中
		if (!Utils.isEmpty(status)) {
			if (!Utils.isNumeric(status)
					|| !Constants.MESSAGE_TYPE.contains(Integer
							.parseInt(status))) {
				ServiceUtils.respondError(response, Constants.ERROR_STATUS);
				return;
			}
			params.setStatus(Integer.parseInt(status));
		}
		// offset limit必须同时制定，判断是否为数字
		if (!Utils.isEmpty(offset) && !Utils.isEmpty(limit)) {
			if (!Utils.isNumeric(offset) || !Utils.isNumeric(limit)) {
				ServiceUtils.respondError(response,
						Constants.ERROR_OFFSET_LIMIT);
				return;
			}
			params.setOffset(Integer.parseInt(offset));
			params.setLimit(Integer.parseInt(limit));
		}

		params.setSenderNick(senderNick);
		params.setMessageType(messageType);

		ContextListener.executor.execute(new QueryProcessor(asyncCtx, params));
	}

	/**
	 * 模块给用户，用户给用户发送消息
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// 参数获得
		request.setCharacterEncoding("UTF-8");
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String senderId = request.getParameter("senderId");
		String senderNick = request.getParameter("senderNick");
		String receiverId = request.getParameter("receiverId");
		String messageType = request.getParameter("messageType");

		// 标题验证
		if (Utils.isEmpty(title)) {
			ServiceUtils.respondError(response, Constants.ERROR_TITLE);
			return;
		}
		// 内容验证
		if (Utils.isEmpty(content)) {
			ServiceUtils.respondError(response, Constants.ERROR_CONTENT);
			return;
		}
		// 发送者验证,发送者id和名字都没填
		if (!Utils.isValidNumericStr(senderId) && Utils.isEmpty(senderNick)) {
			ServiceUtils.respondError(response, Constants.ERROR_SENDER);
			return;
		}
		// 接收者验证
		if (!Utils.isValidNumericStr(receiverId)) {
			ServiceUtils.respondError(response, Constants.ERROR_RECEIVER);
		}

		// 存储消息对象
		long receiver = Long.parseLong(receiverId);

		Message message = new Message(title, content, Long.parseLong(senderId),
				senderNick, receiver, messageType);

		MessageOperation.saveMessageToDB(message);// 保存消息到数据库

		ServiceUtils.respondResult(response, message.getMessageId());// 返回发送成功信息

		notifyNewMessageToUser(receiver, message);// 如果接收者在线，通知接收者
	}

	private void notifyNewMessageToUser(long receiverId, Message message) {

		if (ContextListener.mapUserIdContext.containsKey(receiverId)) {

			AsyncContext asyncContext = ContextListener.mapUserIdContext
					.remove(receiverId);

			if (asyncContext == null) {
				logger.info("asyncContext for id: " + receiverId
						+ " is not exist.");
				return;
			}

			PrintWriter out = null;

			try {
				out = asyncContext.getResponse().getWriter();

				if (out == null) {
					logger.info("asyncContext for id: " + receiverId
							+ " PrintWriter is null");
					return;
				}

				String messageJson = new Gson().toJson(message);
				String callbackFun = asyncContext.getRequest().getParameter(
						"jsonpCallback");
				String result = callbackFun + "(" + messageJson + ")";

				ServiceUtils.respondResult(asyncContext.getResponse(), result);
				asyncContext.complete();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (out != null) {
					out.close();
				}
			}
		}
	}

}
