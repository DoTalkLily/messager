package com.messager.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ThreadPoolExecutor;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;

import com.messager.common.Constants;
import com.messager.dataobject.Message;
import com.messager.processor.RequestProcessor;

/**
 * Servlet implementation class MessageServlet
 */
@WebServlet(urlPatterns = "/message", asyncSupported = true)
public class MessageService extends HttpServlet {
	private static final long serialVersionUID = 1L;

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
		String time = request.getParameter("time");
		// request.setAttribute("org.apache.catalina.ASYNC_SUPPORTED", true);

		AsyncContext asyncCtx = request.startAsync();
		asyncCtx.addListener(new AsyncListener());
		asyncCtx.setTimeout(Constants.TIMEOUT);

		// ThreadPoolExecutor executor = (ThreadPoolExecutor) request
		// .getServletContext().getAttribute("executor");
		// executor.execute(new RequestProcessor(asyncCtx, message));
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
		if (StringUtils.(title)) {
			respondError(response, Constants.ERROR_TITLE);
			return;
		}
		// 内容验证
		if (content == null || content.trim().equals(Constants.EMPTY)) {
			respondError(response, Constants.ERROR_CONTENT);
			return;
		}
		// // 发送者校验
		// if ((senderId == null && senderNick == null) || receiverId == null) {
		// respondError(response, Constants.ERROR_SENDER_OR_RECEIVER);
		// return;
		// }

		if (messageType == null
				|| !Constants.MESSAGE_TYPE.contains(Integer
						.parseInt(messageType))) {
			respondError(response, Constants.ERROR_MESSAGE_TYPE);
			return;
		}

		// 存储消息对象
		Message message = new Message(title, content,
				Integer.parseInt(senderId), senderNick,
				Integer.parseInt(receiverId), Integer.parseInt(messageType));

		AsyncContext asyncCtx = request.startAsync();
		asyncCtx.addListener(new AsyncListener());
		asyncCtx.setTimeout(Constants.TIMEOUT);

		ThreadPoolExecutor executor = (ThreadPoolExecutor) request
				.getServletContext().getAttribute("executor");
		executor.execute(new RequestProcessor(asyncCtx, message));
	}

	/**
	 * 以JSON格式输出
	 * 
	 * @param response
	 */
	private void respondError(HttpServletResponse response, int code) {
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
