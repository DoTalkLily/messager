//package com.messager.service;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.concurrent.ThreadPoolExecutor;
//
//import javax.servlet.AsyncContext;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.codehaus.jackson.map.ObjectMapper;
//import org.codehaus.jackson.node.ObjectNode;
//
//import com.messager.common.Constants;
//import com.messager.dataobject.Message;
//import com.messager.processor.RequestProcessor;
//
///**
// * Servlet implementation class MessageServlet
// */
//@WebServlet(urlPatterns = "/message/group", asyncSupported = true)
//public class GroupMessageService extends HttpServlet {
//	private static final long serialVersionUID = 1L;
//
//	/**
//	 * @see HttpServlet#HttpServlet()
//	 */
//	public GroupMessageService() {
//		super();
//	}
//
//	/**
//	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
//	 *      response)
//	 */
//	protected void doGet(HttpServletRequest request,
//			HttpServletResponse response) throws ServletException, IOException {
//		String time = request.getParameter("time");
//		// request.setAttribute("org.apache.catalina.ASYNC_SUPPORTED", true);
//
//		AsyncContext asyncCtx = request.startAsync();
//		asyncCtx.addListener(new AsyncListener());
//		asyncCtx.setTimeout(Constants.TIMEOUT);
//
//		// ThreadPoolExecutor executor = (ThreadPoolExecutor) request
//		// .getServletContext().getAttribute("executor");
//		// executor.execute(new RequestProcessor(asyncCtx, message));
//	}
//
//	/**
//	 * 发送消息
//	 */
//	protected void doPost(HttpServletRequest request,
//			HttpServletResponse response) throws ServletException, IOException {
//
//		String title = request.getParameter("title");
//		String content = request.getParameter("content");
//		String senderId = request.getParameter("sender_id");
//		String senderNick = request.getParameter("sender_nick");
//		String receiverId = request.getParameter("receiver_id");
//		String messageLevel = request.getParameter("message_level");
//		String type = request.getParameter("type");
//
//		if (title == null || content == null) {
//			respondError(response, 400, "标题或内容不能为空");
//			return;
//		}
//
//		if ((senderId == null && senderNick == null) || receiverId == null) {
//			respondError(response, 401, "发送者或接受者不能为空");
//			return;
//		}
//
//		Message message = new Message(title, content,
//				Integer.parseInt(senderId), senderNick,
//				Integer.parseInt(messageLevel));
//
//		// 如果是群消息
//		if (type != null && Constants.P2G_MESSAGE == Integer.parseInt(type)) {
//			String groupId = request.getParameter("group_id");
//
//			if (groupId == null) {
//				respondError(response, 402, "发送群消息群id不能为空");
//				return;
//			}
//
//			message.setGroupId(Integer.parseInt(groupId));
//			message.setType(Constants.P2G_MESSAGE);
//		}
//
//		AsyncContext asyncCtx = request.startAsync();
//		asyncCtx.addListener(new AsyncListener());
//		asyncCtx.setTimeout(Constants.TIMEOUT);
//
//		ThreadPoolExecutor executor = (ThreadPoolExecutor) request
//				.getServletContext().getAttribute("executor");
//		executor.execute(new RequestProcessor(asyncCtx, message));
//	}
//
//	/**
//	 * 以JSON格式输出
//	 * 
//	 * @param response
//	 */
//	private void respondError(HttpServletResponse response, int code,
//			String errMsg) {
//		response.setCharacterEncoding("UTF-8");
//		response.setContentType("application/json; charset=utf-8");
//		PrintWriter out = null;
//
//		try {
//			out = response.getWriter();
//			ObjectMapper mapper = new ObjectMapper();
//			ObjectNode node = mapper.createObjectNode();
//			node.put("code", code);
//			node.put("err_msg", errMsg);
//
//			out.write(mapper.writeValueAsString(node));
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			if (out != null) {
//				out.close();
//			}
//		}
//	}
// }
