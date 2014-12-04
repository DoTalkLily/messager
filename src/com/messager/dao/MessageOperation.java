package com.messager.dao;

import org.apache.ibatis.session.SqlSession;

import com.messager.common.DBUtils;
import com.messager.dataobject.Message;

public class MessageOperation {

	public static void saveMessageToDB(Message message) {
		SqlSession session = DBUtils.getSqlSession();
		try {
			IMessageOperation userOperation = session
					.getMapper(IMessageOperation.class);
			userOperation.saveMessage(message);
			session.commit();
		} finally {
			DBUtils.closeSession(session);
		}
	}
}
