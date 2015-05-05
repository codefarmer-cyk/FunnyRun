package com.example.funnyrun.mode;

import java.util.Date;

import cn.bmob.v3.datatype.BmobDate;

import com.example.funnyrun.ChronometerService;
import com.example.funnyrun.DistanceService;
import com.example.funnyrun.MyApplication;
import com.example.funnyrun.R;
import com.example.funnyrun.ShareActivity;
import com.example.funnyrun.ChronometerService.MyBinder;
import com.example.funnyrun.location.BaiduMapActivity;
import com.example.funnyrun.mode.FreedomModeActivity.MyReceiver;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ChallengeModeActivity extends Activity {
	private int time;
	private int distance;
	private String addr;
	private String tag;

	private TextView limitedTimeView;
	private TextView limitedDistanceView;
	private Button btn1;

	private ChronometerService.MyBinder myTimeBinder = null;
	private DistanceService.MyBinder myDistanceBinder = null;
	private MyReceiver myReceiver = null;
	private Intent timeIntent, distanceIntent;
	private IntentFilter filter;

	private ServiceConnection timeConn = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			myTimeBinder = (MyBinder) service;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub

		}

	};
	private ServiceConnection distanceConn = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			myDistanceBinder = (com.example.funnyrun.DistanceService.MyBinder) service;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub

		}

	};
	private Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			addr = myDistanceBinder.getAddr();
			if (myTimeBinder != null)
				limitedTimeView.setText("剩余时间："
						+ MyApplication.getTime(time - myTimeBinder.getTime()));
			if (myDistanceBinder != null)
				limitedDistanceView.setText("剩余距离："
						+ (distance - myDistanceBinder.getDistance()) + "m");
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_challenge);
		limitedTimeView = (TextView) findViewById(R.id.limitedTime);
		limitedDistanceView = (TextView) findViewById(R.id.limitedDistance);
		btn1 = (Button) findViewById(R.id.btn1);
		tag = this.getClass().getSimpleName();
		Log.d(tag, "create");
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		time = bundle.getInt("time");
		distance = bundle.getInt("distance");
		limitedTimeView.append(MyApplication.getTime(time));
		limitedDistanceView.append(String.valueOf(distance) + "m");
		timeIntent = new Intent();
		timeIntent.setAction("service.CHRONOMETER_ACTION");
		distanceIntent = new Intent();
		distanceIntent.setAction("service.DISTANCE_ACTION");
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn1:
			if (btn1.getText().toString().equals("开始")) {
				btn1.setText("结束");
				bindService(timeIntent, timeConn, Service.BIND_AUTO_CREATE);
				bindService(distanceIntent, distanceConn,
						Service.BIND_AUTO_CREATE);
				filter = new IntentFilter();
				filter.addAction("action.UPDATE_ACTION");
				myReceiver = new MyReceiver();
				registerReceiver(myReceiver, filter);
			} else {
				btn1.setText("开始");
				startShareActivity();
			}
			break;
		case R.id.btn2:
			startMapActivity();
			break;
		}
	}

	private void startShareActivity() {
		// Log.d(tag, "跳转");
		Intent intent = new Intent(ChallengeModeActivity.this,
				ShareActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("addr", addr);
		bundle.putString("time", String.valueOf(time));
		bundle.putInt("distance", distance);
		BmobDate date = new BmobDate(new Date());
		bundle.putSerializable("date", date);
		// BmobUser user = ((MyApplication) getApplication()).user;
		// bundle.putSerializable("user", user);
		intent.putExtras(bundle);
		// Log.d(tag, "跳转 1");
		startActivity(intent);
		finish();
	}

	public void startMapActivity() {
		Intent intent = new Intent(ChallengeModeActivity.this,
				BaiduMapActivity.class);
		startActivity(intent);
	}

	public class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			myHandler.sendEmptyMessage(0);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (myTimeBinder != null) {
			unbindService(timeConn);
			stopService(timeIntent);
		}
		if (myDistanceBinder != null) {
			unbindService(distanceConn);
			stopService(distanceIntent);
		}
		if (myReceiver != null)
			unregisterReceiver(myReceiver);
	}
}
