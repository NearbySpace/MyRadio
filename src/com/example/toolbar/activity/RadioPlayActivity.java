package com.example.toolbar.activity;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.http.Header;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dolphinradio.R;
import com.example.toolbar.adapter.PlayRadioAdapter;
import com.example.toolbar.application.MyApplication;
import com.example.toolbar.bean.Click_Ranking;
import com.example.toolbar.bean.DownloadEntry;
import com.example.toolbar.bean.DownloadedBean;
import com.example.toolbar.bean.Favorite;
import com.example.toolbar.bean.Hot;
import com.example.toolbar.bean.PlayInfo;
import com.example.toolbar.bean.ProgramListBean;
import com.example.toolbar.bean.ProgramListBean.ProgramListInfo;
import com.example.toolbar.bean.Top;
import com.example.toolbar.common.utils.Common;
import com.example.toolbar.common.utils.ImageLoaderHelper;
import com.example.toolbar.common.utils.LogHelper;
import com.example.toolbar.common.utils.NetUtil;
import com.example.toolbar.db.DBUtil;
import com.example.toolbar.download.DownloadManager;
import com.example.toolbar.entity.PlayButton;
import com.example.toolbar.framework.CircleImageView;
import com.example.toolbar.http.HttpManage;
import com.example.toolbar.http.HttpManage.OnCallBack;
import com.example.toolbar.utils.ConfigUtils;
import com.example.toolbar.utils.ImageUtils;
import com.example.toolbar.utils.MediaUtil;
import com.example.toolbar.utils.SharePreferenceUtil;
import com.example.toolbar.view.progress.CircularProgress;
import com.example.toolbar.widget.SystemBarTintManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 
 * @author 播放界面
 * 
 */
public class RadioPlayActivity extends BaseActivity {
	@ViewInject(R.id.progress)
	private CircularProgress Progress;

	@ViewInject(R.id.activity_myradio_detial_tv_title)
	private TextView musicTitle; // 正在播放的音乐名

	@ViewInject(R.id.refresh)
	private RelativeLayout refresh;

	@ViewInject(R.id.activity_myradio_detial_tt_currenttime)
	private TextView currentTimeProgress; // 当前播放曲目的播放时间

	@ViewInject(R.id.activity_myradio_detial_tt_endtime)
	private TextView endtimeTime; // 当前播放曲目的总时长

	@ViewInject(R.id.activity_myradio_detial_iv_head)
	private CircleImageView mHeadImageView;// 头部的图片

	@ViewInject(R.id.activity_myradio_detial_iv_bot_message)
	private ImageView messageIV; // 私聊

	@ViewInject(R.id.activity_myradio_detial_iv_bot_previous)
	private ImageView previousIV; // 上一首

	@ViewInject(R.id.activity_myradio_detial_iv_bot_open)
	private ImageView playIV; // 播放

	@ViewInject(R.id.activity_myradio_detial_iv_bot_next)
	private ImageView nextIV; // 下一首

	@ViewInject(R.id.activity_myradio_detial_iv_bot_volume)
	private ImageView volumeIV; // 音量

	@ViewInject(R.id.activity_myradio_detial_seekBar_music)
	private SeekBar musicSeekBar;// 进度条

	@ViewInject(R.id.iv_down_radioplay)
	private ImageView download;// 下载

	@ViewInject(R.id.play_radio_imageView2)
	private ImageView imageView2;

	@ViewInject(R.id.like)
	// 收藏
	private ImageView imageViewLike;

	@ViewInject(R.id.fenxiang)
	// 分享
	private ImageView imageViewFenxiang;

	// @ViewInject(R.id.play_radio_lv)
	// private ListView play_radio_lv;

	private String TAG = "RadioPlayActivity";
	private String titleMusicString = ""; // 歌曲标题
	private String host_name = "";// 主播名
	private String host_id = "";// 主播id
	private String group_id = "";// 主播群id
	private String group_tag = "";// 主播群tag,推送标志位用到
	private String music_id = "";
	private String music_url = "";
	private String music_title = "";
	private String program_id;// 当前节目ID
	private String uid;
	private String id;// 网络请求所需的节目id==当前节目ID

	private int listPosition = -1; // 播放歌曲的位置
	private int currentTime; // 当前歌曲播放时间

	private boolean IS_PLAY = true;// 是否正在播放
	private boolean isLocad =false;//是否本地资源播放

	private PlayerReceiver playerReceiver;

	public static final String UPDATE_ACTION = "com.myradio.action.UPDATE_ACTION"; // 更新动作
	public static final String CTL_ACTION = "com.myradio.action.CTL_ACTION"; // 控制动作
	public static final String MUSIC_CURRENT = "com.myradio.action.MUSIC_CURRENT"; // 音乐当前时间改变动作
	public static final String MUSIC_DURATION = "com.myradio.action.MUSIC_DURATION";// 音乐播放长度改变动作
	public static final String MUSIC_PLAYING = "com.myradio.action.MUSIC_PLAYING"; // 音乐正在播放动作
	public static final String REPEAT_ACTION = "com.myradio.action.REPEAT_ACTION"; // 音乐重复播放动作
	public static final String SHUFFLE_ACTION = "com.myradio.action.SHUFFLE_ACTION";// 音乐随机播放动作
	public static final String SHOW_LRC = "com.myradio.action.SHOW_LRC"; // 通知显示歌词
	public static final String MUSIC_FINISH = "com.myradio.action.MUSIC_FINISH";// 音乐播放完
	public static final String OPEN_BTN_CHANGE = "com.myradio.vlook.action.OPEN_BTN_CHANGE";// 音乐播放按钮的更新

	private SharePreferenceUtil mSharePreferenceUtil;
	private Bundle bundle;// 歌曲全部信息传递
	private List<PlayInfo> mList;// 歌曲列表信息
	private PlayInfo playdatas;// 单首歌曲信息
	private List<Favorite> favorite;// 已收藏的节目信息
	private List<String> idList;// 已收藏的节目的id集
	private boolean isCollect = false;
	// private Animation mAnimation;
	private boolean isVisibility = false;
	private PlayRadioAdapter adapter;

	private ImageLoader mImageLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_playradio);
		uid = MyApplication.getInstance().getSpUtil().getUid();
		ViewUtils.inject(this);// 通过反射，查找相应view的，并初始化
		isSdExist();
		initSystemBar();
		initHeadActionBar();
		myOnClickLisenter();
		initData();

	}

	/**
	 * 初始化下载路径 如果有SD卡则返回true，没有就返回false
	 */
	private boolean isSdExist() {

		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			File file = new File(ConfigUtils.SDcardPath);
			if (!file.exists()) {
				file.mkdir();
			}
			File downloadFile = new File(ConfigUtils.SDDownloadPath);
			if (!downloadFile.exists()) {
				downloadFile.mkdir();
			}
			return true;
		} else {
			// 没有SD卡则存在ROM内
			File downloadFile = RadioPlayActivity.this.getFilesDir();
			String path = downloadFile.getAbsolutePath() + "/Download/";
			File f = new File(path);
			if (!f.exists()) {
				f.mkdir();
			}
			return false;
		}
	}

	@SuppressLint("ResourceAsColor")
	private void initSystemBar() {
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setNavigationBarTintEnabled(true);
		tintManager.setTintColor(R.color.color_head);
		int pading = getStatusBarHeight();
		// refresh = (RelativeLayout) findViewById(R.id.refresh);
		// refresh.setPadding(0, pading, 0, 0);

	}

	// 获取状态栏高度
	public int getStatusBarHeight() {
		int result = 0;
		int resourceId = getResources().getIdentifier("status_bar_height",
				"dimen", "android");
		if (resourceId > 0) {
			result = getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		registerReceiver();
		IS_PLAY = MyApplication.myradio_music_is_open;
		openMusicAnimation();
		super.onResume();
	}

	@SuppressLint("NewApi")
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (playerReceiver != null) {
			unregisterReceiver(playerReceiver);
		}

		if (!titleMusicString.isEmpty()) {
			mSharePreferenceUtil = new SharePreferenceUtil(this,
					ConfigUtils.appSharePreferenceName);
			mSharePreferenceUtil.setMyradioMusicTitle(titleMusicString);
			mSharePreferenceUtil.setMyradioMusicID("");
			mSharePreferenceUtil.setMyradioMusicUrl("");
		}

		MyApplication.host_id = host_id;
		super.onDestroy();
	}

	// 初始化音乐播放监听广播
	private void registerReceiver() {
		// 定义和注册广播接收器
		// LogHelper.e("registerReceiver");
		playerReceiver = new PlayerReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(UPDATE_ACTION);
		filter.addAction(MUSIC_CURRENT);
		filter.addAction(MUSIC_DURATION);
		filter.addAction(MUSIC_FINISH);
		filter.addAction(OPEN_BTN_CHANGE);
		registerReceiver(playerReceiver, filter);
	}

	// 通知改变方法
	private void changeNotifitytitle(String title, boolean isopen) {
		// LogHelper.e("changeNotifitytitle" + title);
		Intent intent = new Intent();
		intent.setAction("com.myradio.media.myradio_notifyservice");
		intent.setPackage("com.example.dolphinradio");
		intent.putExtra("title", title);
		intent.putExtra("is_play", isopen);
		intent.putExtra("host_id", host_id);
		startService(intent);
	}

	public void initHeadActionBar() {
		super.initHeadActionBar();
		head_action_title.setText(R.string.app_name);
		mList = new ArrayList<PlayInfo>();
		if (getIntent().getBundleExtra("bundle") != null) {
			bundle = getIntent().getBundleExtra("bundle");
			listPosition = bundle.getInt("position");
			if (bundle.getString("type") != null) {
				if (bundle.getString("type").equals("hot")) {
					List<Hot> list = bundle
							.getParcelableArrayList("music_list");

					for (Hot map : list) {
						PlayInfo playInfo = new PlayInfo(map.getId(),
								map.getTitle(), "", map.getPath(), "", "", "",
								"", map.getThumb(), "", "", "","","");
						mList.add(playInfo);
					}

				} else if (bundle.getString("type").equals("ranking")) {
					List<Click_Ranking> list = bundle
							.getParcelableArrayList("music_list");

					for (Click_Ranking map : list) {
						PlayInfo playInfo = new PlayInfo(map.getId(),
								map.getTitle(), "", map.getPath(), "", "", "",
								"", map.getThumb(), "", "", "","","");
						mList.add(playInfo);
					}
				} else if (bundle.getString("type").equals("top")) {
					List<Top> list = bundle
							.getParcelableArrayList("music_list");
					for (Top map : list) {
						PlayInfo playInfo = new PlayInfo(map.getId(),
								map.getTitle(), "", map.getPath(), "", "", "",
								"", map.getThumb(), "", "", "","","");
						mList.add(playInfo);
					}
				}
			}else if (bundle.getSerializable("ProgramListBean") != null) {// 接收节目单详情的数据
				ProgramListBean bean = (ProgramListBean) bundle
						.getSerializable("ProgramListBean");
				if (bean != null) {
					ArrayList<ProgramListInfo> list = bean.list;
					for (ProgramListInfo info : list) {
						PlayInfo playInfo = new PlayInfo(info.program_id, info.title, "",
								info.path, "", "", "", "", info.thumb, "", "",
								info.nickname,"","");
						mList.add(playInfo);
					}
				}
			} else if (bundle.getString("favorite") != null) {// 取得我的收藏的数据
				PlayInfo playInfo = new PlayInfo(bundle.getString("id"),
						bundle.getString("title"), "", bundle.getString("path"),
						"", "", "", "", bundle.getString("thumb"), "", "", "","","");
				mList.add(playInfo);
				listPosition = 0;

			}else if(bundle.getSerializable("downloadedBean")!=null){
				DownloadedBean downloadedBean=(DownloadedBean) bundle.getSerializable("downloadedBean");
				PlayInfo playInfo = new PlayInfo(downloadedBean.getProgramId(), 
						downloadedBean.getName(), "", 
						downloadedBean.getStoragePath(), "", "", "", "", 
						downloadedBean.getThumb(), "", "", "","","");
				isLocad=true;
				mList.add(playInfo);
				listPosition=0;
			}  

		}else if (getIntent().getStringExtra("path") != null) {
			// if (!IS_PLAY) {

			listPosition = 0;
			PlayInfo playInfo = new PlayInfo("", getIntent().getStringExtra(
					"title"), "", getIntent().getStringExtra("path"), "", "",
					"", "", getIntent().getStringExtra("thumb"), "", "", "","","");
			mList.add(playInfo);
			previousIV.setVisibility(View.GONE);
			nextIV.setVisibility(View.GONE);
			// }
			LogHelper.e("Radio:" + getIntent().getStringExtra("path"));
		} else {

			listPosition = 0;
			PlayInfo playInfo = new PlayInfo(
					"",
					"番茄吃炒蛋",
					"",
					"http://vroad.bbrtv.com/cmradio/uploads/file/20150707/20150707053311_35900.mp3",
					"",
					"",
					"",
					"",
					"http://vroad.bbrtv.com/cmradio/uploads/file/20150925/20150925172201_40959.jpg",
					"", "", "","","");
			mList.add(playInfo);
			previousIV.setVisibility(View.GONE);
			nextIV.setVisibility(View.GONE);
			// toast("获取数据异常，请再次尝试！");
			// finish();
		}
		//

	}

	private void myOnClickLisenter() {
		previousIV.setOnClickListener(this);
		playIV.setOnClickListener(this);
		nextIV.setOnClickListener(this);
		download.setOnClickListener(this);

		imageViewLike.setOnClickListener(this);
		imageViewFenxiang.setOnClickListener(this);

		imageView2.setOnClickListener(this);

	}

	private void initData() {
		mImageLoader = ImageLoader.getInstance();
		idList = new ArrayList<String>();
		LogHelper.e("listPosition:" + listPosition + "size:" + mList.size());

		if (listPosition < 0 && mList.size() > 0) {
			toast("无法获取曲目信息！");
			return;
		}
		Log.i(TAG, "mList长度----->" + mList.size()
				+ "===========listPosition值------>" + listPosition);
		playdatas = mList.get(listPosition);
		titleMusicString = playdatas.getTitle();
		host_name = "";
		host_id = playdatas.getId();
		group_id = "";
		program_id = playdatas.getId();
		music_id = playdatas.getId();
		music_url = playdatas.getPath();
		music_title = playdatas.getTitle();
		musicTitle.setText(music_title);
		// LogHelper.e("music_url:" +music_url +"/nmusic_title" + music_title +
		// "/nthumb" + playdatas.getThumb());
		mImageLoader.displayImage(playdatas.getThumb(), mHeadImageView,
				ImageLoaderHelper.getDisplayImageUnDefaultOptions());
		// setMusicThumb(playdatas.getThumb());
		firstEnter();
		Progress.setVisibility(View.GONE);
		initCollect();
	}

	/**
	 * 初始化收藏按钮，如果该节目已被收藏，则显示已被收藏的状态
	 */
	private void initCollect() {
		String url = HttpManage.myFavoriteUrl;
		if (uid.isEmpty()) {
			return;
		}
		Map<String,Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("uid", uid);
		paramsMap.put("type", 1);
		paramsMap.put("page", 1);
		HttpManage.getNetData(url, paramsMap, 1, new OnCallBack() {
			
			@Override
			public void onSuccess(byte[] arg2) {
				// progress.setVisibility(View.GONE);
				String result = new String(arg2);
				Gson gson = new Gson();
				LogHelper.e("我收藏的节目数据：" + result);

				favorite = gson.fromJson(result,
						new TypeToken<List<Favorite>>() {
						}.getType());
				for (int i = 0; i < favorite.size(); i++) {
					String program_id = favorite.get(i).getId();
					idList.add(program_id);
				}
				if (bundle != null) {
					id = program_id;
				} else {
					id = getIntent().getStringExtra("id");
				}
				if (idList.contains(id)) {
					isCollect = true;
					imageViewLike.setImageResource(R.drawable.iconfont_like);
				} else {
					isCollect = false;
				}
			}
			
			@Override
			public void onFailure(byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				
			}
		});
//		HttpManage.getMyFavorite(new AsyncHttpResponseHandler() {
//			@Override
//			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
//				// progress.setVisibility(View.GONE);
//				String result = new String(arg2);
//				Gson gson = new Gson();
//				LogHelper.e("我收藏的节目数据：" + result);
//
//				favorite = gson.fromJson(result,
//						new TypeToken<List<Favorite>>() {
//						}.getType());
//				for (int i = 0; i < favorite.size(); i++) {
//					String program_id = favorite.get(i).getId();
//					idList.add(program_id);
//				}
//				if (bundle != null) {
//					id = program_id;
//				} else {
//					id = getIntent().getStringExtra("id");
//				}
//				if (idList.contains(id)) {
//					isCollect = true;
//					imageViewLike.setImageResource(R.drawable.iconfont_like);
//				} else {
//					isCollect = false;
//				}
//			}
//
//			@Override
//			public void onFailure(int arg0,
//					@SuppressWarnings("deprecation") Header[] arg1,
//					byte[] arg2, Throwable arg3) {
//				// LogHelper.e("获取数据失败");
//			}
//		}, uid, 1, 1);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.activity_myradio_detial_iv_bot_previous:
			previous_music();
			break;
		case R.id.activity_myradio_detial_iv_bot_open:
			open_pause();
			break;
		case R.id.activity_myradio_detial_iv_bot_next:
			next_music();
			break;
		case R.id.play_radio_imageView2:// 歌曲列表
			Log.i("PlayRadioActivity", "点击了歌曲列表");
			isVisibility = !isVisibility;
			// if (isVisibility) {
			// play_radio_lv.setVisibility(View.VISIBLE);
			// } else {
			// play_radio_lv.setVisibility(View.GONE);
			// }

			break;
		case R.id.iv_down_radioplay:
			// Toast.makeText(RadioPlayActivity.this, "下载尚未开放", 0).show();
			System.out.println("RadioPlayActivity:点击了down");
			HttpManage.downloadProgram(new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
					String content = new String(arg2);
					Map<String, String> map = new HashMap<String, String>();
					Gson gson = new Gson();
					Type type = new TypeToken<Map<String, String>>() {
					}.getType();
					map = gson.fromJson(content, type);
					Log.i("RadioPlayActivity", "下载地址----->" + map.get("path"));
					Log.i("RadioPlayActivity", "歌名----->" + map.get("title"));
					String path = map.get("path");
					String name = map.get("title");
					downloadProgram(path, name);
				}

				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2,
						Throwable arg3) {
					Log.i(TAG, "失败");

				}
			}, program_id);
			break;
		case R.id.like:
			collection();

			break;
		case R.id.fenxiang:
			Intent intent1 = new Intent();
			intent1.setAction("android.intent.action.SEND");
			intent1.addCategory(Intent.CATEGORY_DEFAULT);
			intent1.setType("text/plain");
			intent1.putExtra(Intent.EXTRA_TEXT, "推荐您收听一个节目,名称叫：草莓电台");
			startActivity(intent1);
			break;

		default:
			break;
		}
	}

	/**
	 * 添加或取消收藏
	 */
	private void collection() {
		String mid = MyApplication.getInstance().getSpUtil().getUid();
		String id = getIntent().getStringExtra("id");
		String type_id = "1";
		String url;
		Map<String,Object> paramsMap;
		paramsMap = new HashMap<String, Object>();
		paramsMap.put("mid", mid);
		paramsMap.put("type_id", type_id);
		paramsMap.put("program_id", program_id);
		if (isCollect) {
			url = HttpManage.favoriteDelUrl;
			HttpManage.getNetData(url, paramsMap, 1, new OnCallBack() {
				
				@Override
				public void onSuccess(byte[] arg2) {
					// progress.setVisibility(View.GONE);
					String result = new String(arg2);
					Map<String, String> map = Common.str3map(result);
					if (map.get("status").equals("0")) {
						isCollect = false;
						imageViewLike
								.setImageResource(R.drawable.iconfont_likely);
						Toast.makeText(getApplicationContext(), "收藏已取消", 0)
								.show();
					} else {
						Toast.makeText(getApplicationContext(), "收藏取消失败", 0)
								.show();
					}
				}
				
				@Override
				public void onFailure(byte[] arg2, Throwable arg3) {
					// TODO Auto-generated method stub
					
				}
			});
//			HttpManage.FavoriteDel(new AsyncHttpResponseHandler() {
//				@Override
//				public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
//					// progress.setVisibility(View.GONE);
//					String result = new String(arg2);
//					Map<String, String> map = Common.str3map(result);
//					if (map.get("status").equals("0")) {
//						isCollect = false;
//						imageViewLike
//								.setImageResource(R.drawable.iconfont_likely);
//						Toast.makeText(getApplicationContext(), "收藏已取消", 0)
//								.show();
//					} else {
//						Toast.makeText(getApplicationContext(), "收藏取消失败", 0)
//								.show();
//					}
//				}
//
//				@Override
//				public void onFailure(int arg0,
//						@SuppressWarnings("deprecation") Header[] arg1,
//						byte[] arg2, Throwable arg3) {
//					// LogHelper.e("获取数据失败");
//				}
//			}, uid, program_id, type_id, "1");
		} else {
			url = HttpManage.favoriteAddUrl;
			HttpManage.getNetData(url, paramsMap, 1, new OnCallBack() {
				
				@Override
				public void onSuccess(byte[] arg2) {
					Log.i(TAG, new String(arg2));
					String result = new String(arg2);
					Map<String, String> map = Common.str3map(result);
					if (map.get("status").equals("0")) {
						isCollect = true;
						imageViewLike
								.setImageResource(R.drawable.iconfont_like);
						Toast.makeText(RadioPlayActivity.this, "收藏成功",
								0).show();
					} else {
						Toast.makeText(RadioPlayActivity.this, "收藏失败",
								0).show();
					}
				}
				
				@Override
				public void onFailure(byte[] arg2, Throwable arg3) {
					// TODO Auto-generated method stub
					
				}
			});
//			HttpManage.FavoriteAdd(uid, program_id, type_id,
//					new AsyncHttpResponseHandler() {
//
//						@Override
//						public void onSuccess(int arg0, Header[] arg1,
//								byte[] arg2) {
//							Log.i(TAG, new String(arg2));
//							String result = new String(arg2);
//							Map<String, String> map = Common.str3map(result);
//							if (map.get("status").equals("0")) {
//								isCollect = true;
//								imageViewLike
//										.setImageResource(R.drawable.iconfont_like);
//								Toast.makeText(RadioPlayActivity.this, "收藏成功",
//										0).show();
//							} else {
//								Toast.makeText(RadioPlayActivity.this, "收藏失败",
//										0).show();
//							}
//						}
//
//						@Override
//						public void onFailure(int arg0, Header[] arg1,
//								byte[] arg2, Throwable arg3) {
//							// TODO Auto-generated method stub
//
//						}
//					});
		}

	}

	/**
	 * 下载节目
	 * 
	 * @param url
	 *            下载地址
	 * @param name
	 *            歌名
	 */
	@SuppressWarnings("unused")
	private void downloadProgram(String url, String name) {
		// String state = ConfigUtils.DownloadState_WAITTING;
		int end = url.lastIndexOf(".");
		String format = url.substring(end);// 文件的格式
		String completeName = name + format;
		boolean isSame = isFileSame(completeName);// 判断节目是否已经被下载过
		if (isSame) {
			Toast.makeText(RadioPlayActivity.this, "已经缓存", 0).show();
			return;
		}
		// 检查数据库看是否有正在下载的任务
		// DBUtil db = DBUtil.getInstance(this);
		// Cursor cursor = db.selectData(SQLHelper.TABLE_DOWNLOAD, null, null,
		// null, null, null, null);
		// int count = cursor.getCount();
		//
		// cursor.close();
		String thumb = playdatas.getThumb();
		String path = ConfigUtils.getDownloadPath(RadioPlayActivity.this)
				+ completeName;

		// 初始化下载对象
		DownloadEntry downloadEntry = new DownloadEntry();
		if(program_id!=null){
			downloadEntry.setProgram_id(program_id);
		}
		downloadEntry.setAuthor("无名");
		downloadEntry.setThumb(thumb);
		downloadEntry.setStoragePath(path);
		downloadEntry.setUrl(url);
		downloadEntry.setTitle(name);
		// 开始下载
		DownloadManager.getInstance().beginDownloadFile(RadioPlayActivity.this,
				downloadEntry, DBUtil.getInstance(this));

	}

	/**
	 * 判断文件是否相同
	 * 
	 * @param name
	 * @return
	 */
	private boolean isFileSame(String name) {
		File sd_file = new File(ConfigUtils.SDDownloadPath);
		// File rom_file = new File(getFilesDir().getPath() + "/Download/");
		File[] files;
		String fileName;
		Vector<String> vecFile = new Vector<String>();
		if (sd_file.exists()) {
			// 取得SD卡下的Download目录下的所有文件
			files = sd_file.listFiles();
			Log.i(TAG, "SD卡下" + files);
		} else {
			sd_file.mkdirs();
			return false;
			// 取得ROM下的Download目录下的所有文件
			// files = rom_file.listFiles();
			// Log.i(TAG, "ROM卡下" + files);
		}
		// 历遍判断文件名是否相同
		if (files == null)
			return false;
		for (int iFileLength = 0; iFileLength < files.length; iFileLength++) {
			// 判断是否为文件夹
			if (!files[iFileLength].isDirectory()) {
				fileName = files[iFileLength].getName();
				if (name.equals(fileName)) {
					return true;
				} 
			}
		}
		return false;
	}

	// 是否进入播放音乐
	private void firstEnter() {

		if (NetUtil.getConnectedType(this) == ConnectivityManager.TYPE_WIFI
				&& getIntent().getStringExtra("is_notify") == null
				&& getIntent().getStringExtra("userinfo_shoucang_id") == null) {
			play();
		}
	}

	/**
	 * 播放音乐
	 */
	public void play() {
		// 开始播放的时候为顺序播放

		if (listPosition < 0 || listPosition > mList.size()) {
			toast("暂无歌曲播放");
			return;
		}

		IS_PLAY = true;
		playIV.setImageResource(R.drawable.ic_myradio_detial_pause);
		Intent intent = new Intent();
		intent.setAction("com.myradio.media.MUSIC_SERVICE");
		intent.setPackage("com.example.dolphinradio");
		intent.putExtra("url", music_url);
		intent.putExtra("listPosition", listPosition);
		intent.putExtra("isLocad", isLocad);
		intent.putExtra("MSG", PlayButton.PlayerMsg.PLAY_MSG);
		
		startService(intent);
		openMusicAnimation();
	}

	private void open_pause() {

		if (IS_PLAY) {
			IS_PLAY = false;
			playIV.setImageResource(R.drawable.ic_myradio_detial_open);
			Intent intent = new Intent();
			intent.setAction("com.myradio.media.MUSIC_SERVICE");
			intent.setPackage("com.example.dolphinradio");
			intent.putExtra("MSG", PlayButton.PlayerMsg.PAUSE_MSG);
			startService(intent);
		} else {

			if (listPosition < 0 || mList.size() == 0) {
				toast("暂无歌曲播放");
				return;
			} else if (listPosition < 0 && mList.size() > 0) {
				listPosition = 0;
			}
			IS_PLAY = true;
			playIV.setImageResource(R.drawable.ic_myradio_detial_pause);
			Intent intent = new Intent();
			intent.setAction("com.myradio.media.MUSIC_SERVICE");
			intent.setPackage("com.example.dolphinradio");
			intent.putExtra("url", music_url);
			intent.putExtra("MSG", PlayButton.PlayerMsg.CONTINUE_MSG);
			startService(intent);
		}
		openMusicAnimation();

	}

	/**
	 * 播放进度改变
	 * 
	 * @param progress
	 */
	public void audioTrackChange(int progress) {
		Intent intent = new Intent();
		intent.setAction("com.myradio.media.MUSIC_SERVICE");
		intent.setPackage("com.example.dolphinradio");
		intent.putExtra("url", music_url);
		intent.putExtra("listPosition", listPosition);
		intent.putExtra("MSG", PlayButton.PlayerMsg.PROGRESS_CHANGE);
		intent.putExtra("progress", progress);
		startService(intent);
	}

	/**
	 * 上一首
	 */
	public void previous_music() {
		if (mList.size() == 0) {
			toast("暂无歌曲播放");
			return;
		}

		IS_PLAY = true;
		playIV.setImageResource(R.drawable.ic_myradio_detial_pause);
		// openMusicAnimation();
		if (listPosition < 0 && mList.size() > 0) { // 如果是第一次进入播放器
			listPosition = 0;
			playdatas = mList.get(listPosition);
			program_id = playdatas.getId();
			music_id = playdatas.getId();
			music_url = playdatas.getPath();
			music_title = playdatas.getTitle();
			initCollect();
			play();
			// musicRating.setRating(Integer.valueOf(map.get("playtimes")));
			uploadMusicProgram(playdatas);
			return;
		}

		listPosition = listPosition - 1;
		if (listPosition >= 0) {
			playdatas = mList.get(listPosition);
			titleMusicString = playdatas.getTitle();
			host_name = "";
			host_id = playdatas.getId();
			group_id = "";
			program_id = playdatas.getId();
			music_id = playdatas.getId();
			music_url = playdatas.getPath();
			music_title = playdatas.getTitle();
			// music_url = map.get("path").toString();
			// music_id = map.get("id").toString();
			Intent intent = new Intent();
			intent.setAction("com.myradio.media.MUSIC_SERVICE");
			intent.setPackage("com.example.dolphinradio");
			intent.putExtra("url", music_url);
			intent.putExtra("listPosition", listPosition);
			intent.putExtra("MSG", PlayButton.PlayerMsg.PLAY_MSG);
			startService(intent);
			uploadMusicProgram(playdatas);
		} else {
			listPosition = 0;
			Toast.makeText(RadioPlayActivity.this, "没有上一首了", Toast.LENGTH_SHORT)
					.show();
		}
	}

	/**
	 * 下一首
	 */
	public void next_music() {

		if (mList.size() == 0) {
			toast("暂无歌曲播放");
			return;
		}

		IS_PLAY = true;
		playIV.setImageResource(R.drawable.ic_myradio_detial_pause);
		// openMusicAnimation();
		if (listPosition < 0 && mList.size() > 0) {// 如果是第一次进入播放器
			listPosition = 0;
			play();
			// musicRating.setRating(Integer.valueOf(map.get("playtimes")));
			return;
		}

		listPosition = listPosition + 1;
		if (listPosition <= mList.size() - 1) {
			playdatas = mList.get(listPosition);
			titleMusicString = playdatas.getTitle();
			host_name = "";
			host_id = playdatas.getId();
			group_id = "";
			program_id = playdatas.getId();
			music_id = playdatas.getId();
			music_url = playdatas.getPath();
			music_title = playdatas.getTitle();
			initCollect();
			Intent intent = new Intent();
			intent.setAction("com.myradio.media.MUSIC_SERVICE");
			intent.setPackage("com.example.dolphinradio");
			intent.putExtra("url", music_url);
			intent.putExtra("listPosition", listPosition);
			intent.putExtra("MSG", PlayButton.PlayerMsg.PLAY_MSG);
			startService(intent);
			uploadMusicProgram(playdatas);

		} else {
			listPosition = mList.size() - 1;
			Toast.makeText(RadioPlayActivity.this, "没有下一首了", Toast.LENGTH_SHORT)
					.show();
		}
	}

	// 播放音乐节目单改动
	private void uploadMusicProgram(PlayInfo data) {
		musicSeekBar.setProgress(0);
		musicTitle.setText(data.getTitle());
		music_id = data.getId();
		music_title = data.getTitle();
		music_url = data.getPath();
		// ImageLoader.getInstance().displayImage(data.get("thumb"),
		// musicAnimationIV, ImageLoaderHelper.getDisplayImageOptions());
		mImageLoader.displayImage(data.getThumb(), mHeadImageView,
				ImageLoaderHelper.getDisplayImageUnDefaultOptions());
		// setMusicThumb(data.getThumb());
		openMusicAnimation();

	}

	// 播放图片动画
	private void openMusicAnimation() {

		if (IS_PLAY) {
			// mHeadImageView.startAnimation(mAnimation);
			MyApplication.myradio_music_is_open = true;
			playIV.setImageResource(R.drawable.ic_myradio_detial_pause);
			changeNotifitytitle(titleMusicString, true);
		} else {
			// mHeadImageView.clearAnimation();
			MyApplication.myradio_music_is_open = false;
			playIV.setImageResource(R.drawable.ic_myradio_detial_open);
			changeNotifitytitle(titleMusicString, false);
		}
	}

	public class PlayerReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(MUSIC_CURRENT)) {
				currentTime = intent.getIntExtra("currentTime", -1);
				currentTimeProgress.setText(MediaUtil.formatTime(currentTime));
				int duration = intent.getIntExtra("duration", -1);
				musicSeekBar.setMax(duration);
				musicSeekBar.setProgress(currentTime);
				endtimeTime.setText(MediaUtil.formatTime(duration));
			} else if (action.equals(MUSIC_DURATION)) {
				int duration = intent.getIntExtra("duration", -1);
				musicSeekBar.setMax(duration);
				endtimeTime.setText(MediaUtil.formatTime(duration));
			} else if (action.equals(UPDATE_ACTION)) {
				// 获取Intent中的current消息，current代表当前正在播放的歌曲
				// listPosition = intent.getIntExtra("current", -1);
				// url = mp3Infos.get(listPosition).getUrl();
				// if (listPosition >= 0) {
				// musicTitle.setText(mp3Infos.get(listPosition).getTitle());
				// musicArtist.setText(mp3Infos.get(listPosition).getArtist());
				// }
				// if (listPosition == 0) {
				// finalProgress.setText(MediaUtil.formatTime(mp3Infos.get(
				// listPosition).getDuration()));
				// playBtn.setBackgroundResource(R.drawable.pause_selector);
				// isPause = true;
				// }
			} else if (action.equals(MUSIC_FINISH)) {
				// if (is_music_program_listen_all) {
				// //next_music();
				// LogHelper.e("is_isiable_music_program");
				// } else {
				// IS_PLAY = false;
				// //resetMusic();
				// }

				IS_PLAY = false;
				// resetMusic();
			} else if (action.equals(OPEN_BTN_CHANGE)) {
				boolean is_open = intent.getBooleanExtra("is_open", true);
				if (is_open) {// 正在播放
					playIV.setImageResource(R.drawable.ic_myradio_detial_pause);
					// mHeadImageView.startAnimation(mAnimation);
				} else {// 暂停播放
					playIV.setImageResource(R.drawable.ic_myradio_detial_open);
					// mHeadImageView.clearAnimation();
				}
			}
		}
	}

	// 加载头像
	@SuppressLint("NewApi")
	private void setMusicThumb(final String pathString) {
		// pathString = MyApplication.getInstance().getSpUtil().getHeadIcon();
		if (!pathString.isEmpty()) {
			// LogHelper.e(pathString);
			new AsyncTask<Void, Void, Bitmap>() {

				@Override
				protected Bitmap doInBackground(Void... arg0) {
					// TODO Auto-generated method stub
					// mImageLoader = ImageLoader.getInstance();
					Bitmap bitmap = mImageLoader
							.loadImageSync(pathString, ImageLoaderHelper
									.getDisplayImageUnDefaultOptions());
					return bitmap;
				}

				@Override
				protected void onPostExecute(Bitmap result) {
					// TODO Auto-generated method stub
					super.onPostExecute(result);
					if (result == null) {
						mHeadImageView.setImageResource(R.drawable.user_icon);
					} else {
						mHeadImageView
								.setImageBitmap(ImageUtils.getRoundBitmap(
										RadioPlayActivity.this, result));
						result = null;
					}
				}
			}.execute();
		} else {
			@SuppressWarnings("static-access")
			Bitmap bitmap = new BitmapFactory().decodeResource(getResources(),
					R.drawable.private_main_bg);
			mHeadImageView.setImageBitmap(ImageUtils.getRoundBitmap(
					RadioPlayActivity.this, bitmap));
			if (!bitmap.isRecycled()) {
				bitmap.recycle();
				bitmap = null;
			}
		}
	}

}
