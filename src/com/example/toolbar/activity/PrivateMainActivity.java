package com.example.toolbar.activity;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.dolphinradio.R;
import com.example.toolbar.adapter.AddProgramAdapter;
import com.example.toolbar.adapter.PMprogerAdapter;
import com.example.toolbar.application.MyApplication;
import com.example.toolbar.bean.PrivateMain;
import com.example.toolbar.bean.PrivateMain_Progtam;
import com.example.toolbar.common.utils.ImageLoaderHelper;
import com.example.toolbar.common.utils.LogHelper;
import com.example.toolbar.http.HttpManage;
import com.example.toolbar.http.HttpManage.OnCallBack;
import com.example.toolbar.utils.ImageUtils;
import com.example.toolbar.view.MyListView;
import com.example.toolbar.view.OpAnimationView;
import com.example.toolbar.view.progress.CircularProgress;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PrivateMainActivity extends AppCompatActivity implements
		OnClickListener {
	private ShareActionProvider mShareActionProvider;
	private Toolbar mToolbar;
	private ImageLoader mImageLoader;
	private String pathString;
	private ScrollView sv;
	private CircularProgress progress;

	private MyListView listView;
	// private ImageView show_img;// 节目封面
	private ImageView user_icon;// 用户头像

	private TextView nickname;// 昵称
	private TextView letter;// 私信

	private TextView collect_number;// 收藏数
	private TextView showlist_name;// 节目单
	private TextView focus_number;// 关注数
	private TextView shows_number;// 节目数
	private TextView fans_number;// 粉丝数
	private OpAnimationView opAnimationView;
	private Boolean isRgihtShape = false;
	private AddProgramAdapter adapter;
	
	private String hostId = "";//主播id

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_private_main);
		initViews();
		initData();
	}

	private void initViews() {

		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		// toolbar.setLogo(R.drawable.ic_launcher);
		mToolbar.setTitle("个人主页");
		// getSupportActionBar().setTitle("");
		// getSupportActionBar().setSubtitle("");
		// getSupportActionBar().setLogo(R.drawable.ic_launcher);
		setSupportActionBar(mToolbar);
		mToolbar.setNavigationContentDescription(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mToolbar.setNavigationOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				finish();
			}
		});
		sv = (ScrollView) findViewById(R.id.private_scrollView1);
		progress = (CircularProgress) findViewById(R.id.private_progress);
		user_icon = (ImageView) findViewById(R.id.user_icon);
		nickname = (TextView) findViewById(R.id.nickname);

		collect_number = (TextView) findViewById(R.id.collect_number);
		showlist_name = (TextView) findViewById(R.id.showlist_name);
		fans_number = (TextView) findViewById(R.id.fans_number);
		shows_number = (TextView) findViewById(R.id.shows_number);
		focus_number = (TextView) findViewById(R.id.focus_number);
		opAnimationView = (OpAnimationView) findViewById(R.id.focus);
		opAnimationView.setOnClickListener(this);

		listView = (MyListView) findViewById(R.id.showListView1);
		listView.setOnItemClickListener(clickListener);

		// mToolbar.setOnMenuItemClickListener(new
		// Toolbar.OnMenuItemClickListener() {
		// @Override
		// public boolean onMenuItemClick(MenuItem item) {
		// switch (item.getItemId()) {
		// case R.id.action_settings:
		// Toast.makeText(AddProgramActivity.this, "action_settings",
		// 0).show();
		// break;
		// case R.id.action_share:
		// Toast.makeText(AddProgramActivity.this, "action_share", 0)
		// .show();
		// break;
		// default:
		// break;
		// }
		// return true;
		// }
		// });
	}

	private void initData() {
		String url = HttpManage.personDataUrl;
		if(getIntent().getStringExtra("host_id") != null){
			hostId = getIntent().getStringExtra("host_id");
			LogHelper.e(hostId);
		}
		mImageLoader = ImageLoader.getInstance();
		String mid = MyApplication.getInstance().getSpUtil().getUid();
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("zid", hostId);
		paramsMap.put("mid", mid);
		HttpManage.getNetData(url, paramsMap, 1, new OnCallBack() {
			
			@Override
			public void onSuccess(byte[] arg2) {

				// TODO Auto-generated method stub
				String result = new String(arg2);

				Gson gson = new Gson();
				PrivateMain pm = gson.fromJson(result, PrivateMain.class);
				nickname.setText(pm.getNickname());
				pathString = pm.getAvatar();

				collect_number.setText("被收藏" + pm.getFoo_playbill_num()+ "次");// 收藏数
				
				focus_number.setText(pm.getFavorite_num());// 关注数
				shows_number.setText(pm.getPlaybill_num());// 节目数
				fans_number.setText(pm.getFans_num());// 粉丝数

				PMprogerAdapter pmAdapter = new PMprogerAdapter(
						PrivateMainActivity.this, pm.getProgramme());
				//LogHelper.e("获取节目列表：" + pm.getProgramme());
				listView.setAdapter(pmAdapter);
				progress.setVisibility(View.GONE);
				sv.smoothScrollTo(0, 20);
				getHeadIcon();
			
			}
			
			@Override
			public void onFailure(byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				LogHelper.e("获取数据失败");
			}
		});
		
//		HttpManage.getPrivateData(new AsyncHttpResponseHandler() {
//			@Override
//			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
//				// TODO Auto-generated method stub
//				String result = new String(arg2);
//
//				Gson gson = new Gson();
//				PrivateMain pm = gson.fromJson(result, PrivateMain.class);
//				nickname.setText(pm.getNickname());
//				pathString = pm.getAvatar();
//
//				collect_number.setText("被收藏" + pm.getFoo_playbill_num()+ "次");// 收藏数
//				
//				focus_number.setText(pm.getFavorite_num());// 关注数
//				shows_number.setText(pm.getPlaybill_num());// 节目数
//				fans_number.setText(pm.getFans_num());// 粉丝数
//
//				PMprogerAdapter pmAdapter = new PMprogerAdapter(
//						PrivateMainActivity.this, pm.getProgramme());
//				//LogHelper.e("获取节目列表：" + pm.getProgramme());
//				listView.setAdapter(pmAdapter);
//				progress.setVisibility(View.GONE);
//				sv.smoothScrollTo(0, 20);
//				getHeadIcon();
//			}
//
//			@Override
//			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
//					Throwable arg3) {
//				// TODO Auto-generated method stub
//				LogHelper.e("获取数据失败");
//
//			}
//		}, hostId, mid);
		// getListdata();
	}

	// 加载头像
	@SuppressLint("NewApi")
	private void getHeadIcon() {
		// pathString = MyApplication.getInstance().getSpUtil().getHeadIcon();
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
						user_icon.setImageResource(R.drawable.user_icon);
					} else {
						user_icon.setImageBitmap(ImageUtils.getRoundBitmap(
								PrivateMainActivity.this, result));
						result = null;
					}
				}
			}.execute();
		} else {
			@SuppressWarnings("static-access")
			Bitmap bitmap = new BitmapFactory().decodeResource(getResources(),
					R.drawable.user_icon);
			user_icon.setImageBitmap(ImageUtils.getRoundBitmap(
					PrivateMainActivity.this, bitmap));
			if (!bitmap.isRecycled()) {
				bitmap.recycle();
				bitmap = null;
			}
		}
	}

	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.focus:
			if (isRgihtShape) {
				LogHelper.e("点击关注：" + isRgihtShape);
				isRgihtShape = false;
				opAnimationView.right2add();
			} else {
				LogHelper.e("取消关注：" + isRgihtShape);
				isRgihtShape = true;
				opAnimationView.add2right();

			}
			break;
		}
	}
	
	private OnItemClickListener clickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long itemid) {
			PrivateMain_Progtam main_Progtam  = (PrivateMain_Progtam) parent.getAdapter().getItem(position);
			Intent intent = new Intent(PrivateMainActivity.this,
					ProgramListActivity.class);
			intent.putExtra("is_refresh", true);
			intent.putExtra("programme_id", main_Progtam.getId());
			startActivity(intent);
		}
	};

}
