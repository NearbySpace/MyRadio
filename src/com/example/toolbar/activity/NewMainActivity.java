package com.example.toolbar.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;

import com.example.strawberryradio.R;
import com.example.toolbar.adapter.AddProgramSelectAdapter;
import com.example.toolbar.application.MyApplication;
import com.example.toolbar.bean.SelectProgram;
import com.example.toolbar.bean.UserInfo;
import com.example.toolbar.common.utils.Common;
import com.example.toolbar.common.utils.FileUtils;
import com.example.toolbar.common.utils.LogHelper;
import com.example.toolbar.common.utils.NetUtil;
import com.example.toolbar.fragment.NewFirstFragment;
import com.example.toolbar.fragment.NewTwoFragment;
import com.example.toolbar.framework.UpdateApk;
import com.example.toolbar.http.HttpManage;
import com.example.toolbar.utils.ConfigUtils;
import com.example.toolbar.utils.ControlActivity;
import com.example.toolbar.utils.ImageUtils;
import com.example.toolbar.utils.IntentUtils;
import com.example.toolbar.utils.ToastUtils;
import com.example.toolbar.utils.UserUtils;
import com.example.toolbar.widget.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class NewMainActivity extends AppCompatActivity implements OnClickListener{
	private DrawerLayout mDrawerLayout;
	private ViewPager mViewPager;
	private List<Fragment> viewList;
	private ImageView usericon;
	private TextView nikename;
	private LinearLayout linearlayout;
	private boolean isToLogin=false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_main);
		ControlActivity.getInstance().addActivity(NewMainActivity.this);
		initView();
		UpdateApk.checkVersion(NewMainActivity.this,1);
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if(isToLogin){
			if(!MyApplication.getInstance().getSpUtil().getUid().isEmpty()){
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
	    Fragment fargmentFirst=new NewFirstFragment();
	    Fragment fragmentTwo=new NewTwoFragment();
	    viewList = new ArrayList<Fragment>();// 将要分页显示的View装入数组中       
	    viewList.add(fargmentFirst);  
	    viewList.add(fragmentTwo); 
	    mViewPager.setAdapter(new MyViewPagerAdapter(this.getSupportFragmentManager()));
	    findViewById(R.id.new_add_progmer_button).setOnClickListener(this);
		findViewById(R.id.new_my_program).setOnClickListener(this);
		findViewById(R.id.new_my_favorite).setOnClickListener(this);
		findViewById(R.id.new_setting).setOnClickListener(this);
		findViewById(R.id.new_more_shre).setOnClickListener(this);
		findViewById(R.id.new_addusers).setOnClickListener(this);
		LogHelper.e(MyApplication.getInstance().getSpUtil().getNick());
		nikename.setText(MyApplication.getInstance().getSpUtil().getNick());
		usericon.setOnClickListener(this);
		if(!MyApplication.getInstance().getSpUtil().getUid().isEmpty()){
			initUserData();
		}
	}
	
	@SuppressLint("NewApi") 
	private void setLayoutBackground(int resId){
		Bitmap bm=ImageUtils.getMatchBitmap(NewMainActivity.this, resId);
		Drawable drawable=new BitmapDrawable(getResources(), bm);
		linearlayout.setBackground(drawable);
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
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
	
	private void initUserData() {
		isToLogin=UserUtils.checkLogin(this);
		if(isToLogin){
			return;
		}
		String uid = MyApplication.getInstance().getSpUtil().getUid();
		HttpManage.getMainUserData(uid, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] bt,
					Throwable arg3) {
				// TODO Auto-generated method stub
				LogHelper.e("error");
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] bt) {
				// TODO Auto-generated method stub
				String data = new String(bt);
				LogHelper.e(data);
				Gson gson = new Gson();
				UserInfo info = gson.fromJson(data, UserInfo.class);
				nikename.setText(info.nickname);
				ImageLoader.getInstance().displayImage(info.avatar,
						usericon);
			}
		});
	}
	
	public void switchFragment(int position){
		mViewPager.setCurrentItem(position);
	}
	
	public void openDrawerLayout(){
		mDrawerLayout.openDrawer(Gravity.LEFT);
		initUserData();
	}
	
	public class MyViewPagerAdapter extends FragmentPagerAdapter{

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
		isToLogin=UserUtils.checkLogin(this);
		if(isToLogin){
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
						if(titel.isEmpty()){
							editText.setHint("频道名不能为空");
							return;
						}
						IntentUtils.startActivityForString(NewMainActivity.this,
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
