package com.example.toolbar.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
	private int current=1;//当前位置
	private Intent intent;
	public ArrayList<Fragment> fragments;
	private Menu mMenu;
	private DownloadedFragment mDownloadedFragment;
	private DownloadingFragment mDownloadingFragment;
	
	public Handler handle = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			mDownloadedFragment.changeData();
		}
		
	};
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
		mDownloadingFragment = new DownloadingFragment();
		mDownloadedFragment = new DownloadedFragment();
		fragments.add(mDownloadingFragment);
		fragments.add(mDownloadedFragment);
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		intent=getIntent();
		if(intent.getBooleanExtra("downloading", false)){
			current=0;
		}
		ft.add(R.id.framelayout_download, fragments.get(0));
		ft.add(R.id.framelayout_download, fragments.get(1));
		if(current == 0){
			ft.show(fragments.get(0));
			ft.hide(fragments.get(1));
		}else{
			ft.show(fragments.get(1));
			ft.hide(fragments.get(0));
		}
		ft.commit();
//		ft.replace(R.id.framelayout_download, fragments.get(current)).commit();
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
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.editor, menu);
		mMenu = menu;
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_editor:
			mDownloadedFragment.setLinearLayoutVisibility(View.VISIBLE);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void showMenu(){
		mMenu.findItem(R.id.menu_editor).setVisible(true);
	}
	
	private void hideMenu(){
		mMenu.findItem(R.id.menu_editor).setVisible(false);
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
				hideMenu();
//				getSupportFragmentManager().beginTransaction()
//				.replace(R.id.framelayout_download, fragments.get(0)).commit();
				FragmentTransaction f = getSupportFragmentManager().beginTransaction();
				f.show(fragments.get(0));
				f.hide(fragments.get(1));
				f.commit();
				current=0;
			}
			break;
		case R.id.downloaded_tv:
			if(current!=1){
				tv_downloaded.setTextColor(Color.parseColor("#FF3333"));
				iv_downloaded.setVisibility(View.VISIBLE);
				tv_downloading.setTextColor(Color.parseColor("#000000"));
				iv_downloading.setVisibility(View.GONE);
				showMenu();
//				mDownloadedFragment.changeData();
//				getSupportFragmentManager().beginTransaction()
//				.replace(R.id.framelayout_download, fragments.get(1)).commit();
				FragmentTransaction f = getSupportFragmentManager().beginTransaction();
				f.show(fragments.get(1));
				f.hide(fragments.get(0));
				f.commit();
				current=1;
			}
			break;

		default:
			break;
		}

	}
}
