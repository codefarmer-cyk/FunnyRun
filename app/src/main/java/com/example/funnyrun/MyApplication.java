package com.example.funnyrun;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.bmob.BmobConfiguration;
import com.bmob.BmobPro;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.os.Vibrator;


public class MyApplication extends Application{
	private final static String BmobAppID="b5ef08ccf0565256434d17c4c44cece3";
	public  LocationClient locationClient;
	public LocationClientOption locationClientOption = new LocationClientOption();
	public Vibrator vibrator;
	public BmobUser user;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		SDKInitializer.initialize(this);
		Bmob.initialize(this, BmobAppID);
		vibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
		initLocation();
		initBmobConfig(getApplicationContext());
		user = BmobUser.getCurrentUser(this);
	}		
	
	public void initLocation(){
		locationClient=new LocationClient(this.getApplicationContext());
		locationClientOption.setOpenGps(true);
		locationClientOption.setNeedDeviceDirect(true);
		locationClientOption.setIsNeedAddress(true);
		locationClientOption.setAddrType("all");
		locationClientOption.setLocationMode(LocationMode.Hight_Accuracy);
		locationClientOption.setCoorType("bd09ll");
		locationClientOption.setScanSpan(1000);
		locationClient.setLocOption(locationClientOption);
	}
	
	private void initBmobConfig(Context context) {
		BmobConfiguration config = new BmobConfiguration.Builder(context).customExternalCacheDir("Smile").build();
		BmobPro.getInstance(context).initConfig(config);
	}
	
	public static String getTime(long timeMilles) {
		String result = "";
		long hours = timeMilles / 3600000;
		long minues = (timeMilles % 3600000) / 60000;
		long seconds = (timeMilles % 3600000 % 60000) / 1000;
		if (hours < 10) {
			result += "0";
		}
		result += hours + ":";
		if (minues < 10) {
			result += "0";
		}
		result += minues + ":";
		if (seconds < 10) {
			result += "0";
		}
		result += seconds;
		return result;
	}
}
