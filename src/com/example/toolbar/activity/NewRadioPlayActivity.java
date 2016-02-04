package com.example.toolbar.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.example.strawberryradio.R;
import com.example.toolbar.adapter.PlayRadioAdapter;
import com.example.toolbar.application.MyApplication;
import com.example.toolbar.bean.Click_Ranking;
import com.example.toolbar.bean.DownloadedBean;
import com.example.toolbar.bean.Favorite;
import com.example.toolbar.bean.Hot;
import com.example.toolbar.bean.PlayInfo;
import com.example.toolbar.bean.ProgramClassifyListBean;
import com.example.toolbar.bean.ProgramListBean;
import com.example.toolbar.bean.Top;
import com.example.toolbar.bean.ProgramListBean.ProgramListInfo;
import com.example.toolbar.common.utils.ImageLoaderHelper;
import com.example.toolbar.common.utils.LogHelper;
import com.example.toolbar.common.utils.NetUtil;
import com.example.toolbar.entity.PlayButton;
import com.example.toolbar.fragment.NewFirstFragment;
import com.example.toolbar.utils.ConfigUtils;
import com.example.toolbar.utils.MediaUtil;
import com.example.toolbar.utils.SharePreferenceUtil;
import com.example.toolbar.view.MyVisualizerView;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.audiofx.Visualizer;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class NewRadioPlayActivity extends FragmentActivity {
//	private TextView channel;// 频道
//	private TextView host;// 主播
//	private TextView musicTitle; // 正在播放的音乐名
//	private TextView endtimeTime;// 当前播放曲目的总时长
//	private TextView currentTimeProgress; // 当前播放曲目的播放时间
//	private ImageView previousIV; // 上一首
//	private ImageView playIV; // 播放
//	private ImageView nextIV; // 下一首
//	private ImageView volumeIV; // 音量
//	private ImageView back;
//
//	// 分享
//	private ImageView imageViewFenxiang;
//
//	private String TAG = "RadioPlayActivity";
//	private String titleMusicString = ""; // 歌曲标题
//	private String host_name = "";// 主播名
//	private String host_id = "";// 主播id
//	private String group_id = "";// 主播群id
//	private String group_tag = "";// 主播群tag,推送标志位用到
//	private String music_id = "";
//	private String music_url = "";
//	private String music_title = "";
//	private String program_id;// 当前节目ID
//	private String uid;
//	private String id;// 网络请求所需的节目id==当前节目ID
//	private String notifycation_name="草莓电台";
//
//	private int listPosition = -1; // 播放歌曲的位置
//	private int currentTime; // 当前歌曲播放时间
//
//	private boolean IS_PLAY = true;// 是否正在播放
//	private boolean isLocad = false;// 是否本地资源播放
//
//	private NewPlayerReceiver playerReceiver;
//
//	public static final String UPDATE_ACTION = "com.myradio.action.UPDATE_ACTION"; // 更新动作
//	public static final String CTL_ACTION = "com.myradio.action.CTL_ACTION"; // 控制动作
//	public static final String MUSIC_CURRENT = "com.myradio.action.MUSIC_CURRENT"; // 音乐当前时间改变动作
//	public static final String MUSIC_DURATION = "com.myradio.action.MUSIC_DURATION";// 音乐播放长度改变动作
//	public static final String MUSIC_PLAYING = "com.myradio.action.MUSIC_PLAYING"; // 音乐正在播放动作
//	public static final String REPEAT_ACTION = "com.myradio.action.REPEAT_ACTION"; // 音乐重复播放动作
//	public static final String SHUFFLE_ACTION = "com.myradio.action.SHUFFLE_ACTION";// 音乐随机播放动作
//	public static final String SHOW_LRC = "com.myradio.action.SHOW_LRC"; // 通知显示歌词
//	public static final String MUSIC_FINISH = "com.myradio.action.MUSIC_FINISH";// 音乐播放完
//	public static final String OPEN_BTN_CHANGE = "com.myradio.vlook.action.OPEN_BTN_CHANGE";// 音乐播放按钮的更新
//
//	private SharePreferenceUtil mSharePreferenceUtil;
//	private Bundle bundle;// 歌曲全部信息传递
//	private List<PlayInfo> mList;// 歌曲列表信息
//	private PlayInfo playdatas;// 单首歌曲信息
//	private List<Favorite> favorite;// 已收藏的节目信息
//	private List<String> idList;// 已收藏的节目的id集
//	private boolean isCollect = false;
//	// private Animation mAnimation;
//	private boolean isVisibility = false;
//	private PlayRadioAdapter adapter;
//
//	private ImageLoader mImageLoader;
//	
//	private MyVisualizerView mMyVisualizerView;
//	private byte[] audio={8,9,10,12,11,5,15,7,13,10,9,6,8,12,16,13,18,9,17,8,9,16,
//			16,13,15,8,6,9,12,8,6,6,11,18,16,14};
//	private byte[] mData = new byte[20];
//    private int j=0;
//    private Timer timer;
//    private TimerTask task;
//	private Handler handler=new Handler(){
//
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			mMyVisualizerView.updateVisualizer(mData);
//		}
//		
//	};

	private FragmentManager fm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_radio_play);
		fm=getSupportFragmentManager();
		Fragment mNewFirstFragment=new NewFirstFragment();
		fm.beginTransaction().replace(R.id.new_radio_play_framelayout, mNewFirstFragment).commit();
//		registerReceiver();
//		initView();
//		initHeadActionBar();
//		myOnClickLisenter();
//		initData();
	}

//	@Override
//	protected void onDestroy() {
//		if (playerReceiver != null) {
//			unregisterReceiver(playerReceiver);
//		}
//
//		if (!titleMusicString.isEmpty()) {
//			mSharePreferenceUtil = new SharePreferenceUtil(this,
//					ConfigUtils.appSharePreferenceName);
//			mSharePreferenceUtil.setMyradioMusicTitle(titleMusicString);
//			mSharePreferenceUtil.setMyradioMusicID("");
//			mSharePreferenceUtil.setMyradioMusicUrl("");
//		}
//
//		MyApplication.host_id = host_id;
//		super.onDestroy();
//	}
//
//	@Override
//	public void initView() {
//		super.initView();
//		mMyVisualizerView = (MyVisualizerView) findViewById(R.id.new_main_audio);
//		findViewById(R.id.more_frist).setVisibility(View.GONE);
//		back=(ImageView)findViewById(R.id.Silding_menu);
//		back.setImageResource(R.drawable.back_new_bg);
//		channel = (TextView) findViewById(R.id.new_main_channel);
//		musicTitle = (TextView) findViewById(R.id.new_main_program_name);
//		host = (TextView) findViewById(R.id.new_main_host);
//		endtimeTime = (TextView) findViewById(R.id.new_main_total_time);
//		currentTimeProgress = (TextView) findViewById(R.id.new_main_current_time);
//		previousIV = (ImageView) findViewById(R.id.new_main_previous);
//		playIV = (ImageView) findViewById(R.id.new_main_play);
//		nextIV = (ImageView) findViewById(R.id.new_main_next);
//	}
//
//	// 初始化音乐播放监听广播
//	private void registerReceiver() {
//		// 定义和注册广播接收器
//		// LogHelper.e("registerReceiver");
//		playerReceiver = new NewPlayerReceiver();
//		IntentFilter filter = new IntentFilter();
//		filter.addAction(UPDATE_ACTION);
//		filter.addAction(MUSIC_CURRENT);
//		filter.addAction(MUSIC_DURATION);
//		filter.addAction(MUSIC_FINISH);
//		filter.addAction(OPEN_BTN_CHANGE);
//		registerReceiver(playerReceiver, filter);
//	}
//
//	public void initHeadActionBar() {
////		super.initHeadActionBar();
////		head_action_title.setText(R.string.app_name);
//		mList = new ArrayList<PlayInfo>();
//		if (getIntent().getBundleExtra("bundle") != null) {
//			bundle = getIntent().getBundleExtra("bundle");
//			listPosition = bundle.getInt("position");
//			if (bundle.getString("type") != null) {
//				if (bundle.getString("type").equals("hot")) {
//					List<Hot> list = bundle
//							.getParcelableArrayList("music_list");
//
//					for (Hot map : list) {
//						PlayInfo playInfo = new PlayInfo(map.getId(),
//								map.getTitle(), "", map.getPath(), "", "", "",
//								"", map.getThumb(), "", "", "");
//						mList.add(playInfo);
//					}
//
//				} else if (bundle.getString("type").equals("ranking")) {
//					List<Click_Ranking> list = bundle
//							.getParcelableArrayList("music_list");
//
//					for (Click_Ranking map : list) {
//						PlayInfo playInfo = new PlayInfo(map.getId(),
//								map.getTitle(), "", map.getPath(), "", "", "",
//								"", map.getThumb(), "", "", "");
//						mList.add(playInfo);
//					}
//				} else if (bundle.getString("type").equals("top")) {
//					List<Top> list = bundle
//							.getParcelableArrayList("music_list");
//					for (Top map : list) {
//						PlayInfo playInfo = new PlayInfo(map.getId(),
//								map.getTitle(), "", map.getPath(), "", "", "",
//								"", map.getThumb(), "", "", "");
//						mList.add(playInfo);
//					}
//				}
//			} else if (bundle.getSerializable("ProgramListBean") != null) {// 接收节目单详情的数据
//				ProgramListBean bean = (ProgramListBean) bundle
//						.getSerializable("ProgramListBean");
//				if (bean != null) {
//					notifycation_name=bean.row.programme_name;
//					ArrayList<ProgramListInfo> list = bean.list;
//					for (ProgramListInfo info : list) {
//						PlayInfo playInfo = new PlayInfo(info.id, info.title,
//								"", info.path, "", "", "", "", info.thumb, "",
//								"", info.nickname);
//						mList.add(playInfo);
//					}
//				}
//			} else if (bundle.getString("favorite") != null) {// 取得我的收藏的数据
//				PlayInfo playInfo = new PlayInfo(bundle.getString("id"),
//						bundle.getString("title"), "",
//						bundle.getString("path"), "", "", "", "",
//						bundle.getString("thumb"), "", "", "");
//				mList.add(playInfo);
//				listPosition = 0;
//
//			} else if (bundle.getSerializable("downloadedBean") != null) {
//				DownloadedBean downloadedBean = (DownloadedBean) bundle
//						.getSerializable("downloadedBean");
//				PlayInfo playInfo = new PlayInfo(downloadedBean.getProgramId(),
//						downloadedBean.getName(), "",
//						downloadedBean.getStoragePath(), "", "", "", "",
//						downloadedBean.getThumb(), "", "", "");
//				isLocad = true;
//				mList.add(playInfo);
//				listPosition = 0;
//			} else if(bundle.getParcelable("ProgramClassifyBean") !=null){
//				ProgramClassifyListBean bean=bundle.getParcelable("ProgramClassifyBean");
//				for(int i=0;i<bean.list.size();i++){
//					PlayInfo playInfo = new PlayInfo(bean.list.get(i).id,
//							bean.list.get(i).title, "",
//							bean.list.get(i).path, "", "", "", "",
//							bean.list.get(i).thumb, "", "", bean.list.get(i).owner);
//					mList.add(playInfo);
//				}
//			}
//
//		} else if (getIntent().getStringExtra("path") != null) {
//			// if (!IS_PLAY) {
//
//			listPosition = 0;
//			PlayInfo playInfo = new PlayInfo("", getIntent().getStringExtra(
//					"title"), "", getIntent().getStringExtra("path"), "", "",
//					"", "", getIntent().getStringExtra("thumb"), "", "", "");
//			mList.add(playInfo);
//			previousIV.setVisibility(View.GONE);
//			nextIV.setVisibility(View.GONE);
//			// }
//			LogHelper.e("Radio:" + getIntent().getStringExtra("path"));
//		} else {
//
//			listPosition = 0;
//			PlayInfo playInfo = new PlayInfo(
//					"",
//					"番茄吃炒蛋",
//					"",
//					"http://vroad.bbrtv.com/cmradio/uploads/file/20150707/20150707053311_35900.mp3",
//					"",
//					"",
//					"",
//					"",
//					"http://vroad.bbrtv.com/cmradio/uploads/file/20150925/20150925172201_40959.jpg",
//					"", "", "");
//			mList.add(playInfo);
//			previousIV.setVisibility(View.GONE);
//			nextIV.setVisibility(View.GONE);
//			// toast("获取数据异常，请再次尝试！");
//			// finish();
//		}
//		//
//
//	}
//
//	private void myOnClickLisenter() {
//		previousIV.setOnClickListener(this);
//		playIV.setOnClickListener(this);
//		nextIV.setOnClickListener(this);
//		back.setOnClickListener(this);
//	}
//
//	private void initData() {
//		mImageLoader = ImageLoader.getInstance();
//		idList = new ArrayList<String>();
//		LogHelper.e("listPosition:" + listPosition + "size:" + mList.size());
//
//		if (listPosition < 0 && mList.size() > 0) {
//			toast("无法获取曲目信息！");
//			return;
//		}
//		Log.i(TAG, "mList长度----->" + mList.size()
//				+ "===========listPosition值------>" + listPosition);
//		playdatas = mList.get(listPosition);
//		titleMusicString = playdatas.getTitle();
//		if(playdatas.getOwner().equals(null)){
//			host_name = playdatas.getOwner();
//		}else{
//			host_name = "";
//		}
//		host_id = playdatas.getId();
//		group_id = "";
//		program_id = playdatas.getId();
//		music_id = playdatas.getId();
//		music_url = playdatas.getPath();
//		music_title = playdatas.getTitle();
//		musicTitle.setText(music_title);
//		SavePlayInfo(music_title, host_name);
//		// LogHelper.e("music_url:" +music_url +"/nmusic_title" + music_title +
//		// "/nthumb" + playdatas.getThumb());
//		// mImageLoader.displayImage(playdatas.getThumb(), mHeadImageView,
//		// ImageLoaderHelper.getDisplayImageUnDefaultOptions());
//		// setMusicThumb(playdatas.getThumb());
//		 firstEnter();
//		// Progress.setVisibility(View.GONE);
//		// initCollect();
//	}
//
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.new_main_previous:
//			previous_music();
//			break;
//		case R.id.new_main_play:
//			open_pause();
//			break;
//		case R.id.new_main_next:
//			next_music();
//			break;
//		case R.id.Silding_menu:
//			finish();
//			break;
//		default:
//			break;
//		}
//	}
//
//	// 是否进入播放音乐
//	private void firstEnter() {
//
//		if (NetUtil.getConnectedType(this) == ConnectivityManager.TYPE_WIFI
//				&& getIntent().getStringExtra("is_notify") == null
//				&& getIntent().getStringExtra("userinfo_shoucang_id") == null) {
//			play();
//		}
//	}
//
//	/**
//	 * 上一首
//	 */
//	public void previous_music() {
//		if (mList.size() == 0) {
//			toast("暂无歌曲播放");
//			return;
//		}
//
//		IS_PLAY = true;
//		playIV.setImageResource(R.drawable.play_pause_grey);
//		// openMusicAnimation();
//		if (listPosition < 0 && mList.size() > 0) { // 如果是第一次进入播放器
//			listPosition = 0;
//			playdatas = mList.get(listPosition);
//			if(playdatas.getOwner().equals(null)){
//				host_name = playdatas.getOwner();
//			}else{
//				host_name = "";
//			}
//			program_id = playdatas.getId();
//			music_id = playdatas.getId();
//			music_url = playdatas.getPath();
//			music_title = playdatas.getTitle();
//			play();
//			// musicRating.setRating(Integer.valueOf(map.get("playtimes")));
//			uploadMusicProgram(playdatas);
//			return;
//		}
//
//		listPosition = listPosition - 1;
//		if (listPosition >= 0) {
//			playdatas = mList.get(listPosition);
//			titleMusicString = playdatas.getTitle();
//			if(playdatas.getOwner().equals(null)){
//				host_name = playdatas.getOwner();
//			}else{
//				host_name = "";
//			}
//			host_id = playdatas.getId();
//			group_id = "";
//			program_id = playdatas.getId();
//			music_id = playdatas.getId();
//			music_url = playdatas.getPath();
//			music_title = playdatas.getTitle();
//			SavePlayInfo(music_title, host_name);
//			cancelTimer();
//			// music_url = map.get("path").toString();
//			// music_id = map.get("id").toString();
//			Intent intent = new Intent();
//			intent.setAction("com.myradio.media.MUSIC_SERVICE");
//			intent.setPackage("com.example.strawberryradio");
//			intent.putExtra("url", music_url);
//			intent.putExtra("listPosition", listPosition);
//			intent.putExtra("MSG", PlayButton.PlayerMsg.PLAY_MSG);
//			startService(intent);
//			uploadMusicProgram(playdatas);
//		} else {
//			listPosition = 0;
//			Toast.makeText(NewRadioPlayActivity.this, "没有上一首了",
//					Toast.LENGTH_SHORT).show();
//		}
//	}
//
//	/**
//	 * 下一首
//	 */
//	public void next_music() {
//
//		if (mList.size() == 0) {
//			toast("暂无歌曲播放");
//			return;
//		}
//
//		IS_PLAY = true;
//		playIV.setImageResource(R.drawable.play_pause_grey);
//		// openMusicAnimation();
//		if (listPosition < 0 && mList.size() > 0) {// 如果是第一次进入播放器
//			listPosition = 0;
//			play();
//			// musicRating.setRating(Integer.valueOf(map.get("playtimes")));
//			return;
//		}
//
//		listPosition = listPosition + 1;
//		if (listPosition <= mList.size() - 1) {
//			playdatas = mList.get(listPosition);
//			titleMusicString = playdatas.getTitle();
//			if(playdatas.getOwner().equals(null)){
//				host_name = playdatas.getOwner();
//			}else{
//				host_name = "";
//			}
//			host_id = playdatas.getId();
//			group_id = "";
//			program_id = playdatas.getId();
//			music_id = playdatas.getId();
//			music_url = playdatas.getPath();
//			music_title = playdatas.getTitle();
//			SavePlayInfo(music_title, host_name);
//			cancelTimer();
//			Intent intent = new Intent();
//			intent.setAction("com.myradio.media.MUSIC_SERVICE");
//			intent.setPackage("com.example.strawberryradio");
//			intent.putExtra("url", music_url);
//			intent.putExtra("listPosition", listPosition);
//			intent.putExtra("MSG", PlayButton.PlayerMsg.PLAY_MSG);
//			startService(intent);
//			uploadMusicProgram(playdatas);
//
//		} else {
//			listPosition = mList.size() - 1;
//			Toast.makeText(NewRadioPlayActivity.this, "没有下一首了",
//					Toast.LENGTH_SHORT).show();
//		}
//	}
//
//	// 播放音乐节目单改动
//	private void uploadMusicProgram(PlayInfo data) {
//		// musicSeekBar.setProgress(0);
//		musicTitle.setText(data.getTitle());
//		music_id = data.getId();
//		music_title = data.getTitle();
//		music_url = data.getPath();
//		// ImageLoader.getInstance().displayImage(data.get("thumb"),
//		// musicAnimationIV, ImageLoaderHelper.getDisplayImageOptions());
//		// mImageLoader.displayImage(data.getThumb(), mHeadImageView,
//		// ImageLoaderHelper.getDisplayImageUnDefaultOptions());
//		// setMusicThumb(data.getThumb());
//		openMusicAnimation();
//
//	}
//
//	/**
//	 * 播放音乐
//	 */
//	public void play() {
//		// 开始播放的时候为顺序播放
//
//		if (listPosition < 0 || listPosition > mList.size()) {
//			toast("暂无歌曲播放");
//			return;
//		}
//
//		IS_PLAY = true;
//		SavePlayInfo(music_title, host_name);
//		playIV.setImageResource(R.drawable.play_pause_grey);
//		Intent intent = new Intent();
//		intent.setAction("com.myradio.media.MUSIC_SERVICE");
//		intent.setPackage("com.example.strawberryradio");
//		intent.putExtra("url", music_url);
//		intent.putExtra("listPosition", listPosition);
//		intent.putExtra("isLocad", isLocad);
//		intent.putExtra("MSG", PlayButton.PlayerMsg.PLAY_MSG);
//
//		startService(intent);
//		openMusicAnimation();
//	}
//	
//	private void SavePlayInfo(String programName,String host){
//		MyApplication.getInstance().getSpUtil().setPalyProgramInfo(programName, host);
//	}
//
//	private void open_pause() {
//
//		if (IS_PLAY) {
//			IS_PLAY = false;
//			playIV.setImageResource(R.drawable.play_grey);
//			Intent intent = new Intent();
//			intent.setAction("com.myradio.media.MUSIC_SERVICE");
//			intent.setPackage("com.example.strawberryradio");
//			intent.putExtra("MSG", PlayButton.PlayerMsg.PAUSE_MSG);
//			startService(intent);
//			cancelTimer();
//		} else {
//
//			if (listPosition < 0 || mList.size() == 0) {
//				toast("暂无歌曲播放");
//				return;
//			} else if (listPosition < 0 && mList.size() > 0) {
//				listPosition = 0;
//			}
//			IS_PLAY = true;
//			playIV.setImageResource(R.drawable.play_pause_grey);
//			Intent intent = new Intent();
//			intent.setAction("com.myradio.media.MUSIC_SERVICE");
//			intent.setPackage("com.example.strawberryradio");
//			intent.putExtra("url", music_url);
//			intent.putExtra("MSG", PlayButton.PlayerMsg.CONTINUE_MSG);
//			startService(intent);
//			startTimer();
//		}
//		openMusicAnimation();
//
//	}
//
//	// 播放图片动画
//	private void openMusicAnimation() {
//
//		if (IS_PLAY) {
//			// mHeadImageView.startAnimation(mAnimation);
//			MyApplication.myradio_music_is_open = true;
//			playIV.setImageResource(R.drawable.play_pause_grey);
//			changeNotifitytitle(titleMusicString, true);
//		} else {
//			// mHeadImageView.clearAnimation();
//			MyApplication.myradio_music_is_open = false;
//			playIV.setImageResource(R.drawable.play_grey);
//			changeNotifitytitle(titleMusicString, false);
//		}
//	}
//
//	// 通知改变方法
//	private void changeNotifitytitle(String title, boolean isopen) {
//		// LogHelper.e("changeNotifitytitle" + title);
//		Intent intent = new Intent();
//		intent.setAction("com.myradio.media.myradio_notifyservice");
//		intent.setPackage("com.example.strawberryradio");
//		intent.putExtra("title", title);
//		intent.putExtra("is_play", isopen);
//		intent.putExtra("host_id", host_id);
//		intent.putExtra("notifycation_name", notifycation_name);
//		startService(intent);
//	}
//	
//
//	public class NewPlayerReceiver extends BroadcastReceiver {
//
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			String action = intent.getAction();
//			if (action.equals(MUSIC_CURRENT)) {
//				currentTime = intent.getIntExtra("currentTime", -1);
//				currentTimeProgress.setText(MediaUtil.formatTime(currentTime));
//				int duration = intent.getIntExtra("duration", -1);
//				// musicSeekBar.setMax(duration);
//				// musicSeekBar.setProgress(currentTime);
//				endtimeTime.setText(MediaUtil.formatTime(duration));
//				
//			} else if (action.equals(MUSIC_DURATION)) {
//				int duration = intent.getIntExtra("duration", -1);
//				// musicSeekBar.setMax(duration);
//				endtimeTime.setText(MediaUtil.formatTime(duration));
//				boolean isPlay=intent.getBooleanExtra("isPlay", false);
//				if(isPlay){
//					startTimer();
//				}
//			} else if (action.equals(UPDATE_ACTION)) {
//				// 获取Intent中的current消息，current代表当前正在播放的歌曲
//				// listPosition = intent.getIntExtra("current", -1);
//				// url = mp3Infos.get(listPosition).getUrl();
//				// if (listPosition >= 0) {
//				// musicTitle.setText(mp3Infos.get(listPosition).getTitle());
//				// musicArtist.setText(mp3Infos.get(listPosition).getArtist());
//				// }
//				// if (listPosition == 0) {
//				// finalProgress.setText(MediaUtil.formatTime(mp3Infos.get(
//				// listPosition).getDuration()));
//				// playBtn.setBackgroundResource(R.drawable.pause_selector);
//				// isPause = true;
//				// }
//			} else if (action.equals(MUSIC_FINISH)) {
//				// if (is_music_program_listen_all) {
//				// //next_music();
//				// LogHelper.e("is_isiable_music_program");
//				// } else {
//				// IS_PLAY = false;
//				// //resetMusic();
//				// }
//				IS_PLAY = false;
//				Log.i(TAG, "MUSIC_FINISH");
//				cancelTimer();
//				// resetMusic();
//			} else if (action.equals(OPEN_BTN_CHANGE)) {
//				boolean is_open = intent.getBooleanExtra("is_open", true);
//				if (is_open) {// 正在播放
//					
//					playIV.setImageResource(R.drawable.play_pause_grey);
//					// mHeadImageView.startAnimation(mAnimation);
//				} else {// 暂停播放
//					playIV.setImageResource(R.drawable.play_grey);
//					// mHeadImageView.clearAnimation();
//				}
//			}
//		}
//	}
//	
//	private void startTimer(){
//		if(timer==null){
//			timer=new Timer();
//		}
//		if(task==null){
//			task=new TimerTask() {
//				
//				@Override
//				public void run() {
//					int k=j;
//					for(int i=0;i<20;i++){
//						mData[i]=audio[k];
//						k++;
//						if(k==(audio.length-1)){
//							k=0;
//						}
//					}
//					j++;
//					if(j==(audio.length-1)){
//						j=0;
//					}
//					handler.sendEmptyMessage(1);
//				}
//			};
//		}
//		if(timer!=null && task!=null){
//			timer.schedule(task, 1000, 200);
//		}
//	}
//	
//	private void cancelTimer(){
//		if(timer!=null){
//			timer.cancel();
//			timer=null;
//		}
//		if(task!=null){
//			task.cancel();
//			task=null;
//		}
//	}
	
}
