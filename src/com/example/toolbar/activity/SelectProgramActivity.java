package com.example.toolbar.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.strawberryradio.R;
import com.example.toolbar.adapter.AddProgramSelectAdapter;
import com.example.toolbar.application.MyApplication;
import com.example.toolbar.bean.SelectProgram;
import com.example.toolbar.bean.UploadProgramList;
import com.example.toolbar.bean.UploadProgramList.ProgramListInfo;
import com.example.toolbar.bean.UploadProgramList.ProgramListInfo.ProgramClassifyInfo;
import com.example.toolbar.bean.UploadProgramList.ProgramListInfo.ProgramInfo;
import com.example.toolbar.common.utils.LogHelper;
import com.example.toolbar.http.HttpManage;
import com.example.toolbar.pulltorefreshlistview.PullToRefreshBase.OnRefreshListener;
import com.example.toolbar.pulltorefreshlistview.PullToRefreshListView;
import com.example.toolbar.utils.IntentUtils;
import com.example.toolbar.utils.ToastUtils;
import com.example.toolbar.view.MyToast;
import com.example.toolbar.view.progress.CircularProgress;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class SelectProgramActivity extends AppCompatActivity implements OnClickListener{
	private final int OK = 32;//返回码
	private ShareActionProvider mShareActionProvider;
	private Toolbar mToolbar;
	private PullToRefreshListView mPullRefreshListView;
	private Bundle bundle;

	private TextView mTextViewMunber;
	private Button mButtonSubmit;
	private TextView tv_remind;
	private TextView remind_net;
	private ListView listView;
	private CircularProgress progress;
	private AddProgramSelectAdapter adapter;
	private Map<String, ArrayList<String>> map;
	private Map<String, Map<String,String>> map_classify;
	private List<SelectProgram> show;
	private List<SelectProgram> searchShow=new ArrayList<SelectProgram>();
	private ArrayList<String> idList;
	private SearchView mSearchView;
	private String show_titel;
	private String page = "0";
	private String TAG="SelectProgramActivity";
	private boolean isSendSuccess=false;
	
	private final Timer timer = new Timer();
	private TimerTask task;
	private UploadProgramList mUpList;
	private int mMaxSize;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_program);
		bundle = this.getIntent().getExtras(); /* 获取Bundle中的数据 */
		show_titel = bundle.getString("titel");
		idList=new ArrayList<String>();
		map=new HashMap<String, ArrayList<String>>();
		map_classify=new HashMap<String, Map<String,String>>();
		initViews();
		initData();
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		Log.i(TAG, "触发onActivityResult,------返回码："+arg1);
		if(OK != arg1){
			return;
		}
		int programSize=0;
		String key=String.valueOf(arg0);
		if(arg2.getStringArrayListExtra("program_ids")!=null){
			map.put(key, arg2.getStringArrayListExtra("program_ids"));
		}
		if(arg2.getStringExtra("classifyId") != null){
			Map<String,String> map=new HashMap<String, String>();
			map.put("id", arg2.getStringExtra("classifyId"));
			if(arg2.getStringExtra("time") != null){
				map.put("time", arg2.getStringExtra("time"));
			}else{
				map.put("time", 5+"");
			}
			map_classify.put(key, map);
		}else{
			if(map_classify.get(key) != null){
				map_classify.remove(key);
			}
		}
		
		for(ArrayList<String> l:map.values()){
			programSize+=l.size();
		}
		
		mMaxSize= programSize ;
//		idList.addAll(arg2.getStringArrayListExtra("program_ids"));
		mTextViewMunber.setText("已选择了（ "+(programSize+map_classify.size())+" ）个节目");
			
		super.onActivityResult(arg0, arg1, arg2);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.search, menu);
		 final MenuItem item = menu.findItem(R.id.abc_search);
		   mSearchView = (SearchView) MenuItemCompat.getActionView(item);
		   mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

			@Override
			public boolean onQueryTextChange(String arg0) {
				doSearch(arg0);
				return false;
			}

			@Override
			public boolean onQueryTextSubmit(String arg0) {
				// TODO Auto-generated method stub
				return false;
			}
			
			
		});
		return true;
	}
	
	private void doSearch(String text){
		searchShow.clear();
		for(int i=0;i<show.size();i++){
			if(show.get(i).getTitle().contains(text)){
				searchShow.add(show.get(i));
			}
		}
		if(searchShow.size()==0){
			tv_remind.setVisibility(View.VISIBLE);
		}else{
			tv_remind.setVisibility(View.GONE);
		}
		if(adapter==null){
			adapter = new AddProgramSelectAdapter(
					SelectProgramActivity.this, show);
			listView.setAdapter(adapter);
		}
		adapter.refreshData(searchShow);
	}
	
	private void initViews() {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mToolbar.setTitle("选择节目类型");
		progress = (CircularProgress) findViewById(R.id.progress);
		// toolbar.setLogo(R.drawable.ic_launcher);
		// getSupportActionBar().setTitle("");
		// getSupportActionBar().setSubtitle("");
		// getSupportActionBar().setLogo(R.drawable.ic_launcher);

		setSupportActionBar(mToolbar);
		mTextViewMunber=(TextView) findViewById(R.id.select_program_tv_munber);
		mButtonSubmit=(Button) findViewById(R.id.select_program_bt_submit);
		mButtonSubmit.setOnClickListener(this);
		tv_remind=(TextView) findViewById(R.id.selectProgram_remind_info);
		remind_net= (TextView) findViewById(R.id.selectProgram_remind_net);
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		// listView = (ListView) findViewById(R.id.select_program);
		listView = mPullRefreshListView.getRefreshableView();
		listView.setOnItemClickListener(new onItemClickListener());
		
		mToolbar.setNavigationContentDescription(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mToolbar.setNavigationOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				finish();
			}
		});
		 //toolbar菜单监听
		 mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
		
		@Override
		public boolean onMenuItemClick(MenuItem arg0) {
			
			return false;
		}
		 });
		// 刷新监听
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				if (mPullRefreshListView.getCurrentMode() == 1) { // 往下拉刷新
					page = "1";
					initData();
				} else {
					page = "0";
					initData();
				}

			}
		});
	}

	private void initData() {
		HttpManage.getSelectProgram(new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				progress.setVisibility(View.GONE);
				remind_net.setVisibility(View.GONE);
				String result = new String(arg2);
				Gson gson = new Gson();
				LogHelper.e("选择节目数据：" + result);

				show = gson.fromJson(result,
						new TypeToken<List<SelectProgram>>() {
						}.getType());

				adapter = new AddProgramSelectAdapter(
						SelectProgramActivity.this, show);
				listView.setAdapter(adapter);
				// LogHelper.e("获取节目数据：" + show.toString());
				mPullRefreshListView.onRefreshComplete();
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// LogHelper.e("获取数据失败");
				progress.setVisibility(View.GONE);
				remind_net.setVisibility(View.VISIBLE);
			}
		}, page);
	}

	// 控制toolbar菜单项
	// public boolean onOptionsItemSelected(MenuItem item) {
	// switch (item.getItemId()) {
	// case R.id.action_settings:
	// Toast.makeText(AddProgramActivity.this, "action_settings", 0)
	// .show();
	// break;
	// case R.id.action_share:
	// Toast.makeText(AddProgramActivity.this, "action_share", 1).show();
	// break;
	// default:
	// break;
	// }
	// return super.onOptionsItemSelected(item);
	// }

	public class onItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long arg3) {
			// TODO Auto-generated method stub
			@SuppressWarnings("unchecked")
			SelectProgram data = (SelectProgram) adapter.getItem(position);
			// 获得选中项的对象
			String id = data.getId();
			Intent intent=new Intent(SelectProgramActivity.this, AddProgramActivity.class);
			intent.putExtra("id", id);
			Log.i(TAG, "节目id："+id);
			intent.putExtra("classifyName", data.getTitle());
			intent.putExtra("titel", show_titel);
			intent.putExtra("position", position);
			if(map.get(String.valueOf(position))!=null ){
				//不是第一次进入，需携带数据进去，恢复上一次的状态
				intent.putStringArrayListExtra("idlist", map.get(String.valueOf(position)));
			}
			if(map_classify.get(String.valueOf(position)) !=null){
				intent.putExtra("time", map_classify.get(String.valueOf(position)).get("time"));
			}
			
			startActivityForResult(intent, position);
//			IntentUtils.startActivityForString_Dubble(
//					SelectProgramActivity.this, AddProgramActivity.class, "id",
//					id, "titel", show_titel);
		}

	}
	
	private void sendProgram() {
		String mid = MyApplication.getInstance().getSpUtil().getUid();
		//添加节目失败时，为避免重复，要把idList清空
		for(ArrayList<String> l :map.values()){
			idList.addAll(l);
		}
		Log.i("SelectProgramActivity", "idList--------->"+idList);
		if(idList != null && idList.size() != 0 || map_classify.size() != 0){
			String result;
			result=initUploadProgramList(idList);
			if(getIntent().getStringExtra("from") !=null){
				if(getIntent().getStringExtra("from").equals("Programme")){
					String programme_id=getIntent().getStringExtra("programme_id");
					HttpManage.addProgramInProgramme(mid, programme_id, result, 
							new AsyncHttpResponseHandler() {
								
								@Override
								public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
									// TODO Auto-generated method stub
									Toast.makeText(SelectProgramActivity.this, "节目添加成功", Toast.LENGTH_SHORT).show();
									setResult(RESULT_OK);
									finish();
								}
								
								@Override
								public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
									
									idList.clear();
								}
							});
				}
			}else{
				HttpManage.sendProgram(new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
//						progress.setVisibility(View.GONE);
						String result = new String(arg2);
						LogHelper.e("返回的结果："+result);
						Toast.makeText(SelectProgramActivity.this, "节目添加成功", Toast.LENGTH_SHORT).show();
						isSendSuccess=true;
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						String result = new String(arg2);
						idList.clear();
						Toast.makeText(SelectProgramActivity.this, "添加节目失败，请重试一次", 0).show();
					}
				}, mid, result, show_titel);
			}
		}else{
			Toast.makeText(SelectProgramActivity.this, "没有选择要添加的节目，请选择", 0).show();
		}
		
	}
	
//	private String result;
	/**
	 * 初始化要上传内容的格式
	 */
	private String initUploadProgramList(List<String> idList){
		List<ProgramInfo> programInfos=new ArrayList<SelectProgramActivity.ProgramInfo>();
		for(Map<String,String> map : map_classify.values()){
			ProgramInfo p=new SelectProgramActivity.ProgramInfo();
//			ProgramClassifyInfo p=new SelectProgramActivity.ProgramClassifyInfo();
			p.setId(map.get("id"));
			p.setTime(map.get("time"));
			p.setType("2");
			programInfos.add(p);
//			programClassifyInfos.add(p);
		}
		
		for(int i=0; i<idList.size();i++){
			ProgramInfo p=new SelectProgramActivity.ProgramInfo();
			p.setId(idList.get(i));
			p.setTime("3");
			p.setType("1");
			programInfos.add(p);
		}
		
		Gson gson=new Gson();
		String result="";
		result=gson.toJson(programInfos);
		result=result.replace("\"", "-");
		return result;
	}
	
	public class ProgramInfo{
		public String time;
		public String id;
		public String type;
		public String getTime() {
			return time;
		}
		public void setTime(String time) {
			this.time = time;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		
		
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.select_program_bt_submit:
				
			if(!isSendSuccess){
				sendProgram();
			}else{
				ToastUtils.show(SelectProgramActivity.this, "已经提交过了，请勿重复提交", Toast.LENGTH_SHORT);
				return;
			}
			
			break;

		default:
			break;
		}
		
	}
}
