package com.example.toolbar.adapter;

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
import com.example.toolbar.bean.Top;
import com.example.toolbar.common.utils.ImageLoaderHelper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class TopAdapter extends BaseAdapter {

	private Context mContext;
	private List<Top> top;

	private ImageLoader mImageLoader;
	private LayoutInflater inflater;

	public TopAdapter(Context context, List<Top> top) {
		this.mContext = context;
		this.top = top;
		inflater = LayoutInflater.from(context);

		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
	}

	@Override
	public int getCount() {
		return top == null ? 0 : top.size();
	}

	@Override
	public Object getItem(int position) {
		return top.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView holderView = null;
		View view = convertView;

		if (view == null) {
			holderView = new HolderView();
			view = LayoutInflater.from(mContext).inflate(
					R.layout.item_fristshows_top, parent, false);

			holderView.imageView = (ImageView) view
					.findViewById(R.id.top_image);
			holderView.textView = (TextView) view.findViewById(R.id.top_text);

			view.setTag(holderView);
		} else {
			holderView = (HolderView) view.getTag();
		}
		mImageLoader.displayImage(top.get(position).getThumb(), holderView.imageView,
				ImageLoaderHelper.getDisplayImageOptions());
//		holderView.imageView.setImageResource((Integer) mList.get(position)
//				.get("pic"));
		holderView.textView.setText(top.get(position).getTitle());
		return view;
	}

	class HolderView {
		ImageView imageView;
		TextView textView;
	}
}
