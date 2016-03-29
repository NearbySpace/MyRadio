package com.example.toolbar.activity;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dolphinradio.R;
import com.example.toolbar.common.utils.Common;
import com.example.toolbar.http.HttpManage;
import com.example.toolbar.http.HttpManage.OnCallBack;
import com.example.toolbar.utils.UserUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class SuggestionActivity extends AppCompatActivity {
	private Toolbar mToolbar;
	private TextView tv_content;
	private Button bt_sure;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_suggestion);
		initView();
	}
	private void initView() {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mToolbar.setTitle("意见反馈");
		setSupportActionBar(mToolbar);
		mToolbar.setNavigationContentDescription(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mToolbar.setNavigationOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				finish();
			}
		});
		tv_content=(TextView) findViewById(R.id.suggestion_content);
		bt_sure=(Button) findViewById(R.id.suggestion_sure);
		bt_sure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 提交意见
				sendContent();
			}
		});
	}
	
	private void sendContent(){
		String content=tv_content.getText().toString();
		String mid=UserUtils.getUid();
		String url = HttpManage.userSuggstionUrl;
		Map<String,Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("mid", mid);
		paramsMap.put("content", content);
		HttpManage.getNetData(url, paramsMap, 0, new OnCallBack() {
			
			@Override
			public void onSuccess(byte[] arg2) {
				// TODO Auto-generated method stub
				String result=new String(arg2);
				Log.i("SuggestionActivity", result);
				Map<String,String> map=Common.str3map(result);
				if(map.get("status").equals("0")){
					Toast.makeText(SuggestionActivity.this, "感谢您对我们所提的意见", 0).show();
				}
			}
			
			@Override
			public void onFailure(byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(SuggestionActivity.this, "很抱歉，提交失败了,请稍后再试", 0).show();
			}
		});
//		HttpManage.userSuggestion(mid, content, new AsyncHttpResponseHandler() {
//			
//			@Override
//			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
//				String result=new String(arg2);
//				Log.i("SuggestionActivity", result);
//				Map<String,String> map=Common.str3map(result);
//				if(map.get("status").equals("0")){
//					Toast.makeText(SuggestionActivity.this, "感谢您对我们所提的意见", 0).show();
//				}
//				
//			}
//			
//			@Override
//			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
//				// TODO Auto-generated method stub
//				Toast.makeText(SuggestionActivity.this, "很抱歉，提交失败了,请稍后再试", 0).show();
//			}
//		});
	}
	
}
