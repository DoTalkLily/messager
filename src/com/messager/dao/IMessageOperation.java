package com.messager.dao;

import java.util.List;
import java.util.Map;

import com.messager.dataobject.Message;

public interface IMessageOperation {

	public Message getMessageByID(int messageId);

	public List<Message> getMessageList(Map<String, Object> params);

	public void saveMessage(Message message);

}