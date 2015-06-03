package com.lr0775.eventbusdemo;

import android.app.Application;

import com.lr0775.event.EventBus;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		EventBus.getInstance().start();
	}

}
