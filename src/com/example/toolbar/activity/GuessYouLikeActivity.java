package com.example.toolbar.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.strawberryradio.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class GuessYouLikeActivity extends AppCompatActivity {
	private GridView gv;
	private TextView tv_current_pager_name;
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
		tv_current_pager_name.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		
	}
	
}
