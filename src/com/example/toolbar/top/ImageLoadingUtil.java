package com.example.toolbar.top;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.example.dolphinradio.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class ImageLoadingUtil {

	/**
	 *获取图片的加载类 
	 *默认
	 * */
	public static DisplayImageOptions getImageOptions(){
		
		DisplayImageOptions	options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.mall_defalut)
		.showImageForEmptyUri(R.drawable.mall_defalut)
		.showImageOnFail(R.drawable.mall_defalut)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.considerExifParams(true)
		//.displayer(new RoundedBitmapDisplayer(20))
		//.displayer(new FadeInBitmapDisplayer(100))
		.build();
		return options;
	}

	
	/**
	 *获取图片的加载类 
	 *加载默认图片
	 * */
	public static DisplayImageOptions getImageOptions(int defaultDrawalbe){
		
		DisplayImageOptions	options = new DisplayImageOptions.Builder()
		.showImageOnLoading(defaultDrawalbe)
		.showImageForEmptyUri(defaultDrawalbe)
		.showImageOnFail(defaultDrawalbe)
		.cacheInMemory(true)
		.cacheOnDisk(true)
	    .bitmapConfig(Bitmap.Config.RGB_565)
		.considerExifParams(true)
		//.displayer(new RoundedBitmapDisplayer(20))
	//	.displayer(new FadeInBitmapDisplayer(100))
		.build();
		return options;
	}
	public static DisplayImageOptions getImageOptions2(){
		
		DisplayImageOptions	options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.mall_defalut)
		.showImageForEmptyUri(R.drawable.mall_defalut)
		.showImageOnFail(R.drawable.mall_defalut)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		   .bitmapConfig(Bitmap.Config.ARGB_8888)
		//.displayer(new RoundedBitmapDisplayer(20))
		//.displayer(new FadeInBitmapDisplayer(100))
		.build();
		return options;
	}

	/**
	 * 加载图片
	 * */
	public static void loadingImg(ImageView iv,String picUrl,DisplayImageOptions options){
		ImageLoader.getInstance().displayImage(WifiURLTo3G(picUrl), iv, options, animateFirstListener);
	}
	public static void loadingImg(ImageView iv,String picUrl){
		ImageLoader.getInstance().displayImage(WifiURLTo3G(picUrl), iv, getImageOptions(), animateFirstListener);
	}
	

	
	
	public static void loadingImg(ImageView iv,String picUrl,DisplayImageOptions options,ImageLoadingListener listener){
		ImageLoader.getInstance().displayImage(WifiURLTo3G(picUrl), iv, options, listener);
	}
	public static String netStr;
	public static String WifiURLTo3G(String url){
		
//		if(url.startsWith("http://img.oss.shuihulu.com/")&&url.indexOf("@")>0){
//			if(url.endsWith("_90Q")||url.endsWith("_98Q")){
//				String end=url.substring(url.length()-4, url.length());
//				netStr=NetService.getCurrentNetType(App.getIns());
//				System.out.println("网络"+netStr);
//				if(netStr.equals("wifi")){
//					url=url.replaceAll(end, "_98Q");
//				}else{
//					//if(isCache(end, url)==null){
//						url=url.replaceAll(end, "_90Q");
////					}else{
////						url=url.replaceAll(end, "_98Q");
////					}
//				}
//			}
//		}
		return url;
	}
 
	
	private static ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				
//				if(netStr.equals("wifi")){
//					Urls u=new Urls();
//					u.setUrl(imageUri);
//					u.setDatStr(Util.getSystemDate());
//					db.update(u);
//				}
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
}
