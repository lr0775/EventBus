package com.lr0775.eventbusdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.lr0775.event.EventBus;
import com.lr0775.event.EventBus.Event;
import com.lr0775.event.EventBus.EventSubscriber;
import com.lr0775.util.TaskUtil;

public class MainActivity extends Activity implements EventSubscriber,
		OnClickListener {

	private TextView mMyResultTv;
	private TextView mBResultTv;
	private TextView mCResultTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mMyResultTv = (TextView) findViewById(R.id.my_result_tv);
		mBResultTv = (TextView) findViewById(R.id.b_activity_result_tv);
		mCResultTv = (TextView) findViewById(R.id.c_activity_result_tv);
		findViewById(R.id.b_activity_btn).setOnClickListener(this);

		EventBus.getInstance().register(this, 1, 2, 3);

		TaskUtil.getMainResult();
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
		case 1:
			if (event.success) {
				mMyResultTv.setText(event.result1 + "");
			}
			break;
		case 2:
			if (event.success) {
				mBResultTv.setText(event.result1 + "");
			}
			break;
		case 3:
			if (event.success) {
				mCResultTv.setText(event.result1 + "");
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.b_activity_btn:
			intent = new Intent(this, BActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}

	}

}
