package com.example.toolbar.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.http.Header;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.strawberryradio.R;
import com.example.toolbar.adapter.DialogDownloadSelectAdapter;
import com.example.toolbar.adapter.ProgramListAdapter;
import com.example.toolbar.bean.DownloadEntry;
import com.example.toolbar.bean.PlayInfo;
import com.example.toolbar.bean.ProgramClassifyListBean;
import com.example.toolbar.bean.ProgramListBean;
import com.example.toolbar.bean.ProgramListBean.ProgramListInfo.ProgramInfo;
import com.example.toolbar.bean.ProgramListBean.ProgrammeInfo;
import com.example.toolbar.bean.ProgramListBean.ProgramListInfo;
import com.example.toolbar.bean.ProgrammeEditInfo;
import com.example.toolbar.common.utils.Common;
import com.example.toolbar.common.utils.FileUtils;
import com.example.toolbar.common.utils.ImageLoaderHelper;
import com.example.toolbar.common.utils.LogHelper;
import com.example.toolbar.db.DBUtil;
import com.example.toolbar.download.DownloadManager;
import com.example.toolbar.http.HttpManage;
import com.example.toolbar.service.PlayerManage;
import com.example.toolbar.utils.ConfigUtils;
import com.example.toolbar.utils.UserUtils;
import com.example.toolbar.view.MyListView;
import com.example.toolbar.view.MyToast;
import com.example.toolbar.view.progress.CircularProgress;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.mobeta.android.dslv.DragSortListView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProgramListActivity extends AppCompatActivity implements
		OnClickListener {
	private ShareActionProvider mShareActionProvider;
	private Toolbar mToolbar;
	private ImageLoader mImageLoader;
	private ScrollView sv;
	private LinearLayout ll_editor;
	private LinearLayout ll_main_info;

	// private MyListView listView;
	private DragSortListView mDragSortListView;
	private ImageView show_img;// 节目封面
	private ImageView user_icon;

	private TextView showlist_name;// 节目单
	private TextView user_nikename;

	private TextView collect_number;
	private TextView share_number;
	private TextView comment_icon;
	private TextView download;
	private TextView remind;
	private TextView add_program;
	private TextView sure_editor;
	private TextView cancel_editor;

	private CircularProgress progress;

	private ProgramListBean bean;
	// private ArrayList<ProgramListInfo> mProgramListInfos;
	// private AddProgramAdapter adapter;
	private ProgramListAdapter mProgramListAdapter;
	private boolean isCollected = false;
	private String programme_id;
	private String mid;

	private List<String> checkedIdList;
	private List<ProgramListInfo> downloadList;//要下载节目的id集
//	private List<Map<String,Map<String,String>>> downloadList;
//	private Map<String, Map<String,String>> downloadInfo;
	
	private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {

		@Override
		public void drop(int from, int to) {
			ProgramListInfo info = (ProgramListInfo) mProgramListAdapter
					.getItem(from);
			mProgramListAdapter.remove(from);
			mProgramListAdapter.inster(info, to);
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_list);
		checkedIdList = new ArrayList<String>();
		downloadList = new ArrayList<ProgramListInfo>();
//		downloadInfo = new HashMap<String, Map<String,String>>();
		programme_id = getIntent().getStringExtra("programme_id");
		mid = UserUtils.getUid();
		Log.i("ProgramListActivity", "数据加载programme_id---->" + programme_id);
		initViews();
		initData();
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		Log.i("ProgramListAcivity", "触发了onActivityResult");
		initData();
		super.onActivityResult(arg0, arg1, arg2);
//		if(Activity.RESULT_OK==arg1){
//			initData();
//		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);// must store the new intent unless getIntent() will

		if (getIntent().getBooleanExtra("is_refresh", false)) {// 从个人主页进入的话，需要刷新数据
			initData();
		}
	}

	private void initViews() {
		mImageLoader = ImageLoader.getInstance();
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mToolbar.setTitle("节目单");
		setSupportActionBar(mToolbar);
		sv = (ScrollView) findViewById(R.id.program_list_scrollview);
		show_img = (ImageView) findViewById(R.id.show_img);
		user_icon = (ImageView) findViewById(R.id.user_icon);
		user_icon.setOnClickListener(this);
		showlist_name = (TextView) findViewById(R.id.showlist_name);
		user_nikename = (TextView) findViewById(R.id.user_nikename);

		collect_number = (TextView) findViewById(R.id.collect_number);
		share_number = (TextView) findViewById(R.id.share_number);
		comment_icon = (TextView) findViewById(R.id.message_number);
		download = (TextView) findViewById(R.id.program_list_tv_donwload);

		progress = (CircularProgress) findViewById(R.id.program_list_progress);
		remind = (TextView) findViewById(R.id.program_list_remind_tv);
		add_program = (TextView) findViewById(R.id.program_list_add);
		ll_editor = (LinearLayout) findViewById(R.id.program_list_editor_sure_or_cancel);
		ll_main_info = (LinearLayout) findViewById(R.id.program_list_main_info);
		sure_editor = (TextView) findViewById(R.id.program_list_editor_sure);
		cancel_editor = (TextView) findViewById(R.id.program_list_editor_cancel);

		share_number.setOnClickListener(this);
		collect_number.setOnClickListener(this);
		add_program.setOnClickListener(this);
		sure_editor.setOnClickListener(this);
		cancel_editor.setOnClickListener(this);
		download.setOnClickListener(this);

		mDragSortListView = (DragSortListView) findViewById(R.id.show_list);
		mDragSortListView.setDivider(null);
		mDragSortListView.setOnItemClickListener(new MyOnItemClickListener());
		mDragSortListView.setDropListener(onDrop);// 设置拖动监听
		mDragSortListView.setDragEnabled(false);
		// 设置左上角的图标
		mToolbar.setNavigationContentDescription(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		// 左上角的图标的点击事件
		mToolbar.setNavigationOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				finish();
			}
		});
		// 对工具条上的菜单进行监听
		mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem arg0) {
				if (bean == null) {
					return true;
				}
				switch (arg0.getItemId()) {
				case R.id.menu_editor:
					ll_editor.setVisibility(View.VISIBLE);
					int marginButtomHeight = ll_editor.getHeight();
					ll_main_info.setPadding(0, 0, 0, marginButtomHeight);

					if (mProgramListAdapter == null) {
						mProgramListAdapter = new ProgramListAdapter(
								ProgramListActivity.this, bean.list);
						mProgramListAdapter.setIsEditor(true);
						mDragSortListView.setAdapter(mProgramListAdapter);
					} else {
						mProgramListAdapter.setIsEditor(true);
						mProgramListAdapter.notifyDataSetChanged();
					}
					mDragSortListView.setDragEnabled(true);
					mDragSortListView.setDividerHeight(1);
					break;

				default:
					break;
				}
				return true;
			}
		});
	}

	private void initData() {
		HttpManage.getProgramListData(programme_id,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						String result = new String(arg2);
						Gson gson = new Gson();
						try {
							bean = gson.fromJson(result, ProgramListBean.class);
						} catch (Exception e) {
							MyToast.show("数据有误，请稍后加载", ProgramListActivity.this);
							progress.setVisibility(View.GONE);
							return;
						}
						mImageLoader.displayImage(bean.row.programme_thumb,
								show_img,
								ImageLoaderHelper.getDisplayImageOptions());
						mImageLoader.displayImage(bean.row.member_thumb,
								user_icon,
								ImageLoaderHelper.getDisplayImageOptions());
						user_nikename.setText(bean.row.member_name);
						showlist_name.setText(bean.row.programme_name);
						collect_number.setText(bean.row.programme_fav);
						if (bean.row.is_favorite.equals("1")) {
							changeDrawable(id[0]);
						} else {
							changeDrawable(id[1]);
						}
						comment_icon.setText(bean.row.programme_comment);
						share_number.setText(bean.row.programme_share);
//						if(mProgramListAdapter==null){
//							mProgramListAdapter = new ProgramListAdapter(
//									ProgramListActivity.this, bean.list);
//						}
						mProgramListAdapter = new ProgramListAdapter(
								ProgramListActivity.this, bean.list);
						mDragSortListView.setAdapter(mProgramListAdapter);
						sv.smoothScrollTo(0, 20);
						sv.setVisibility(View.VISIBLE);
						progress.setVisibility(View.GONE);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						Log.i("ProgramListActivity", "数据加载失败：id---->"
								+ "该id不存在");
						progress.setVisibility(View.GONE);
						remind.setVisibility(View.VISIBLE);
					}
				});

	}

	/**
	 * 将要删除的节目的id添加到集合checkedIdList
	 * 
	 * @param id
	 */
	public void addDeleteProgramId(String id) {
		if (checkedIdList == null) {
			checkedIdList = new ArrayList<String>();
		}
		if (!checkedIdList.contains(id)) {
			checkedIdList.add(id);
		}
		Log.i("ProgramListActivity", "checkedIdList的长度:" + checkedIdList.size());
	}

	public void removeProgramId(String id) {
		if (checkedIdList.contains(id))
			checkedIdList.remove(id);
		Log.i("ProgramListActivity",
				"移除后checkedIdList的长度:" + checkedIdList.size());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// 取得菜单布局
		getMenuInflater().inflate(R.menu.editor, menu);
		return super.onCreateOptionsMenu(menu);
	}

	// 是否正在播放这个节目单
	private boolean isPlayingThisList = false;

	public class MyOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long arg3) {

			Intent intent = new Intent(ProgramListActivity.this,
					NewRadioPlayActivity.class);
			if (!isPlayingThisList) {
				PlayerManage.getInstance().clearList();
				PlayerManage.getInstance().clearListForClassifyList();
				PlayerManage.position = 0;
				ArrayList<ProgramListInfo> list = bean.list;
				for (ProgramListInfo info : list) {
					PlayInfo playInfo = new PlayInfo(info.program_id,
							info.title, "", info.path, "", "", "", "",
							info.thumb, "", "", info.nickname, info.timespan,
							info.type_id);
					PlayerManage.getInstance().addPlayInfo(playInfo);
					if (info.type_id.equals("2")) {
						ArrayList<PlayInfo> typeTwoList = new ArrayList<PlayInfo>();
//						getOneClassifyContent(1, info.program_id);
						for(ProgramInfo programInfo : info.contentlist){
							PlayInfo playInfo2 = new PlayInfo(programInfo.id,
									programInfo.title, "", programInfo.path, "", "", "", "",
									programInfo.thumb, "", "", programInfo.owner, "",
									"");
							typeTwoList.add(playInfo2);
						}
						PlayerManage.getInstance().addPlayInfoForClassifyList(typeTwoList, info.program_id);
					}
				}
			}
//			for (PlayInfo info : PlayerManage.getInstance().getPlayInfos()) {
//				if (info.getType_id().equals("2")) {
//					getOneClassifyContent(1, info.getId());
//				}
//			}
			isPlayingThisList = true;// 当要播放本页的节目的时候，isPlayingThisList赋值true

			PlayerManage.position = position;
			intent.putExtra("channelName", bean.row.programme_name);
			// bundle.putSerializable("ProgramListBean", bean);
			// bundle.putInt("position", position);
			// intent.putExtra("bundle", bundle);
			startActivity(intent);

		}

	}

	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.user_icon:
			Intent intent = new Intent(ProgramListActivity.this,
					PrivateMainActivity.class);
			intent.putExtra("host_id", bean.row.member_id);
			startActivity(intent);
			// finish();
			break;
		case R.id.collect_number:
			if (mid != null) {
				if (isCollected) {
					favoriteProgramListDel();
				} else {
					favoriteProgramListAdd();
				}
				isCollected = (!isCollected);
			} else {
				UserUtils.showLoginDialog(ProgramListActivity.this);
			}
			break;
		case R.id.share_number:
			// 分享
			Intent intent1 = new Intent();
			intent1.setAction("android.intent.action.SEND");
			intent1.addCategory(Intent.CATEGORY_DEFAULT);
			intent1.setType("text/plain");
			intent1.putExtra(Intent.EXTRA_TEXT, "推荐您使用一款软件,名称叫：海豚电台");
			startActivity(intent1);
			break;
		case R.id.program_list_tv_donwload:
			//弹出对话框，显示可下载的内容
			showDownloadDialog();
			break;
		case R.id.program_list_add:
			Intent addProgram= new Intent(ProgramListActivity.this,
					SelectProgramActivity.class);
			addProgram.putExtra("from", "Programme");
			addProgram.putExtra("programme_id", programme_id);
			startActivityForResult(addProgram, 5);
			break;
		case R.id.program_list_editor_sure:
			cancelEditor();
			mProgramListAdapter.setIsSure(true);
			//更新限制时间（timespan）的数据
			mProgramListAdapter.notifyDataSetChanged();
			programSort(programme_id);
			isPlayingThisList = false;
			break;
		case R.id.program_list_editor_cancel:
			cancelEditor();
			mProgramListAdapter.setIsSure(false);
			mProgramListAdapter.notifyDataSetChanged();
			break;

		default:
			break;
		}

	}

	private void showDownloadDialog() {
		final AlertDialog dialog;
		AlertDialog.Builder builder = new AlertDialog.Builder(ProgramListActivity.this);
		View view = LayoutInflater.from(ProgramListActivity.this).inflate(R.layout.dialog_download_select, null);
		ListView lv = (ListView) view.findViewById(R.id.dialog_download_select_lv);
		Button cancel = (Button) view.findViewById(R.id.dialog_download_select_cancel);
		Button sure = (Button) view.findViewById(R.id.dialog_download_select_sure);
		final DialogDownloadSelectAdapter ddsa = new DialogDownloadSelectAdapter(this, bean.list);
		lv.setAdapter(ddsa);
		builder.setView(view);
		dialog = builder.show();
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ProgramListInfo pli = (ProgramListInfo)ddsa.getItem(position);
				String program_id = pli.id;
				CheckBox cb = (CheckBox) view.findViewById(R.id.item_dialog_download_select_checkbox);
				if(cb.isChecked()){
					cb.setChecked(false);
//					if(downloadIdList.contains(program_id)) 
//						downloadIdList.remove(program_id);
//					if(downloadInfo.containsKey(program_id))
//						downloadInfo.remove(program_id);
					if(downloadList.contains(pli))  downloadList.remove(pli);
				}else{
					cb.setChecked(true);
//					downloadIdList.add(program_id);
//					Map<String, String> map = new HashMap<String, String>();
//					map.put("url", pli.path);
//					map.put("title", pli.title);
//					downloadInfo.put(program_id, map);
//					downloadList.add(downloadInfo);
					downloadList.add(pli);
				}
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
		sure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				for( Map<String, String> map : downloadInfo.values()){
//					Log.i("ProgramListAcivity", map.get("title")+"------>"+map.get("url"));
//				}
				for(ProgramListInfo l : downloadList){
					downloadProgram(l);
				}
				dialog.dismiss();
			}
		});
		
	}
	
	/**
	 * 下载节目
	 * 
	 */
	@SuppressWarnings("unused")
	private void downloadProgram(ProgramListInfo programlistInfo) {
		// String state = ConfigUtils.DownloadState_WAITTING;
		int end = programlistInfo.path.lastIndexOf(".");
		String format = programlistInfo.path.substring(end);// 文件的格式
		String completeName = programlistInfo.title + format;
//		boolean isSame = isFileSame(completeName);// 判断节目是否已经被下载过
		boolean isSame = FileUtils.checkFileExists(ConfigUtils.SDDownloadPath+completeName);
		if (isSame) {
			Toast.makeText(this, "已经缓存", 0).show();
			return;
		}
		// 检查数据库看是否有正在下载的任务
		// DBUtil db = DBUtil.getInstance(this);
		// Cursor cursor = db.selectData(SQLHelper.TABLE_DOWNLOAD, null, null,
		// null, null, null, null);
		// int count = cursor.getCount();
		//
		// cursor.close();
		String thumb = programlistInfo.thumb;
		String path = ConfigUtils.getDownloadPath(this)
				+ completeName;

		// 初始化下载对象
		DownloadEntry downloadEntry = new DownloadEntry();
		downloadEntry.setProgram_id(programlistInfo.id);
		downloadEntry.setAuthor("无名");
		downloadEntry.setThumb(thumb);
		downloadEntry.setStoragePath(path);
		downloadEntry.setUrl(programlistInfo.path);
		downloadEntry.setTitle(programlistInfo.title);
		// 开始下载
		DownloadManager.getInstance().beginDownloadFile(this,
				downloadEntry, DBUtil.getInstance(this));

	}
	

	/**
	 * 取消编辑状态的设置
	 */
	private void cancelEditor() {
		ll_editor.setVisibility(View.GONE);
		ll_main_info.setPadding(0, 0, 0, 0);
		mProgramListAdapter.setIsEditor(false);
		mDragSortListView.setDragEnabled(false);
		mDragSortListView.setDividerHeight(0);
	}
	
	/**
	 * 提交更改信息
	 * @param mid
	 * @param programme_id
	 */
	private void submitEditInfo(String mid,String programme_id){
		ArrayList<ProgrammeEditInfo> list =new ArrayList<ProgrammeEditInfo>();
		String json;
		for(int i=0;i<bean.list.size();i++){
			ProgrammeEditInfo info=new ProgrammeEditInfo();
			info.setId(bean.list.get(i).program_id);
			info.setType(bean.list.get(i).type_id);
			info.setIs_delete(checkedIdList.contains(bean.list.get(i).program_id)?"1":"0");
			info.setTime(bean.list.get(i).timespan);
			Log.i("ProgramListActivity","info.setTime(bean.list.get(i).timespan)="+ bean.list.get(i).timespan);
			list.add(info);
		}
		Gson gson=new Gson();
		json=gson.toJson(list);
		Log.i("ProgramListActivity", "json:"+json);
		json=json.replace("\"", "-");
		
		HttpManage.editProgramme(mid, programme_id, json, 
				new AsyncHttpResponseHandler() {
					
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method stub
						isPlayingThisList = false;
						checkedIdList.clear();
//						mProgramListAdapter.notifyDataSetChanged();
						initData();
						progress.setVisibility(View.GONE);
					}
					
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						// TODO Auto-generated method stub
						MyToast.show("节目信息上传失败", ProgramListActivity.this);
						mProgramListAdapter.notifyDataSetChanged();
						progress.setVisibility(View.GONE);
					}
				});
	}

	private void programSort(final String programme_id) {
		String program_ids = "";
		for (int i = 0; i < bean.list.size(); i++) {
			program_ids += bean.list.get(i).id;
			if (bean.list.size() - 1 != i) {
				program_ids += ",";
			}
		}

		progress.setVisibility(View.VISIBLE);
		HttpManage.programListSort(programme_id, program_ids,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						String result = new String(arg2);
						Log.i("programListSort", result);
						// 排完序再操作
						submitEditInfo(mid, programme_id);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						MyToast.show("排序结果提交失败", ProgramListActivity.this);
					}
				});
	}


	private int[] id = { R.drawable.icon_collect_selected,
			R.drawable.icon_collect_small_line };

	private void favoriteProgramListAdd() {
		HttpManage.FavoriteListAdd(programme_id, mid, "1",
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						String result = new String(arg2);
						Log.i("ProgramListActivity", "收藏返回的数据：" + result);
						Map<String, String> map = Common.str3map(result);
						switch (Integer.valueOf(map.get("status"))) {
						case 0:
							changeDrawable(id[0]);
							Toast.makeText(ProgramListActivity.this, "节目单收藏成功",
									0).show();
							return;

						default:
							break;
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						// TODO Auto-generated method stub
						Log.i("ProgramListActivity", "收藏失败");
					}
				});
	}

	private void favoriteProgramListDel() {
		HttpManage.FavoriteListDel(programme_id, mid, "1",
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						String result = new String(arg2);
						Log.i("ProgramListActivity", "取消收藏返回的数据：" + result);
						Map<String, String> map = Common.str3map(result);
						switch (Integer.valueOf(map.get("status"))) {
						case 0:
							// 更换TextView上的图片
							// Drawable rightDrawable =
							// getResources().getDrawable(R.drawable.icon_collect_small_line);
							// rightDrawable.setBounds(0, 0,
							// rightDrawable.getMinimumWidth(),
							// rightDrawable.getMinimumHeight());
							// collect_number.setCompoundDrawables(null,
							// rightDrawable, null,null);
							changeDrawable(id[1]);
							Toast.makeText(ProgramListActivity.this,
									"节目单取消收藏成功", 0).show();
							return;

						default:
							break;
						}

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						// TODO Auto-generated method stub

					}
				});
	}

	// 更换TextView上的图片
	private void changeDrawable(int drawableID) {
		Drawable rightDrawable = getResources().getDrawable(drawableID);
		rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(),
				rightDrawable.getMinimumHeight());
		collect_number.setCompoundDrawables(null, rightDrawable, null, null);
	}
}
