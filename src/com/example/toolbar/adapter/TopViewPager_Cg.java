//package com.example.toolbar.adapter;
//
//import java.util.List;
//
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.drawable.Drawable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.Toast;
//
//import com.example.dolphinradio.R;
//import com.example.toolbar.bean.Top;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.assist.FailReason;
//import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
//
//public class TopViewPager_Cg {
//	public Context context;
//	private LayoutInflater inflater;
//	private List<Top> mList;
//	private ImageLoader mImageLoader;
//
//	public TopViewPager_Cg(Context context, List<Top> list) {
//		this.context = context;
//		inflater = LayoutInflater.from(context);
//		mList = list;
//		mImageLoader = ImageLoader.getInstance();
//	}
//
//	/**
//	 * Return the number of views available.
//	 */
//	public int getCount() {
//		// TODO Auto-generated method stub
//		return mList.size();
//	}
//
//	/**
//	 * Remove a page for the given position. 滑动过后就销毁 ，销毁当前页的前一个的前一个的页！
//	 * instantiateItem(View container, int position) This method was deprecated
//	 * in API level . Use instantiateItem(ViewGroup, int)
//	 */
//	public void destroyItem(ViewGroup container, int position, Object object) {
//		// TODO Auto-generated method stub
//		container.removeView((View) object);
//
//	}
//
//	public boolean isViewFromObject(View arg0, Object arg1) {
//		// TODO Auto-generated method stub
//		return arg0 == arg1;
//	}
//
//	/**
//	 * Create the page for the given position.
//	 */
//	public Object instantiateItem(final ViewGroup container, final int position) {
//
//		View imageLayout = inflater.inflate(R.layout.item_fristshows_top, container, false);
//		assert imageLayout != null;
//		ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
//		final ProgressBar progressBar = (ProgressBar) imageLayout.findViewById(R.id.loading);
//
//		mImageLoader.displayImage(mList.get(position).getThumb(), imageView, new SimpleImageLoadingListener() {
//			public void onLoadingStarted(String imageUri, View view) {
//				progressBar.setVisibility(View.VISIBLE);
//			}
//
//			@Override
//			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//				String message = null;
//				switch (failReason.getType()) {
//				case IO_ERROR:
//					message = "Input/Output error";
//					break;
//				case DECODING_ERROR:
//					message = "Image can't be decoded";
//					break;
//				case NETWORK_DENIED:
//					message = "Downloads are denied";
//					break;
//				case OUT_OF_MEMORY:
//					message = "Out Of Memory error";
//					break;
//				case UNKNOWN:
//					message = "Unknown error";
//					break;
//				}
//
//				progressBar.setVisibility(View.GONE);
//			}
//
//			@Override
//			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//				progressBar.setVisibility(View.GONE);
//			}
//		});
//
//		container.addView(imageLayout, 0);
//		return imageLayout;
//
//	}
//}
