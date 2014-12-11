package com.alibaba.base.messager.service;

import java.io.IOException;

import javax.servlet.AsyncEvent;

public class AsyncListener implements javax.servlet.AsyncListener {
	private String callback;

	public String getCallback() {
		return callback;
	}

	public void setCallback(String callback) {
		this.callback = callback;
	}

	@Override
	public void onComplete(AsyncEvent asyncEvent) throws IOException {
		System.out.println("complete!!");
	}

	@Override
	public void onError(AsyncEvent asyncEvent) throws IOException {
		System.out.println("error!!");
	}

	@Override
	public void onStartAsync(AsyncEvent asyncEvent) throws IOException {

	}

	@Override
	public void onTimeout(AsyncEvent asyncEvent) throws IOException {
		asyncEvent.getAsyncContext().getResponse().getWriter().write("timeout");
		asyncEvent.getAsyncContext().complete();
	}

}
