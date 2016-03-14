package com.example.toolbar.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

import com.example.strawberryradio.R;
import com.google.gson.Gson;

public class SplashActivity extends Activity {
	private final String TAG = "SplashActivity";
	private String[] data;
	private final int SUCCESS = 2;
	private final int IO_ERROR = 1;
	private final int URL_ERROR = 0;
	private boolean isUpdata = false;
	private TextView tv_version;
	private SharedPreferences sp;
	// private SharedPreferences sp;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			int number = msg.what;
			switch (number) {
			case URL_ERROR:
				Toast.makeText(SplashActivity.this, "URL有误", 0).show();
				enterHome();
				break;
			case IO_ERROR:
				Toast.makeText(SplashActivity.this, "接收数据时出错", 0).show();
				enterHome();
				break;
			case SUCCESS:
				// 对比版本号，不同就弹出升级对话框

				Toast.makeText(SplashActivity.this, "连接成功", 0).show();
				enterHome();
				break;
			}
		}

	};

	private MediaPlayer mPlayer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_activity);
		tv_version = (TextView) findViewById(R.id.tv_version);
		tv_version.setText("" + getVersionName());
		sp = getSharedPreferences("phoneInfo",MODE_PRIVATE);
		savePhoneInfo();
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					mPlayer = MediaPlayer.create(SplashActivity.this, R.raw.dolphin);
					mPlayer.start();
					Thread.sleep(800);
					enterHome();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}).start();
	}

	private String getVersionName() {
		PackageManager pm = getPackageManager();
		try {
			PackageInfo info = pm.getPackageInfo("com.example.strawberryradio",
					0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new String("null");
	}
	
	/**
	 * 判断是否首次安装，如果是就保存手机信息
	 */
	private void savePhoneInfo(){
		String isFirst ;//0不是首次安装，1是首次安装
		Editor editor = sp.edit();
		isFirst = sp.getString("isFirst", "1");
		if(isFirst.equals("1")){
//			editor.putString("isFirst", isFirst);
			editor.putString("phone_model", android.os.Build.MODEL);//手机型号
			editor.putString("phone_brand", android.os.Build.BRAND);//手机品牌
			editor.putString("phone_os", "1");
			editor.commit();
		}
	}
	

	/**
	 * 进入主页面
	 */
	private void enterHome() {
		Intent intent = new Intent(SplashActivity.this, NewMainActivity.class);
		intent.putExtra("version", getVersionName());
		startActivity(intent);
		// if(MyApplication.getInstance().getSpUtil().getIsAutoLogin()){
		// Intent intent=new Intent(SplashActivity.this,MainActivity.class);
		// startActivity(intent);
		// }else{
		// Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
		// startActivity(intent);
		// }
		finish();

	}

}
