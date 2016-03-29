package com.example.toolbar.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.dolphinradio.R;
import com.example.toolbar.application.MyApplication;
import com.example.toolbar.http.HttpManage;
import com.example.toolbar.http.HttpManage.OnCallBack;
import com.example.toolbar.utils.SharePreferenceUtil;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class GuessYouLikeActivity extends AppCompatActivity {
	private GridView gv;
//	private TextView tv_current_pager_name;
	private List<Map<String,String>> list;
	private Toolbar mToolbar;
	private String[] title={"小S的故事","郑秀文的锐变","刘德华","速度与激情",
			"黄家驹","陈洁仪","周星驰","美女主播"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guess_you_like);
		intiData();
		intiView();
	}
	private void intiData() {
		list=new ArrayList<Map<String,String>>();
		for(int i=0;i<8;i++){
			Map map=new HashMap<String,String>();
			map.put("title", title[i]);
			list.add(map);
		}
		String url = "http://vroad.bbrtv.com/cmradio/index.php?d=android&c=api&m=android_version";
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("uid", MyApplication.getInstance().getSpUtil().getUid());
//		map.put("type", 1);
//		map.put("page", 1);
		HttpManage.getNetData(url, null, 1, new OnCallBack(){

			@Override
			public void onSuccess(byte[] arg2) {
				String result = new String(arg2);
				Log.i("GuessYouLikeActivity", "收藏的数据："+result);
			}

			@Override
			public void onFailure(byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				Log.i("GuessYouLikeActivity", "参数为空");
			}
			
		});
	}
	private void intiView() {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mToolbar.setTitle("猜你喜欢");
		setSupportActionBar(mToolbar);
		mToolbar.setNavigationContentDescription(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mToolbar.setNavigationOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				finish();
			}
		});
		gv=(GridView) findViewById(R.id.gv_guess_you_like);
		SimpleAdapter sa=new SimpleAdapter(getApplicationContext(), list, R.layout.item_gv_guess_you_like,
				new String[]{"title"}, new int[]{R.id.tv_title_item_gv_guess});
		gv.setAdapter(sa);
//		tv_current_pager_name.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				finish();
//				
//			}
//		});
		
	}
	
}
