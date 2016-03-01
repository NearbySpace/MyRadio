package com.example.toolbar.activity;

import com.example.strawberryradio.R;
import com.example.toolbar.application.MyApplication;
import com.example.toolbar.bean.UserInfo;
import com.example.toolbar.common.utils.LogHelper;
import com.google.gson.Gson;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends Activity {
	private final String TAG = "SplashActivity";
	private String[] data;
	private final int SUCCESS = 2;
	private final int IO_ERROR = 1;
	private final int URL_ERROR = 0;
	private boolean isUpdata = false;
	private TextView tv_version;
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
		
		if (isUpdata) {
			// 停2秒，联网检查版本是否有跟新
			getConnetUpdate();
		} else {
			// 进入主界面
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						Thread.sleep(1000);
						mPlayer = MediaPlayer.create(SplashActivity.this, R.raw.dolphin);
						mPlayer.start();
						Thread.sleep(1000);
						enterHome();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}).start();

		}
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
		return new String("1.0");
	}

	private void getConnetUpdate() {
		// 联网获得数据，并解析数据
		new Thread(new Runnable() {
			Message msg = new Message();

			// long starttime=System.currentTimeMillis();
			// @Override
			// public void run() {
			//
			// try {
			// URL url=new URL("http://"+"IP"+":80/book.xml");
			// try {
			// HttpURLConnection conn=(HttpURLConnection) url.openConnection();
			// conn.setRequestMethod("GET");
			// conn.setConnectTimeout(5000);
			// conn.setReadTimeout(3000);
			// int code=conn.getResponseCode();
			// if(code==200){
			// InputStream is=conn.getInputStream();
			// String version=StreamTools.readFromStream(is);
			// //解析得到数据
			// data=parse(version);
			// System.out.println(is);
			// Log.i(TAG, "联网成功");
			// msg.what=SUCCESS;
			// }else{
			// Log.i(TAG, "联网失败");
			// }
			// long endtime=System.currentTimeMillis();
			// try {
			// Thread.sleep((Math.abs(2000-(endtime-starttime))));
			// } catch (InterruptedException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			//
			// } catch (IOException e) {
			// //根据捕获的错误的类型，提醒用户是哪里出错了，导致检查不到版本更新
			// msg.what=IO_ERROR;
			// e.printStackTrace();
			// }
			// } catch (MalformedURLException e) {
			// msg.what=URL_ERROR;
			// e.printStackTrace();
			// }finally{
			// handler.sendMessage(msg);
			// }
			//
			// }

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				msg.what = SUCCESS;
				handler.sendMessage(msg);
			}
		}).start();
	}

	/**
	 * 解析数据
	 * 
	 * @param josn
	 * @return
	 */
	private String[] parse(String josn) {
		Gson gson = new Gson();
		return null;
	}

	/**
	 * 进入主页面
	 */
	private void enterHome() {
		Intent intent = new Intent(SplashActivity.this, NewMainActivity.class);
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
