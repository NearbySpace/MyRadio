package com.example.toolbar.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.example.dolphinradio.R;
import com.example.toolbar.application.MyApplication;
import com.example.toolbar.bean.UserInfo;
import com.example.toolbar.common.utils.LogHelper;
import com.example.toolbar.common.utils.MyJsonUtils;
import com.example.toolbar.fragment.NewFirstFragment;
import com.example.toolbar.fragment.NewTwoFragment;
import com.example.toolbar.framework.UpdateApk;
import com.example.toolbar.http.HttpManage;
import com.example.toolbar.http.HttpManage.OnCallBack;
import com.example.toolbar.service.LocationService;
import com.example.toolbar.utils.ConfigUtils;
import com.example.toolbar.utils.ControlActivity;
import com.example.toolbar.utils.ImageUtils;
import com.example.toolbar.utils.IntentUtils;
import com.example.toolbar.utils.SharePreferenceUtil;
import com.example.toolbar.utils.UserUtils;
import com.example.toolbar.widget.MaterialDialog;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class NewMainActivity extends AppCompatActivity implements
		OnClickListener {
	private DrawerLayout mDrawerLayout;
	private ViewPager mViewPager;
	private List<Fragment> viewList;
	private ImageView usericon;
	private TextView nikename;
	private LinearLayout linearlayout;
	private boolean isToLogin = false;
	private LocationService locationService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_main);
		ControlActivity.getInstance().addActivity(NewMainActivity.this);
		initView();
		initLocationService();
		UpdateApk.checkVersion(NewMainActivity.this, 1);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if (isToLogin) {
			if (!MyApplication.getInstance().getSpUtil().getUid().isEmpty()) {
				initUserData();
			}
		}
	}

	private void initView() {
		mDrawerLayout = (DrawerLayout) findViewById(R.id.new_drawer);
		linearlayout = (LinearLayout) findViewById(R.id.new_main_linearlayout);
		usericon = (ImageView) findViewById(R.id.new_usericon);
		nikename = (TextView) findViewById(R.id.new_nikename);
		mViewPager = (ViewPager) findViewById(R.id.new_main_pager);
		Fragment fargmentFirst = new NewFirstFragment();
		Fragment fragmentTwo = new NewTwoFragment();
		viewList = new ArrayList<Fragment>();// 将要分页显示的View装入数组中
		viewList.add(fargmentFirst);
		viewList.add(fragmentTwo);
		mViewPager.setAdapter(new MyViewPagerAdapter(this
				.getSupportFragmentManager()));
		findViewById(R.id.new_add_progmer_button).setOnClickListener(this);
		findViewById(R.id.new_my_program).setOnClickListener(this);
		findViewById(R.id.new_my_favorite).setOnClickListener(this);
		findViewById(R.id.new_setting).setOnClickListener(this);
		findViewById(R.id.new_more_shre).setOnClickListener(this);
		findViewById(R.id.new_addusers).setOnClickListener(this);
		LogHelper.e(MyApplication.getInstance().getSpUtil().getNick());
		nikename.setText(MyApplication.getInstance().getSpUtil().getNick());
		usericon.setOnClickListener(this);
		if (!MyApplication.getInstance().getSpUtil().getUid().isEmpty()) {
			initUserData();
		}
	}

	private void initLocationService() {
		locationService = ((MyApplication) getApplication()).locationService;
		locationService.registerListener(mListener);
		locationService.setLocationOption(locationService
				.getDefaultLocationClientOption());
		locationService.start();
	}

	@SuppressLint("NewApi")
	private void setLayoutBackground(int resId) {
		Bitmap bm = ImageUtils.getMatchBitmap(NewMainActivity.this, resId);
		Drawable drawable = new BitmapDrawable(getResources(), bm);
		linearlayout.setBackground(drawable);
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		switch (arg0) {
		case 1:
			if (arg1 == 1) {
				initUserData();
			}

			break;

		default:
			break;
		}

	}

	private void initUserData() {
		isToLogin = UserUtils.checkLogin(this);
		if (isToLogin) {
			return;
		}
		
		String url = HttpManage.mainUserDataUrl;
		String uid = MyApplication.getInstance().getSpUtil().getUid();
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("uid", uid);
		HttpManage.getNetData(url, paramsMap, 1, new OnCallBack() {
			
			@Override
			public void onSuccess(byte[] arg2) {
				String data = new String(arg2);
				LogHelper.e(data);
				Gson gson = new Gson();
				UserInfo info = gson.fromJson(data, UserInfo.class);
				nikename.setText(info.nickname);
				ImageLoader.getInstance().displayImage(info.avatar, usericon);
				
			}
			
			@Override
			public void onFailure(byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				LogHelper.e("error");
			}
		});
//		HttpManage.getMainUserData(uid, new AsyncHttpResponseHandler() {
//
//			@Override
//			public void onFailure(int arg0, Header[] arg1, byte[] bt,
//					Throwable arg3) {
//				// TODO Auto-generated method stub
//				LogHelper.e("error");
//			}
//
//			@Override
//			public void onSuccess(int arg0, Header[] arg1, byte[] bt) {
//				// TODO Auto-generated method stub
//				String data = new String(bt);
//				LogHelper.e(data);
//				Gson gson = new Gson();
//				UserInfo info = gson.fromJson(data, UserInfo.class);
//				nikename.setText(info.nickname);
//				ImageLoader.getInstance().displayImage(info.avatar, usericon);
//			}
//		});
	}

	public void switchFragment(int position) {
		mViewPager.setCurrentItem(position);
	}
	
	public void openDrawerLayout() {
		mDrawerLayout.openDrawer(Gravity.LEFT);
		initUserData();
	}

	public class MyViewPagerAdapter extends FragmentPagerAdapter {

		public MyViewPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return viewList.size();
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			return viewList.get(arg0);
		}

	}

	private MaterialDialog mMaterialDialog;

	@Override
	public void onClick(View v) {
		isToLogin = UserUtils.checkLogin(this);
		if (isToLogin) {
			return;
		}

		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.new_usericon:
			UserUtils.checkLogin(NewMainActivity.this);
			intent.setClass(NewMainActivity.this, UpUserIconActivity.class);
			// startActivityForResult(intent, SET_STUDENT_ICON);
			startActivityForResult(intent, 1);
			break;

		case R.id.new_my_program:
			intent.setClass(NewMainActivity.this, MyProgramActivity.class);
			// startActivityForResult(intent, SET_STUDENT_ICON);
			startActivity(intent);
			break;

		case R.id.new_my_favorite:
			intent.setClass(NewMainActivity.this, MyFavoriteActivity.class);
			// startActivityForResult(intent, SET_STUDENT_ICON);
			startActivity(intent);
			break;

		case R.id.new_more_shre:
			intent.setClass(NewMainActivity.this, MyDownLoadActivity.class);
			// startActivityForResult(intent, SET_STUDENT_ICON);
			startActivity(intent);
			break;

		case R.id.new_setting:
			intent.setClass(NewMainActivity.this, SettingActivity.class);
			// startActivityForResult(intent, SET_STUDENT_ICON);
			startActivity(intent);
			break;
		case R.id.new_addusers:
			intent.setClass(NewMainActivity.this, SuggestionActivity.class);
			startActivity(intent);
			break;

		case R.id.new_add_progmer_button:
			// 自定义布局
			final EditText editText;
			Button positiveButton;
			Button negativeButton;
			mMaterialDialog = new MaterialDialog(this);
			if (mMaterialDialog != null) {
				View view = LayoutInflater.from(this).inflate(
						R.layout.add_v_channel, null);
				editText = (EditText) view.findViewById(R.id.editText2);
				positiveButton = (Button) view.findViewById(R.id.qd);
				negativeButton = (Button) view.findViewById(R.id.qx);
				positiveButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						String titel = editText.getText().toString();
						if (titel.isEmpty()) {
							editText.setHint("频道名不能为空");
							return;
						}
						IntentUtils.startActivityForString(
								NewMainActivity.this,
								SelectProgramActivity.class, "titel", titel);
						// startActivityForResult(int ent, SET_STUDENT_ICON);
						mMaterialDialog.dismiss();
					}
				});
				negativeButton.setOnClickListener(new OnClickListener() {

					public void onClick(View arg0) {
						mMaterialDialog.dismiss();
					}
				});

				mMaterialDialog.setView(view).show();
			}

			break;
		default:
			break;
		}

	}
	
	
	private boolean isCommitSuccess = false;
	private boolean isAgainCommit = true;//是否再次提交
	/**
	 * 访问统计
	 */
	private void visitStatistics(String city, String district, String lnglat) {
		SharedPreferences sp = getSharedPreferences("phoneInfo", MODE_PRIVATE);
		String isFirst = sp.getString("isFirst", "0");
		String phone_model = sp.getString("phone_model", "null");
		String phone_brand = sp.getString("phone_brand", "null");
		String phone_os = "1";
//		SharePreferenceUtil spu = new SharePreferenceUtil(NewMainActivity.this,
//				ConfigUtils.appSharePreferenceName);
		String mid = MyApplication.getInstance().getSpUtil().getUid();
		String url = HttpManage.visitStatisticsUrl;
		if(mid.equals("")){
			mid = "0";
		}
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("url", url);
		paramsMap.put("mid", mid);
		paramsMap.put("city", city);
		paramsMap.put("district", district);
		paramsMap.put("lnglat", lnglat);
		paramsMap.put("version", getIntent().getStringExtra("version"));
		paramsMap.put("os_version", Build.VERSION.RELEASE);
		paramsMap.put("phone_model", phone_model);
		paramsMap.put("phone_brand", phone_brand);
		paramsMap.put("phone_os", phone_os);
		paramsMap.put("isfirst", isFirst);
		HttpManage.getNetData(url, paramsMap, 0, new OnCallBack() {
			
			@Override
			public void onSuccess(byte[] arg2) {
				// TODO Auto-generated method stub
				String result;
				result = new String(arg2);
				Log.i("NewMainActivity", "访问统计返回的结果："+result);
				isCommitSuccess = MyJsonUtils.isCheckStringState(result);
				if(isCommitSuccess){
					locationService.stop();
				}else{
					isAgainCommit = true;
				}
			}
			
			@Override
			public void onFailure(byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				Log.i("NewMainActivity", "访问统计返回的结果：联网失败");
				isAgainCommit = true;
			}
		});
		
//		HttpManage.visitStatistics(mid, city, district, lnglat, getIntent().getStringExtra("version"),
//				Build.VERSION.RELEASE, phone_model, phone_brand, phone_os, isFirst,
//				new AsyncHttpResponseHandler() {
//					
//					@Override
//					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
//						String result;
//						result = new String(arg2);
//						Log.i("NewMainActivity", "访问统计返回的结果："+result);
//						isCommitSuccess = MyJsonUtils.isCheckStringState(result);
//						if(isCommitSuccess){
//							locationService.stop();
//						}else{
//							isAgainCommit = true;
//						}
//						
//					}
//					
//					@Override
//					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
//						Log.i("NewMainActivity", "访问统计返回的结果：联网失败");
//						isAgainCommit = true;
//					}
//				});
	}

	

	private BDLocationListener mListener = new BDLocationListener() {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// TODO Auto-generated method stub
			if (null != location
					&& location.getLocType() != BDLocation.TypeServerError) {
				StringBuffer sb = new StringBuffer(256);
				String city = location.getCity();
				String district = location.getDistrict();
				String lnglat = location.getLongitude()+","+location.getLatitude();
				if(isAgainCommit){ //需要判断是否需要重新提交一次，不然会提交多次
					visitStatistics(city, district, lnglat);
					isAgainCommit = false;
				}
				
			}
		}

	};


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			
			exitBy2Click(); // 调用双击退出函数
		}
		return false;
	}

	private static Boolean isExit = false;

	private void exitBy2Click() {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true; // 准备退出
			Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false; // 取消退出
				}
			}, 1500); // 如果1.5秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

		} else {
			finish();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		viewList.clear();
	}

}
