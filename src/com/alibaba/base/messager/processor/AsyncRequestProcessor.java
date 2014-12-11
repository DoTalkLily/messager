package com.alibaba.base.messager.processor;

import javax.servlet.AsyncContext;

import org.apache.ibatis.session.SqlSession;

import com.alibaba.base.messager.common.DBUtils;
import com.alibaba.base.messager.common.ServiceUtils;
import com.alibaba.base.messager.dao.IMessageOperation;
import com.alibaba.base.messager.dataobject.Message;

/**
 * 消息推送功能处理线程
 * 
 * @author li.lli
 * 
 */
public class AsyncRequestProcessor implements Runnable {

	private AsyncContext asyncContext;

	private Message message;

	public AsyncRequestProcessor() {
	}

	public AsyncRequestProcessor(AsyncContext asyncContext, Message message) {
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

		ServiceUtils.respondResult(asyncContext.getResponse(),
				message.getMessageId());

		asyncContext.complete();
	}
}
