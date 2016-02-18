package com.example.toolbar.activity;

import java.util.ArrayList;

import org.apache.http.Header;

import com.example.strawberryradio.R;
import com.example.toolbar.bean.PlayInfo;
import com.example.toolbar.bean.ProgramClassifyListBean;
import com.example.toolbar.bean.ProgramClassifyListBean.Info;
import com.example.toolbar.bean.ProgramListBean.ProgramListInfo;
import com.example.toolbar.common.utils.ImageLoaderHelper;
import com.example.toolbar.http.HttpManage;
import com.example.toolbar.service.PlayerManage;
import com.example.toolbar.view.progress.CircularProgress;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProgramClassifyListActivity extends AppCompatActivity{
	private final String TAG="ProgramClassifyListActivity";
	private ListView mListView;
	private CircularProgress mCircularProgress;
	private TextView tv_remind;
	private Toolbar mToolbar;
	private PullToRefreshListView mPullToRefreshListView;
	private ProgramClassifyAdapter mAdapter;
	private ProgramClassifyListBean bean;
	private ImageLoader mImageLoader;
	private int page = 0;
	private String type_id;
	private String top_name;
	//是否正在播放这个节目单
	private boolean isPlayingThisList = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_program_classify_list);
		initView();
		initData();
	}
	private void initView() {
		type_id=getIntent().getStringExtra("type_id");
		top_name=getIntent().getStringExtra("top_name");
		mCircularProgress=(CircularProgress) findViewById(R.id.program_classify_progress);
		tv_remind=(TextView) findViewById(R.id.program_classify_remind);
		mImageLoader=ImageLoader.getInstance();
		mToolbar=(Toolbar) findViewById(R.id.toolbar);
		mToolbar.setTitle(top_name);
		setSupportActionBar(mToolbar);
		mToolbar.setNavigationContentDescription(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mToolbar.setNavigationOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				finish();
			}
		});
		mPullToRefreshListView=(PullToRefreshListView) findViewById(R.id.program_classify_list_ptrlv);
		mListView=mPullToRefreshListView.getRefreshableView();
		mPullToRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				page = +1;
				initData();
				
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				page = 0;
				initData();
			}
		});
//		mListView=(ListView) findViewById(R.id.program_classify_list_lv);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent=new Intent();
				intent.setClass(ProgramClassifyListActivity.this, NewRadioPlayActivity.class);
//				Bundle bundle=new Bundle();
//				bundle.putParcelable("ProgramClassifyBean", bean);
//				bundle.putInt("position", position);
//				intent.putExtra("bundle", bundle);
//				intent.putExtra("path", bean.list.get(position).path);
//				intent.putExtra("title", bean.list.get(position).title);
//				intent.putExtra("thumb", bean.list.get(position).thumb);
				intent.putExtra("channelName",top_name);
				if(!isPlayingThisList){
					PlayerManage.getInstance().clearList();
					ArrayList<Info> list = bean.list;
					for(Info info : list){
						PlayInfo playInfo = new PlayInfo(info.id, info.title,
								"", info.path, "", "", "", "", info.thumb, "",
								"", "","","");
						PlayerManage.getInstance().addPlayInfo(playInfo);
					}
				}
				PlayerManage.position=position;
				isPlayingThisList=true;
				startActivity(intent);
			}
		});
		
		mListView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void initData(){
		
		HttpManage.getProgramClassifyListData(page, type_id, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				String result=new String(arg2);
				Gson gson=new Gson();
				bean=gson.fromJson(result, ProgramClassifyListBean.class);
				if(mAdapter==null){
					mAdapter=new ProgramClassifyAdapter();
					mListView.setAdapter(mAdapter);
				}else{
					mAdapter.notifyDataSetChanged();
				}
				mPullToRefreshListView.onRefreshComplete();
				mCircularProgress.setVisibility(View.GONE);
				tv_remind.setVisibility(View.GONE);
				
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				mCircularProgress.setVisibility(View.GONE);
				tv_remind.setVisibility(View.VISIBLE);
			}
		});
	}
	
	class ProgramClassifyAdapter extends BaseAdapter{
		Holder holder;
		ProgramClassifyListBean.Info info;
		@Override
		public int getCount() {
			
			return bean.list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return bean.list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			info=bean.list.get(position);
			if(convertView==null){
				holder=new Holder();
				convertView=LayoutInflater.from(ProgramClassifyListActivity.this).inflate(R.layout.item_program_classify_list, null);
				holder.iv=(ImageView) convertView.findViewById(R.id.program_classify_list_item_iv);
				holder.tv_title=(TextView) convertView.findViewById(R.id.program_classify_list_item_title);
				holder.tv_name=(TextView) convertView.findViewById(R.id.program_classify_list_item_name);
				holder.tv_time=(TextView) convertView.findViewById(R.id.program_classify_list_item_time);
				convertView.setTag(holder);
			}else{
				holder=(Holder) convertView.getTag();
			}
			mImageLoader.displayImage(info.thumb, holder.iv, ImageLoaderHelper.getDisplayImageOptions());
			holder.tv_title.setText(info.title);
			holder.tv_name.setText("作者："+info.owner);
			//holder.tv_time.setText("时长："+info.program_time);
			return convertView;
		}
		
	}
	
	class Holder{
		public ImageView iv;
		public TextView tv_title;
		public TextView tv_name;
		public TextView tv_time;
		
	}
}
