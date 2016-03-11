package com.example.toolbar.application;

import java.io.File;

import org.apache.http.util.VersionInfo;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Vibrator;
import android.view.Display;
import android.view.WindowManager;

import com.baidu.mapapi.SDKInitializer;
import com.example.toolbar.bean.UserInfo;
import com.example.toolbar.service.LocationService;
import com.example.toolbar.top.MyDiskCache;
import com.example.toolbar.uncaughtexception.DefaultExceptionHandler;
import com.example.toolbar.utils.ConfigUtils;
import com.example.toolbar.utils.SharePreferenceUtil;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class MyApplication extends Application {
	public static MyApplication mApplication;
	public SharePreferenceUtil sharePreferenceUtil;
	public int display_width = 480;// 屏幕宽度
	public int display_height = 800;// 屏幕高度
	public String city = ""; // 当前所在 城市
	public String district = ""; // 当前所在城区
	public String street = ""; // 当前所在街道路名
	public String longlat = ""; // 当前所在 经纬度
	public static boolean myradio_music_is_open = false;// 是否在播放音乐
	public static String host_id = "";// 播放的音乐id
	
	public LocationService locationService;
    public Vibrator mVibrator;
//	public UserInfo mUserInfo;
//	
//	public void setUserInfo(UserInfo info){
//		mUserInfo = info;
//	}
//	
//	public UserInfo getUserinfo(){
//		
//		return mUserInfo;
//	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mApplication = this;
		initMyDate();
		initImageLoader(getApplicationContext());
		Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(
				getApplicationContext()));
		/***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());  
	}

	private void initMyDate() {
		// TODO Auto-generated method stub
		sharePreferenceUtil = new SharePreferenceUtil(this,
				ConfigUtils.appSharePreferenceName);

		WindowManager wm = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		display_width = display.getWidth();
		display_height = display.getHeight();
		// 初始化定位数据
		// mLocationClient = new LocationClient(getApplicationContext());
		// LocationClientOption option = new LocationClientOption();
		// option.setOpenGps(true);
		// option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
		// option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		// option.setScanSpan(10 * 1000);// 设置发起定位请求的间隔时间为 10秒
		// option.setOpenGps(false);//打开gps
		// mLocationClient.setLocOption(option);
		// mMyLocationListener = new MyLocationListener();
		// mLocationClient.registerLocationListener(mMyLocationListener);//注册监听
		// 百度地图在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
		// SDKInitializer.initialize(this);
	}

	public synchronized static MyApplication getInstance() {

		return mApplication;
	}

	public synchronized SharePreferenceUtil getSpUtil() {
		if (sharePreferenceUtil == null)
			sharePreferenceUtil = new SharePreferenceUtil(this,
					ConfigUtils.appSharePreferenceName);
		return sharePreferenceUtil;
	}

	public static void initImageLoader(Context context) {
		// Create default options which will be used for every
		// displayImage(...) call if no options will be passed to this method

		/**
		 * 01.String imageUri = "http://site.com/image.png"; // 网络图片 02.String
		 * imageUri = "file:///mnt/sdcard/image.png"; //SD卡图片 03.String imageUri
		 * = "content://media/external/audio/albumart/13"; // 媒体文件夹 04.String
		 * imageUri = "assets://image.png"; // assets 05.String imageUri =
		 * "drawable://" + R.drawable.image; // drawable文件
		 */
		File cacheDir = StorageUtils.getOwnCacheDirectory(context,
				"tiaoqu/cache"/* +currentFileDir */);
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisc(true).build();
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context)
				.threadPoolSize(3)
				// 线程池内加载的数量
				.threadPriority(Thread.NORM_PRIORITY - 1)
				// 设置图片加载线程的优先级,默认为Thread.NORM_PRIORITY-1
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				// 设置图片加载和显示队列处理的类型 默认为QueueProcessingType.FIFO
				.denyCacheImageMultipleSizesInMemory()
				// 设置拒绝缓存在内存中一个图片多个大小 默认为允许,(同一个图片URL)根据不同大小的imageview保存不同大小图片
				.memoryCache(new LRULimitedMemoryCache(2 * 1024 * 1024))
				// 设置内存缓存
				.memoryCacheExtraOptions(display.getWidth(),
						display.getHeight())
				// 内存缓存的设置选项 (最大图片宽度,最大图片高度) 默认当前屏幕分辨率
				.memoryCacheSizePercentage(13)
				// 设置内存缓存最大大小占当前应用可用内存的百分比
				// 设置硬盘缓存
				// 默认为StorageUtils.getCacheDirectory(getApplicationContext())
				// 即/mnt/sdcard/android/data/包名/cache/
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize(50 * 1024 * 1024)
				.discCache(new MyDiskCache(cacheDir))// 自定义缓存路径
				// .disckCache(new
				// TotalSizeLimitedDiscCache(StorageUtils.getCacheDirectory(context),6*1024*1024))
				.discCacheFileCount(100) // 设置硬盘缓存的文件的最多个数
				.defaultDisplayImageOptions(defaultOptions)
				// .writeDebugLogs() // 打印DebugLogs // for // release // app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

	/**
	 * 获取App安装包信息
	 * 
	 * @return
	 */
	public PackageInfo getPackageInfo() {
		PackageInfo info = null;
		try {
			info = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace(System.err);
		}
		if (info == null)
			info = new PackageInfo();
		return info;
	}

}
