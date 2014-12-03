package com.messager.dataobject;

import java.sql.Timestamp;

public class Message {

	int status;// 阅读状态,默认新消息

	int senderId;// 发送者id

	int receiverId;// 接收者id

	int messageType;// 消息类型 通知—1，错误—2，警告—3

	long messageId;// 消息id

	String title;// 消息标题

	String content;// 消息内容

	String senderNick;// 发送者用户名

	Timestamp sendDate;// 发送时间

	public Message(String title, String content, int senderId,
			String senderNick, int receiverId, int messageType) {
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

	public int getSenderId() {
		return senderId;
	}

	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}

	public int getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(int receiverId) {
		this.receiverId = receiverId;
	}

	public int getMessageType() {
		return messageType;
	}

	public void setMessageType(int messageType) {
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
