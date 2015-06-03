package com.lr0775.event;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

public class EventBus {

	private final ExecutorService mExecutorService;

	private volatile static EventBus instance;

	private final Handler mMainThreadPoster;

	private final ConcurrentLinkedQueue<SubscriberWrapper> mSubscriberQueue;

	private final LinkedBlockingQueue<EventWrapper> mEventQueue;

	private volatile boolean executorRunning;

	/** Returns singleton class instance */
	public static EventBus getInstance() {
		if (instance == null) {
			synchronized (EventBus.class) {
				if (instance == null) {
					instance = new EventBus();
				}
			}
		}
		return instance;
	}

	protected EventBus() {
		executorRunning = false;
		mExecutorService = Executors.newFixedThreadPool(1);
		mSubscriberQueue = new ConcurrentLinkedQueue<SubscriberWrapper>();
		mEventQueue = new LinkedBlockingQueue<EventWrapper>();
		mMainThreadPoster = new Handler(Looper.getMainLooper());
	}

	public synchronized void start() {

		if (!executorRunning) {
			executorRunning = true;
			mExecutorService.execute(new EventDispatcher());
		}
	}

	public void register(EventSubscriber subscriber, int... ids) {
		SubscriberWrapper subscriberWrapper = new SubscriberWrapper(subscriber,
				true, ids);
		mSubscriberQueue.add(subscriberWrapper);
	}

	public void unregister(EventSubscriber subscriber) {
		for (SubscriberWrapper subscriberWrapper : mSubscriberQueue) {
			if (subscriberWrapper.subscriber == subscriber) {
				subscriberWrapper.active = false;
				mSubscriberQueue.remove(subscriberWrapper);
				break;
			}
		}
	}

	public void publish(Event event) {
		queue(event);
	}

	public synchronized void shutdown() {
		mExecutorService.shutdown();
	}

	class EventDispatcher implements Runnable {

		public void run() {
			try {
				try {
					while (true) {
						EventWrapper eventWrapper = mEventQueue.take();
						ArrayList<WeakReference<SubscriberWrapper>> refList = eventWrapper.refList;

						for (WeakReference<SubscriberWrapper> ref : refList) {
							SubscriberWrapper subscriberWrapper = ref.get();
							if (subscriberWrapper != null
									&& subscriberWrapper.active) {
								mMainThreadPoster.post(new OnEventRunnable(
										subscriberWrapper.subscriber,
										eventWrapper.event));
							}
						}
					}
				} catch (InterruptedException e) {
					Log.w("EventDispatcher", Thread.currentThread().getName()
							+ " was interruppted", e);
				}
			} finally {
				executorRunning = false;
			}
		}
	}

	private void queue(Event event) {
		EventWrapper eventWrapper = new EventWrapper();
		eventWrapper.event = event;
		eventWrapper.refList = event.refList;
		if (eventWrapper.refList != null && eventWrapper.refList.size() > 0) {
			try {
				mEventQueue.put(eventWrapper);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void initEvent(Event event) {
		event.refList = new ArrayList<WeakReference<SubscriberWrapper>>();
		for (SubscriberWrapper subscriberWrapper : mSubscriberQueue) {

			HashSet<Integer> eventIdSet = subscriberWrapper.eventIdSet;
			if (eventIdSet != null && eventIdSet.contains(event.id)) {
				event.refList.add(new WeakReference<SubscriberWrapper>(
						subscriberWrapper));
			}
		}
	}

	private static class EventWrapper {
		Event event;
		ArrayList<WeakReference<SubscriberWrapper>> refList;
	}

	private static class SubscriberWrapper {

		EventSubscriber subscriber;
		boolean active;
		HashSet<Integer> eventIdSet;

		public SubscriberWrapper(EventSubscriber subscriber, boolean active,
				int... ids) {
			this.subscriber = subscriber;
			this.active = active;
			if (ids != null) {
				eventIdSet = new HashSet<Integer>();
				for (int id : ids) {
					eventIdSet.add(id);
				}
			}
		}
	}

	public interface EventSubscriber {

		void onEvent(Event event);

	}

	private static class OnEventRunnable implements Runnable {

		EventSubscriber subscriber;
		Event event;

		OnEventRunnable(EventSubscriber subscriber, Event event) {
			this.subscriber = subscriber;
			this.event = event;
		}

		@Override
		public void run() {
			subscriber.onEvent(event);
		}
	}

	public static class Event {

		/** 事件的唯一标识id */
		public final int id;

		private ArrayList<WeakReference<SubscriberWrapper>> refList;

		/** 事件状态 */
		public boolean success;
		public boolean empty;
		public boolean parseError;
		public boolean netError;

		/** 事件包含的值 */
		public Object arg1;
		public Object arg2;

		public Object result1;
		public Object result2;

		public Event(int id) {
			this.id = id;
			EventBus.getInstance().initEvent(this);
		}

		public boolean handleException() {
			if (netError) {
				// show toast
				return true;
			}
			if (parseError) {
				// show toast
				return true;
			}
			return false;
		}
	}
}
