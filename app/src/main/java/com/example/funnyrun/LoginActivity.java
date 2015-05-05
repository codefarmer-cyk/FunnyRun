package com.example.funnyrun;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

import com.example.funnyrun.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	EditText et_username;
	EditText et_password;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);	
		et_username = (EditText) findViewById(R.id.username);
		et_password = (EditText) findViewById(R.id.password);
		BmobUser user = ((MyApplication)getApplication()).user;
		if (user != null) {
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
		}
	}

	public void onClick(View view) {
		String username, password;
		username = et_username.getText().toString().trim();
		password = et_password.getText().toString().trim();
		if (username.equals("")) {
			Toast.makeText(this, "Please input username!", Toast.LENGTH_SHORT)
					.show();
			return;
		} else if (password.equals("")) {
			Toast.makeText(this, "Plase input password!", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		BmobUser user = ((MyApplication)getApplication()).user;
		switch (view.getId()) {
		case R.id.btn_login:
			user.setUsername(username);
			user.setPassword(password);
			user.login(this, new SaveListener() {

				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					Intent intent = new Intent(LoginActivity.this,
							MainActivity.class);
					startActivity(intent);
				}

				@Override
				public void onFailure(int arg0, String arg1) {
					// TODO Auto-generated method stub
					toast("Login fail!\n" + arg1);
				}
			});

			// Toast.makeText(
			// this,
			// "username=" + username.getText().toString() + "\npassword="
			// + password.getText().toString()
			// + "\nclick login button", Toast.LENGTH_SHORT)
			// .show();
			break;
		case R.id.btn_register:
			user.setUsername(username);
			user.setPassword(password);		
//			user.setEmail("594701648@qq.com");
			user.signUp(this, new SaveListener() {
				
				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					Intent intent = new Intent(LoginActivity.this,
							MainActivity.class);
					startActivity(intent);
				}
				
				@Override
				public void onFailure(int arg0, String arg1) {
					// TODO Auto-generated method stub
					toast("register fail!\n"+arg1);
				}
			});
			// Toast.makeText(this, "click register",
			// Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}

	private void toast(String arg) {
		Toast.makeText(this, arg, Toast.LENGTH_SHORT).show();
	}
}
