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
import com.example.toolbar.bean.PrivateMain_Progtam;
import com.example.strawberryradio.R;
import com.example.toolbar.common.utils.ImageLoaderHelper;
import com.example.toolbar.common.utils.LogHelper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * 个人主页节目列表适配器
 * 
 * @author
 * 
 */
public class PMprogerAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private Map<String, String> mDataMap;
	private List<PrivateMain_Progtam> progtam;
	private ViewHolder viewHolder;
	private ImageLoader mImageLoader;

	public PMprogerAdapter(Context context, List<PrivateMain_Progtam> progtam) {
		super();
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		this.progtam = progtam;
		mImageLoader = ImageLoader.getInstance();
	}

	private class ViewHolder {
		TextView title;
		TextView shoutin, program_num;
		ImageView imageView;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return progtam == null ? 0 : progtam.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return progtam.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup group) {
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater
					.inflate(R.layout.item_private_program, null);
			viewHolder.imageView = (ImageView) convertView
					.findViewById(R.id.shows_icon);
			viewHolder.title = (TextView) convertView
					.findViewById(R.id.shows_titel);
			viewHolder.program_num = (TextView) convertView
					.findViewById(R.id.program_num);
			viewHolder.shoutin = (TextView) convertView
					.findViewById(R.id.play_number);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.title.setText(progtam.get(position).getTitle());
		viewHolder.program_num.setText(progtam.get(position).getProgram_num() + "个节目");
		viewHolder.shoutin.setText("播放" + progtam.get(position).getPlaytimes() + "次");
		//LogHelper.e("获取播放数据："+progtam.get(position).getTitle());
		mImageLoader.displayImage(progtam.get(position).getThumb(),
				viewHolder.imageView,
				ImageLoaderHelper.getDisplayImageOptions());
		return convertView;
	}

}
