package com.messager.dataobject;

import java.sql.Timestamp;

import com.messager.common.Constants;

public class Message {

	int status;// 阅读状态,默认未读消息

	long senderId;// 发送者id

	long receiverId;// 接收者id

	long messageId;// 消息id

	String title;// 消息标题

	String content;// 消息内容

	String senderNick;// 发送者用户名

	String messageType;// 消息类型

	Timestamp sendDate;// 发送时间

	public Message(String title, String content, long senderId,
			String senderNick, long receiverId, String messageType) {
		this.status = Constants.MESSAGE_UNREAD;// 默认未读消息
		this.title = title;
		this.content = content;
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.senderNick = senderNick;
		this.messageType = messageType;
		this.sendDate = new Timestamp(System.currentTimeMillis());
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getSenderId() {
		return senderId;
	}

	public void setSenderId(long senderId) {
		this.senderId = senderId;
	}

	public long getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(long receiverId) {
		this.receiverId = receiverId;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public long getMessageId() {
		return messageId;
	}

	public void setMessageId(long messageId) {
		this.messageId = messageId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSenderNick() {
		return senderNick;
	}

	public void setSenderNick(String senderNick) {
		this.senderNick = senderNick;
	}

	public Timestamp getSendDate() {
		return sendDate;
	}

	public void setSendDate(Timestamp sendDate) {
		this.sendDate = sendDate;
	}

}
