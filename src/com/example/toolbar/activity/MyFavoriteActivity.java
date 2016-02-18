package com.example.toolbar.activity;

import java.util.ArrayList;
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
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.strawberryradio.R;
import com.example.toolbar.adapter.FavoriteAdapter;
import com.example.toolbar.application.MyApplication;
import com.example.toolbar.bean.Favorite;
import com.example.toolbar.bean.PlayInfo;
import com.example.toolbar.bean.ProgramListBean.ProgramListInfo;
import com.example.toolbar.common.utils.Common;
import com.example.toolbar.common.utils.LogHelper;
import com.example.toolbar.http.HttpManage;
import com.example.toolbar.service.PlayerManage;
import com.example.toolbar.view.SlideCutListView;
import com.example.toolbar.view.SlideCutListView.RemoveDirection;
import com.example.toolbar.view.SlideCutListView.RemoveListener;
import com.example.toolbar.view.progress.CircularProgress;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class MyFavoriteActivity extends AppCompatActivity implements
		RemoveListener {
	private ShareActionProvider mShareActionProvider;
	private Toolbar mToolbar;
	private SlideCutListView slideCutListView;

	private CircularProgress progress;
	private TextView tv_show_number;
	private FavoriteAdapter adapter;
	private int page = 1;
	private List<Favorite> show;
	private List<Favorite> programLists;
	private List<Favorite> programs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myfavorite);
		// bundle = this.getIntent().getExtras(); /* 获取Bundle中的数据 */
		// show_titel = bundle.getString("id");
		initViews();
		initData();
	}

	private void initViews() {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mToolbar.setTitle("我的收藏");
		setSupportActionBar(mToolbar);
		slideCutListView = (SlideCutListView) findViewById(R.id.slideCutListView);
		progress=(CircularProgress) findViewById(R.id.favorite_progress);
		tv_show_number=(TextView) findViewById(R.id.favorite_tv_number);
		slideCutListView.setRemoveListener(this);
		mToolbar.setNavigationContentDescription(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mToolbar.setNavigationOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				finish();
			}
		});
		slideCutListView.setOnItemClickListener(new onItemClickListener());
		slideCutListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Favorite data = (Favorite) adapter.getItem(position);
				if(data==null){
					return true;
				}
				showDelDialog(data);
//				String show_id = data.getId();
//				FavoriteDel(show_id);
				return true;
			}
		});
		slideCutListView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if(programLists!=null&&programs!=null){
					if(firstVisibleItem>=programLists.size()+1){
						tv_show_number.setText("节目共："+programs.size()+"个");
					}else{
						tv_show_number.setText("节目单共："+programLists.size()+"个");
					}
				}
			}
		});
	}

	private void initData() {
		String uid = MyApplication.getInstance().getSpUtil().getUid();
		int type = 1;

		HttpManage.getMyFavorite(new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// progress.setVisibility(View.GONE);
				String result = new String(arg2);
				Gson gson = new Gson();
				LogHelper.e("我收藏的节目数据：" + result);
				show = gson.fromJson(result, new TypeToken<List<Favorite>>() {
				}.getType());
				if(show!=null&&show.size()!=0){
					classifyProgram();
					if(programLists.size()!=0){
						tv_show_number.setText("节目单共："+programLists.size()+"个");
					}else{
						tv_show_number.setText("节目共："+programs.size()+"个");
					}
					adapter = new FavoriteAdapter(MyFavoriteActivity.this, 
							programLists,programs);
					slideCutListView.setAdapter(adapter);
				}else{
					//提示用户没有收藏有节目
					tv_show_number.setText("您还没有收藏的节目");
				}
				tv_show_number.setVisibility(View.VISIBLE);
				progress.setVisibility(View.GONE);
				// LogHelper.e("获取节目数据：" + show.toString());
			}

			@Override
			public void onFailure(int arg0,
					@SuppressWarnings("deprecation") Header[] arg1,
					byte[] arg2, Throwable arg3) {
				// LogHelper.e("获取数据失败");
			}
		}, uid, type, page);
	}
	
	//对节目单和节目分类
	private void classifyProgram(){
		programLists=new ArrayList<Favorite>();
		programs=new ArrayList<Favorite>();
		for(int i=0;i<show.size();i++){
			if(show.get(i).getList_type().equals("1")){
				programs.add(show.get(i));
			}else{
				programLists.add(show.get(i));
			}
		}
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
	private boolean isPlayingThisList=false;
	/*
	 * 跳转播放页
	 */
	public class onItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long arg3) {
			Favorite favorite=(Favorite) adapter.getItem(position);
			if(favorite==null){
				return;
			}
			if(position==programLists.size()){
				return;
			}else if(position<programLists.size()){
				//跳转到节目列表
				Intent intent1=new Intent(MyFavoriteActivity.this,ProgramListActivity.class);
				intent1.putExtra("programme_id",favorite.getId());
				startActivity(intent1);
				return;
			}else{
				Intent intent=new Intent(MyFavoriteActivity.this,NewRadioPlayActivity.class);
//				Bundle bundle=new Bundle();
//				bundle.putString("favorite", "favorite");
//				bundle.putString("id", favorite.getId());
//				bundle.putString("path", favorite.getPath());
//				bundle.putString("thumb", favorite.getThumb());
//				bundle.putString("title", favorite.getTitle());
//				intent.putExtra("bundle", bundle);
				if(!isPlayingThisList){
					PlayerManage.getInstance().clearList();
					for(Favorite info : programs){
						PlayInfo playInfo = new PlayInfo(info.getId(), info.getTitle(),
								"", info.getPath(), "", "", "", "", info.getThumb(), "",
								"", "","","");
						PlayerManage.getInstance().addPlayInfo(playInfo);
					}
				}
				PlayerManage.position=position-programLists.size()-1;
				isPlayingThisList=true;
				startActivity(intent);
			}
		}

	}

	// 滑动删除之后的回调方法
	@Override
	public void removeItem(RemoveDirection direction, int position) {
		Favorite data = (Favorite) adapter.getItem(position);
		String show_id = data.getId();

		switch (direction) {
		case RIGHT:
			show.remove(adapter.getItem(position));
			FavoriteDel(data);
			break;
		case LEFT:
			show.remove(adapter.getItem(position));
			FavoriteDel(data);
			break;

		default:
			break;
		}

	}
	
	public class DeleteLisener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private AlertDialog dialog;
	private void showDelDialog(final Favorite data){
		AlertDialog.Builder builder=new AlertDialog.Builder(MyFavoriteActivity.this);
		View view=LayoutInflater.from(MyFavoriteActivity.this).inflate(R.layout.dialog_delete, null);
		builder.setView(view);
		dialog=builder.create();
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
				FavoriteDel(data);
				dialog.dismiss();
			}
		});
	}

	private void FavoriteDel(final Favorite data) {
		String uid = MyApplication.getInstance().getSpUtil().getUid();
		String program_id = data.getId();
		String type_id= "1";
		LogHelper.e("节目ID：" + data.getId());
		HttpManage.FavoriteDel(new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// progress.setVisibility(View.GONE);
				String result = new String(arg2);
				Map<String,String> map=Common.str3map(result);
				if(map.get("status").equals("0")){
					Toast.makeText(getApplicationContext(), "取消收藏成功",0).show();
					if(programLists.contains(data)){
						programLists.remove(data);
					}else if(programs.contains(data)){
						programs.remove(data);
					}
					adapter.uploadMsg(programLists,programs);
					isPlayingThisList=false;
				}else{
					Toast.makeText(getApplicationContext(), "取消收藏失败",0).show();
				}
			}

			@Override
			public void onFailure(int arg0,
					@SuppressWarnings("deprecation") Header[] arg1,
					byte[] arg2, Throwable arg3) {
				// LogHelper.e("获取数据失败");
			}
		}, uid, program_id, type_id,data.getList_type());
	}
}
