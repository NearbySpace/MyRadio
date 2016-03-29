package com.example.toolbar.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dolphinradio.R;
import com.example.toolbar.bean.Program;
import com.example.toolbar.bean.Top;
import com.example.toolbar.common.utils.ImageLoaderHelper;
import com.example.toolbar.common.utils.LogHelper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ProgramAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater inflater;
	private ImageLoader mImageLoader;
	private List<Program> programs;

	public ProgramAdapter(Context context, List<Program> programs) {
		this.mContext = context;
		this.inflater = LayoutInflater.from(context);
		this.programs = programs;
		mImageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
	}
	public void setProgram(List<Program> programs) {
		this.programs = programs;
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		return programs == null ? 0 : programs.size();
	}

	@Override
	public Object getItem(int position) {
		return programs.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = this.inflater.inflate(R.layout.item_program,
					null);
			holder.iv = (ImageView) convertView.findViewById(R.id.pro_image);
			holder.tv = (TextView) convertView.findViewById(R.id.pro_text);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		mImageLoader.displayImage(programs.get(position).getThumb(), holder.iv,
				ImageLoaderHelper.getDisplayImageOptions());
		holder.tv.setText(programs.get(position).getTitle());
//		LogHelper.e("PRO:"+programs.get(position).getTitle());
		return convertView;
	}

	private class ViewHolder {
		ImageView iv;
		TextView tv;
	}

}
