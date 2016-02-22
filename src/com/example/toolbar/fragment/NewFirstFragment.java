package com.example.toolbar.fragment;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import org.apache.http.Header;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.strawberryradio.R;
import com.example.toolbar.activity.NewMainActivity;
import com.example.toolbar.activity.RadioPlayActivity;
import com.example.toolbar.adapter.PlayRadioAdapter;
import com.example.toolbar.application.MyApplication;
import com.example.toolbar.bean.DownloadEntry;
import com.example.toolbar.bean.Favorite;
import com.example.toolbar.bean.PickerBean;
import com.example.toolbar.bean.PlayInfo;
import com.example.toolbar.bean.ProgramClassifyListBean;
import com.example.toolbar.common.utils.Common;
import com.example.toolbar.common.utils.NetUtil;
import com.example.toolbar.db.DBUtil;
import com.example.toolbar.download.DownloadManager;
import com.example.toolbar.entity.PlayButton;
import com.example.toolbar.http.HttpManage;
import com.example.toolbar.service.PlayerManage;
import com.example.toolbar.utils.ConfigUtils;
import com.example.toolbar.utils.MediaUtil;
import com.example.toolbar.utils.SharePreferenceUtil;
import com.example.toolbar.utils.UserUtils;
import com.example.toolbar.view.FocusedTextView;
import com.example.toolbar.view.PickerView;
import com.example.toolbar.view.PickerView.onSelectListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class NewFirstFragment extends Fragment implements OnClickListener {
	private View view;
	private TextView tv_channelName;
	private TextView host;// 主播
	private FocusedTextView musicTitle; // 正在播放的音乐名
	private TextView endtimeTime;// 当前播放曲目的总时长
	private TextView currentTimeProgress; // 当前播放曲目的播放时间
	private ImageView previousIV; // 上一首
	private ImageView playIV; // 播放
	private ImageView nextIV; // 下一首
	private ImageView share;// 分享
	private ImageView collect;// 收藏
	private ImageView menue_or_back;// 侧栏菜单或放回键
	private ImageView program_list;//节目列表
	private ImageView first_more;
	private ImageView download;
	private PickerView mPickerView;
	private ListView play_list;
	private ProgressBar mProgressBar;
	// 分享
	private ImageView imageViewFenxiang;

	private String TAG = "RadioPlayActivity";
	private String titleMusicString = ""; // 歌曲标题
	private String host_name = "";// 主播名
	private String host_id = "";// 主播id
	private String group_id = "";// 主播群id
	private String group_tag = "";// 主播群tag,推送标志位用到
	private String music_id = "";
	private String music_url = "";
	private String music_title = "";
	private String time_limit = "";//播放的时间限制
	private String program_id;// 当前节目ID
	private String uid;
	private String id;// 网络请求所需的节目id==当前节目ID
	private String channelName = "草莓电台";
	private String pickerCurrentId="";

	private int listPosition = -1; // 播放歌曲的位置
	private int currentTime; // 当前歌曲播放时间
	private int duration;// 当前歌曲时长

	private boolean IS_PLAY = true;// 是否正在播放
	private boolean isLocad = false;// 是否本地资源播放
	private boolean isMain = true;// 是否主页
	private boolean isAgain = false;// 播放完后，是否重新播放
	private boolean isReload = false;//是否重新加载数据

	private NewPlayerReceiver playerReceiver;
	private SeekBar seekBar;

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
	private List<String> idListOfFavorite;// 已收藏的节目的id集
	private Map<String, String> map;// 用于取得默认节目信息
	private Map<String, String> playing_map;// 取得正在播放节目信息

	private List<String> picker_string;
	private ProgramClassifyListBean bean;
	private PickerBean mPickerBean;
	private boolean isCollect = false;
	// private Animation mAnimation;
	private boolean isVisibility = false;
	private PlayRadioAdapter adapter;

	private ImageLoader mImageLoader;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isMain = getActivity() instanceof NewMainActivity;// 判断是否主页
		registerReceiver();
		uid = MyApplication.getInstance().getSpUtil().getUid();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		idListOfFavorite = new ArrayList<String>();
		if (isMain) {
			initMainData();
		} else {
			initHeadActionBar();
		}
//		initPopupWindow();
		myOnClickLisenter();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_new_first, null);
		initView();
		return view;
	}

	private boolean isRecoveryPlayInfo = false;// 是否恢复播放信息

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		if (isMain && isRecoveryPlayInfo) {
			recoveryPlayInfo();
			if (IS_PLAY) {
				playIV.setImageResource(R.drawable.play_pause_white);
			} else {
				playIV.setImageResource(R.drawable.play_white);
			}
			setCollectIcon(program_id);
		}
		isRecoveryPlayInfo = true;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (IS_PLAY) {
		}
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (playerReceiver != null) {
			getActivity().unregisterReceiver(playerReceiver);
		}

		if (!titleMusicString.isEmpty()) {
			mSharePreferenceUtil = new SharePreferenceUtil(getActivity(),
					ConfigUtils.appSharePreferenceName);
			mSharePreferenceUtil.setMyradioMusicTitle(titleMusicString);
			mSharePreferenceUtil.setMyradioMusicID("");
			mSharePreferenceUtil.setMyradioMusicUrl("");
		}

		MyApplication.host_id = host_id;
	}


	public void initView() {
		tv_channelName=(TextView) view.findViewById(R.id.new_main_channel_name);
		first_more = (ImageView) view.findViewById(R.id.more_frist);
		menue_or_back = (ImageView) view.findViewById(R.id.Silding_menu);
		download = (ImageView) view.findViewById(R.id.new_main_download);
		mProgressBar = (ProgressBar) view.findViewById(R.id.new_main_progressbar);
		seekBar=(SeekBar) view.findViewById(R.id.new_main_SeekBar);
		seekBar.setEnabled(false);
		mPickerView = (PickerView) view.findViewById(R.id.new_main_picker);
		if (!isMain) {
			first_more.setVisibility(View.GONE);
			menue_or_back.setImageResource(R.drawable.login_back);
			mPickerView.setVisibility(View.GONE);
			tv_channelName.setTextSize(40);
			tv_channelName.setVisibility(View.VISIBLE);
			tv_channelName.setClickable(false);
		}else{
//			tv_channelName.setTextSize(17);
			tv_channelName.setClickable(true);
			tv_channelName.setOnClickListener(this);
		}
		share = (ImageView) view.findViewById(R.id.new_main_share);
		collect = (ImageView) view.findViewById(R.id.new_main_collect);
		musicTitle = (FocusedTextView) view
				.findViewById(R.id.new_main_program_name);
		host = (TextView) view.findViewById(R.id.new_main_host);
		endtimeTime = (TextView) view.findViewById(R.id.new_main_total_time);
		currentTimeProgress = (TextView) view
				.findViewById(R.id.new_main_current_time);
		
		program_list=(ImageView) view.findViewById(R.id.new_main_program_list);
		previousIV = (ImageView) view.findViewById(R.id.new_main_previous);
		playIV = (ImageView) view.findViewById(R.id.new_main_play);
		nextIV = (ImageView) view.findViewById(R.id.new_main_next);
		play_list=(ListView) view.findViewById(R.id.new_main_play_list);
		play_list.setOnItemClickListener(new MyOnItemClickListener());
		mPickerView.setOnSelectListener(new onSelectListener() {
			@Override
			public void onSelect(String title, String classifyId) {
				pickerCurrentId=classifyId;
				channelName=title;
				Log.i(TAG, "classifyId---->"+classifyId);
				if(isCompleteGetOneClassifyContent){
					getOneClassifyContent(1, pickerCurrentId);
				}
				
			}
		});
	}
	



	// 初始化音乐播放监听广播
	private void registerReceiver() {
		// 定义和注册广播接收器
		// LogHelper.e("registerReceiver");
		playerReceiver = new NewPlayerReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(UPDATE_ACTION);
		filter.addAction(MUSIC_CURRENT);
		filter.addAction(MUSIC_DURATION);
		filter.addAction(MUSIC_FINISH);
		filter.addAction(OPEN_BTN_CHANGE);
		getActivity().registerReceiver(playerReceiver, filter);
	}

	private void initMainData() {
		picker_string = new ArrayList<String>();
//		map = MyApplication.getInstance().getSpUtil().getDefaultProgram();
//		channelName = map.get("classifyName");

		listPosition = 0;
//		getOneClassifyContent(1, map.get("classifyID"));
		mProgressBar.setVisibility(View.VISIBLE);
		HttpManage.getNewMainPickerData(new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				String result = new String(arg2);
				Log.i(TAG, "result:" + result);
				Gson gson = new Gson();
				mPickerBean = gson.fromJson(result, PickerBean.class);
				for (int i = 0; i < mPickerBean.list.size(); i++) {
					picker_string.add(mPickerBean.list.get(i).title);
					Log.i(TAG, "picker" + i + ":"
							+ mPickerBean.list.get(i).title+"------>id:"+mPickerBean.list.get(i).id);
				}
				if (mPickerBean.list == null) {
					tv_channelName.setVisibility(View.VISIBLE);
					tv_channelName.setText("数据加载失败，请点击重新加载");
					isReload = true;
					mPickerView.setVisibility(View.GONE);
					Log.i(TAG, "mPickerBean.list-------->是空");
				}else{
					Log.i(TAG, "mPickerBean.list-------->是不空");
					mPickerView.setVisibility(View.VISIBLE);
					mPickerView.setData(mPickerBean);
					mPickerView.setSelected(0);
					tv_channelName.setVisibility(View.GONE);
					getOneClassifyContent(1,mPickerBean.list.get(0).id);
					channelName = mPickerBean.list.get(0).title ;
					isReload = false;
				}
				mProgressBar.setVisibility(View.GONE);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				mPickerView.setVisibility(View.GONE);
				tv_channelName.setVisibility(View.VISIBLE);
				tv_channelName.setText("数据加载失败，请点击重新加载");
				isReload = true;
				mProgressBar.setVisibility(View.GONE);
			}
		});

	}

	private boolean isCompleteGetOneClassifyContent =true;
	/**
	 * 获得一个分类的内容
	 * @param page
	 * @param classifyID
	 */
	private void getOneClassifyContent(int page, String classifyID) {
		//重置数据
		PlayerManage.position = 0;
		if(bean!=null){
			bean=null;
			isCompleteGetOneClassifyContent=false;
		}
		
		HttpManage.getProgramClassifyListData(page, classifyID,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						String result = new String(arg2);
						Gson gson = new Gson();
						bean = gson.fromJson(result,
								ProgramClassifyListBean.class);
						if(bean.list.size()<=0){
							Toast.makeText(getActivity(), "不存在节目", Toast.LENGTH_SHORT).show();
							isCompleteGetOneClassifyContent=true;
							return;
						}else{
							PlayerManage.getInstance().getPlayInfos().clear();
						}
						for (int i = 0; i < bean.list.size(); i++) {
							PlayInfo playInfo = new PlayInfo(
									bean.list.get(i).id,
									bean.list.get(i).title, "", bean.list
											.get(i).path, "", "", "", "",
									bean.list.get(i).thumb, "", "", bean.list
											.get(i).owner,"","");
							PlayerManage.getInstance().addPlayInfo(playInfo);
						}
						mList=PlayerManage.getInstance().getPlayInfos();
						Log.i(TAG, "mList长度："+mList.size());
						listPosition=PlayerManage.position;
						if(mList.size()==0){
							getOneClassifyContent(1, pickerCurrentId);
						}else{
							initData();
						}
						isCompleteGetOneClassifyContent=true;
						tv_channelName.setVisibility(View.GONE);
						isReload = false;
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						// TODO Auto-generated method stub
						isCompleteGetOneClassifyContent=true;
						tv_channelName.setVisibility(View.VISIBLE);
						tv_channelName.setText("数据加载失败，请点击重新加载");
						isReload = true;
					}
				});
	}
	
	public void initHeadActionBar() {
		// super.initHeadActionBar();
		// head_action_title.setText(R.string.app_name);
//		mList = new ArrayList<PlayInfo>();
		mList=PlayerManage.getInstance().getPlayInfos();
		listPosition=PlayerManage.position;
		channelName=getActivity().getIntent().getStringExtra("channelName");
		if(channelName != null && !channelName.isEmpty()){
			tv_channelName.setText(channelName);
		}else{
			channelName = " ";
		}
		initData();
	}

	private void myOnClickLisenter() {
		previousIV.setOnClickListener(this);
		playIV.setOnClickListener(this);
		nextIV.setOnClickListener(this);
		menue_or_back.setOnClickListener(this);
		first_more.setOnClickListener(this);
		share.setOnClickListener(this);
		collect.setOnClickListener(this);
		program_list.setOnClickListener(this);
		download.setOnClickListener(this);
	}

	private void initData() {
		mImageLoader = ImageLoader.getInstance();
		if(mList.size()<=0){
			Toast.makeText(getActivity(), "歌曲不存在", Toast.LENGTH_SHORT)
			.show();
			return;
		}
		if (listPosition < 0 && mList.size() > 0) {
			Toast.makeText(getActivity(), "无法获取曲目信息！", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		
		playdatas = mList.get(listPosition);
//		for(PlayInfo info : PlayerManage.getInstance().getPlayInfos()){
//			if(info.getType_id().equals("2")){
//				getOneClassifyContent(1, info.getId());
//				isTypeTwo=true;
//			}else{
//				isTypeTwo=false;
//			}
//		}
		
		titleMusicString = playdatas.getTitle();
		host_name = playdatas.getOwner();
		if (host_name == null) {
			host_name = "无名";
		}
		host_id = playdatas.getId();
		group_id = "";
		program_id = playdatas.getId();
		music_id = playdatas.getId();
		time_limit=playdatas.getTimespan();
		music_url = playdatas.getPath();
		music_title = playdatas.getTitle();

		musicTitle.setText(music_title);
		host.setText("主播：" + host_name);
		// SavePlayInfo(music_title, host_name);
		initCollect();
		// LogHelper.e("music_url:" +music_url +"/nmusic_title" + music_title +
		// "/nthumb" + playdatas.getThumb());
		// mImageLoader.displayImage(playdatas.getThumb(), mHeadImageView,
		// ImageLoaderHelper.getDisplayImageUnDefaultOptions());
		// setMusicThumb(playdatas.getThumb());
		firstEnter();
		
		// Progress.setVisibility(View.GONE);
		// initCollect();
	}

	/**
	 * 初始化收藏按钮，如果该节目已被收藏，则显示已被收藏的状态
	 */
	private void initCollect() {
		if (uid.isEmpty()) {
			return;
		}
		HttpManage.getMyFavorite(new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// progress.setVisibility(View.GONE);
				String result = new String(arg2);
				Gson gson = new Gson();
				favorite = gson.fromJson(result,
						new TypeToken<List<Favorite>>() {
						}.getType());
				for (int i = 0; i < favorite.size(); i++) {
					String program_id = favorite.get(i).getId();
					if (!idListOfFavorite.contains(program_id)) {
						idListOfFavorite.add(program_id);
					}
				}
				setCollectIcon(program_id);
			}

			@Override
			public void onFailure(int arg0,
					@SuppressWarnings("deprecation") Header[] arg1,
					byte[] arg2, Throwable arg3) {
				// LogHelper.e("获取数据失败");
			}
		}, uid, 1, 1);
	}

	private void setCollectIcon(String program_id) {
		if (idListOfFavorite.isEmpty()) {
			return;
		}
		if (idListOfFavorite.contains(program_id)) {
			isCollect = true;
			collect.setImageResource(R.drawable.collected_first);
		} else {
			isCollect = false;
			collect.setImageResource(R.drawable.collect_first);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.new_main_channel_name:
			if(isReload)  initMainData();
			break;
		case R.id.new_main_previous:
			previous_music();
			break;
		case R.id.new_main_play:
			open_pause();
			break;
		case R.id.new_main_next:
			next_music();
			break;
		case R.id.Silding_menu:
			if (isMain) {
				((NewMainActivity) getActivity()).openDrawerLayout();
			} else {
				getActivity().finish();
			}
			break;
		case R.id.more_frist:
			((NewMainActivity) getActivity()).switchFragment(1);
			break;
		case R.id.new_main_share:
			share();
			break;
		case R.id.new_main_collect:
			if (!UserUtils.checkLogin(getActivity())) {
				collection();
			}
			break;
		case R.id.new_main_download:
			getDownloadInfo(program_id);
			break;
		case R.id.new_main_program_list:
			isVisibility = !isVisibility;
			showOrHidePlayList(isVisibility);
			break;
		default:
			break;
		}
	}

	private void share(){
		Intent intent1 = new Intent();
		intent1.setAction("android.intent.action.SEND");
		intent1.addCategory(Intent.CATEGORY_DEFAULT);
		intent1.setType("text/plain");
		intent1.putExtra(Intent.EXTRA_TEXT, "推荐您收听一个节目,名称叫：海豚电台");
		startActivity(intent1);
	}
	
	private void showOrHidePlayList(boolean isVisibility){
		if(isVisibility){
			program_list.setImageResource(R.drawable.program_list_show);
			if(adapter==null){
				adapter=new PlayRadioAdapter(getActivity(), PlayerManage.getInstance().getPlayInfos());
				play_list.setAdapter(adapter);
			}
			play_list.setVisibility(View.VISIBLE);
			adapter.notifyDataSetChanged();
		}else{
			program_list.setImageResource(R.drawable.program_list_hide);
			play_list.setVisibility(View.GONE);
		}
	}


	/**
	 * 添加或取消收藏
	 */
	private void collection() {
		String mid = MyApplication.getInstance().getSpUtil().getUid();
		String type_id = "1";
		if (isCollect) {
			HttpManage.FavoriteDel(new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
					// progress.setVisibility(View.GONE);
					String result = new String(arg2);
					Map<String, String> map = Common.str3map(result);
					if (map.get("status").equals("0")) {
						isCollect = false;
						collect.setImageResource(R.drawable.collect_first);
						Toast.makeText(getActivity(), "收藏已取消", 0).show();
					} else {
						Toast.makeText(getActivity(), "收藏取消失败", 0).show();
					}
				}

				@Override
				public void onFailure(int arg0,
						@SuppressWarnings("deprecation") Header[] arg1,
						byte[] arg2, Throwable arg3) {
					// LogHelper.e("获取数据失败");
				}
			}, mid, program_id, type_id, "1");
		} else {
			HttpManage.FavoriteAdd(mid, program_id, type_id,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							Log.i(TAG, new String(arg2));
							String result = new String(arg2);
							Map<String, String> map = Common.str3map(result);
							if (map.get("status").equals("0")) {
								isCollect = true;
								collect.setImageResource(R.drawable.collected_first);
								Toast.makeText(getActivity(), "收藏成功", 0).show();
							} else {
								Toast.makeText(getActivity(), "收藏失败", 0).show();
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							// TODO Auto-generated method stub

						}
					});
		}

	}
	
	private void getDownloadInfo(String program_id){
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
	}
	
	/**
	 * 下载节目
	 * @param url  下载地址
	 * @param name 歌名
	 */
	@SuppressWarnings("unused")
	private void downloadProgram(String url, String name) {
		// String state = ConfigUtils.DownloadState_WAITTING;
		int end = url.lastIndexOf(".");
		String format = url.substring(end);// 文件的格式
		String completeName = name + format;
		boolean isSame = isFileSame(completeName);// 判断节目是否已经被下载过
		if (isSame) {
			Toast.makeText(getActivity(), "已经缓存", 0).show();
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
		String path = ConfigUtils.getDownloadPath(getActivity())
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
		DownloadManager.getInstance().beginDownloadFile(getActivity(),
				downloadEntry, DBUtil.getInstance(getActivity()));

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

		if (NetUtil.getConnectedType(getActivity()) == ConnectivityManager.TYPE_WIFI
				&& getActivity().getIntent().getStringExtra("is_notify") == null
				&& getActivity().getIntent().getStringExtra(
						"userinfo_shoucang_id") == null) {
			play();
		}
	}

	/**
	 * 上一首
	 */
	public void previous_music() {
		if (mList.size() == 0) {
			Toast.makeText(getActivity(), "暂无歌曲播放", Toast.LENGTH_SHORT).show();
			return;
		}

		IS_PLAY = true;
		playIV.setImageResource(R.drawable.play_pause_white);
		// openMusicAnimation();
		if (listPosition < 0 && mList.size() > 0) { // 如果是第一次进入播放器
			listPosition = 0;
			playdatas = mList.get(listPosition);
			program_id = playdatas.getId();
			music_id = playdatas.getId();
			time_limit=playdatas.getTimespan();
			music_url = playdatas.getPath();
			music_title = playdatas.getTitle();
			host_name = playdatas.getOwner();
			if (host_name == null) {
				host_name = "无名";
			}
			musicTitle.setText(music_title);
			host.setText("主播：" + host_name);
			play();
			// musicRating.setRating(Integer.valueOf(map.get("playtimes")));
			uploadMusicProgram(playdatas);
			return;
		}
		PlayerManage.position-=1;
		listPosition = PlayerManage.position;
		if (listPosition >= 0) {
			playdatas = mList.get(listPosition);
			// titleMusicString = playdatas.getTitle();
//			host_id = playdatas.getId();
//			group_id = "";
			program_id = playdatas.getId();
			music_id = playdatas.getId();
			time_limit=playdatas.getTimespan();
			music_url = playdatas.getPath();
			music_title = playdatas.getTitle();
			host_name = playdatas.getOwner();
			if (host_name == null) {
				host_name = "无名";
			}
			musicTitle.setText(music_title);
			host.setText("主播：" + host_name);
			savePlayInfo(channelName, music_title, host_name, program_id,
					music_url);
			setCollectIcon(program_id);
			// music_url = map.get("path").toString();
			// music_id = map.get("id").toString();
			Intent intent = new Intent();
			intent.setAction("com.myradio.media.MUSIC_SERVICE");
			intent.setPackage("com.example.strawberryradio");
//			intent.putExtra("url", music_url);
//			intent.putExtra("listPosition", listPosition);
//			intent.putExtra("time_limit", time_limit);
			intent.putExtra("MSG", PlayButton.PlayerMsg.PLAY_MSG);
//			if(playdatas.getType_id().equals("2")){
//				intent.putExtra("isTypeTwo", true);
//			}
			getActivity().startService(intent);
			uploadMusicProgram(playdatas);
		} else {
			PlayerManage.position = 0;
			Toast.makeText(getActivity(), "没有上一首了", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 下一首
	 */
	public void next_music() {

		if (mList.size() == 0) {
			Toast.makeText(getActivity(), "暂无歌曲播放", Toast.LENGTH_SHORT).show();
			return;
		}

		IS_PLAY = true;
		playIV.setImageResource(R.drawable.play_pause_white);
		// openMusicAnimation();
		if (listPosition < 0 && mList.size() > 0) {// 如果是第一次进入播放器
			listPosition = 0;
			play();
			// musicRating.setRating(Integer.valueOf(map.get("playtimes")));
			return;
		}
		PlayerManage.position+=1;
		listPosition = PlayerManage.position;
		if (listPosition <= mList.size() - 1) {
			playdatas = mList.get(listPosition);
			// titleMusicString = playdatas.getTitle();
//			host_id = playdatas.getId();
//			group_id = "";
			program_id = playdatas.getId();
			music_id = playdatas.getId();
			time_limit=playdatas.getTimespan();
			music_url = playdatas.getPath();
			music_title = playdatas.getTitle();
			host_name = playdatas.getOwner();
			if (host_name == null) {
				host_name = "无名";
			}
			musicTitle.setText(music_title);
			host.setText("主播：" + host_name);
			savePlayInfo(channelName, music_title, host_name, program_id,
					music_url);
			setCollectIcon(program_id);
			Intent intent = new Intent();
			intent.setAction("com.myradio.media.MUSIC_SERVICE");
			intent.setPackage("com.example.strawberryradio");
			intent.putExtra("time_limit", time_limit);
			intent.putExtra("listPosition", listPosition);
			intent.putExtra("MSG", PlayButton.PlayerMsg.PLAY_MSG);
//			if(playdatas.getType_id().equals("2")){
//				intent.putExtra("isTypeTwo", true);
//			}
			getActivity().startService(intent);
			uploadMusicProgram(playdatas);

		} else {
			PlayerManage.position = PlayerManage.getInstance().getPlayInfos().size()-1;
			Toast.makeText(getActivity(), "没有下一首了", Toast.LENGTH_SHORT).show();
		}
	}

	// 播放音乐节目单改动
	private void uploadMusicProgram(PlayInfo data) {
		// musicSeekBar.setProgress(0);
		musicTitle.setText(data.getTitle());
		music_id = data.getId();
		music_title = data.getTitle();
		music_url = data.getPath();
		// ImageLoader.getInstance().displayImage(data.get("thumb"),
		// musicAnimationIV, ImageLoaderHelper.getDisplayImageOptions());
		// mImageLoader.displayImage(data.getThumb(), mHeadImageView,
		// ImageLoaderHelper.getDisplayImageUnDefaultOptions());
		// setMusicThumb(data.getThumb());
		openMusicAnimation();

	}

	/**
	 * 播放音乐
	 */
	public void play() {
		// 开始播放的时候为顺序播放

		if (listPosition < 0 || listPosition > mList.size()) {
			Toast.makeText(getActivity(), "暂无歌曲播放", Toast.LENGTH_SHORT).show();
			return;
		}

		IS_PLAY = true;
		savePlayInfo(channelName, music_title, host_name, program_id, music_url);
		playIV.setImageResource(R.drawable.play_pause_white);
		Intent intent = new Intent();
		intent.setAction("com.myradio.media.MUSIC_SERVICE");
		intent.setPackage("com.example.strawberryradio");
//		intent.putExtra("url", music_url);
//		intent.putExtra("time_limit", time_limit);
//		intent.putExtra("listPosition", listPosition);
//		intent.putExtra("isTypeTwo", isTypeTwo);
		intent.putExtra("isLocad", isLocad);
		intent.putExtra("MSG", PlayButton.PlayerMsg.PLAY_MSG);

		getActivity().startService(intent);
		openMusicAnimation();
	}

	private void open_pause() {

		if (IS_PLAY) {
			IS_PLAY = false;
			// playIV.setImageResource(R.drawable.play_grey);
			Intent intent = new Intent();
			intent.setAction("com.myradio.media.MUSIC_SERVICE");
			intent.setPackage("com.example.strawberryradio");
			intent.putExtra("MSG", PlayButton.PlayerMsg.PAUSE_MSG);
			getActivity().startService(intent);
		} else {

			if (listPosition < 0 || mList.size() == 0) {
				Toast.makeText(getActivity(), "暂无歌曲播放", Toast.LENGTH_SHORT)
						.show();
				return;
			} else if (listPosition < 0 && mList.size() > 0) {
				listPosition = 0;
			}
			IS_PLAY = true;
			// playIV.setImageResource(R.drawable.play_pause_grey);
			Intent intent = new Intent();
			intent.setAction("com.myradio.media.MUSIC_SERVICE");
			intent.setPackage("com.example.strawberryradio");
			intent.putExtra("url", music_url);
			intent.putExtra("MSG", PlayButton.PlayerMsg.CONTINUE_MSG);
			intent.putExtra("isAgain", isAgain);
			getActivity().startService(intent);
			isAgain = false;
		}
		openMusicAnimation();

	}

	// 播放图片动画
	private void openMusicAnimation() {

		if (IS_PLAY) {
			// mHeadImageView.startAnimation(mAnimation);
			MyApplication.myradio_music_is_open = true;
			playIV.setImageResource(R.drawable.play_pause_white);
			changeNotifitytitle(music_title, true);
		} else {
			// mHeadImageView.clearAnimation();
			MyApplication.myradio_music_is_open = false;
			playIV.setImageResource(R.drawable.play_white);
			changeNotifitytitle(music_title, false);
		}
	}

	// 通知改变方法
	private void changeNotifitytitle(String title, boolean isopen) {
		// LogHelper.e("changeNotifitytitle" + title);
		Intent intent = new Intent();
		intent.setAction("com.myradio.media.myradio_notifyservice");
		intent.setPackage("com.example.strawberryradio");
		intent.putExtra("title", title);
		intent.putExtra("is_play", isopen);
		intent.putExtra("host_id", host_id);
		intent.putExtra("notifycation_name", channelName);
		getActivity().startService(intent);
	}

	private void savePlayInfo(String channelName, String programName,
			String host, String program_id, String url) {
		MyApplication
				.getInstance()
				.getSpUtil()
				.setPalyProgramInfo(channelName, programName, host, program_id,
						url);
	}

	private void recoveryPlayInfo() {
		playing_map = MyApplication.getInstance().getSpUtil()
				.getPalyProgramInfo();
		channelName = playing_map.get("channelName");
		music_title = playing_map.get("programName");
		host_name = playing_map.get("host");
		program_id = playing_map.get("programID");
		Log.i("NewFirstFragment:recoveryPlayInfo---->program_id:",
				playing_map.get("programID"));
		music_url = playing_map.get("url");

		musicTitle.setText(music_title);
		host.setText("主播：" + host_name);
	}
	
	class MyOnItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			PlayerManage.position=position;
			playdatas = mList.get(PlayerManage.position);
			musicTitle.setText(playdatas.getTitle());
			host.setText("主播：" + playdatas.getOwner());
			play();
		}
		
	}

	public class NewPlayerReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(MUSIC_CURRENT)) {
				currentTime = intent.getIntExtra("currentTime", -1);
				currentTimeProgress.setText(MediaUtil.formatTime(currentTime));
				seekBar.setProgress(currentTime);
				if (duration != intent.getIntExtra("duration", -1)) {
					duration = intent.getIntExtra("duration", -1);
					endtimeTime.setText(MediaUtil.formatTime(duration));
				}
			} else if (action.equals(MUSIC_DURATION)) {
				duration = intent.getIntExtra("duration", -1);
				 seekBar.setMax(duration);
				endtimeTime.setText(MediaUtil.formatTime(duration));
				mProgressBar.setVisibility(View.GONE);
				boolean isPlay = intent.getBooleanExtra("isPlay", false);
				if (isPlay) {
				}
			} else if (action.equals(UPDATE_ACTION)) {
				if(intent.getBooleanExtra("without", false)){
					Toast.makeText(getActivity(), "没有下一首了,将重头开始", Toast.LENGTH_SHORT).show();
					return;
				}
				playdatas = PlayerManage.getInstance().getPlayInfos().get(PlayerManage.position);
				titleMusicString = playdatas.getTitle();
				host_name = playdatas.getOwner();
				if (host_name == null) {
					host_name = "无名";
				}
				host_id = playdatas.getId();
				group_id = "";
				program_id = playdatas.getId();
				music_id = playdatas.getId();
				time_limit=playdatas.getTimespan();
				music_url = playdatas.getPath();
				music_title = playdatas.getTitle();

				musicTitle.setText(music_title);
				host.setText("主播：" + host_name);
				// SavePlayInfo(music_title, host_name);
				setCollectIcon(program_id);
				IS_PLAY = true;
				changeNotifitytitle(music_title, IS_PLAY);
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
				openMusicAnimation();
				isAgain = true;
				Log.i(TAG, "MUSIC_FINISH");
				// resetMusic();
			} else if (action.equals(OPEN_BTN_CHANGE)) {
				boolean is_open = intent.getBooleanExtra("is_open", true);
				if (is_open) {// 正在播放

					playIV.setImageResource(R.drawable.play_pause_white);
					IS_PLAY = true;
					// mHeadImageView.startAnimation(mAnimation);
				} else {// 暂停播放
					playIV.setImageResource(R.drawable.play_white);
					IS_PLAY = false;
					// mHeadImageView.clearAnimation();
				}
			}
		}
	}

}
