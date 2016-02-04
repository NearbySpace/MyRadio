package com.example.toolbar.adapter;

/*
 * 轮播广告适配器
 * 
 */

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.example.strawberryradio.R;
import com.example.toolbar.activity.ProgramListActivity;
import com.example.toolbar.activity.RadioPlayActivity;
import com.example.toolbar.bean.Top;

public class TopFlowViewAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	AQuery aQuery;
	// private static final int[] ids = { R.drawable.test1, R.drawable.test2,
	// R.drawable.test3 };
	private List<Top> top;

	public TopFlowViewAdapter(Context context) {
		mContext = context;
		aQuery = new AQuery(context);
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public TopFlowViewAdapter(Context context, List<Top> top) {
		mContext = context;
		aQuery = new AQuery(context);
		this.top = top;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return Integer.MAX_VALUE; // 返回最大值来实现循环
	}
	
	

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_flowview, null);
		}

		aQuery.id(((ImageView) convertView.findViewById(R.id.imgView))).image(
				top.get(position % top.size()).getThumb());

		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String id = top.get(position % top.size()).getId();
				//Intent intent = new Intent(mContext, RadioPlayActivity.class);
				// Bundle bundle = new Bundle();
				// bundle.putInt("image_id", ids[position%ids.length]);

				// intent.putExtras(bundle);
				// mContext.startActivity(intent);
//				Toast.makeText(mContext,
//						"点击了第" + (position % top.size() + 1) + "张图片",
//						Toast.LENGTH_SHORT).show();
//
//				intent.putExtra("programme_id", id);
//				mContext.startActivity(intent);

				Intent intent = new Intent(mContext,
						RadioPlayActivity.class);
				Bundle bundle = new Bundle();
				bundle.putParcelableArrayList("music_list",
						(ArrayList) top);
				bundle.putInt("position", (position % top.size()));
				bundle.putString("type", "top");
				intent.putExtra("bundle", bundle);
				mContext.startActivity(intent);

			}
		});
		return convertView;
	}

}
