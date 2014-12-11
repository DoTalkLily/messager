package com.alibaba.base.messager.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.alibaba.base.messager.dataobject.Message;
import com.alibaba.base.messager.dataobject.QueryObject;

public interface IMessageOperation {

	public Message getMessageById(QueryObject queryParams);

	public List<Message> getMessageList(QueryObject queryParams);

	public void saveMessage(Message message);

	public void updateMessage(@Param("receiverId") long receiverId,
			@Param("messageId") long messageId, @Param("toStatus") int toStauts);

	public void updateMessageList(@Param("receiverId") long receiverId,
			@Param("messageIds") long[] messageIds,
			@Param("toStatus") int toStauts);
}