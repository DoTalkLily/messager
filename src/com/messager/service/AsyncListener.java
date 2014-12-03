package com.messager.service;

import java.io.IOException;

import javax.servlet.AsyncEvent;

public class AsyncListener implements javax.servlet.AsyncListener {

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
