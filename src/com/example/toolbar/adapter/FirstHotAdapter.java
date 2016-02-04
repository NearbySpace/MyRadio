package com.example.toolbar.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.toolbar.bean.Click_Ranking;
import com.example.strawberryradio.R;
import com.example.toolbar.common.utils.ImageLoaderHelper;
import com.example.toolbar.common.utils.LogHelper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
/**
 * 适配器
 * @author 
 *
 */
public class FirstHotAdapter extends BaseAdapter {
	
	private Context mContext;
	private LayoutInflater mInflater;
	private List<Click_Ranking> ranking;
	private ViewHolder viewHolder;
	private ImageLoader mImageLoader;

	public FirstHotAdapter(Context context, List<Click_Ranking> ranking) {
		super();
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		this.ranking = ranking;
		mImageLoader = ImageLoader.getInstance();
	}
	
	private class ViewHolder{
		TextView title;
		TextView shoutin,name;
		ImageView imageView;
		
	}

	@Override
	public int getCount() {
		return ranking == null ? 0 : ranking.size();
	}

	@Override
	public Object getItem(int position) {
		return ranking.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup group) {
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_fristshows_hot, null);
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.ranking_icon);
			viewHolder.title = (TextView) convertView.findViewById(R.id.ranking_titel);
			viewHolder.name = (TextView) convertView.findViewById(R.id.ranking_name);
			viewHolder.shoutin = (TextView) convertView.findViewById(R.id.ranking_mid);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.title.setText(ranking.get(position).getTitle());
		viewHolder.name.setText(ranking.get(position).getOwner());
		viewHolder.name.setText(ranking.get(position).getOwner());
		viewHolder.name.setText(ranking.get(position).getMid());
		
		mImageLoader.displayImage(ranking.get(position).getThumb(), viewHolder.imageView,
				ImageLoaderHelper.getDisplayImageOptions());
		return convertView;
	}

}
