package com.example.toolbar.top;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class App extends Application {
	public static Handler handler = new Handler();
	public static final boolean DEBUG = true;
	public static String version = "-1";
	public DisplayMetrics dm = new DisplayMetrics();
	private static App instance;
//	public final static String appName="xzzl";
	 /**
	  * 程序包名
	  */
	 public final static String appPacketName = "com.example.dolphinradio";

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		try {
			PackageManager pm = getPackageManager();
			PackageInfo pi = pm.getPackageInfo(this.getPackageName(), 0);
			version = pi.versionName;
		} catch (PackageManager.NameNotFoundException e) {
		}

		initImageLoader(getApplicationContext());
	}
	 
	
	public static String currentFileDir;
	public static void initImageLoader(Context context) {
	//	currentFileDir = new SimpleDateFormat("yyMMdd").format(new Date());
		File cacheDir = StorageUtils.getOwnCacheDirectory(context,"tiaoqu/cache"/*+currentFileDir*/);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize(50 * 1024 * 1024)
				// 50 Mb
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.discCache(new MyDiskCache(cacheDir))// 自定义缓存路径
				.writeDebugLogs()
				.build();
		ImageLoader.getInstance().init(config);
		//thread.start();
	}

	public synchronized static App getIns() {
		return instance;
	}
	
	

}
