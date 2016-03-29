package com.example.toolbar.common.utils;

import android.graphics.Bitmap;

import com.example.dolphinradio.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class ImageLoaderHelper {
	
	public ImageLoaderHelper() {
		// TODO Auto-generated constructor stub
	}
	
	public static DisplayImageOptions getDisplayImageOptionsForStretched(){
		
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		// 设置正在加载图片
		 .showImageOnLoading(R.drawable.image_default) //1.8.7新增
		.showImageForEmptyUri(R.drawable.image_default)//设置图片Uri为空或是错误的时候显示的图片 
		.showImageOnFail(R.drawable.image_load_fail)	// 设置加载失败图片
		.cacheInMemory(true)//设置下载的图片是否缓存在内存中  
		.cacheOnDisc(true) //设置下载的图片是否缓存在SD卡中  
		.considerExifParams(true) //是否考虑JPEG图像EXIF参数（旋转，翻转）
		.bitmapConfig(Bitmap.Config.RGB_565)  
		.displayer(new FadeInBitmapDisplayer(200))//是否图片加载好后渐入的动画时间  
		.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
		.build();
		return options;
	} 
	
	public static DisplayImageOptions getDisplayImageOptions(){
		
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		// 设置正在加载图片
		 .showImageOnLoading(R.drawable.image_default) //1.8.7新增
		.showImageForEmptyUri(R.drawable.image_default)//设置图片Uri为空或是错误的时候显示的图片 
		.showImageOnFail(R.drawable.image_default)	// 设置加载失败图片
		.cacheInMemory(true)//设置下载的图片是否缓存在内存中  
		.cacheOnDisc(true) //设置下载的图片是否缓存在SD卡中  
		.considerExifParams(true) //是否考虑JPEG图像EXIF参数（旋转，翻转）
		.bitmapConfig(Bitmap.Config.RGB_565)  
		.displayer(new FadeInBitmapDisplayer(200))//是否图片加载好后渐入的动画时间  
		.imageScaleType(ImageScaleType.EXACTLY)
		.build();
		return options;
	} 
	
	public static DisplayImageOptions getDisplayTalkImageOptions(){
	
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		// 设置正在加载图片
		.showImageForEmptyUri(R.drawable.image_default)//设置图片Uri为空或是错误的时候显示的图片 
		.showImageOnFail(R.drawable.image_default)	// 设置加载失败图片    image_load_fail
		.cacheInMemory(true)//设置下载的图片是否缓存在内存中  
		.cacheOnDisc(true) //设置下载的图片是否缓存在SD卡中  
		.considerExifParams(true) //是否考虑JPEG图像EXIF参数（旋转，翻转）
		.bitmapConfig(Bitmap.Config.RGB_565)  
		.imageScaleType(ImageScaleType.EXACTLY)
		.build();
		return options;
	} 
	
	public static DisplayImageOptions getDisplayImageUnDefaultOptions(){
		
		DisplayImageOptions options = new DisplayImageOptions.Builder()
//		.showStubImage(R.drawable.image_default) //设置图片在下载期间显示的图片  
		// 设置正在加载图片
		// .showImageOnLoading(R.drawable.image_default) //1.8.7新增
//		.showImageForEmptyUri(R.drawable.image_default)//设置图片Uri为空或是错误的时候显示的图片 
//		.showImageOnFail(R.drawable.image_default)	// 设置加载失败图片
		.cacheInMemory(true)//设置下载的图片是否缓存在内存中  
		.cacheOnDisc(true) //设置下载的图片是否缓存在SD卡中  
		.considerExifParams(true) //是否考虑JPEG图像EXIF参数（旋转，翻转）
		.bitmapConfig(Bitmap.Config.RGB_565) 
		.displayer(new FadeInBitmapDisplayer(200))//是否图片加载好后渐入的动画时间  
		.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
		.build();
		return options;
	} 
	
	public static DisplayImageOptions getRoundDisplayImageOptions(){
		
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(R.drawable.usericon_bg)
		.showImageOnFail(R.drawable.usericon_bg)
		// 设置加载失败图片
		.cacheInMemory(true).cacheOnDisc(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.considerExifParams(true) //是否考虑JPEG图像EXIF参数（旋转，翻转）
	//	.displayer(new RoundedBitmapDisplayer(360)) // 设置图片角度,0为方形，360为圆角
		.imageScaleType(ImageScaleType.EXACTLY)
		.build();
		return options;
		
	}

}
