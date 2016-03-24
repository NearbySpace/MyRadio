package com.example.toolbar.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.strawberryradio.R;
import com.example.toolbar.adapter.AddProgramAdapter;
import com.example.toolbar.adapter.AddProgramAdapter.OnItemClickClass;
import com.example.toolbar.application.MyApplication;
import com.example.toolbar.bean.AddProgram;
import com.example.toolbar.bean.AddProgram.AddProgramInfo;
import com.example.toolbar.common.utils.LogHelper;
import com.example.toolbar.http.HttpManage;
import com.example.toolbar.pulltorefreshlistview.PullToRefreshBase.OnRefreshListener;
import com.example.toolbar.pulltorefreshlistview.PullToRefreshListView;
import com.example.toolbar.view.MyToast;
import com.example.toolbar.view.progress.CircularProgress;
import com.example.toolbar.widget.MaterialDialog;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class AddProgramActivity extends AppCompatActivity implements
		OnClickListener {
	private final String TAG = "AddProgramActivity";
	private final int OK = 32;
	private ShareActionProvider mShareActionProvider;
	private Toolbar mToolbar;
	private Button sendInfo;
	private PullToRefreshListView mPullRefreshListView;
	private ListView listView;
	private TextView classify_name;
	private CheckBox cb_classify;
	private EditText et_set_time;
	private TextView load_fail_remind;
	private AddProgramAdapter adapter;
	private CircularProgress progress;
	private ArrayList<String> idList;
	private List<String> titleList;
	private AddProgram bean;
	private int page = 0;
	private String titel;
	private String typeId;
	private boolean isChange = false;
	private boolean isChecked = false;
	private final Timer timer = new Timer();
	private TimerTask task;
	private Intent intent;
	private MaterialDialog mMaterialDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addprogram);
		initViews();
		initData();
	}

	@SuppressWarnings("unchecked")
	private void initViews() {
		intent = new Intent();
		if (getIntent().getStringArrayListExtra("idlist") != null) {
			idList = getIntent().getStringArrayListExtra("idlist");
		} else {
			idList = new ArrayList<String>();
		}
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		// toolbar.setLogo(R.drawable.ic_launcher);
		mToolbar.setTitle("添加节目");
		// getSupportActionBar().setTitle("");
		// getSupportActionBar().setSubtitle("");
		// getSupportActionBar().setLogo(R.drawable.ic_launcher);

		setSupportActionBar(mToolbar);
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		listView = mPullRefreshListView.getRefreshableView();
		// listView.setOnItemClickListener(new MyOnItemClickListener());
		classify_name = (TextView) findViewById(R.id.add_program_classify_name);
		cb_classify = (CheckBox) findViewById(R.id.cb_add_program_classify);
		et_set_time = (EditText) findViewById(R.id.add_program_classify_duration);
		load_fail_remind = (TextView) findViewById(R.id.addProgram_remind);
		sendInfo = (Button) findViewById(R.id.send_info);
		sendInfo.setOnClickListener(this);
		mToolbar.setNavigationContentDescription(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		mToolbar.setNavigationOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				intent.putStringArrayListExtra("program_ids", null);
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		});
		progress = (CircularProgress) findViewById(R.id.progress);
		cb_classify.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					et_set_time.setVisibility(View.VISIBLE);
					et_set_time.setFocusable(true);
				} else {
					et_set_time.setVisibility(View.GONE);
				}
				AddProgramActivity.this.isChecked = isChecked;
			}
		});
		// toolbar监听
		// mToolbar.setOnMenuItemClickListener(new
		// Toolbar.OnMenuItemClickListener() {
		// @Override
		// public boolean onMenuItemClick(MenuItem item) {
		// switch (item.getItemId()) {
		// case R.id.action_settings:
		// Toast.makeText(AddProgramActivity.this, "action_settings",
		// 0).show();
		// break;
		// case R.id.action_share:
		// Toast.makeText(AddProgramActivity.this, "action_share", 0)
		// .show();
		// break;
		// default:
		// break;
		// }
		// return true;
		// }
		// });
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				if (mPullRefreshListView.getCurrentMode() == 1) { // 往下拉刷新
					page = 0;
					initData();
				} else {
					page += 1;
					initData();
				}

			}
		});
	}

	private void initData() {
		String uid = MyApplication.getInstance().getSpUtil().getUid();
		Bundle bundle = this.getIntent().getExtras(); /* 获取Bundle中的数据 */
		typeId = bundle.getString("id");
		Log.i(TAG, "typeId----->" + typeId);
		titel = bundle.getString("titel");
		classify_name.setText(this.getIntent().getStringExtra("classifyName")+"类");
		if(this.getIntent().getStringExtra("time") != null){
			cb_classify.setChecked(true);
			et_set_time.setText(this.getIntent().getStringExtra("time"));
			isChecked=true;
		}
		LogHelper.e("获取节目标题：" + titel);
		HttpManage.getAddProgramlist(new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				progress.setVisibility(View.GONE);
				String result = new String(arg2);
				Gson gson = new Gson();
				bean = gson.fromJson(result, AddProgram.class);
				for(AddProgramInfo info : bean.list){
					if(idList.isEmpty()){
						break;
					}
					for(String id :idList){
						if(info.id.equals(id)){
							info.checkBox_status = true;
						}
					}
				}
				adapter = new AddProgramAdapter(AddProgramActivity.this,
						bean.list, onItemClickClass);
				listView.setAdapter(adapter);
				mPullRefreshListView.onRefreshComplete();
				load_fail_remind.setVisibility(View.GONE);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				load_fail_remind.setVisibility(View.VISIBLE);
			}
		}, typeId);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getMenuInflater().inflate(R.menu.main, menu);
		/* ShareActionProvider */
		// mShareActionProvider = (ShareActionProvider) MenuItemCompat
		// .getActionProvider(menu.findItem(R.id.action_share));
		// Intent intent = new Intent(Intent.ACTION_SEND);
		// intent.setType("text/*");
		// mShareActionProvider.setShareIntent(intent);
		return super.onCreateOptionsMenu(menu);
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// getMenuInflater().inflate(R.menu.main, menu);
	// /* ShareActionProvider */
	// mShareActionProvider = (ShareActionProvider) MenuItemCompat
	// .getActionProvider(menu.findItem(R.id.action_share));
	// Intent intent = new Intent(Intent.ACTION_SEND);
	// intent.setType("text/*");
	// mShareActionProvider.setShareIntent(intent);
	// return super.onCreateOptionsMenu(menu);
	// }

	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// switch (item.getItemId()) {
	// case R.id.action_settings:
	// Toast.makeText(AddProgramActivity.this, "action_settings", 0)
	// .show();
	// break;
	// case R.id.action_share:
	// Toast.makeText(AddProgramActivity.this, "action_share", 1).show();
	// break;
	// default:
	// break;
	// }
	// return super.onOptionsItemSelected(item);
	// }
	AddProgramAdapter.OnItemClickClass onItemClickClass = new OnItemClickClass() {
		public void OnItemClick(View v, int Position, CheckBox checkBox) {
			AddProgramInfo data = (AddProgramInfo) adapter.getItem(Position);
			if (checkBox.isChecked()) {
				checkBox.setChecked(false);
				// 获得选中项的对象
				String id = data.id;
				idList.remove(id);
				bean.list.get(Position).checkBox_status = false;
				Log.i(TAG, "img remove position->" + Position);
			} else {
				checkBox.setChecked(true);
				Log.i(TAG, "img choise position->" + Position);
				// 获得选中项的对象
				String id = data.id;
				// title = (new StringBuilder(" ")).append(title).toString();
				idList.add(id);
				bean.list.get(Position).checkBox_status = true;
			}
		}
	};

	// class MyOnItemClickListener implements OnItemClickListener {
	//
	// @Override
	// public void onItemClick(AdapterView<?> parent, View view, int position,
	// long arg3) {
	// LogHelper.e("  " + adapter.getItem(position));
	// AddProgram data = (AddProgram) adapter.getItem(position);
	// // 获得选中项的对象
	// // String id = data.getId();
	// CheckBox checkBox=(CheckBox) view.findViewById(R.id.select_img);
	// if (checkBox.isChecked()) {
	// checkBox.setChecked(false);
	// // 获得选中项的对象
	// String id = data.getId();
	// idList.remove(id);
	// Log.i(TAG, "img choise position->" + position);
	// } else {
	// checkBox.setChecked(true);
	// Log.i(TAG, "img remove position->" + position);
	// // 获得选中项的对象
	// String id = data.getId();
	// // title = (new StringBuilder(" ")).append(title).toString();
	// idList.add(id);
	// }
	// // titel = (new StringBuilder(" ")).append(titel).toString();
	// // idList.add(id);
	// // idList.remove(id);
	// // Toast.makeText(getApplicationContext(),
	// // "你选择了第" + position + "选中的子项数:" + titleList.size(),
	// // Toast.LENGTH_SHORT).show();
	// // IntentUtils.startActivityForString(HomeWorkActivity.this,
	// // HomeworkItemActivity.class, "id", dataMap.get("id"));
	// }
	//
	// }

	private void sendProgram() {
		String uid = MyApplication.getInstance().getSpUtil().getUid();
		String id;
		Log.i(TAG, "idList--------->" + idList);
		if (idList != null && idList.size() != 0) {
			LogHelper.e("获取节目ID：" + idList.get(0));
			// LogHelper.e("获取节目标题：" + titel);
			id = idList.get(0);
			// StringBuffer s4 = new StringBuffer(idList.get(0));
			if (idList.size() >= 1) {
				for (int i = 1; i < idList.size(); i++) {
					id += "," + idList.get(i).toString();
					// s4.append(","+idList.get(i).toString());
					// id = (new
					// StringBuilder(",")).append(idList.get(i)).toString();
				}
				LogHelper.e("节目ID数组---->" + id);
			}
			HttpManage.sendProgram(new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
					progress.setVisibility(View.GONE);
					String result = new String(arg2);
					LogHelper.e(result);
					MyToast.show("节目添加成功", getApplicationContext());
					task = new TimerTask() {

						@Override
						public void run() {
							MyToast.removeView();

						}

					};
					timer.schedule(task, 2000);
					// finish();
				}

				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2,
						Throwable arg3) {
					String result = new String(arg2);
					LogHelper.e(result);
					Toast.makeText(AddProgramActivity.this, "添加节目失败，请重试一次", 0)
							.show();
				}
			}, uid, id, titel);
		} else {
			Toast.makeText(AddProgramActivity.this, "没有选择要添加的节目，请选择", 0).show();
		}

	}

	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.send_info:
			// endSize=idList.size();
			// isChange=endSize-startSize>0;
			// if(!isChange){
			// intent.putStringArrayListExtra("program_ids", null);
			// }
			if (isChecked) {
				intent.putExtra("classifyId", typeId);
				if (!et_set_time.getText().toString().trim().isEmpty()){
					intent.putExtra("time", et_set_time.getText().toString().trim());
				}else {
					intent.putExtra("time", 5 + "");
				}
			}else{
				
			} 
			intent.putStringArrayListExtra("program_ids", idList);
			setResult(OK, intent);
			finish();
			// dialog();
			break;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			intent.putStringArrayListExtra("program_ids", null);
			setResult(Activity.RESULT_OK, intent);
			finish();
		}
		return false;

	}

	private void dialog() {
		mMaterialDialog = new MaterialDialog(this);
		if (mMaterialDialog != null) {
			// 不包含标题
			// final MaterialDialog materialDialog = new MaterialDialog(this);
			mMaterialDialog.setMessage("是否添加已选节目")
					.setPositiveButton("确定", new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							sendProgram();
							Toast.makeText(AddProgramActivity.this, "上传中",
									Toast.LENGTH_LONG).show();
							mMaterialDialog.dismiss();
						}
					}).setNegativeButton("取消", new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Toast.makeText(AddProgramActivity.this, "取消",
									Toast.LENGTH_LONG).show();
							mMaterialDialog.dismiss();
						}
					}).setCanceledOnTouchOutside(false).show();
			// You can change the message anytime.
			// mMaterialDialog.setTitle("提示");
			// .setOnDismissListener(
			// new DialogInterface.OnDismissListener() {
			// @Override
			// public void onDismiss(DialogInterface dialog) {
			// Toast.makeText(AddProgramActivity.this,
			// "消失了onDismiss", Toast.LENGTH_SHORT)
			// .show();
			// }
			// }).show();

		}
	}
}
