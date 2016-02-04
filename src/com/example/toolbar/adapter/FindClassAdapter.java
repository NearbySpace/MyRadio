package com.example.toolbar.adapter;

import java.util.ArrayList;

import com.example.strawberryradio.R;
import com.example.toolbar.activity.ProgramClassifyListActivity;
import com.example.toolbar.bean.FindBean.ProgramClassify;
import com.example.toolbar.utils.DensityUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 节目适配器
 * 
 * @author
 * 
 */
public class FindClassAdapter extends BaseAdapter {
	private ArrayList<ProgramClassify> type_list;
	private Context mContext;
	private int[] color={0xff5599ff,0xffff88c2,0xffaaaaaa,0xffff7744,0xffbbff66,0xffff8888};
	public FindClassAdapter(Context context,ArrayList<ProgramClassify> type_list) {
		super();
		this.mContext=context;
		this.type_list=type_list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return type_list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return type_list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView tv=new TextView(mContext);
		tv.setBackgroundResource(R.drawable.corners_tv_bg);
		tv.setGravity(Gravity.CENTER);
		tv.setHeight(DensityUtil.dip2px(mContext, 30));
		tv.setTextSize(16);
		tv.setText(type_list.get(position).title);
		switch (position%color.length) {
		case 0:
			tv.setTextColor(color[0]);
			break;
		case 1:
			tv.setTextColor(color[1]);
			break;
		case 2:
			tv.setTextColor(color[2]);
			break;
		case 3:
			tv.setTextColor(color[3]);
			break;
		case 4:
			tv.setTextColor(color[4]);
			break;
		case 5:
			tv.setTextColor(color[5]);
			break;
		
		}
		return tv;
	}

}
