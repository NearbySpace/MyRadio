package com.example.toolbar.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;

import com.example.strawberryradio.R;
import com.example.toolbar.adapter.SearchAdapter;
import com.example.toolbar.common.utils.Common;
import com.example.toolbar.http.HttpManage;
import com.example.toolbar.view.progress.CircularProgress;
import com.example.toolbar.widget.SystemBarTintManager;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SearchActivity extends Activity implements OnClickListener{
	private final String TAG="SearchActivity";
	private EditText edit;
	private ImageView iv_back;
	private CircularProgress progress;
	private TextView tv_remind;
	private TextView tv_content_remind;
	private TextView tv_search_sure;
	private TextView tv_search_all;
	private TextView tv_search_program_list;
	private TextView tv_search_program;
	private TextView tv_search_user;
	private TextView tv_search_number;
	private ListView mListView;
	private List<Map<String,String>> list;
	private List<Map<String,String>> typeList;
	private List<TextView> tvList;
	private int current=0;
	private int showType=0;
	private SearchAdapter mAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		initSystemBar();
		initView();
		
	}
	private void initView() {
		typeList=new ArrayList<Map<String,String>>();
		tvList=new ArrayList<TextView>();
		edit=(EditText) findViewById(R.id.search_edit);
		iv_back=(ImageView) findViewById(R.id.search_back);
		tv_search_sure=(TextView) findViewById(R.id.search_sure);
		tv_search_all=(TextView) findViewById(R.id.search_all);
		tv_search_program_list=(TextView) findViewById(R.id.search_program_list);
		tv_search_program=(TextView) findViewById(R.id.search_program);
		tv_search_user=(TextView) findViewById(R.id.search_user);
		tv_search_number=(TextView) findViewById(R.id.search_number_tv);
		mListView=(ListView) findViewById(R.id.search_lv);
		progress=(CircularProgress) findViewById(R.id.search_progress);
		tv_remind=(TextView) findViewById(R.id.search_tv_net_remind);
		tv_content_remind=(TextView) findViewById(R.id.search_tv_content_remind);
		
		tvList.add(tv_search_all);
		tvList.add(tv_search_program);
		tvList.add(tv_search_program_list);
		tvList.add(tv_search_user);
		
		iv_back.setOnClickListener(this);
		tv_search_sure.setOnClickListener(this);
		tv_search_all.setOnClickListener(this);
		tv_search_program_list.setOnClickListener(this);
		tv_search_program.setOnClickListener(this);
		tv_search_user.setOnClickListener(this);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent=new Intent();
				Log.i(TAG,"点击的位置---->"+typeList.get(position).get("type"));
				switch (Integer.parseInt(typeList.get(position).get("type"))) {
				case 1:
					intent.setClass(SearchActivity.this, RadioPlayActivity.class);
					intent.putExtra("path", typeList.get(position).get("path"));
					intent.putExtra("title",typeList.get(position).get("title"));
					intent.putExtra("thumb",typeList.get(position).get("thumb"));
					break;
				case 2:
					intent.setClass(SearchActivity.this, ProgramListActivity.class);
					intent.putExtra("programme_id", typeList.get(position).get("id"));
					break;
				case 3:
					intent.setClass(SearchActivity.this,PrivateMainActivity.class);
					intent.putExtra("host_id", typeList.get(position).get("id"));
					break;
					
				default:
					break;
				}
				
				
				startActivity(intent);
				
				
			}
		});
		
	}
	
	@SuppressLint("ResourceAsColor")
	private void initSystemBar() {
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setNavigationBarTintEnabled(true);
		tintManager.setTintColor(Color.RED);
		int pading = getStatusBarHeight();
	}

	// 获取状态栏高度
	public int getStatusBarHeight() {
		int result = 0;
		int resourceId = getResources().getIdentifier("status_bar_height",
				"dimen", "android");
		if (resourceId > 0) {
			result = getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}
	
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.search_back:
			finish();
			break;
		case R.id.search_sure:
			progress.setVisibility(View.VISIBLE);
			typeList.clear();
			getSearchResult();
			break;
		case R.id.search_all:
			searchStateChange(0, tv_search_all, "搜全部");
//			if(current!=0){
//				tvList.get(current).setBackgroundColor(0xfff5f5f5);
//				tvList.get(current).setTextColor(0xff444444);
//				tv_search_all.setBackgroundColor(0xffff3333);
//				tv_search_all.setTextColor(0xffffffff);
//				edit.setHint("搜全部");
//				typeList.clear();
//				tv_search_number.setVisibility(View.GONE);
//				current=0;
//			}
//			showType=0;
			break;
		case R.id.search_program:
			searchStateChange(1, tv_search_program, "搜节目");
//			if(current!=1){
//				tvList.get(current).setBackgroundColor(0xfff5f5f5);
//				tvList.get(current).setTextColor(0xff444444);
//				tv_search_program.setBackgroundColor(0xffff3333);
//				tv_search_program.setTextColor(0xffffffff);
//				edit.setHint("搜节目");
//				typeList.clear();
//				if(mAdapter!=null){
//					mAdapter.dataChange(typeList);
//				}
//				tv_search_number.setVisibility(View.GONE);
//				current=1;
//			}
//			showType=1;
			break;
		case R.id.search_program_list:
			searchStateChange(2, tv_search_program_list, "搜播单");
//			if(current!=2){
//				tvList.get(current).setBackgroundColor(0xfff5f5f5);
//				tvList.get(current).setTextColor(0xff444444);
//				tv_search_program_list.setBackgroundColor(0xffff3333);
//				tv_search_program_list.setTextColor(0xffffffff);
//				edit.setHint("搜播单");
//				typeList.clear();
//				if(mAdapter!=null){
//					mAdapter.dataChange(typeList);
//				}
//				tv_search_number.setVisibility(View.GONE);
//				current=2;
//			}
//			showType=2;
			break;
		case R.id.search_user:
			searchStateChange(3, tv_search_user, "搜用户");
//			if(current!=3){
//				tvList.get(current).setBackgroundColor(0xfff5f5f5);
//				tvList.get(current).setTextColor(0xff444444);
//				tv_search_user.setBackgroundColor(0xffff3333);
//				tv_search_user.setTextColor(0xffffffff);
//				edit.setHint("搜用户");
//				typeList.clear();
//				if(mAdapter!=null){
//					mAdapter.dataChange(typeList);
//				}
//				tv_search_number.setVisibility(View.GONE);
//				current=3;
//			}
//			showType=3;
			break;

		default:
			break;
		}

		
	}
	
	private void searchStateChange(int position,TextView tv,String remindWord){
		if(current!=position){
			tvList.get(current).setBackgroundColor(0xfff5f5f5);
			tvList.get(current).setTextColor(0xff444444);
			tv.setBackgroundColor(0xffff3333);
			tv.setTextColor(0xffffffff);
			edit.setHint(remindWord);
			typeList.clear();
			if(mAdapter!=null){
				mAdapter.dataChange(typeList);
			}
			tv_search_number.setVisibility(View.GONE);
			current=position;
			showType=position;
		}
	}
	
	
	private void getSearchResult(){
		String keyword=edit.getText().toString().trim();
		HttpManage.getSearchData(keyword, new AsyncHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				tv_remind.setText("数据加载失败，请检查网络连接");
				tv_remind.setVisibility(View.VISIBLE);
				progress.setVisibility(View.GONE);
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				String result=new String(arg2);	
				Log.i(TAG, result);
				list=Common.strtolist(result);
				switch (showType) {
				case 0:
					getTypeData(0+"");
					break;
				case 1:
					getTypeData(1+"");
					break;
				case 2:
					getTypeData(2+"");
					break;
				case 3:
					getTypeData(3+"");
					break;

				default:
					break;
				}
				progress.setVisibility(View.GONE);
				if(typeList.isEmpty()){
					tv_content_remind.setVisibility(View.VISIBLE);
				}else{
					tv_content_remind.setVisibility(View.GONE);
				}
				if(mAdapter==null){
					mAdapter=new SearchAdapter(SearchActivity.this,typeList);
					mListView.setAdapter(mAdapter);
				}
				tv_search_number.setText("搜索结果("+typeList.size()+")");
				tv_search_number.setVisibility(View.VISIBLE);
				mAdapter.dataChange(typeList);
			}
			
		} );
	}
	
	private void getTypeData(String type){
		if(type.equals("0")){
			typeList=list;
			return;
		}
		for(int i=0;i<list.size();i++){
			if(list.get(i).get("type").equals(type)){
				typeList.add(list.get(i));
			}
		}
	}
	
//	class SearchAdapter extends BaseAdapter{
//		private Holder holder;
//		@Override
//		public int getCount() {
//			// TODO Auto-generated method stub
//			return list.size();
//		}
//
//		@Override
//		public Object getItem(int position) {
//			// TODO Auto-generated method stub
//			return list.get(position);
//		}
//
//		@Override
//		public long getItemId(int position) {
//			// TODO Auto-generated method stub
//			return position;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			if(convertView==null){
//				convertView=LayoutInflater.from(SearchActivity.this).inflate(R.layout.item_lv_search, null);
//				holder=new Holder();
//				holder.tv_title=(TextView) convertView.findViewById(R.id.search_lv_title_item);
//				holder.tv_subtitle=(TextView) convertView.findViewById(R.id.search_lv_subtitle_item);
//				holder.tv_type=(TextView) convertView.findViewById(R.id.search_lv_type_item);
//				convertView.setTag(holder);
//			}else{
//				holder=(Holder) convertView.getTag();
//			}
//			holder.tv_title.setText(list.get(position).get("title"));
//			holder.tv_subtitle.setText(list.get(position).get("nickname"));
//			switch (Integer.parseInt(list.get(position).get("type"))) {
//			case 1:
//				holder.tv_type.setText("节目");
//				break;
//			case 2:
//				holder.tv_type.setText("节目单列表");
//				break;
//			case 3:
//				holder.tv_type.setText("会员列表");
//				break;
//
//			default:
//				break;
//			}
//			return convertView;
//		}
//		
//	}
//	
//	class Holder{
//		public TextView tv_title;
//		public TextView tv_subtitle;
//		public TextView tv_type;
//	}
}
