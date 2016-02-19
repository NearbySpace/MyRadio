package com.example.toolbar.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.strawberryradio.R;
import com.example.toolbar.fragment.DownloadedFragment;
import com.example.toolbar.fragment.DownloadingFragment;

public class MyDownLoadActivity extends AppCompatActivity implements
		OnClickListener {
	private Toolbar mToolbar;
	private TextView tv_downloading;
	private TextView tv_downloaded;
	private ImageView iv_downloading;
	private ImageView iv_downloaded;
	private ArrayList<Fragment> fragments;
	private int current=1;//当前位置
	private Intent intent;

//	/**
//	 * 根据情况设置默认页 
//	 * @param current 值只能是0或1
//	 */
//	public MyDownLoadActivity(int current) {
//		super();
//		this.current=current;
//	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mydownload);
		initViews();
	}

	private void initViews() {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mToolbar.setTitle("我的下载");
		setSupportActionBar(mToolbar);
		mToolbar.setNavigationContentDescription(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mToolbar.setNavigationOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				finish();
			}
		});
		fragments=new ArrayList<Fragment>();
		fragments.add(new DownloadingFragment());
		fragments.add(new DownloadedFragment());
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		intent=getIntent();
		if(intent.getBooleanExtra("downloading", false)){
			current=0;
		}
		ft.replace(R.id.framelayout_download, fragments.get(current)).commit();
		iv_downloaded=(ImageView) findViewById(R.id.downloaded_iv);
		iv_downloading=(ImageView) findViewById(R.id.downloading_iv);
		tv_downloaded = (TextView) findViewById(R.id.downloaded_tv);
		tv_downloading = (TextView) findViewById(R.id.downloading_tv);
		if(current==0){
			tv_downloading.setTextColor(Color.parseColor("#FF3333"));
			iv_downloading.setVisibility(View.VISIBLE);
		}else{
			tv_downloaded.setTextColor(Color.parseColor("#FF3333"));
			iv_downloaded.setVisibility(View.VISIBLE);
		}
		tv_downloaded.setOnClickListener(this);
		tv_downloading.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.downloading_tv:
			if(current!=0){
				tv_downloading.setTextColor(Color.parseColor("#FF3333"));
				iv_downloading.setVisibility(View.VISIBLE);
				tv_downloaded.setTextColor(Color.parseColor("#000000"));
				iv_downloaded.setVisibility(View.GONE);
				getSupportFragmentManager().beginTransaction()
				.replace(R.id.framelayout_download, fragments.get(0)).commit();
				current=0;
			}
			break;
		case R.id.downloaded_tv:
			if(current!=1){
				tv_downloaded.setTextColor(Color.parseColor("#FF3333"));
				iv_downloaded.setVisibility(View.VISIBLE);
				tv_downloading.setTextColor(Color.parseColor("#000000"));
				iv_downloading.setVisibility(View.GONE);
				getSupportFragmentManager().beginTransaction()
				.replace(R.id.framelayout_download, fragments.get(1)).commit();
				current=1;
			}
			break;

		default:
			break;
		}

	}
}