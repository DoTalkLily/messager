package com.messager.dao;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.messager.common.Utils;
import com.messager.dataobject.Message;

public class Test {

	public static void main(String[] args) {
		System.out.println(Utils.timeStamp2Date("1252639886",
				"yyyy-MM-dd HH:mm:ss"));
		Message m = new Message("aa", "content", 11, "lily", 232, "通知");
		try {
			System.out.println(new ObjectMapper().writeValueAsString(m));
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// public static void main(String[] args) {
	// SqlSession session = DBUtils.getSqlSession();
	// try {
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

	// } finally {
	// DBUtils.closeSession(session);
	// }
	// }
}