package com.example.toolbar.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dolphinradio.R;
import com.example.toolbar.application.MyApplication;
import com.example.toolbar.bean.AlreadyPlayedList;
import com.example.toolbar.common.utils.ImageLoaderHelper;
import com.example.toolbar.common.utils.LogHelper;
import com.example.toolbar.http.HttpManage;
import com.example.toolbar.http.HttpManage.OnCallBack;
import com.example.toolbar.view.SlideCutListView;
import com.example.toolbar.view.SlideCutListView.RemoveDirection;
import com.example.toolbar.view.SlideCutListView.RemoveListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class AlreadyPlayedListActivity extends AppCompatActivity implements
		RemoveListener {
	private Toolbar mToolbar;
	private SlideCutListView slideCutListView;
	private List<AlreadyPlayedList> beans;
	private MyAdapter adapter;
	private int type = 3;
	private int page = 1;
	private ImageLoader mImageLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_already_played_list);
		mImageLoader = ImageLoader.getInstance();
		 initViews();
		 initData();
	}

	
	private void initData() {
		String url = HttpManage.alreadyPlayedListUrl;
		String uid = MyApplication.getInstance().getSpUtil().getUid();
		Map<String,Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("uid", uid);
		paramsMap.put("type", type);
		paramsMap.put("page", page);
		HttpManage.getNetData(url, paramsMap, 1, new OnCallBack() {
			
			@Override
			public void onSuccess(byte[] arg2) {

				String result = new String(arg2);
				Gson gson = new Gson();
				LogHelper.e("已播过的节目数据：" + result);
				beans = gson.fromJson(result,
						new TypeToken<List<AlreadyPlayedList>>() {
						}.getType());
				adapter = new MyAdapter();
				slideCutListView.setAdapter(new MyAdapter());
			}
			
			@Override
			public void onFailure(byte[] arg2, Throwable arg3) {
				LogHelper.e("获取已播过的节目数据失败：" + new String(arg2));
			}
		});
		
//		HttpManage.getMyFavorite(new AsyncHttpResponseHandler() {
//
//			@Override
//			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
//				String result = new String(arg2);
//				Gson gson = new Gson();
//				LogHelper.e("已播过的节目数据：" + result);
//				beans = gson.fromJson(result,
//						new TypeToken<List<AlreadyPlayedList>>() {
//						}.getType());
//				adapter = new MyAdapter();
//				slideCutListView.setAdapter(new MyAdapter());
//
//			}
//
//			@Override
//			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
//					Throwable arg3) {
//				LogHelper.e("获取已播过的节目数据失败：" + new String(arg2));
//
//			}
//		}, uid, type, page);

	}

	private void initViews() {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mToolbar.setTitle("已播放列表");
		setSupportActionBar(mToolbar);

		slideCutListView = (SlideCutListView) findViewById(R.id.lv);
		slideCutListView.setRemoveListener(this);
		slideCutListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

			}
		});

		mToolbar.setNavigationContentDescription(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mToolbar.setNavigationOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				finish();
			}
		});

	}

	@Override
	public void removeItem(RemoveDirection direction, int position) {
		AlreadyPlayedList data = (AlreadyPlayedList) adapter.getItem(position);
		String show_id = data.getId();
		// adapter.remove(adapter.getItem(position));
		// show.remove(adapter.getItem(position));
		switch (direction) {
		case RIGHT:
			Toast.makeText(this, "向右删除  " + position, Toast.LENGTH_SHORT)
					.show();
			DeleteItem(show_id);
			break;
		case LEFT:
			Toast.makeText(this, "向左删除  " + position, Toast.LENGTH_SHORT)
					.show();
			DeleteItem(show_id);
			break;

		default:
			break;
		}
	}

	private void DeleteItem(String show_id) {
		// TODO Auto-generated method stub

	}
	
	class MyAdapter extends BaseAdapter {
		Holder holder;

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return beans == null ? 0 : beans.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return beans.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			AlreadyPlayedList bean = beans.get(position);
			if (convertView == null) {
				holder = new Holder();
				convertView = LayoutInflater.from(
						AlreadyPlayedListActivity.this).inflate(
						R.layout.item_already_played_list, null);
				holder.iv = (ImageView) convertView
						.findViewById(R.id.show_played_icon);
				holder.tv_title = (TextView) convertView
						.findViewById(R.id.show_played_titel);
				holder.tv_host = (TextView) convertView
						.findViewById(R.id.shows_played_host);
				holder.tv_time = (TextView) convertView
						.findViewById(R.id.shows_played_time);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
			LogHelper.e("title" +bean+"------->"+bean.getTitle());
			holder.tv_title.setText(bean.getTitle());
			holder.tv_host.setText(bean.getOwner());
			holder.tv_time.setText(bean.getPlaytimes());
			mImageLoader.displayImage(bean.getThumb(), holder.iv,
					ImageLoaderHelper.getDisplayImageOptions());
			return convertView;
		}

	}

	class Holder {
		private ImageView iv;
		private TextView tv_title;
		private TextView tv_host;
		private TextView tv_time;
	}

}
