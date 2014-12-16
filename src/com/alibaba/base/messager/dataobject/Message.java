package com.alibaba.base.messager.dataobject;

import java.util.Date;

import com.alibaba.base.messager.common.Constants;

public class Message {

	int status;// 阅读状态,默认未读消息

	String senderId;// 发送者id

	String receiverId;// 接收者id

	long messageId;// 消息id

	String title;// 消息标题

	String content;// 消息内容

	String senderNick;// 发送者用户名

	String messageType;// 消息类型

	Date date;// 发送时间

	public Message() {

	}

	public Message(String title, String content, String senderId,
			String senderNick, String receiverId, String messageType) {
		this.status = Constants.MESSAGE_UNREAD;// 默认未读消息
		this.title = title;
		this.content = content;
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.senderNick = senderNick;
		this.messageType = messageType;
		this.date = new Date();
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public String getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
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

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public Date getSendDate() {
		return date;
	}

	public void setSendDate(Date sendDate) {
		this.date = sendDate;
	}
}
