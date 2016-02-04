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
public class ProgerAdapter_Cg extends BaseAdapter {
	
	private Context mContext;
	private LayoutInflater mInflater;
	private Map<String, String> mDataMap;
	private List<Map<String, String>> mList;
	private ViewHolder viewHolder;
	private ImageLoader mImageLoader;

	public ProgerAdapter_Cg(Context context, List<Map<String, String>> list) {
		super();
		// TODO Auto-generated constructor stub
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		mList = list;
		mDataMap = new HashMap<String, String>();
		mImageLoader = ImageLoader.getInstance();
//		mImageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
	}
	
	private class ViewHolder{
		TextView title;
		TextView shoutin,name;
		ImageView imageView;
		
	}
	
	public void uploadMsg(List<Map<String, String>> data){
		mList.addAll(data);
		notifyDataSetChanged();
	}
	
	public void uploadMsg(Map<String, String> data){
		mList.add(data);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
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
			convertView = mInflater.inflate(R.layout.item_program, null);
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.pro_image);
			viewHolder.title = (TextView) convertView.findViewById(R.id.pro_text);
			viewHolder.name = (TextView) convertView.findViewById(R.id.textView1);
			viewHolder.shoutin = (TextView) convertView.findViewById(R.id.textView2);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Map<String, String> dataMap = mList.get(position);
		viewHolder.title.setText(dataMap.get("title"));
		viewHolder.name.setText(dataMap.get("playtimes"));
		viewHolder.shoutin.setText(dataMap.get("owner"));
		
//		if (dataMap.get("name").equals("0")) {
//			viewHolder.content.setVisibility(View.GONE);
//		}else if (dataMap.get("type").equals("1")) {
//			viewHolder.content.setVisibility(View.VISIBLE);
//			viewHolder.content.setText(dataMap.get("title"));
//		}
		mImageLoader.displayImage(dataMap.get("thumb"), viewHolder.imageView, ImageLoaderHelper.getDisplayImageOptions());
		return convertView;
	}

}
