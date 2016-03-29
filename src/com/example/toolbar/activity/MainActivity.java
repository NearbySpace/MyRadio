package com.example.toolbar.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Shader.TileMode;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dolphinradio.R;
import com.example.toolbar.adapter.PagerAdapter;
import com.example.toolbar.application.MyApplication;
import com.example.toolbar.bean.UserInfo;
import com.example.toolbar.common.utils.AppManager;
import com.example.toolbar.common.utils.Common;
import com.example.toolbar.common.utils.FileUtils;
import com.example.toolbar.common.utils.ImageLoaderHelper;
import com.example.toolbar.common.utils.LogHelper;
import com.example.toolbar.common.utils.NetUtil;
import com.example.toolbar.fragment.FirstFragment;
import com.example.toolbar.framework.UpdateApk;
import com.example.toolbar.http.HttpManage;
import com.example.toolbar.http.HttpManage.OnCallBack;
import com.example.toolbar.utils.ConfigUtils;
import com.example.toolbar.utils.ControlActivity;
import com.example.toolbar.utils.ImageUtils;
import com.example.toolbar.utils.IntentUtils;
import com.example.toolbar.utils.OneKeyExit;
import com.example.toolbar.utils.SharePreferenceUtil;
import com.example.toolbar.utils.ToastUtils;
import com.example.toolbar.utils.UserUtils;
import com.example.toolbar.widget.MaterialDialog;
import com.example.toolbar.widget.PagerSlidingTabStrip;
import com.example.toolbar.widget.SuperAwesomeCardFragment;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MainActivity extends AppCompatActivity implements OnClickListener {
	OneKeyExit exit = new OneKeyExit();
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private ShareActionProvider mShareActionProvider;
	private PagerSlidingTabStrip mPagerSlidingTabStrip;
	private ViewPager mViewPager;
	private Toolbar mToolbar;
	private MaterialDialog mMaterialDialog;

	private ImageView usericon;
	private TextView nikename, attentionTv, fansTv, messageTv;
	private ImageLoader mImageLoader;
	private String pathString;
	private List<Fragment> fragmentList;
	private NotificationCompat.Builder mBuilder;
	private NotificationManager mNotifitionManager;
	private Map<String, String> map;
	private boolean isToLogin=false;//是否进入登录业，标记第一次进来不会进的登录页
	/**
	 * 四个Fragment（页面）
	 */
	private FirstFragment firstFragment;

	private SharePreferenceUtil sp;
	private boolean IS_CHECK＿UPDATE = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ControlActivity.getInstance().addActivity(MainActivity.this);
		map = new HashMap<String, String>();
		mNotifitionManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		initViews();
		updateVersion();
		
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if(isToLogin){
			if(!MyApplication.getInstance().getSpUtil().getUid().isEmpty()){
				initData();
			}
		}
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		switch (arg0) {
		case 1:
			if(arg1==1){
				initUserData();
			}
			
			break;

		default:
			break;
		}
		
	}
	private void updateVersion() {
		// if (!NetUtil.isNetConnected(MainActivity.this)) {
		// return;
		// }
		// HttpManage.getVersion(new AsyncHttpResponseHandler() {
		//
		// @Override
		// public void onFailure(int arg0, Header[] arg1, byte[] arg2,
		// Throwable arg3) {
		//
		// }
		//
		// @Override
		// public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
		// String result = new String(arg2);
		// Log.i("MainAcivity", "版本信息:" + result);
		// map=Common.str3map(result);
		// // Gson gson = new Gson();
		// // Type type = new TypeToken<Map<String, String>>() {
		// // }.getType();
		// // map = gson.fromJson(result, type);
		// Log.i("MainAcivity-->MAP", map.get("version"));
		// if (!map.get("version").equals(getVesionName())) {
		// // 弹出升级对话框
		// showDialog();
		// }
		// }
		// });

		if (!IS_CHECK＿UPDATE) {
			IS_CHECK＿UPDATE = true;
			checkVersion(MainActivity.this);
		}

	}

	private String getVesionName() {
		PackageManager pm = getPackageManager();
		try {
			PackageInfo info = pm.getPackageInfo("com.example.dolphinradio",
					0);
			return info.versionName;
		} catch (NameNotFoundException e) {

			e.printStackTrace();
		}
		return null;
	}

	/** 初始化通知栏 */
	private void initNotify() {
		mBuilder = new NotificationCompat.Builder(this);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 1,
				new Intent(), 0);
		mBuilder.setWhen(System.currentTimeMillis())
				// 通知产生的时间，会在通知信息里显示
				.setContentIntent(pendingIntent)
				.setTicker("草莓电台" + map.get("version"))
				.setSmallIcon(R.drawable.app_logo)
				.setContentTitle("草莓电台" + map.get("version"))
				// .setNumber(number)//显示数量
				// .setPriority(Notification.PRIORITY_DEFAULT)// 设置该通知优先级
				.setAutoCancel(true)// 设置这个标志当用户单击面板就可以让通知将自动取消
				.setOngoing(true);// ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)

		// Notification.DEFAULT_ALL Notification.DEFAULT_SOUND 添加声音 //
		// requires VIBRATE permission

	}

	private void showDialog() {
		AlertDialog.Builder builder = new Builder(MainActivity.this);
		builder.setTitle("有新版本更新了！");
		builder.setMessage("本次更新内容：" + map.get("message"));
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 下载新版APP
				HttpUtils http = new HttpUtils();
				Log.i("MainActivity", "http的地址" + http);
				http.download(
						"http://vroad.bbrtv.com/cmradio/uploads/file/20151010/20151010113037_44560.mp3",
						Environment.getExternalStorageDirectory()
								.getAbsolutePath() + "20151010113037.mp3",
						true, true, new RequestCallBack<File>() {

							@Override
							public void onFailure(HttpException arg0,
									String arg1) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onSuccess(ResponseInfo<File> arg0) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onLoading(long total, long current,
									boolean isUploading) {
								// TODO Auto-generated method stub
								super.onLoading(total, current, isUploading);
								Log.i("MainActivity", "下载进度：" + current);
							}

						});
				// 关闭对话框
				dialog.dismiss();
			}
		});
		builder.show();
	}

	private void initViews() {
		mBuilder = new NotificationCompat.Builder(this);
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		// toolbar.setLogo(R.drawable.ic_launcher);
		mToolbar.setTitle("草莓电台");
		setSupportActionBar(mToolbar);
		// getSupportActionBar().setTitle("");
		// getSupportActionBar().setSubtitle("");
		// getSupportActionBar().setLogo(R.drawable.ic_launcher);
		//设置菜单点击事件
		mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {
				case R.id.action_settings:
					if(UserUtils.checkLogin(MainActivity.this)){
						return true;
					}
					Intent intent = new Intent(MainActivity.this,
							SettingActivity.class);
					startActivity(intent);
					break;

				// case R.id.action_share:
				// Toast.makeText(MainActivity.this, "action_share", 0).show();
				// break;

				case R.id.ab_search:
					Intent intent1 = new Intent(MainActivity.this,
							SearchActivity.class);
					startActivity(intent1);
					break;

				default:
					break;
				}
				return true;
			}
		});
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		/* findView */
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				mToolbar, R.string.drawer_open, R.string.drawer_close){
			
			@Override
			public void onDrawerOpened(View drawerView) {
				// TODO Auto-generated method stub
					initUserData();
				super.onDrawerOpened(drawerView);
			}
		};
		mDrawerToggle.syncState();
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		fragmentList = new ArrayList<Fragment>();
		firstFragment = new FirstFragment();
		mPagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
		mPagerSlidingTabStrip.setViewPager(mViewPager);
		mPagerSlidingTabStrip
				.setOnPageChangeListener(new OnPageChangeListener() {

					@Override
					public void onPageSelected(int arg0) {
						// colorChange(arg0);
					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {
					}

					@Override
					public void onPageScrollStateChanged(int arg0) {
					}
				});
		initTabsValue();
		FragmentManager manager = this.getSupportFragmentManager();
		initLeftView();
	}

	private void initLeftView() {
		usericon = (ImageView) findViewById(R.id.usericon);
		nikename = (TextView) findViewById(R.id.nikename);

		attentionTv = (TextView) findViewById(R.id.main_menu_tv_attention);
		fansTv = (TextView) findViewById(R.id.main_menu_tv_fans);
		messageTv = (TextView) findViewById(R.id.main_menu_tv_message);

		findViewById(R.id.add_progmer_button).setOnClickListener(this);
		findViewById(R.id.my_program).setOnClickListener(this);
		findViewById(R.id.my_favorite).setOnClickListener(this);
		findViewById(R.id.setting).setOnClickListener(this);
		findViewById(R.id.more_shre).setOnClickListener(this);
		findViewById(R.id.adduser).setOnClickListener(this);
		findViewById(R.id.addusers).setOnClickListener(this);
		LogHelper.e(MyApplication.getInstance().getSpUtil().getNick());
		nikename.setText(MyApplication.getInstance().getSpUtil().getNick());
		usericon.setOnClickListener(this);
		if(!MyApplication.getInstance().getSpUtil().getUid().isEmpty()){
			initData();
		}
	}

	private void initData() {
		// TODO Auto-generated method stub
		initUserData();
	}

	
	private void initUserData() {
		isToLogin=UserUtils.checkLogin(this);
		if(isToLogin){
			return;
		}
		String uid = MyApplication.getInstance().getSpUtil().getUid();
		String url = HttpManage.mainUserDataUrl;
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

	}

	/**
	 * mPagerSlidingTabStrip
	 * 
	 */
	private void initTabsValue() {
		// 底部游标颜色
		mPagerSlidingTabStrip.setIndicatorColor(Color.RED);
		// tab的分割线颜色
		mPagerSlidingTabStrip.setDividerColor(getResources().getColor(
				R.color.color_gray_light));
		// tab背景
		mPagerSlidingTabStrip.setBackgroundColor(Color.parseColor("#FFFFFF"));
		// mPagerSlidingTabStrip.setBackgroundResource(R.drawable.image_main_top_bg);
		// tab底线高度
		mPagerSlidingTabStrip.setUnderlineHeight((int) TypedValue
				.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources()
						.getDisplayMetrics()));
		// 游标高度
		mPagerSlidingTabStrip.setIndicatorHeight((int) TypedValue
				.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources()
						.getDisplayMetrics()));
		// 选中的文字颜色
		mPagerSlidingTabStrip.setSelectedTextColor(Color.RED);
		// 正常文字颜色
		mPagerSlidingTabStrip.setTextColor(getResources().getColor(
				R.color.color_text_black));
		mPagerSlidingTabStrip.setTextSize((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP, 18, getResources()
						.getDisplayMetrics()));
		mPagerSlidingTabStrip.setShouldExpand(true);
	}

	/**
	 * 
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private void colorChange(int position) {
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				SuperAwesomeCardFragment.getBackgroundBitmapPosition(position));
		Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
			/**
			 * 
			 */
			@Override
			public void onGenerated(Palette palette) {
				Palette.Swatch vibrant = palette.getVibrantSwatch();
				mPagerSlidingTabStrip.setBackgroundColor(vibrant.getRgb());
				mPagerSlidingTabStrip.setTextColor(vibrant.getTitleTextColor());
				mPagerSlidingTabStrip.setIndicatorColor(colorBurn(vibrant
						.getRgb()));

				mToolbar.setBackgroundColor(vibrant.getRgb());
				if (android.os.Build.VERSION.SDK_INT >= 21) {
					Window window = getWindow();
					window.setStatusBarColor(colorBurn(vibrant.getRgb()));
					window.setNavigationBarColor(colorBurn(vibrant.getRgb()));
				}
			}
		});
	}

	/**
	 * @return
	 */
	private int colorBurn(int RGBValues) {
		int alpha = RGBValues >> 24;
		int red = RGBValues >> 16 & 0xFF;
		int green = RGBValues >> 8 & 0xFF;
		int blue = RGBValues & 0xFF;
		red = (int) Math.floor(red * (1 - 0.1));
		green = (int) Math.floor(green * (1 - 0.1));
		blue = (int) Math.floor(blue * (1 - 0.1));
		return Color.rgb(red, green, blue);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		/* ShareActionProvider */
		// mShareActionProvider = (ShareActionProvider) MenuItemCompat
		// .getActionProvider(menu.findItem(R.id.action_share));
		// Intent intent = new Intent(Intent.ACTION_SEND);
		// intent.setType("text/*");
		// mShareActionProvider.setShareIntent(intent);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			Toast.makeText(MainActivity.this, "action_settings", 0).show();
			break;
		case R.id.ab_search:
			Toast.makeText(MainActivity.this, "action_search", 0).show();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	// 加载头像
	@SuppressLint("NewApi")
	private void getHeadIcon() {
		pathString = MyApplication.getInstance().getSpUtil().getHeadIcon();
		LogHelper.e("头像地址：" + pathString);
		if (!pathString.isEmpty()) {
			// LogHelper.e(pathString);
			new AsyncTask<Void, Void, Bitmap>() {

				@Override
				protected Bitmap doInBackground(Void... arg0) {
					// TODO Auto-generated method stub
					mImageLoader = ImageLoader.getInstance();
					Bitmap bitmap = mImageLoader.loadImageSync(pathString,
							ImageLoaderHelper.getDisplayImageOptions());
					return bitmap;
				}

				@Override
				protected void onPostExecute(Bitmap result) {
					// TODO Auto-generated method stub
					super.onPostExecute(result);
					if (result == null) {
						usericon.setImageResource(R.drawable.user_icon);
					} else {
						usericon.setImageBitmap(ImageUtils.getRoundBitmap(
								MainActivity.this, result));
						result = null;
					}
				}
			}.execute();
		} else {
			@SuppressWarnings("static-access")
			Bitmap bitmap = new BitmapFactory().decodeResource(getResources(),
					R.drawable.user_icon);
			usericon.setImageBitmap(ImageUtils.getRoundBitmap(
					MainActivity.this, bitmap));
			if (!bitmap.isRecycled()) {
				bitmap.recycle();
				bitmap = null;
			}
		}
	}

	/* ***************FragmentPagerAdapter***************** */
	public class MyPagerAdapter extends FragmentPagerAdapter {

		private final String[] TITLES = { "首页", "播单", "发现", "排行", "0.0", "0.0",
				"0.0", "0.0" };

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}

		@Override
		public Fragment getItem(int position) {
			return SuperAwesomeCardFragment.newInstance(position);
		}

	}

	@Override
	public void onClick(View arg0) {
		isToLogin=UserUtils.checkLogin(this);
		if(isToLogin){
			return;
		}
		Intent intent = new Intent();
		switch (arg0.getId()) {
		case R.id.usericon:
			UserUtils.checkLogin(MainActivity.this);
			intent.setClass(MainActivity.this, UpUserIconActivity.class);
			// startActivityForResult(intent, SET_STUDENT_ICON);
			startActivityForResult(intent, 1);
			break;
			
		case R.id.my_program:
			intent.setClass(MainActivity.this, MyProgramActivity.class);
			// startActivityForResult(intent, SET_STUDENT_ICON);
			startActivity(intent);
			break;
			
		case R.id.main_menu_tv_attention:
			intent.setClass(MainActivity.this, MyProgramActivity.class);
			startActivity(intent);
			break;
		case R.id.main_menu_tv_fans:
			intent.setClass(MainActivity.this, MyProgramActivity.class);
			startActivity(intent);
			break;
		case R.id.main_menu_tv_message:
			intent.setClass(MainActivity.this, MyProgramActivity.class);
			startActivity(intent);
			break;			

		case R.id.my_favorite:
			intent.setClass(MainActivity.this, MyFavoriteActivity.class);
			// startActivityForResult(intent, SET_STUDENT_ICON);
			startActivity(intent);
			break;
			
		case R.id.more_shre:
			intent.setClass(MainActivity.this, MyDownLoadActivity.class);
			// startActivityForResult(intent, SET_STUDENT_ICON);
			startActivity(intent);
			break;
			
		case R.id.adduser:
			intent.setClass(MainActivity.this, AlreadyPlayedListActivity.class);
			// startActivityForResult(intent, SET_STUDENT_ICON);
			startActivity(intent);
			break;
			
		case R.id.setting:
			intent.setClass(MainActivity.this, SettingActivity.class);
			// startActivityForResult(intent, SET_STUDENT_ICON);
			startActivity(intent);
			break;
		case R.id.addusers:
			intent.setClass(MainActivity.this, SuggestionActivity.class);
			startActivity(intent);
			break;
			
		case R.id.add_progmer_button:
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
						IntentUtils.startActivityForString(MainActivity.this,
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v7.app.AppCompatActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		if(!MyApplication.getInstance().getSpUtil().getIsAutoLogin()){
			MyApplication.getInstance().getSpUtil().clearData();
		}
		super.onDestroy();
		Log.i("MainActivity", "onDestroy");
	}

	/**
	 * 监听返回--是否退出程序
	 */
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

//	private void pressAgainExit() {
//
//		if (exit.isExit()) {
//			// UIHelper.Exit(this);
//			AppManager.getAppManager().AppExit(MainActivity.this);
//		} else {
//			Toast.makeText(getApplicationContext(), "再按一次返回键退出",
//					Toast.LENGTH_SHORT).show();
//			exit.doExitInOneSecond();
//		}
//	}

	/**
	 * 检查是否有新版本
	 * 
	 * @param context
	 *            上下文
	 * @param isCheckApk
	 *            防止频繁弹出toast
	 */
	public void checkVersion(final Context context) {
		if (!NetUtil.isNetConnected(context)) {
			return;
		}
		MyApplication app = MyApplication.getInstance();
		final PackageInfo pinfo = app.getPackageInfo();
		new AsyncTask<Object, Object, String>() {
			protected String doInBackground(Object... params) {

				String url = ConfigUtils.baseurl
						+ "index.php?d=android&c=api&m=android_version";
				return com.example.toolbar.http.HttpUtils.getString(url);
			}

			protected void onPostExecute(String version) {
				if (version == null) {
					ToastUtils.showShort(context, "网络错误,请稍候尝试!");
					return;
				}
				if (version.equals("")) {
					ToastUtils.showShort(context, "网络错误 ,请稍候尝试!");
					return;
				}
				Map<String, String> map = Common.str2mapstr(version);
				// 发现新版本
				if (map.isEmpty()) {
					ToastUtils.showShort(context, "网络错误 ,无法检查更新！");
					return;
				}
				if (map.containsKey("version")
						&& !map.get("version").equals(pinfo.versionName)) {

					AlertDialog.Builder builder = new AlertDialog.Builder(
							context);
					builder.setTitle("海豚电台客户端有新的版本啦,V" + map.get("version"));
					builder.setMessage(Html.fromHtml(map.get("message")));
					builder.setPositiveButton("立即更新",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									ToastUtils
											.show(context, "开始下载新版本安装包...", 1);
									boolean isEnough = FileUtils
											.isSKCardSpaceEnough(); // 计算存储卡的大小
									if (isEnough) {
										File file = new File(
												ConfigUtils.SDcardPath);

										if (!file.exists()) {
											file.mkdirs();
										}
										new UpdateApk(context)
												.doNewVersionUpdate();
									} else {
										Toast.makeText(context,
												"内存卡不可用或者空间不足...请检查后再进行下载！", 1)
												.show();
									}

								}
							});

					builder.setNegativeButton("暂不",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int arg1) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}

							});
					// builder.setCancelable(false);
					builder.show();

				} else {
					// ToastUtils.showShort(context, "您以是最新版本，暂无更新！");
				}
			}
		}.execute();
	}
	
}
