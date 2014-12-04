package com.messager.processor;

import org.apache.ibatis.session.SqlSession;

import com.messager.common.DBUtils;
import com.messager.dao.IMessageOperation;
import com.messager.dataobject.Message;

public class SendProcessor implements Runnable {

	private Message message;

	public SendProcessor(Message message) {
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
	}
}
