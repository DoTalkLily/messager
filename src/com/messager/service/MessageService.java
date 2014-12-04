package com.messager.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.messager.common.Constants;
import com.messager.common.Utils;
import com.messager.dao.MessageOperation;
import com.messager.dataobject.Message;
import com.messager.processor.QueryProcessor;

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
		String userId = request.getParameter("user_id");
		String end = request.getParameter("end_time");
		String begin = request.getParameter("begin_time");
		String senderId = request.getParameter("sender_id");
		String senderNick = request.getParameter("sender_nick");
		String messageType = request.getParameter("message_type");
		String status = request.getParameter("status");
		String offset = request.getParameter("offset");
		String limit = request.getParameter("limit");

		// 参数校验
		// 用户id不能为空
		if (!Utils.isValidNumericStr(userId)) {
			respondError(response, Constants.ERROR_USERID);
			return;
		}
		// 如果发送者id不为空，判断是否为数字
		if (!Utils.isEmpty(senderId)) {
			if (!Utils.isNumeric(senderId)) {
				respondError(response, Constants.ERROR_SENDER);
				return;
			}
		}
		// 开始时间和结束时间必须同时指定
		if (!Utils.isEmpty(begin) && !Utils.isEmpty(end)) {

			if (!Utils.isNumeric(begin) || !Utils.isNumeric(end)) {
				respondError(response, Constants.ERROR_TIME_INTERVAL);
				return;
			}

			begin = Utils.timeStamp2Date(begin, Constants.DATE_FORMAT);
			end = Utils.timeStamp2Date(end, Constants.DATE_FORMAT);
		}
		// 状态如果指定，判断是否在有效状态中
		if (!Utils.isEmpty(status)) {
			if (!Utils.isNumeric(status)
					|| !Constants.MESSAGE_TYPE.contains(Integer
							.parseInt(status))) {
				respondError(response, Constants.ERROR_STATUS);
				return;
			}
		}
		// offset limit必须同时制定，判断是否为数字
		if (!Utils.isEmpty(offset) && !Utils.isEmpty(limit)) {
			if (!Utils.isNumeric(offset) || !Utils.isNumeric(limit)) {
				respondError(response, Constants.ERROR_OFFSET_LIMIT);
				return;
			}
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", Long.parseLong(userId));
		params.put("begin", begin);
		params.put("end", end);
		params.put("senderId", senderId);
		params.put("senderNick", senderNick);
		params.put("messageType", messageType);
		params.put("status", status);
		params.put("offset", offset);
		params.put("limit", limit);

		AsyncContext asyncCtx = request.startAsync();
		asyncCtx.addListener(new AsyncListener());
		asyncCtx.setTimeout(Constants.TIMEOUT);

		ContextListener.executor.execute(new QueryProcessor(asyncCtx, params));
	}

	/**
	 * 模块给用户，用户给用户发送消息
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// 参数获得
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String senderId = request.getParameter("sender_id");
		String senderNick = request.getParameter("sender_nick");
		String receiverId = request.getParameter("receiver_id");
		String messageType = request.getParameter("message_type");

		// 标题验证
		if (Utils.isEmpty(title)) {
			respondError(response, Constants.ERROR_TITLE);
			return;
		}
		// 内容验证
		if (Utils.isEmpty(content)) {
			respondError(response, Constants.ERROR_CONTENT);
			return;
		}
		// 发送者验证,发送者id和名字都没填
		if (!Utils.isValidNumericStr(senderId) && Utils.isEmpty(senderNick)) {
			respondError(response, Constants.ERROR_SENDER);
			return;
		}
		// 接收者验证
		if (!Utils.isValidNumericStr(receiverId)) {
			respondError(response, Constants.ERROR_RECEIVER);
		}

		// 存储消息对象
		long receiver = Long.parseLong(receiverId);

		Message message = new Message(title, content, Long.parseLong(senderId),
				senderNick, receiver, messageType);

		MessageOperation.saveMessageToDB(message);// 保存消息到数据库

		respondResult(response, "message_id", message.getMessageId());// 返回发送成功信息

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

				respondResult(asyncContext.getResponse(), "message", message);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (out != null) {
					out.close();
				}
			}
		}
	}

	private void respondResult(ServletResponse servletResponse,
			String dataName, Object dataValue) {
		String result = null;
		PrintWriter out = null;

		try {
			// 构建结果字符串
			ObjectMapper mapper = new ObjectMapper();
			ObjectNode node = mapper.createObjectNode();
			node.put("code", Constants.SUCCESS);
			node.put("err_msg", "");
			ObjectNode dataNode = mapper.createObjectNode();
			dataNode.put(dataName, mapper.writeValueAsString(dataValue));
			node.put("data", dataNode);
			result = mapper.writeValueAsString(node);

			// 发送结果字符串
			out = servletResponse.getWriter();
			out.write(result);
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
	private void respondError(ServletResponse response, int code) {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out = null;

		try {
			out = response.getWriter();
			ObjectMapper mapper = new ObjectMapper();
			ObjectNode node = mapper.createObjectNode();
			node.put("code", code);
			node.put("err_msg", Constants.ERROR_MESSAGE.get(code));

			out.write(mapper.writeValueAsString(node));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
}
