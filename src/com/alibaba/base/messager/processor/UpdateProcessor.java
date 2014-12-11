package com.alibaba.base.messager.processor;

import javax.servlet.AsyncContext;

import com.alibaba.base.messager.common.ServiceUtils;
import com.alibaba.base.messager.dao.MessageOperation;

public class UpdateProcessor implements Runnable {

	private int toStatus;

	private long receiverId;

	private long[] messageIds;

	private AsyncContext asyncContext;

	public UpdateProcessor() {
	}

	public UpdateProcessor(AsyncContext asyncContext, long receiverId,
			long[] messageIds, int toStatus) {
		this.toStatus = toStatus;
		this.messageIds = messageIds;
		this.receiverId = receiverId;
		this.asyncContext = asyncContext;

	}

	@Override
	public void run() {
		MessageOperation.updateMessageStatus(receiverId, messageIds, toStatus);
		ServiceUtils.respondResult(asyncContext.getResponse(), messageIds);
		asyncContext.complete();
	}
}
