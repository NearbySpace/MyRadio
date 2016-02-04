package com.example.toolbar.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.strawberryradio.R;
import com.example.toolbar.bean.PlayInfo;

public class PlayRadioAdapter extends BaseAdapter {
	private List<PlayInfo> mlist;
	private Holder holder;
	private Context context;
	private PlayInfo info;
	public PlayRadioAdapter(Context context,List<PlayInfo> mlist) {
		super();
		this.context=context;
		this.mlist=mlist;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mlist.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			holder=new Holder();
			convertView=LayoutInflater.from(context).inflate(R.layout.item_play_radio, null);
			holder.tv_ranking=(TextView) convertView.findViewById(R.id.play_radio_item_ranking);
			holder.tv_title=(TextView) convertView.findViewById(R.id.play_radio_item_title);
			holder.tv_subtitle=(TextView) convertView.findViewById(R.id.play_radio_item_subtitle);
			holder.iv_download=(ImageView) convertView.findViewById(R.id.play_radio_item_iv_download);
			convertView.setTag(holder);
		}else{
			holder=(Holder) convertView.getTag();
		}
		info=mlist.get(position);
		holder.tv_ranking.setText((position+1)+".");
		holder.tv_title.setText(info.getTitle());
		holder.tv_subtitle.setText(info.getOwner());
		holder.iv_download.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 下载操作
				
			}
		});
		return convertView;
	}
	
	class Holder{
		 TextView tv_ranking;
		 TextView tv_title;
		 TextView tv_subtitle;
		 ImageView iv_download;
	}

}
