package com.example.funnyrun.mode;

import com.example.funnyrun.R;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ChallengeModeSelectActivity extends Activity {
	String[] level;
	int[] distance;
	int[] time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		level = getResources().getStringArray(R.array.level);
		distance = getResources().getIntArray(R.array.distance);
		time = getResources().getIntArray(R.array.time);
		setContentView(R.layout.activity_challenge_select);
		ListView list1 = (ListView) findViewById(R.id.listView);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.array_item, level);
		list1.setAdapter(adapter);
		list1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Toast.makeText(ChallengeModeSelectActivity.this,
						"click " + level[position], Toast.LENGTH_SHORT).show();
				Bundle bundle = new Bundle();
				bundle.putString("mode", "challenge");
				bundle.putInt("level", position);
				bundle.putInt("distance", distance[position % 2]);
				bundle.putInt("time", time[position % 3]);				
				Intent intent = new Intent(ChallengeModeSelectActivity.this,
						com.example.funnyrun.mode.ChallengeModeActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}

}
