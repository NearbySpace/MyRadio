package com.example.toolbar.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.strawberryradio.R;
import com.example.toolbar.activity.RadioPlayActivity;
import com.example.toolbar.application.MyApplication;
import com.example.toolbar.entity.PlayButton;

/**
 * music notice service
 * 
 */
public class MyradioNotifyService extends Service {

	private String CLICK_OPEN_ACTION = "com.myradio.action.music.open";// 打开音乐
	private String CLICK_PAUSE_ACTION = "com.myradio.action.music.pause";//
	private String CLICK_CLOSE_ACTION = "com.myradio.action.music.close";// 关闭音乐
	private String CLICK_PREVIOUS_ACTION = "com.myradio.action.music.previous";//
	private String CLICK_NEXT_ACTION = "com.myradio.action.music.next";//
	private String PLAY_FINISH_ACTION="com.myradio.action.MUSIC_FINISH";//播放结束
	public  String OPEN_BTN_CHANGE = "com.myradio.vlook.action.OPEN_BTN_CHANGE";// 音乐播放按钮的更新
	private boolean is_play = MyApplication.myradio_music_is_open;
	private String host_id = "";
	private String title = "";
	private String natifycation_name="草莓电台";

	BroadcastReceiver onClickReceiver;
//	private boolean isAgain=false;//是否重新播放

	NotificationManager mNotificationManager;
	Notification mNotification;
	public static final int MUSIC_NOTIFY_ID = 1566;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		register();
		registerBroadcastReceiver();
		registerNotification();
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent == null) {
			return super.onStartCommand(intent, flags, startId);
		}

		title = intent.getStringExtra("title") + ""; // 歌曲名称
		host_id = intent.getStringExtra("host_id") + ""; // 主播id
		is_play = intent.getBooleanExtra("is_play", false);
		natifycation_name=intent.getStringExtra("notifycation_name");
		changeNotifitytitle(title, natifycation_name,is_play);
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		mNotificationManager.cancel(MUSIC_NOTIFY_ID);
		unregisterReceiver(onClickReceiver);
		super.onDestroy();
	}

	private void registerBroadcastReceiver() {
		// TODO Auto-generated method stub
		IntentFilter filter = new IntentFilter();
		filter.addAction(CLICK_OPEN_ACTION);
		filter.addAction(CLICK_PAUSE_ACTION);
		filter.addAction(CLICK_CLOSE_ACTION);
		filter.addAction(PLAY_FINISH_ACTION);
		registerReceiver(onClickReceiver, filter);
	}

	// 播放音乐的通知栏
	@SuppressWarnings("deprecation")
	private void registerNotification() {

		String ns = Context.NOTIFICATION_SERVICE;// 注册广播
		mNotificationManager = (NotificationManager) this.getSystemService(ns);
		mNotification = new Notification(android.R.drawable.ic_media_play,
				"海豚电台", System.currentTimeMillis());
		mNotification.contentView = new RemoteViews(this.getPackageName(),
				R.layout.myradio_notification_layout);
		mNotification.contentView.setTextViewText(
				R.id.myradio_notification_layout_title, "草莓电台");
		mNotification.contentView.setImageViewResource(
				R.id.myradio_notification_layout_open,
				R.drawable.icon_notify_open);
		Intent intent2 = new Intent(CLICK_CLOSE_ACTION);
		PendingIntent pi2 = PendingIntent.getBroadcast(this, 0, intent2, 0);
		mNotification.contentView.setOnClickPendingIntent(
				R.id.myradio_notification_close, pi2);
		mNotification.flags = Notification.FLAG_NO_CLEAR;
	}

	// 通知改变方法
	private void changeNotifitytitle(String title,String contentTitle, boolean isopen) {
		Intent intent = null;
		PendingIntent pi = null;
		mNotification.contentView.setTextViewText(
				R.id.myradio_notification_layout_title, title);
			mNotification.contentView.setTextViewText(
					R.id.myradio_notification_layout_contentTitle, contentTitle);
		if (isopen) {
			mNotification.contentView.setImageViewResource(
					R.id.myradio_notification_layout_open,
					R.drawable.icon_notify_pause);
		} else {
			mNotification.contentView.setImageViewResource(
					R.id.myradio_notification_layout_open,
					R.drawable.icon_notify_open);
		}
		intent = new Intent(CLICK_OPEN_ACTION);
		pi = PendingIntent.getBroadcast(this, 0, intent, 0);
		mNotification.contentView.setOnClickPendingIntent(
				R.id.myradio_notification_layout_open, pi);

		//Intent intent_onclick_notify = new Intent(this, RadioPlayActivity.class);
		Intent intent_onclick_notify = new Intent();
		
		intent_onclick_notify.putExtra("host_id", host_id);
		intent_onclick_notify.putExtra("is_notify", "1");// 判断是点击通知栏进入Myradio，可以不用默认播放第一首
		PendingIntent pi_onclick_notify = PendingIntent.getActivity(this, 0,
				intent_onclick_notify, PendingIntent.FLAG_UPDATE_CURRENT);
		mNotification.contentIntent = pi_onclick_notify;
		mNotificationManager.notify(MUSIC_NOTIFY_ID, mNotification);

	}

	/**
	 * 广播注册
	 */
	public void register() {
		onClickReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if (action.equals(CLICK_OPEN_ACTION)) {
					// 在这里处理点击事件
					open_pause();
					mNotificationManager.notify(MUSIC_NOTIFY_ID, mNotification);
					Log.i("MyradioNotifyService", "点击了：CLICK_OPEN_ACTION");
				} else if (action.equals(CLICK_PAUSE_ACTION)) {
					
				} else if (action.equals(CLICK_CLOSE_ACTION)) {
					stopMyradio();
					Log.i("MyradioNotifyService", "点击了：CLICK_OPEN_ACTION");
				} else if(action.equals(PLAY_FINISH_ACTION)){
//					isAgain=true;
					is_play=false;
					
				}
			}

		};
	}
	
	private void play() {
		
		
	}

	private void open_pause() {

		if (is_play) {
			is_play = false;
			changeNotifitytitle(title, natifycation_name,is_play);
			MyApplication.myradio_music_is_open = false;
			Intent intent = new Intent();
			intent.setAction("com.myradio.media.MUSIC_SERVICE");
			intent.setPackage("com.example.strawberryradio");
			intent.putExtra("MSG", PlayButton.PlayerMsg.PAUSE_MSG);
			startService(intent);

			// 发送广播，将被Activity组件中的BroadcastReceiver接收到
			Intent sendIntent = new Intent(OPEN_BTN_CHANGE);
			sendIntent.putExtra("is_open", false);
			sendBroadcast(sendIntent);

		} else {
			is_play = true;
			changeNotifitytitle(title, natifycation_name,is_play);
			MyApplication.myradio_music_is_open = true;
			Intent intent = new Intent();
			intent.setAction("com.myradio.media.MUSIC_SERVICE");
			intent.setPackage("com.example.strawberryradio");
			intent.putExtra("MSG", PlayButton.PlayerMsg.CONTINUE_MSG);
//			intent.putExtra("isAgain", isAgain);
//			intent.putExtra("isFromNotify", true);
			startService(intent);

			// 发送广播，将被Activity组件中的BroadcastReceiver接收到
			Intent sendIntent = new Intent(OPEN_BTN_CHANGE);
			sendIntent.putExtra("is_open", true);
			sendBroadcast(sendIntent);
//			isAgain=false;
		}
	}

	private void stopMyradio() {
		MyApplication.myradio_music_is_open = false;
		// 5.0后服务要以显示的方式启动
		Intent intent = new Intent();
		intent.setAction("com.myradio.media.MUSIC_SERVICE");
		intent.setPackage("com.example.strawberryradio");
		stopService(intent);
		if (mNotificationManager != null) {
			mNotificationManager.cancel(MUSIC_NOTIFY_ID);
		}
	}

}
