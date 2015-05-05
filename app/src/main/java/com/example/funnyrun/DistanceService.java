package com.example.funnyrun;

import java.util.ArrayList;
import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;

public class DistanceService extends Service{
	int distance=0;
	boolean flag =true;
	String addr="";
	private LocationClient myLocationClient;
	private BDLocation myLocation;
	private LatLng pointA=null;
	private LatLng pointB=null;	
	private List<LatLng> points;
	MyBinder myBinder = new MyBinder();
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return myBinder;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		myLocationClient=((MyApplication)getApplication()).locationClient;
		points = new ArrayList<LatLng>();
		myLocationClient.registerLocationListener(new BDLocationListener(){
			
			@Override
			public void onReceiveLocation(BDLocation arg0) {
				// TODO Auto-generated method stub
				myLocation = arg0;
				pointB=pointA;
				addr=arg0.getAddrStr();
				pointA=new LatLng(arg0.getLatitude(),arg0.getLongitude());
				points.add(pointA);
				if(pointB!=null){
					distance+=DistanceUtil.getDistance(pointA,pointB);					
					Intent intent = new Intent();
					intent.setAction("action.UPDATE_ACTION");
					sendBroadcast(intent);
				}
			}
		});
		myLocationClient.start();		
	}	
	
	public class MyBinder extends Binder{
		public  int getDistance(){
			return distance;
		}
		public ArrayList<LatLng> getPoints(){
			return (ArrayList<LatLng>) points;			
		}
		public String getAddr(){
			return addr;
		}
	}
	
	
	
}
