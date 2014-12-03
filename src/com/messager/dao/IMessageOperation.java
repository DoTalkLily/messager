package com.messager.dao;

import java.util.List;

import com.messager.dataobject.Message;

public interface IMessageOperation {

	public Message getMessageByID(int messageId);

	public List<Message> getMessageList(int senderId);

	public void saveMessage(Message message);

}