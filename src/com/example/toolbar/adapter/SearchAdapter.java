package com.example.toolbar.adapter;

import java.util.List;
import java.util.Map;

import com.example.strawberryradio.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SearchAdapter extends BaseAdapter {
	private Holder holder;
	private Context mContext;
	private List<Map<String,String>> list;
	public SearchAdapter(Context mContext,List<Map<String,String>> list) {
		super();
		this.list=list;
		this.mContext=mContext;
	}

	public void dataChange(List<Map<String,String>> list){
		this.list=list;
		notifyDataSetChanged();
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
		if(convertView==null){
			convertView=LayoutInflater.from(mContext).inflate(R.layout.item_lv_search, null);
			holder=new Holder();
			holder.tv_title=(TextView) convertView.findViewById(R.id.search_lv_title_item);
			holder.tv_subtitle=(TextView) convertView.findViewById(R.id.search_lv_subtitle_item);
			holder.tv_type=(TextView) convertView.findViewById(R.id.search_lv_type_item);
			convertView.setTag(holder);
		}else{
			holder=(Holder) convertView.getTag();
		}
		holder.tv_title.setText(list.get(position).get("title"));
		holder.tv_subtitle.setText(list.get(position).get("nickname"));
		switch (Integer.parseInt(list.get(position).get("type"))) {
		case 1:
			holder.tv_type.setText("节目");
			break;
		case 2:
			holder.tv_type.setText("节目单列表");
			break;
		case 3:
			holder.tv_type.setText("会员列表");
			break;

		default:
			break;
		}
		return convertView;
	}
	
	class Holder{
		public TextView tv_title;
		public TextView tv_subtitle;
		public TextView tv_type;
	}

}
