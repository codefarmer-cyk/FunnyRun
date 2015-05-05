package com.example.funnyrun;

import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.SaveListener;

import com.example.funnyrun.javabean.MyUser;
import com.example.funnyrun.javabean.Score;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class ShareActivity extends Activity{

	private String tag=this.getClass().getSimpleName();
	private Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);
		Log.d(tag, "��ʼ��");
		intent =getIntent();
		uploadData();
	}
	
	private void uploadData(){
		Bundle bundle = intent.getExtras();
		Score myScore = new Score();
		//myScore.setAuthor((MyUser) bundle.get("user"));
		myScore.setAddress(bundle.getString("addr"));
		myScore.setCostTime(bundle.getString("time"));
		myScore.setDistance(bundle.getInt("distance"));
		myScore.setScore(bundle.getInt("score"));
		myScore.setUpdateTime((BmobDate) bundle.get("date"));	
		myScore.save(this, new SaveListener() {

			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(ShareActivity.this, "����ϴ�ʧ�ܣ�", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				Toast.makeText(ShareActivity.this, "����ϴ��ɹ���", Toast.LENGTH_LONG).show();
			}
			
		});
	}

}
