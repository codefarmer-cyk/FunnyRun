package com.example.funnyrun;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class ChronometerService extends Service {
	boolean flag = true;
	long startTime;
	int time;
	MyBinder myBinder = new MyBinder();

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		flag=true;
		return myBinder;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		startTime = System.currentTimeMillis();
		new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				while (flag) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					time = (int) (System.currentTimeMillis() - startTime);
					Intent intent = new Intent("action.UPDATE_ACTION");
					sendBroadcast(intent);
				}
			}

		}.start();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		this.flag = false;
	}

	public class MyBinder extends Binder {
		public int getTime() {
			return time;
		}
	}
	

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		flag=false;
		return super.onUnbind(intent);
	}
	
}
