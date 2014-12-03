package com.messager.processor;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.AsyncContext;

import org.apache.ibatis.session.SqlSession;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;

import com.messager.common.DBUtils;
import com.messager.dao.IMessageOperation;
import com.messager.dataobject.Message;

public class RequestProcessor implements Runnable {

	private AsyncContext asyncContext;

	private Message message;

	public RequestProcessor() {
	}

	public RequestProcessor(AsyncContext asyncContext, Message message) {
		this.asyncContext = asyncContext;
		this.message = message;
	}

	@Override
	public void run() {
		SqlSession session = DBUtils.getSqlSession();
		try {
			IMessageOperation userOperation = session
					.getMapper(IMessageOperation.class);
			userOperation.saveMessage(this.message);
			session.commit();
		} finally {
			DBUtils.closeSession(session);
		}

		PrintWriter out = null;

		try {
			out = asyncContext.getResponse().getWriter();
			ObjectMapper mapper = new ObjectMapper();
			ObjectNode node = mapper.createObjectNode();
			node.put("code", 200);
			ObjectNode data = mapper.createObjectNode();
			data.put("message_id", message.getMessageId());
			node.put("data", data);
			node.put("err_msg", "");
			out.write(mapper.writeValueAsString(node));
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
		asyncContext.complete();
	}
}
