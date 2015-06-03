package com.lr0775.eventbusdemo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.lr0775.event.EventBus;
import com.lr0775.event.EventBus.Event;
import com.lr0775.event.EventBus.EventSubscriber;
import com.lr0775.util.TaskUtil;

public class CActivity extends Activity implements EventSubscriber {

	private TextView mMyResultTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_c);

		mMyResultTv = (TextView) findViewById(R.id.my_result_tv);

		EventBus.getInstance().register(this, 3);

		TaskUtil.getCResult();
	}

	@Override
	protected void onDestroy() {
		EventBus.getInstance().unregister(this);
		super.onDestroy();
	}

	@Override
	public void onEvent(Event event) {
		if (isFinishing()) {
			return;
		}
		switch (event.id) {
		case 3:
			if (event.success) {
				mMyResultTv.setText(event.result1 + "");
			}
			break;
		default:
			break;
		}
	}

}
