package com.example.toolbar.utils;

import android.content.Context;
import android.os.Environment;

public class ConfigUtils {
	public static String appSharePreferenceName = "strawberryradio_sharepreference";
	public static String apkName = "strawberryradio.apk";
	public static String baseurl = "http://vroad.bbrtv.com/cmradio/";
	public static String SDRootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
	public static String SDcardPath = SDRootPath + "/Strawberry/"; // App主目录	
	public static String SDDownloadPath = SDcardPath + "Download/"; // App文件下载目录	
	public static String SDImageCachePath = SDcardPath + "/ImageCache/"; // 图片缓存目录
//	public static String ROMDownloadPath="/data/data/com.example.strawberryradio/Download/";//内存中的下载路径
	public static String cameraimagepath = "cameratemp.jpg";
	public static String avatar = "avatar.jpg";
	public static String pickimagepath = "picktemp.jpg";
	//下载状态的表示值
	public static String DownloadState_DOING = "1";
	public static String DownloadState_WAITTING = "2";
	public static String DownloadState_FINISH = "3";
	public static String DownloadState_FAIL = "4";
	public static String DownloadState_PAUSE = "5";
	
	public static String getDownloadPath(Context context){
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			return SDDownloadPath;
		}
		return context.getFilesDir().getPath()+"/ImageDownload";
		
	}
}
