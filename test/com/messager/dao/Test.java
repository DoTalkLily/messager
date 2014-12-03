package com.messager.dao;

import org.apache.ibatis.session.SqlSession;

import com.messager.common.DBUtils;
import com.mysql.jdbc.StringUtils;

public class Test {

	public static void main(String[] args) {
		SqlSession session = DBUtils.getSqlSession();
		try {
			// IMessageOperation userOperation = session
			// .getMapper(IMessageOperation.class);
			// Message message = userOperation.getMessageByID(1);

			// if (message != null) {
			// System.out.println(message.getTitle());
			// System.out.println(message.getContent());
			// }

			// List<Message> messages = userOperation.getMessageList(1212);

			// for (Message msg : messages) {
			// System.out.println(msg.getTitle() + ":" + msg.getContent());
			// }

			System.out.println(StringUtils.isEmptyOrWhitespaceOnly(""));
			System.out.println(StringUtils.isEmptyOrWhitespaceOnly("  "));
			System.out.println(StringUtils.isEmptyOrWhitespaceOnly(null));
		} finally {
			DBUtils.closeSession(session);
		}
	}
}