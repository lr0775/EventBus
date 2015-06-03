package com.lr0775.util;

import com.lr0775.event.EventBus;
import com.lr0775.event.EventBus.Event;

public class TaskUtil {

	/**
	 * 模拟MainActivity的异步操作，约定好的事件ID是1。
	 */
	public static void getMainResult() {
		ThreadPool.getInstance().executeTask(new Runnable() {

			@Override
			public void run() {
				final Event event = new Event(1);
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				event.success = true;
				event.result1 = "MainActivity的结果";
				EventBus.getInstance().publish(event);
			}
		});
	}

	/**
	 * 模拟BActivity的异步操作，约定好的事件ID是2。
	 */
	public static void getBResult() {
		ThreadPool.getInstance().executeTask(new Runnable() {

			@Override
			public void run() {
				final Event event = new Event(2);
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				event.success = true;
				event.result1 = "BActivity的结果";
				EventBus.getInstance().publish(event);
			}
		});
	}

	/**
	 * 模拟CActivity的异步操作，约定好的事件ID是3。
	 */
	public static void getCResult() {
		ThreadPool.getInstance().executeTask(new Runnable() {

			@Override
			public void run() {
				final Event event = new Event(3);
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				event.success = true;
				event.result1 = "CActivity的结果";
				EventBus.getInstance().publish(event);
			}
		});
	}
}
