package com.example.funnyrun.location;

import java.util.ArrayList;
import java.util.List;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerDragListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.DotOptions;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.funnyrun.DistanceService;
import com.example.funnyrun.DistanceService.MyBinder;
import com.example.funnyrun.MyApplication;
import com.example.funnyrun.R;
import com.example.funnyrun.mode.FreedomModeActivity.MyReceiver;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class BaiduMapActivity extends Activity {
	private MapView mapView;
	private BaiduMap baiduMap;
	private LocationClient locationClient;
	private MyLocationListener locationListener;
	private LocationMode locationMode;
	private LatLng targetPoint;
	private List<LatLng> points = null;
	private BitmapDescriptor targetIcon = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_en);;
	private Marker myPolyline;
	private Marker targetMarker;
	private NotifyLister notifyLister;
	private DistanceService.MyBinder myPointsBinder = null;
	boolean isFirstLoc = true;
	private Vibrator vibrator;
	private Intent pointsIntent;
	private SDKReceiver sdkReceiver;
	private IntentFilter sdkFilter;
	private ServiceConnection pointsConn = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			myPointsBinder = (com.example.funnyrun.DistanceService.MyBinder) service;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub

		}

	};
	private Handler myNotifyListerHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (notifyLister != null) {
				locationClient.removeNotifyEvent(notifyLister);
			}
			notifyLister = new NotifyLister();
			notifyLister.SetNotifyLocation(targetPoint.latitude,
					targetPoint.longitude, 1, "bd09ll");
			locationClient.registerNotify(notifyLister);
			if (targetMarker != null)
				targetMarker.remove();
			OverlayOptions overlay = new MarkerOptions().icon(targetIcon)
					.position(targetPoint).zIndex(10).draggable(true);
			targetMarker = (Marker) baiduMap.addOverlay(overlay);
		}
	};

	private Handler myLocationHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (points!=null&&points.size() > 1) {
				if (myPolyline != null)
					myPolyline.remove();
				if (myPointsBinder != null)
					points = myPointsBinder.getPoints();
				OverlayOptions overlay = new PolylineOptions().width(10)
						.color(0xAAFF0000).points(points);
				Polyline p = (Polyline) baiduMap.addOverlay(overlay);
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_location);
		vibrator = (Vibrator) getApplicationContext().getSystemService(
				Service.VIBRATOR_SERVICE);
		mapView = (MapView) findViewById(R.id.bmapView);
		baiduMap = mapView.getMap();
		locationMode = LocationMode.NORMAL;
		baiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
				locationMode, true, null));
		baiduMap.setMyLocationEnabled(true);
		targetPoint = null;
		MapStatusUpdate u = MapStatusUpdateFactory.zoomTo(19);
		baiduMap.animateMapStatus(u);

		pointsIntent = new Intent();
		pointsIntent.setAction("service.DISTANCE_ACTION");
		baiduMap.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onMapClick(LatLng arg0) {
				// TODO Auto-generated method stub
				targetPoint = arg0;
				myNotifyListerHandler.sendEmptyMessage(0);
				Toast.makeText(
						BaiduMapActivity.this,
						"终点设置成功\n维度为：" + arg0.latitude + "\n经度为："
								+ arg0.longitude, Toast.LENGTH_SHORT).show();
			}
		});
		baiduMap.setOnMarkerDragListener(new OnMarkerDragListener() {

			@Override
			public void onMarkerDragStart(Marker arg0) {
				// TODO Auto-generated method stub
				locationClient.removeNotifyEvent(notifyLister);
			}

			@Override
			public void onMarkerDragEnd(Marker arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(
						BaiduMapActivity.this,
						"终点坐标更新\n维度为：" + arg0.getPosition().latitude + "\n经度为："
								+ arg0.getPosition().longitude,
						Toast.LENGTH_SHORT).show();
				targetPoint = arg0.getPosition();
				notifyLister.SetNotifyLocation(targetPoint.latitude,
						targetPoint.longitude, 1, "bd09ll");
				locationClient.registerNotify(notifyLister);
			}

			@Override
			public void onMarkerDrag(Marker arg0) {
				// TODO Auto-generated method stub

			}
		});

		locationClient = ((MyApplication) getApplication()).locationClient;
		locationListener = new MyLocationListener();
		locationClient.registerLocationListener(locationListener);
		bindService(pointsIntent,pointsConn,Service.BIND_AUTO_CREATE);			
		IntentFilter filter = new IntentFilter();
		filter.addAction("action.UPDATE_ACTION");

		// LocationClientOption locationClientOption = new
		// LocationClientOption();
		// locationClientOption.setOpenGps(true);
		// locationClientOption.setCoorType("bd09ll");
		// locationClientOption.setScanSpan(1000);
		// locationClient.setLocOption(locationClientOption);
		sdkReceiver = new SDKReceiver();
		sdkFilter = new IntentFilter();
		sdkFilter
				.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
		sdkFilter
				.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
		sdkFilter
				.addAction(SDKInitializer.SDK_BROADTCAST_INTENT_EXTRA_INFO_KEY_ERROR_CODE);
		registerReceiver(sdkReceiver, sdkFilter);
//		locationClient.start();
	}

	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation arg0) {
			// TODO Auto-generated method stub
			if (arg0 == null || mapView == null) {
				return;
			}
			MyLocationData locaData = new MyLocationData.Builder()
					.accuracy(arg0.getRadius()).direction(100)
					.latitude(arg0.getLatitude())
					.longitude(arg0.getLongitude()).build();
//			points.add(new LatLng(arg0.getLatitude(), arg0.getLongitude()));
			baiduMap.setMyLocationData(locaData);
			myLocationHandler.sendEmptyMessage(0);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng latlng = new LatLng(arg0.getLatitude(),
						arg0.getLongitude());
				MapStatusUpdate update = MapStatusUpdateFactory
						.newLatLng(latlng);
				baiduMap.animateMapStatus(update);
			}
		}
	}

	public class NotifyLister extends BDNotifyListener {
		public void onNotify(BDLocation mlocation, float distance) {
			vibrator.vibrate(1000);// 振动提醒已到设定位置附近
			Toast.makeText(BaiduMapActivity.this, "震动提醒", Toast.LENGTH_SHORT)
					.show();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mapView.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mapView.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		locationClient.stop();
		baiduMap.clear();
		baiduMap.setMyLocationEnabled(false);
		mapView.onDestroy();
		mapView = null;
		targetIcon.recycle();
		locationClient.removeNotifyEvent(notifyLister);
		unbindService(pointsConn);
		unregisterReceiver(sdkReceiver);
	}
		

	public class SDKReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			String s = intent.getAction();
			// Log.d(LTAG, "action: " + s);
			// TextView text = (TextView) findViewById(R.id.text_Info);
			// text.setTextColor(Color.RED);
			if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
				// text.setText("key 验证出错! 请在 AndroidManifest.xml 文件中检查 key 设置");
				Toast.makeText(BaiduMapActivity.this,
						"key 验证出错! 请在 AndroidManifest.xml 文件中检查 key 设置",
						Toast.LENGTH_LONG).show();
			} else if (s
					.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
				// text.setText("网络出错");
				Toast.makeText(BaiduMapActivity.this, "网络出错", Toast.LENGTH_LONG)
						.show();
			} else if (s
					.equals(SDKInitializer.SDK_BROADTCAST_INTENT_EXTRA_INFO_KEY_ERROR_CODE)) {
				Toast.makeText(BaiduMapActivity.this, "秘钥错误", Toast.LENGTH_LONG)
						.show();
			}
		}
	}

}
