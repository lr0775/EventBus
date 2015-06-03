package com.lr0775.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {

	private volatile static ThreadPool instance;

	private final ExecutorService mExecutorService;

	/** Returns singleton class instance */
	public static ThreadPool getInstance() {
		if (instance == null) {
			synchronized (ThreadPool.class) {
				if (instance == null) {
					instance = new ThreadPool();
				}
			}
		}
		return instance;
	}

	private ThreadPool() {
		mExecutorService = Executors.newCachedThreadPool();
	}

	public void executeTask(Runnable runnable) {
		mExecutorService.execute(runnable);
	}

}
