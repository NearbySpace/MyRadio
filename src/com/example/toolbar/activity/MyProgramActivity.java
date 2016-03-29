package com.example.toolbar.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dolphinradio.R;
import com.example.toolbar.adapter.AddProgramSelectAdapter;
import com.example.toolbar.application.MyApplication;
import com.example.toolbar.bean.SelectProgram;
import com.example.toolbar.common.utils.Common;
import com.example.toolbar.common.utils.LogHelper;
import com.example.toolbar.http.HttpManage;
import com.example.toolbar.http.HttpManage.OnCallBack;
import com.example.toolbar.pulltorefreshlistview.PullToRefreshBase.OnRefreshListener;
import com.example.toolbar.pulltorefreshlistview.PullToRefreshListView;
import com.example.toolbar.utils.ToastUtils;
import com.example.toolbar.view.progress.CircularProgress;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class MyProgramActivity extends AppCompatActivity {
	private static final int SHOW_SUBACTIVITY = 1;
	private ShareActionProvider mShareActionProvider;
	private Toolbar mToolbar;
	private PullToRefreshListView mPullRefreshListView;
	private Bundle bundle;

	private ListView listView;
	private CircularProgress progress;
	private AddProgramSelectAdapter adapter;
	private List<SelectProgram> show;
	private String uid;
	private String show_titel;
	private int page = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myprogram);
		uid=MyApplication.getInstance().getSpUtil().getUid();
		initViews();
		initData();
	}

	private void initViews() {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mToolbar.setTitle("我的节目单");
		progress = (CircularProgress) findViewById(R.id.progress);
		// toolbar.setLogo(R.drawable.ic_launcher);
		// getSupportActionBar().setTitle("");
		// getSupportActionBar().setSubtitle("");
		// getSupportActionBar().setLogo(R.drawable.ic_launcher);

		setSupportActionBar(mToolbar);
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.my_program_list);
		// listView = (ListView) findViewById(R.id.select_program);
		listView = mPullRefreshListView.getRefreshableView();
		listView.setOnItemClickListener(new onItemClickListener());
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				showDelDialog(show.get(position));
				return true;
			}
		});
		mToolbar.setNavigationContentDescription(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mToolbar.setNavigationOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				finish();
			}
		});
		
		// 刷新监听
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				if (mPullRefreshListView.getCurrentMode() == 1) { // 往下拉刷新
					page += 1;
					initData();
				} else {
					page = 0;
					initData();
				}

			}
		});
	}

	private void initData() {
		String url = HttpManage.myProgrammeUrl;
		Map<String,Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("page", page);
		paramsMap.put("mid", MyApplication.getInstance().getSpUtil().getUid());
		HttpManage.getNetData(url, paramsMap, 1, new OnCallBack() {
			
			@Override
			public void onSuccess(byte[] arg2) {
				progress.setVisibility(View.GONE);
				String result = new String(arg2);
				Gson gson = new Gson();
				LogHelper.e("选择节目数据：" + result);

				show = gson.fromJson(result,
						new TypeToken<List<SelectProgram>>() {
						}.getType());

				adapter = new AddProgramSelectAdapter(MyProgramActivity.this,
						show);
				listView.setAdapter(adapter);
				// LogHelper.e("获取节目数据：" + show.toString());
				mPullRefreshListView.onRefreshComplete();
				//LogHelper.e("size:" +adapter.getCount() +"|"+ show.get(0).getTitle());
				//LogHelper.e("uid" + MyApplication.getInstance().getSpUtil().getUid());
			}
			
			@Override
			public void onFailure(byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				ToastUtils.show(MyProgramActivity.this, "连接网络失败", Toast.LENGTH_SHORT);
			}
		});
//		HttpManage.getMyProgram(new AsyncHttpResponseHandler() {
//			@Override
//			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
//				progress.setVisibility(View.GONE);
//				String result = new String(arg2);
//				Gson gson = new Gson();
//				LogHelper.e("选择节目数据：" + result);
//
//				show = gson.fromJson(result,
//						new TypeToken<List<SelectProgram>>() {
//						}.getType());
//
//				adapter = new AddProgramSelectAdapter(MyProgramActivity.this,
//						show);
//				listView.setAdapter(adapter);
//				// LogHelper.e("获取节目数据：" + show.toString());
//				mPullRefreshListView.onRefreshComplete();
//				//LogHelper.e("size:" +adapter.getCount() +"|"+ show.get(0).getTitle());
//				//LogHelper.e("uid" + MyApplication.getInstance().getSpUtil().getUid());
//			}
//
//			@Override
//			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
//					Throwable arg3) {
//				// LogHelper.e("获取数据失败");
//			}
//		}, page, MyApplication.getInstance().getSpUtil().getUid());
	}
	
	private AlertDialog dialog;
	private void showDelDialog(final SelectProgram info){
		AlertDialog.Builder builder=new AlertDialog.Builder(MyProgramActivity.this);
		View view=LayoutInflater.from(MyProgramActivity.this).inflate(R.layout.dialog_delete, null);
		dialog=builder.create();
		dialog.setView(view,0,0,0,0);
		dialog.show();
		Button cancel=(Button) view.findViewById(R.id.delete_dialog_cancel);
		Button sure=(Button) view.findViewById(R.id.delete_dialog_sure);
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		sure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				deleteProgram(info);
				dialog.dismiss();
			}
		});
	}
	
	
	private void deleteProgram(final SelectProgram info) {
		String url = HttpManage.delProgrammeUrl;
		Map<String,Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("programme_id", info.getId());
		paramsMap.put("mid", uid);
		HttpManage.getNetData(url, paramsMap, 1, new OnCallBack() {
			
			@Override
			public void onSuccess(byte[] arg2) {
				String result=new String(arg2);
				Map<String,String> map=Common.str3map(result);
				if(map.get("status").equals("0")){
					show.remove(info);
					adapter.notifyDataSetChanged();
				}
			}
			
			@Override
			public void onFailure(byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				ToastUtils.show(MyProgramActivity.this, "连网络失败", Toast.LENGTH_SHORT);
			}
		});
		
		
//		HttpManage.deleteMyProgram(info.getId(), uid, new AsyncHttpResponseHandler() {
//			
//			@Override
//			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
//				String result=new String(arg2);
//				Map<String,String> map=Common.str3map(result);
//				if(map.get("status").equals("0")){
//					show.remove(info);
//					adapter.notifyDataSetChanged();
//				}
//				
//			}
//			
//			@Override
//			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
		
	}

	public class onItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long arg3) {
			// TODO Auto-generated method stub
			@SuppressWarnings("unchecked")
			SelectProgram data = (SelectProgram) adapter.getItem(position);
			// 获得选中项的对象
			String id = data.getId();
			Intent intent=new Intent(MyProgramActivity.this,ProgramListActivity.class);
			intent.putExtra("programme_id", id);
			startActivity(intent);
		}

	}
}
