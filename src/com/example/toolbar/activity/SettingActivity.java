package com.example.toolbar.activity;

import java.util.List;

import org.apache.http.Header;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.dolphinradio.R;
import com.example.toolbar.adapter.FindClassAdapter;
import com.example.toolbar.adapter.FindImgAdapter;
import com.example.toolbar.application.MyApplication;
import com.example.toolbar.bean.FindBean;
import com.example.toolbar.framework.UpdateApk;
import com.example.toolbar.http.HttpManage;
import com.example.toolbar.http.HttpManage.OnCallBack;
import com.example.toolbar.utils.ControlActivity;
import com.example.toolbar.utils.UserUtils;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class SettingActivity extends AppCompatActivity implements
		OnClickListener {
	private Toolbar mToolbar;
	private LayoutInflater layoutInflater;
	private TextView tv_verson;
	private TextView tv_program_name;
	private AlertDialog checkUpdateDialog ;

	private FindBean bean;
	private List<FindBean.ProgramClassify> mProgramClassify;
	private String mProgramData[] ;
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		layoutInflater = LayoutInflater.from(SettingActivity.this);
		setContentView(R.layout.activity_setting);
		ControlActivity.getInstance().addActivity(SettingActivity.this);
		sp=MyApplication.getInstance().getSharedPreferences("SettingInfo", MODE_PRIVATE);
		initView();
		
	}

	private void initView() {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mToolbar.setTitle("设置");
		setSupportActionBar(mToolbar);
		mToolbar.setNavigationContentDescription(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mToolbar.setNavigationOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				finish();
			}
		});
		tv_verson = (TextView) findViewById(R.id.setting_tv_vesion);
		tv_program_name = (TextView) findViewById(R.id.setting_program_name);
		tv_program_name.setText(MyApplication.getInstance().getSpUtil()
				.getDefaultProgram().get("classifyName"));
		
		findViewById(R.id.setting_safe_rl).setOnClickListener(this);
		findViewById(R.id.setting_exit).setOnClickListener(this);
		findViewById(R.id.setting_tv_update).setOnClickListener(this);
		findViewById(R.id.setting_program).setOnClickListener(this);
		tv_verson.setText("当前版本：" + getVesionName());
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v7.app.AppCompatActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		ControlActivity.getInstance().removeActivity(SettingActivity.this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.setting_safe_rl:
			UserUtils.checkLogin(SettingActivity.this);
			// 跳转到密码更换页面
			intent.setClass(SettingActivity.this, ChangePasswordActivity.class);
			startActivity(intent);
			break;
		case R.id.setting_program:
			initProgramData();
//			showChoiceDialog();
			break;
		case R.id.setting_exit:
			// 跳转对话框
			showExitDialog();
			break;
		case R.id.setting_tv_update:
			// 检查更新
			checkUpdateDialog = showCheckDialog();
			UpdateApk.checkVersion(SettingActivity.this,2);
			break;

		default:
			break;
		}

	}

	private AlertDialog showCheckDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
		View view = View.inflate(SettingActivity.this, R.layout.dialog_check_update, null);
		builder.setView(view);
		AlertDialog dialog = builder.show();
		dialog.setCancelable(false);
		return dialog;
		
	}
	
	public void closeCheckDialog() {
		checkUpdateDialog.dismiss();
	}
	
	

	private void initProgramData() {
		String url = HttpManage.findDataUrl;
		HttpManage.getNetData(url, null, 1, new OnCallBack() {
			
			@Override
			public void onSuccess(byte[] arg2) {

				String result = new String(arg2);
				Log.i("setting", result);
				Gson gson = new Gson();
				bean = gson.fromJson(result, FindBean.class);
				mProgramClassify = bean.type_list;
				if(mProgramClassify!=null && mProgramClassify.size()!=0){
					
					mProgramData=new String[mProgramClassify.size()];
					for (int i = 0; i < mProgramClassify.size(); i++) {
						mProgramData[i] = mProgramClassify.get(i).title;
					}
				}
				showChoiceDialog();
			
				
			}
			
			@Override
			public void onFailure(byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				Log.i("Setting", "获取数据失败");
			}
		});
//		HttpManage.getFindData(new AsyncHttpResponseHandler() {
//
//			@Override
//			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
//				String result = new String(arg2);
//				Log.i("setting", result);
//				Gson gson = new Gson();
//				bean = gson.fromJson(result, FindBean.class);
//				mProgramClassify = bean.type_list;
//				if(mProgramClassify!=null && mProgramClassify.size()!=0){
//					
//					mProgramData=new String[mProgramClassify.size()];
//					for (int i = 0; i < mProgramClassify.size(); i++) {
//						mProgramData[i] = mProgramClassify.get(i).title;
//					}
//				}
//				showChoiceDialog();
//			}
//
//			@Override
//			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
//					Throwable arg3) {
//				Log.i("Setting", "获取数据失败");
//
//			}
//		});
	}

	// private void showLoginDialog(){
	// final Dialog dialog;
	// AlertDialog.Builder builder=new
	// AlertDialog.Builder(SettingActivity.this);
	// builder.setMessage("您尚未登录，请先登录再操作");
	// dialog=builder.create();
	// dialog.show();
	// builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// dialog.dismiss();
	// }
	// });
	// builder.setPositiveButton("登录", new DialogInterface.OnClickListener(){
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// Intent intent=new Intent(SettingActivity.this,LoginActivity.class);
	// startActivity(intent);
	// dialog.dismiss();
	// finish();
	// }
	// });
	// }

	private void showChoiceDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				SettingActivity.this);
//		Log.i("setting", "mProgramData长度："+mProgramData.length);
		int programPosition=sp.getInt("programPosition", 0);
		builder.setSingleChoiceItems(mProgramData, programPosition,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						tv_program_name.setText(mProgramData[which]);
						// 把选择的节目类型信息保存下来
						MyApplication
								.getInstance()
								.getSpUtil()
								.setDefaultProgram(
										mProgramClassify.get(which).id,
										mProgramData[which]);
						Editor editor=sp.edit();
						editor.putInt("programPosition", which);
						editor.commit();
						dialog.dismiss();
					}
				});
		builder.show();
		

	}

	private void showExitDialog() {
		final Dialog dialog;
		AlertDialog.Builder builder = new AlertDialog.Builder(
				SettingActivity.this);
		View v = layoutInflater.inflate(R.layout.dialog_setting_exit, null);
		builder.setView(v);
		dialog = builder.create();
		dialog.show();
		TextView tv1 = (TextView) v
				.findViewById(R.id.cuetom_dialog_tv_exit_radio);
		TextView tv2 = (TextView) v
				.findViewById(R.id.cuetom_dialog_tv_exit_account);
		tv1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 退出应用的操作
				dialog.dismiss();
				ControlActivity.getInstance().removeAll();

			}
		});
		tv2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 退出当前账号的操作
				MyApplication.getInstance().getSpUtil().setIsAutoLogin(false);
				Intent intent = new Intent(SettingActivity.this,
						LoginActivity.class);
				intent.putExtra("again", true);
				startActivity(intent);
				MyApplication.getInstance().getSpUtil().clearData();
				dialog.dismiss();
				ControlActivity.getInstance().removeAll();
			}
		});
	}

}
