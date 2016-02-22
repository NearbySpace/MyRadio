package com.example.toolbar.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.example.strawberryradio.R;
import com.example.toolbar.bean.Click_Ranking;
import com.example.toolbar.bean.Hot;
import com.example.toolbar.bean.Top;
import com.example.toolbar.bean.FindBean.IconTop;
import com.example.toolbar.common.utils.ImageLoaderHelper;
import com.example.toolbar.common.utils.LogHelper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
/**
 * 节目适配器
 * @author 
 *
 */
public class FindImgAdapter extends BaseAdapter {
	
	private Context mContext;
	private LayoutInflater mInflater;
	private Map<String, String> mDataMap;
	private ArrayList<IconTop> radio_list; ;
	private ViewHolder viewHolder;
	private ImageLoader mImageLoader;

	public FindImgAdapter(LayoutInflater inflater, ArrayList<IconTop> radio_list) {
		super();
		// TODO Auto-generated constructor stub
//		this.mContext = context;
//		if(context == null){
//			Log.i("FindImgAdapter", "context为空");
//		}
//		mInflater = LayoutInflater.from(context);
		mInflater = inflater;
		this.radio_list = radio_list;
		mImageLoader = ImageLoader.getInstance();
//		mImageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
	}
	
	private class ViewHolder{
		ImageView imageView;
	}
	
//	public void uploadMsg(List<Map<String, String>> data){
//		mList.addAll(data);
//		notifyDataSetChanged();
//	}
//	
//	public void uploadMsg(Map<String, String> data){
//		mList.add(data);
//		notifyDataSetChanged();
//	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return radio_list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return radio_list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup group) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_find_img, null);
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.find_img);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if(position==5){
			viewHolder.imageView.setScaleType(ScaleType.CENTER_CROP);
		}
		mImageLoader.displayImage(radio_list.get(position).avatar, viewHolder.imageView,
				ImageLoaderHelper.getDisplayImageOptions());
		return convertView;
	}

}
