package com.alibaba.base.messager.processor;

import org.apache.ibatis.session.SqlSession;

import com.alibaba.base.messager.common.DBUtils;
import com.alibaba.base.messager.dao.IMessageOperation;
import com.alibaba.base.messager.dataobject.Message;

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
