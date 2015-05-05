package com.example.funnyrun.mode;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;

import com.example.funnyrun.ChronometerService;
import com.example.funnyrun.ChronometerService.MyBinder;
import com.example.funnyrun.DistanceService;
import com.example.funnyrun.MyApplication;
import com.example.funnyrun.R;
import com.example.funnyrun.ShareActivity;
import com.example.funnyrun.location.BaiduMapActivity;

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

public class FreedomModeActivity extends Activity {
	private String time;
	private int distance = 0;
	private Button btn;
	private TextView timeView;
	private TextView distanceView;
	private ChronometerService.MyBinder myTimeBinder = null;
	private DistanceService.MyBinder myDistanceBinder = null;
	private MyReceiver myReceiver=null;
	private Intent timeIntent, distanceIntent;
	private String addr;
	private IntentFilter filter;
	private String tag = this.getClass().getSimpleName();
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
			time = ((MyApplication)getApplication()).getTime(myTimeBinder.getTime());
			distance = myDistanceBinder.getDistance();
			if (myTimeBinder != null)
				timeView.setText(time);
			if (myDistanceBinder != null)
				distanceView.setText(distance + "m");
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_freedom);		
		btn = (Button) findViewById(R.id.btn);
		timeView = (TextView) findViewById(R.id.time);
		distanceView = (TextView) findViewById(R.id.distance);
		timeIntent = new Intent();
		timeIntent.setAction("service.CHRONOMETER_ACTION");
		distanceIntent = new Intent();
		distanceIntent.setAction("service.DISTANCE_ACTION");	
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn:
			if (btn.getText().toString().equals("开始")) {
				btn.setText("结束");
				bindService(timeIntent, timeConn, Service.BIND_AUTO_CREATE);
				bindService(distanceIntent, distanceConn,
						Service.BIND_AUTO_CREATE);
				filter = new IntentFilter();
				filter.addAction("action.UPDATE_ACTION");
				myReceiver = new MyReceiver();
				registerReceiver(myReceiver, filter);
			} else {
				btn.setText("开始");
//				unbindService(timeConn);
//				unbindService(distanceConn);
//				stopService(timeIntent);
//				stopService(distanceIntent);
//				unregisterReceiver(myReceiver);
				startShareActivity();				
			}
			break;
		case R.id.btn_map:
			startMapActivity();
			break;

		}
	}

	private void startShareActivity() {
//		Log.d(tag, "跳转");
		Intent intent = new Intent(FreedomModeActivity.this,
				ShareActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("addr", addr);
		bundle.putString("time", time);
		bundle.putInt("distance", distance);
		BmobDate date = new BmobDate(new Date());
		bundle.putSerializable("date", date);
		//BmobUser user = ((MyApplication) getApplication()).user;
		//bundle.putSerializable("user", user);
		intent.putExtras(bundle);
//		Log.d(tag, "跳转 1");
		startActivity(intent);
		finish();
	}

	public void startMapActivity() {
		Intent intent = new Intent(FreedomModeActivity.this,
				BaiduMapActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
//		unregisterReceiver(myReceiver);
	}
	
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
//		registerReceiver(myReceiver, filter);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(myTimeBinder!=null){unbindService(timeConn);stopService(timeIntent);}
		if(myDistanceBinder!=null){unbindService(distanceConn);stopService(distanceIntent);}
		if(myReceiver!=null)unregisterReceiver(myReceiver);
	}
	
	

	public class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			myHandler.sendEmptyMessage(0);
		}

	}
}
