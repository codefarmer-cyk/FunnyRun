package com.example.funnyrun;

import java.util.ArrayList;
import java.util.List;

import com.example.funnyrun.location.BaiduMapActivity;
import com.example.funnyrun.mode.FreedomModeActivity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		listView = (ListView) findViewById(R.id.listView);
		listView.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_expandable_list_item_1, getData()));
	}

	private List<String> getData() {
		List<String> data = new ArrayList<String>();
		data.add("自由模式");
		data.add("自定义模式");
		data.add("挑战模式");
		data.add("极限模式");
		data.add("疯狂模式");
		return data;
	}
	
	private void toast(int arg){
		Toast.makeText(this, "Nothing in "+arg, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Class targetClass = null;
				switch(position){		
				case 0: targetClass = com.example.funnyrun.mode.FreedomModeActivity.class;break;
				case 1: targetClass = com.example.funnyrun.location.BaiduMapActivity.class;break;
				case 2: targetClass = com.example.funnyrun.mode.ChallengeModeSelectActivity.class;break;
				default:;
				}				
				if(targetClass!=null){
					Intent intent =new Intent(MainActivity.this,targetClass);
//					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				}				
			}
		});

	}
}
