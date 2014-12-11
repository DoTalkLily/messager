package com.alibaba.base.messager.processor;

import java.util.List;

import javax.servlet.AsyncContext;

import com.alibaba.base.messager.common.ServiceUtils;
import com.alibaba.base.messager.dao.MessageOperation;
import com.alibaba.base.messager.dataobject.Message;
import com.alibaba.base.messager.dataobject.QueryObject;

public class QueryProcessor implements Runnable {

	private QueryObject queryParams;

	private AsyncContext asyncContext;

	public QueryProcessor() {
	}

	public QueryProcessor(AsyncContext asyncContext, QueryObject queryParams) {
		this.queryParams = queryParams;
		this.asyncContext = asyncContext;
	}

	@Override
	public void run() {
		if (queryParams.getMessageId() > 0) {
			Message message = MessageOperation.getMessageById(queryParams);

			if (message == null) {
				ServiceUtils.respondResult(asyncContext.getResponse(), null);
				return;
			}

			ServiceUtils.respondResult(asyncContext.getResponse(), message);
		} else {
			List<Message> messages = MessageOperation
					.queryMessages(queryParams);

			if (messages == null) {
				ServiceUtils.respondResult(asyncContext.getResponse(), null);
				return;
			}

			ServiceUtils.respondResult(asyncContext.getResponse(), messages);
		}
		asyncContext.complete();
	}
}
