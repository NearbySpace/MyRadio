package com.example.toolbar.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.dolphinradio.R;
import com.example.toolbar.bean.DownloadedBean;
import com.example.toolbar.common.utils.ImageLoaderHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class DownloadedFragmentAdapter extends BaseAdapter {
	private Context context;
	private List<DownloadedBean> downloadedBeanList;
	private ImageLoader mImageLoader;
	private Holder holder;
	private boolean isEditor = false;
	public DownloadedFragmentAdapter(Context context,List<DownloadedBean> downloadedBeanList) {
		super();
		this.context = context;
		this.downloadedBeanList = downloadedBeanList;
		mImageLoader = ImageLoader.getInstance();
	}
	
	@Override
	public int getCount() {
		
		return downloadedBeanList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return downloadedBeanList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	public void setEditorState(boolean isEditor){
		this.isEditor = isEditor;
		notifyDataSetChanged();
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		DownloadedBean info = downloadedBeanList.get(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_downloaded,
					null);
			holder = new Holder();
			holder.iv = (ImageView) convertView
					.findViewById(R.id.downloaded_iv);
			holder.tv_title = (TextView) convertView
					.findViewById(R.id.downloaded_title_item);
			holder.tv_size = (TextView) convertView
					.findViewById(R.id.downloaded_size_item);
			holder.tv_source = (TextView) convertView
					.findViewById(R.id.downloaded_source_item);
			holder.tv_time = (TextView) convertView
					.findViewById(R.id.downloaded_time_item);
			holder.checkBox = (CheckBox) convertView.findViewById(R.id.downloaded_checkbox_item);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		if(isEditor){
			holder.checkBox.setVisibility(View.VISIBLE);
		}else{
			holder.checkBox.setVisibility(View.GONE);
		}
		mImageLoader.displayImage(info.getThumb(), holder.iv,
				ImageLoaderHelper.getDisplayImageOptions());
		holder.tv_title.setText(info.getName());
		holder.tv_size.setText("未知");
		holder.tv_source.setText(info.getAuthor());
		holder.checkBox.setChecked(info.getChecked_state());
		return convertView;
	
	}

	class Holder {
		public ImageView iv;
		public TextView tv_title;
		public TextView tv_size;
		public TextView tv_source;
		public TextView tv_time;
		public CheckBox checkBox;
	}
	
	
}
