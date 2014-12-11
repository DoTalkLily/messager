package com.alibaba.base.messager.dataobject;

public class MessageRecord {

	int status;// 消息状态 未读-1 已读-2 删除-3

	int receiverId;// 消息接收者id

	long messageId;// 消息内容id

	public MessageRecord(int status, int receiverId, long messageId) {
		this.status = status;
		this.receiverId = receiverId;
		this.messageId = messageId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(int receiverId) {
		this.receiverId = receiverId;
	}

	public long getMessageId() {
		return messageId;
	}

	public void setMessageId(long messageId) {
		this.messageId = messageId;
	}

}
