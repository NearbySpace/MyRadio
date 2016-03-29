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
import com.example.toolbar.bean.Program;
import com.example.toolbar.bean.SelectProgram;
import com.example.dolphinradio.R;
import com.example.toolbar.common.utils.ImageLoaderHelper;
import com.example.toolbar.common.utils.LogHelper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
/**
 * 适配器
 * @author 
 *
 */
public class AddProgramSelectAdapter extends BaseAdapter {
	
	private Context mContext;
	private LayoutInflater mInflater;
	private Map<String, String> mDataMap;
	private List<SelectProgram> program;
	private ViewHolder viewHolder;
	private ImageLoader mImageLoader;

	public AddProgramSelectAdapter(Context context, List<SelectProgram> program) {
		super();
		// TODO Auto-generated constructor stub
		
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		this.program = program;
//		LogHelper.e("获取节目数据：" + program.toString());
		mImageLoader = ImageLoader.getInstance();
//		mImageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
	}
	
	public void refreshData(List<SelectProgram> program){
		this.program = program;
		notifyDataSetChanged();
	}
	
	private class ViewHolder{
		TextView title;
		TextView show_number,name;
		ImageView imageView;
		
	}

	@Override
	public int getCount() {
		return program == null ? 0 : program.size();
	}

	@Override
	public Object getItem(int position) {
		return program.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup group) {
		// TODO Auto-generated method stub
		if (convertView == null) {
//			LogHelper.e("获取节目数据：" + program.toString());
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_addproger_select, null);
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.show_icon);
			viewHolder.title = (TextView) convertView.findViewById(R.id.show_titel);
			viewHolder.show_number = (TextView) convertView.findViewById(R.id.shows_number);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.title.setText(program.get(position).getTitle());
		viewHolder.show_number.setText(program.get(position).getProgram_count());
//		LogHelper.e("获取节目标题："+program.get(position).getTitle());
//		if (dataMap.get("name").equals("0")) {
//			viewHolder.content.setVisibility(View.GONE);
//		}else if (dataMap.get("type").equals("1")) {
//			viewHolder.content.setVisibility(View.VISIBLE);
//			viewHolder.content.setText(dataMap.get("title"));
//		}
		mImageLoader.displayImage(program.get(position).getThumb(), viewHolder.imageView,
				ImageLoaderHelper.getDisplayImageOptions());
		return convertView;
	}

}
