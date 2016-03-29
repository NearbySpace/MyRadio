/*
 * Copyright (C) 2011 Patrik �kerfeldt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.toolbar.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.dolphinradio.R;
import com.example.toolbar.bean.Top;
import com.example.toolbar.common.utils.ImageLoaderHelper;
import com.example.toolbar.common.utils.LogHelper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
/*
 * 首页轮播图片
 * */
public class FlowViewAdatpter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private List<Top> top;
	private ImageView imageView;
	private ProgressBar mProgressBar;
	private ViewHolder viewHolder;

	// private static final int[] ids = {R.drawable.myradio_host_bg,
	// R.drawable.myradio_bg};

	public FlowViewAdatpter(Context context) {
		mContext = context;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public FlowViewAdatpter(Context context, List<Top> top) {
		this.top = top;
		mContext = context;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	private class ViewHolder {

		ImageView imageView;
		ProgressBar bar;
	}

	@Override
	public int getCount() {
		return Integer.MAX_VALUE; // 返回很大的值使得getView中的position不断增大来实现循环
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_flowview, null);
			viewHolder.imageView = (ImageView) convertView
					.findViewById(R.id.imgView);
			viewHolder.bar = (ProgressBar) convertView
					.findViewById(R.id.myradio_flowimage_item_progressbar);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (top != null) {
			LogHelper.e("top地址:" + top.get(position % top.size()).getThumb());
			ImageLoader.getInstance().displayImage(
					top.get(position % top.size()).getThumb(),
					viewHolder.imageView,
					ImageLoaderHelper.getDisplayImageOptions(),
					new ImageLoadingListener() {

						@Override
						public void onLoadingStarted(String imageUri, View view) {
							// TODO Auto-generated method stub
							viewHolder.bar.setVisibility(View.GONE);
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							// TODO Auto-generated method stub
							viewHolder.bar.setVisibility(View.GONE);
							viewHolder.imageView
									.setImageResource(R.drawable.image_load_fail);
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							// TODO Auto-generated method stub
							viewHolder.bar.setVisibility(View.GONE);
							// Bitmap imageBitmap =
							// ImageUtils.scaleBitmap(loadedImage);
							// imageView.setImageBitmap(imageBitmap);
							// imageView.setImageBitmap(ImageUtils.scaleBitmapWidth(loadedImage));
						}

						@Override
						public void onLoadingCancelled(String imageUri,
								View view) {
							// TODO Auto-generated method stub

						}
					});
			viewHolder.imageView.setOnClickListener(new ImageOnClick(mContext,
					top.get(position % top.size()) + ""));

		}

		return convertView;
	}

	// 查看图片
	private class ImageOnClick implements OnClickListener {

		Context context;
		String ImageUrl;

		public ImageOnClick(Context context, String path) {
			super();
			// TODO Auto-generated constructor stub
			this.context = context;
			ImageUrl = path;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			// Intent intent = new Intent(context, ImageViewPreview.class);
			// intent.putExtra("thumb", ImageUrl);
			// context.startActivity(intent);
		}

	}

}
