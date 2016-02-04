package com.example.toolbar.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.strawberryradio.R;
import com.example.toolbar.common.utils.AppManager;
import com.example.toolbar.utils.ConfigUtils;
import com.example.toolbar.utils.SharePreferenceUtil;
import com.example.toolbar.utils.ToastUtils;


/**
 * 
 * @author hxc activity基类
 */
public class BaseActivity extends Activity implements OnClickListener {
	public  LayoutInflater inflater;
	public  TextView head_title, head_action_title, head_action_both_title,head_action_both_righttitle;
	public  ImageView head_action_backimage, head_action_both_back,head_action_both_rightiv;
	public  ProgressBar head_progressbar, head_action_progressBar,head_action_both_progressBar;
	public  SharePreferenceUtil mSharePreferenceUtil;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		inflater = getLayoutInflater();
		mSharePreferenceUtil = new SharePreferenceUtil(this, ConfigUtils.appSharePreferenceName);
		// 添加Activity到堆栈
		AppManager.getAppManager().addActivity(this);
	}

	public void initView() {
		// TODO Auto-generated method stub

		head_title = (TextView) findViewById(R.id.head_title);
		head_progressbar = (ProgressBar) findViewById(R.id.head_progressBar);

	}

	public void initHeadActionBar() {

		head_action_title = (TextView) findViewById(R.id.head_action_title);
		head_action_backimage = (ImageView) findViewById(R.id.head_action_backimage);
		head_action_backimage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		head_action_progressBar = (ProgressBar) findViewById(R.id.head_action_left_progressBar);
	}

//	public void initHeadActionBothTitle() {
//		head_action_both_title = (TextView) findViewById(R.id.head_action_both_title);
//		head_action_both_righttitle = (TextView) findViewById(R.id.head_action_both_righttitle);
//		head_action_both_back = (ImageView) findViewById(R.id.head_action_both_backimage);
//		head_action_both_back.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				finish();
//				overridePendingTransition(R.anim.activity_push_down_in,
//						R.anim.activity_push_down_out);
//			}
//		});
//		head_action_both_progressBar = (ProgressBar) findViewById(R.id.head_action_both_progressBar);
//	}
	
//	public void initHeadActionBothImage() {
//		head_action_both_title = (TextView) findViewById(R.id.head_action_both_title);
//		head_action_both_back = (ImageView) findViewById(R.id.head_action_both_backimage);
//		head_action_both_rightiv = (ImageView) findViewById(R.id.head_action_both_rightimage);
//		head_action_both_back.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				finish();
//				overridePendingTransition(R.anim.activity_push_down_in,
//						R.anim.activity_push_down_out);
//			}
//		});
//		head_action_both_progressBar = (ProgressBar) findViewById(R.id.head_action_both_progressBar);
//	}
	
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		default:
			break;
		}
	}
	
	public void toast(String content){
		
		ToastUtils.showShort(this, content);
	}

}
