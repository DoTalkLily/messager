package com.messager.processor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.AsyncContext;

import org.apache.ibatis.session.SqlSession;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;

import com.messager.common.DBUtils;
import com.messager.dao.IMessageOperation;
import com.messager.dataobject.Message;

public class QueryProcessor implements Runnable {

	private AsyncContext asyncContext;

	private Map<String, Object> params;

	public QueryProcessor() {
	}

	public QueryProcessor(AsyncContext asyncContext, Map<String, Object> params) {
		this.asyncContext = asyncContext;
		this.params = params;
	}

	@Override
	public void run() {
		SqlSession session = DBUtils.getSqlSession();
		List<Message> messages = null;

		try {
			IMessageOperation userOperation = session
					.getMapper(IMessageOperation.class);
			messages = userOperation.getMessageList(this.params);
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
			node.put("data", mapper.writeValueAsString(messages));
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
