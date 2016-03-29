package com.example.toolbar.service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.audiofx.Visualizer;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Switch;

import com.example.toolbar.application.MyApplication;
import com.example.toolbar.common.utils.NetUtil;
import com.example.toolbar.entity.PlayButton;
/***
 * 
 * @author hxc
 * myradio音乐播放服务
 */
@SuppressLint("NewApi")
public class PlayerService extends Service {
	private MediaPlayer mediaPlayer; // 媒体播放器对象
	private String path; 			// 音乐文件路径
	private long time_limit;
	private int msg;				//播放信息
	private boolean isPause = false; 		// 暂停状态
	private int current = 0; 		// 记录当前正在播放的音乐
	private int current_two=0;  //类型2的歌曲播放位置
//	private List<Mp3Info> mp3Infos;	//存放Mp3Info对象的集合
	private int status = 3;			//播放状态，默认为顺序播放
//	private MyReceiver myReceiver;	//自定义广播接收器
	private int currentTime;		//当前播放进度
	private int duration;			//播放长度
	private int index = 0;			//歌词检索值
	private boolean isLocad=false;//是否本地播放
	private boolean isFromAlarmManager = false;
	private boolean isTypeTwo = false;
	private boolean isContinue = false;//当网络不为WiFi是是否继续播放
//	private boolean isAgain=false;//是否重新播放
	private String classifyID;
	
	private Visualizer mVisualizer;// 定义系统的频谱
	
	//服务要发送的一些Action
	public static final String UPDATE_ACTION = "com.myradio.action.UPDATE_ACTION";	//更新动作
//	public static final String CTL_ACTION = "com.myradio.action.CTL_ACTION";		//控制动作
	public static final String MUSIC_CURRENT = "com.myradio.action.MUSIC_CURRENT";	//当前音乐播放时间更新动作
	public static final String MUSIC_DURATION = "com.myradio.action.MUSIC_DURATION";//新音乐长度更新动作
	public static final String MUSIC_FINISH = "com.myradio.action.MUSIC_FINISH";//音乐播放完
	//	public static final String SHOW_LRC = "com.myradio.action.SHOW_LRC";			//通知显示歌词
	private NotificationManager notificationManager;// 通知管理类
	private AlarmManager mAlarmManager;
	private PendingIntent pendingIntent;
	/**
	 * handler用来接收消息，来发送广播更新播放时间
	 */
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				if(mediaPlayer != null) {
					currentTime = mediaPlayer.getCurrentPosition(); // 获取当前音乐播放的位置					
					Intent intent = new Intent();
					intent.setAction(MUSIC_CURRENT);
					intent.putExtra("currentTime", currentTime);
					intent.putExtra("duration", duration);	//通过Intent来传递歌曲的总长度
					sendBroadcast(intent); // 给PlayerActivity发送广播
					if (currentTime != duration && duration > currentTime) {
						handler.sendEmptyMessageDelayed(1, 1000);
					}
				}
				break;
			case 2:
				PlayerManage.position += 1;
				if(PlayerManage.position>=PlayerManage.getInstance().getPlayInfos().size()){
					Intent sendIntent = new Intent(UPDATE_ACTION);
					sendIntent.putExtra("without", true);
					// 发送广播，将被Activity组件中的BroadcastReceiver接收到
					sendBroadcast(sendIntent);
					PlayerManage.position=0;
				}
				Intent intent = new Intent();
				intent.setAction("com.myradio.media.MUSIC_SERVICE");
				intent.setPackage("com.example.dolphinradio");
				intent.putExtra("MSG", PlayButton.PlayerMsg.NEXT_MSG);
				intent.putExtra("isFromAlarmManager", true);
				startService(intent);
				break;

			default:
				break;
			}
		};
	};

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("service", "service created");
		mediaPlayer = new MediaPlayer();
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mAlarmManager=(AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent();
		intent.setAction("com.myradio.media.MUSIC_SERVICE");
		intent.setPackage("com.example.dolphinradio");
		intent.putExtra("MSG", PlayButton.PlayerMsg.NEXT_MSG);
		intent.putExtra("isFromAlarmManager", true);
		pendingIntent = PendingIntent.getService(getApplicationContext(),
				132, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		/**
		 * 设置音乐播放完成时的监听器
		 */
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
//				if (status == 1) { // 单曲循环
//					mediaPlayer.start();
//				} else if (status == 2) { // 全部循环
//					current++;
//					if(current > mp3Infos.size() - 1) {	//变为第一首的位置继续播放
//						current = 0;
//					}
//					Intent sendIntent = new Intent(UPDATE_ACTION);
//					sendIntent.putExtra("current", current);
//					// 发送广播，将被Activity组件中的BroadcastReceiver接收到
//					sendBroadcast(sendIntent);
//					path = mp3Infos.get(current).getUrl();
//					play(0);
//				} else if (status == 3) { // 顺序播放
//					current++;	//下一首位置
//					if (current <= mp3Infos.size() - 1) {
//						Intent sendIntent = new Intent(UPDATE_ACTION);
//						sendIntent.putExtra("current", current);
//						// 发送广播，将被Activity组件中的BroadcastReceiver接收到
//						sendBroadcast(sendIntent);
//						path = mp3Infos.get(current).getUrl();
//						play(0);
//					}else {
//						mediaPlayer.seekTo(0);
//						current = 0;
//						Intent sendIntent = new Intent(UPDATE_ACTION);
//						sendIntent.putExtra("current", current);
//						// 发送广播，将被Activity组件中的BroadcastReceiver接收到
//						sendBroadcast(sendIntent);
//					}
//				} else if(status == 4) {	//随机播放
//					current = getRandomIndex(mp3Infos.size() - 1);
//					System.out.println("currentIndex ->" + current);
//					Intent sendIntent = new Intent(UPDATE_ACTION);
//					sendIntent.putExtra("current", current);
//					// 发送广播，将被Activity组件中的BroadcastReceiver接收到
//					sendBroadcast(sendIntent);
//					path = mp3Infos.get(current).getUrl();
//					play(0);
//				}
			}
		});

//		myReceiver = new MyReceiver();
//		IntentFilter filter = new IntentFilter();
//		filter.addAction(CTL_ACTION);
//		filter.addAction(SHOW_LRC);
//		registerReceiver(myReceiver, filter);
	}
	
	private void setLimitTime(long time){
//		if(mAlarmManager != null){
//			mAlarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, 
//					SystemClock.elapsedRealtime()+time, pendingIntent);
//			Log.i("PlayService", "定时器启动了");
//		}
//		Runnable runnable=new Runnable() {
//			
//			@Override
//			public void run() {
//				Intent intent = new Intent();
//				intent.setAction("com.myradio.media.MUSIC_SERVICE");
//				intent.setPackage("com.example.dolphinradio");
//				intent.putExtra("MSG", PlayButton.PlayerMsg.NEXT_MSG);
//				intent.putExtra("isFromAlarmManager", true);
//				startService(intent);
//			}
//		};
//		handler.removeCallbacks(runnable);
//		handler.postDelayed(runnable, time);
		handler.removeMessages(2);
		handler.sendEmptyMessageDelayed(2, time);
	}

	/**
	 * 获取随机位置
	 * @param end
	 * @return
	 */
	protected int getRandomIndex(int end) {
		int index = (int) (Math.random() * end);
		return index;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		//Log.d("service", "service onStartCommand");
		// 设置音频流 - STREAM_MUSIC：音乐回放即媒体音量
//		((Activity) getApplicationContext()).setVolumeControlStream(AudioManager.STREAM_MUSIC);
		if (intent == null) {
			return super.onStartCommand(intent, flags, startId);
		}
//		if(!intent.getBooleanExtra("isFromNotify", false)){
//			path = intent.getStringExtra("url") + "";		//歌曲路径
//		}
//		path = intent.getStringExtra("url") + "";		//歌曲路径
//		current = intent.getIntExtra("listPosition", -1);	//当前播放歌曲的在mp3Infos的位置
		msg = intent.getIntExtra("MSG", 0);			//播放信息
		String timespan=PlayerManage.getInstance().getPlayInfos().get(PlayerManage.position).getTimespan();
		try {
			Float time=Float.valueOf(timespan);
			time_limit=(long) (time*60*1000);
		} catch (Exception e) {
			time_limit=0;
		}
//		Log.i("PlayService", "时间限制---->time_limt:"+time_limit);
//		String time=PlayerManage.getInstance().getPlayInfos().get(PlayerManage.position).getTimespan();
//		if(time != null && !time.isEmpty()){
//			Float times=Float.valueOf(time);
//			time_limit=(long) (times*60*1000);
//			Log.i("PlayService", "时间限制---->time_limt:"+time_limit);
//		}else{
//			time_limit=0;
//		}
		isLocad=intent.getBooleanExtra("isLocad", false);
		isFromAlarmManager=intent.getBooleanExtra("isFromAlarmManager", false);
		current=PlayerManage.position; 
		if(isTypeTwo=PlayerManage.getInstance().getPlayInfos().get(current).getType_id().equals("2")){
			classifyID = PlayerManage.getInstance().getPlayInfos().get(current).getId();
			current_two=0;
			if(PlayerManage.getInstance().getPlayInfosForClassifyList().get(classifyID) == null){
				Log.i("PlayService", "classifyID类的map是空");
				return super.onStartCommand(intent, flags, startId);
			}
			if(PlayerManage.getInstance().getPlayInfosForClassifyList().get(classifyID).size()==0){
				Log.i("PlayService", "classifyID类的map是空但没有节目");
				return super.onStartCommand(intent, flags, startId);
			}else{
				path = PlayerManage.getInstance().getPlayInfosForClassifyList().get(classifyID).get(current_two).getPath();
			}
				
		}else{
			path = PlayerManage.getInstance().getPlayInfos().get(current).getPath();
		}
//		if(isTypeTwo=intent.getBooleanExtra("isTypeTwo", false)){
//			current_two=0;
//			path = PlayerManage.getInstance().getPlayInfosForClassifyList().get(current_two).getPath();	
//		}else{
//			path = PlayerManage.getInstance().getPlayInfos().get(current).getPath();
//		}
		if( current>=PlayerManage.getInstance().getPlayInfos().size()){
			return super.onStartCommand(intent, flags, startId);
		}
		Log.i("PlayService" ,"path:"+path );
		if (msg == PlayButton.PlayerMsg.PLAY_MSG) {	//直接播放音乐
			play(0);
		} else if (msg == PlayButton.PlayerMsg.PAUSE_MSG) {	//暂停
			pause();	
		} else if (msg == PlayButton.PlayerMsg.STOP_MSG) {		//停止
			stop();
		} else if (msg == PlayButton.PlayerMsg.CONTINUE_MSG) {	//继续播放
//			isAgain=intent.getBooleanExtra("isAgain", false);
//			if(isAgain){
//				play(0);
//			}else{
//				resume();
//				Log.i("PlayerService:resume()------>", "resume");
//			}
			resume();
		} else if (msg == PlayButton.PlayerMsg.PRIVIOUS_MSG) {	//上一首
			previous();
		} else if (msg == PlayButton.PlayerMsg.NEXT_MSG) {		//下一首
			next();
		} else if (msg == PlayButton.PlayerMsg.PROGRESS_CHANGE) {	//进度更新
			currentTime = intent.getIntExtra("progress", -1);
			play(currentTime);
		} else if (msg == PlayButton.PlayerMsg.PLAYING_MSG) {
			handler.sendEmptyMessage(1);
			
		}
			
		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * 初始化歌词配置
	 */
//	public void initLrc(){
//		mLrcProcess = new LrcProcess();
//		//读取歌词文件
//		mLrcProcess.readLRC(mp3Infos.get(current).getUrl());
//		//传回处理后的歌词文件
//		lrcList = mLrcProcess.getLrcList();
//		PlayerActivity.lrcView.setmLrcList(lrcList);
//		//切换带动画显示歌词
//		PlayerActivity.lrcView.setAnimation(AnimationUtils.loadAnimation(PlayerService.this,R.anim.alpha_z));
//		handler.post(mRunnable);
//	}
//	Runnable mRunnable = new Runnable() {
//		
//		@Override
//		public void run() {
//			PlayerActivity.lrcView.setIndex(lrcIndex());
//			PlayerActivity.lrcView.invalidate();
//			handler.postDelayed(mRunnable, 100);
//		}
//	};
	
//	/**
//	 * 根据时间获取歌词显示的索引值
//	 * @return
//	 */
//	public int lrcIndex() {
//		if(mediaPlayer.isPlaying()) {
//			currentTime = mediaPlayer.getCurrentPosition();
//			duration = mediaPlayer.getDuration();
//		}
//		if(currentTime < duration) {
//			for (int i = 0; i < lrcList.size(); i++) {
//				if (i < lrcList.size() - 1) {
//					if (currentTime < lrcList.get(i).getLrcTime() && i == 0) {
//						index = i;
//					}
//					if (currentTime > lrcList.get(i).getLrcTime()
//							&& currentTime < lrcList.get(i + 1).getLrcTime()) {
//						index = i;
//					}
//				}
//				if (i == lrcList.size() - 1
//						&& currentTime > lrcList.get(i).getLrcTime()) {
//					index = i;
//				}
//			}
//		}
//		return index;
//	}
	/**
	 * 播放音乐
	 * 
	 * @param position
	 */
	private void play(int currentTime) {
		
		try {
			//initLrc();
			mediaPlayer.reset();// 把各项参数恢复到初始状态
			mediaPlayer.setDataSource(path);
			duration=0;
			mediaPlayer.setOnPreparedListener(new PreparedListener(currentTime));// 注册一个监听器			
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {//播放完成
				public void onCompletion(MediaPlayer arg0) {
					if(isTypeTwo){
						current_two+=1;
						if(current_two>=PlayerManage.getInstance().getPlayInfosForClassifyList().get(classifyID).size()){
							current_two=0;
							isPause=false;
							Intent intent = new Intent();
							intent.setAction(MUSIC_FINISH);
							sendBroadcast(intent);
							return;
						}
						path = PlayerManage.getInstance().getPlayInfosForClassifyList().get(classifyID).get(current_two).getPath();	
						play(0);
						return;
					}
					isPause=false;
					Intent intent = new Intent();
					intent.setAction(MUSIC_FINISH);
					sendBroadcast(intent);
				}		
			});
			if(isLocad){
				mediaPlayer.prepare(); // 进行缓冲，播放本地音乐时使用
			}else{
				mediaPlayer.prepareAsync();//播放网络资源时使用
			}
			handler.sendEmptyMessage(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * 暂停音乐
	 */
	private void pause() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
			isPause = true;
		}
	}

	private void resume() {
		if (isPause) {
			mediaPlayer.start();
			isPause = false;
		}else {
//			play(0);
			mediaPlayer.seekTo(0);
			mediaPlayer.start();
		}
	}

	/**
	 * 上一首
	 */
	private void previous() {
//		Intent sendIntent = new Intent(UPDATE_ACTION);
//		sendIntent.putExtra("current", current);
//		if(isFromAlarmManager){
//			PlayerManage.position -= 1;
//			if(PlayerManage.position==-1){
//				PlayerManage.position=0;
//			}
//		}
//		// 发送广播，将被Activity组件中的BroadcastReceiver接收到
//		sendBroadcast(sendIntent);
		play(0);
	}

	/**
	 * 下一首
	 */
	private void next() {
//		if(isFromAlarmManager){
//			Intent sendIntent = new Intent(UPDATE_ACTION);
//			sendIntent.putExtra("current", current);
//			if(PlayerManage.position>=PlayerManage.getInstance().getPlayInfos().size()){
//				PlayerManage.position=PlayerManage.getInstance().getPlayInfos().size()-1;
//				sendIntent.putExtra("without", true);
//			}
//			// 发送广播，将被Activity组件中的BroadcastReceiver接收到
//			sendBroadcast(sendIntent);
//		}
		Intent sendIntent = new Intent(UPDATE_ACTION);
		sendIntent.putExtra("current", current);
		// 发送广播，将被Activity组件中的BroadcastReceiver接收到
		sendBroadcast(sendIntent);
		play(0);
	}

	/**
	 * 停止音乐
	 */
	private void stop() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			try {
				mediaPlayer.prepare(); // 在调用stop后如果需要再次通过start进行播放,需要之前调用prepare函数
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		Intent intent = new Intent();
		intent.setAction(MUSIC_FINISH);
		sendBroadcast(intent);
	}

	@Override
	public void onDestroy() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
		
		Intent intent = new Intent();
		intent.setAction(MUSIC_FINISH);
		sendBroadcast(intent);
		if(mAlarmManager!=null){
			mAlarmManager.cancel(pendingIntent);
		}
		//handler.removeCallbacks(mRunnable);
		//notificationManager.cancel(MyRadioActivity.myradio_notify_flag);
	}

	
	private String lastProgramId="0";
	/**
	 * 
	 * 实现一个OnPrepareLister接口,当音乐准备好的时候开始播放
	 * 
	 */
	private final class PreparedListener implements OnPreparedListener {
		private int currentTime;

		public PreparedListener(int currentTime) {
			this.currentTime = currentTime;
		}

		@Override
		public void onPrepared(MediaPlayer mp) {
			mediaPlayer.start(); // 开始播放
			
			if(time_limit != 0 && 
					!lastProgramId.equals(PlayerManage.getInstance().getPlayInfos().get(PlayerManage.position).getId())){
				handler.removeMessages(2);
				setLimitTime(time_limit);
				lastProgramId=PlayerManage.getInstance().getPlayInfos().get(PlayerManage.position).getId();
			}
			
			if (currentTime > 0) { // 如果音乐不是从头播放
				mediaPlayer.seekTo(currentTime);
			}
			Intent intent = new Intent();
			intent.setAction(MUSIC_DURATION);
			if(duration==0){
				duration = mediaPlayer.getDuration();//调用start()后，才能取得真正的总长度
				handler.sendEmptyMessage(1);
			}
			intent.putExtra("duration", duration);	//通过Intent来传递歌曲的总长度
			intent.putExtra("isPlay",true);
			sendBroadcast(intent);
		}
		
	}	

//	public class MyReceiver extends BroadcastReceiver {
//
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			int control = intent.getIntExtra("control", -1);
//			switch (control) {
//			case 1:
//				status = 1; // 将播放状态置为1表示：单曲循环
//				break;
//			case 2:
//				status = 2;	//将播放状态置为2表示：全部循环
//				break;
//			case 3:
//				status = 3;	//将播放状态置为3表示：顺序播放
//				break;
//			case 4:
//				status = 4;	//将播放状态置为4表示：随机播放
//				break;
//			}
//			
//			String action = intent.getAction();
//			if(action.equals(SHOW_LRC)){
//				current = intent.getIntExtra("listPosition", -1);
//			//	initLrc();
//			}
//		}
//	}
	
	

}
