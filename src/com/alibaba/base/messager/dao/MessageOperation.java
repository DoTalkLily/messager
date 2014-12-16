package com.alibaba.base.messager.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.alibaba.base.messager.common.DBUtils;
import com.alibaba.base.messager.dataobject.Message;
import com.alibaba.base.messager.dataobject.QueryObject;

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

	public static List<Message> queryMessages(QueryObject queryParams) {
		SqlSession session = DBUtils.getSqlSession();
		List<Message> messages = null;

		try {
			IMessageOperation userOperation = session
					.getMapper(IMessageOperation.class);

			messages = userOperation.getMessageList(queryParams);

			session.commit();
		} finally {
			DBUtils.closeSession(session);
		}

		return messages;
	}

	public static Message getMessageById(QueryObject queryParams) {
		SqlSession session = DBUtils.getSqlSession();
		Message message = null;

		try {
			IMessageOperation userOperation = session
					.getMapper(IMessageOperation.class);

			message = userOperation.getMessageById(queryParams);

			session.commit();
		} finally {
			DBUtils.closeSession(session);
		}

		return message;
	}

	public static void updateMessageStatus(String receiverId,
			long[] messageIds, int toStatus) {
		SqlSession session = DBUtils.getSqlSession();

		try {
			IMessageOperation userOperation = session
					.getMapper(IMessageOperation.class);

			if (messageIds.length == 1) {
				userOperation
						.updateMessage(receiverId, messageIds[0], toStatus);
			} else {
				userOperation.updateMessageList(receiverId, messageIds,
						toStatus);
			}

			session.commit();
		} finally {
			DBUtils.closeSession(session);
		}
	}
}
