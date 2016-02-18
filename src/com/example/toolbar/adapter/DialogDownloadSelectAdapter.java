package com.example.toolbar.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.strawberryradio.R;
import com.example.toolbar.bean.ProgramListBean.ProgramListInfo;

public class DialogDownloadSelectAdapter extends BaseAdapter {
	private Context context;
	private List<ProgramListInfo> list;
	private Holder holder;
	private ProgramListInfo info;
	
	public DialogDownloadSelectAdapter(Context context,ArrayList<ProgramListInfo> list) {
		super();
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView == null){
			holder = new Holder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_dialog_download_select, null);
			holder.tv_name = (TextView) convertView.findViewById(R.id.item_dialog_download_select_name);
			holder.cb = (CheckBox) convertView.findViewById(R.id.item_dialog_download_select_checkbox);
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		info = list.get(position);
		holder.tv_name.setText(info.title);
		return convertView;
	}
	
	class Holder{
		public TextView tv_name;
		public CheckBox cb;
	}

}
