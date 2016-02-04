package com.example.toolbar.activity;

import java.util.Map;

import org.apache.http.Header;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.strawberryradio.R;
import com.example.toolbar.adapter.AddProgramAdapter;
import com.example.toolbar.adapter.PMprogerAdapter;
import com.example.toolbar.application.MyApplication;
import com.example.toolbar.bean.PrivateMain;
import com.example.toolbar.common.utils.ImageLoaderHelper;
import com.example.toolbar.common.utils.LogHelper;
import com.example.toolbar.http.HttpManage;
import com.example.toolbar.utils.ImageUtils;
import com.example.toolbar.view.MyListView;
import com.example.toolbar.view.OpAnimationView;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class LikeShowsActivity extends AppCompatActivity implements
		OnClickListener {
	private ShareActionProvider mShareActionProvider;
	private Toolbar mToolbar;
	private ImageLoader mImageLoader;
	private String pathString;

	private MyListView listView;
	// private ImageView show_img;// 节目封面
	private ImageView user_icon;// 用户头像
	private TextView title;

	private TextView nickname;// 昵称
	private TextView letter;// 私信

	private TextView collect_number;// 收藏数
	private TextView showlist_name;// 节目单
	private TextView focus_number;// 关注数
	private TextView shows_number;// 节目数
	private TextView fans_number;// 粉丝数
	private OpAnimationView opAnimationView;
	private Boolean isRgihtShape = false;
	private AddProgramAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_like_show);
		initViews();
		initData();
	}

	private void initViews() {

		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		// toolbar.setLogo(R.drawable.ic_launcher);
		mToolbar.setTitle("喜欢的节目");
		// getSupportActionBar().setTitle("");
		// getSupportActionBar().setSubtitle("");
		// getSupportActionBar().setLogo(R.drawable.ic_launcher);
		setSupportActionBar(mToolbar);
		mToolbar.setNavigationContentDescription(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mToolbar.setNavigationOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				finish();
			}
		});

		user_icon = (ImageView) findViewById(R.id.user_icon);
		title = (TextView) findViewById(R.id.titel);
		
		nickname = (TextView) findViewById(R.id.nickname);

		collect_number = (TextView) findViewById(R.id.collect_number);
		showlist_name = (TextView) findViewById(R.id.showlist_name);
		fans_number = (TextView) findViewById(R.id.fans_number);
		shows_number = (TextView) findViewById(R.id.shows_number);
		focus_number = (TextView) findViewById(R.id.focus_number);
		opAnimationView = (OpAnimationView) findViewById(R.id.focus);
		opAnimationView.setOnClickListener(this);

		listView = (MyListView) findViewById(R.id.showListView1);
		listView.setOnItemClickListener(new onItemClickListener());

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
	}

	private void initData() {
		mImageLoader = ImageLoader.getInstance();
		String zid = MyApplication.getInstance().getSpUtil().getUid();
		String mid = MyApplication.getInstance().getSpUtil().getUid();
		HttpManage.getPrivateData(new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				String result = new String(arg2);

				Gson gson = new Gson();
				PrivateMain pm = gson.fromJson(result, PrivateMain.class);
				LogHelper.e("获取返回值：" + result);
				nickname.setText(pm.getNickname());
				pathString = pm.getAvatar();
//				mImageLoader.displayImage(pm.getAvatar(), user_icon,
//						ImageLoaderHelper.getDisplayImageOptions());

				PMprogerAdapter pmAdapter = new PMprogerAdapter(
						LikeShowsActivity.this, pm.getProgramme());
				LogHelper.e("获取节目列表：" + pm.getProgramme());
				listView.setAdapter(pmAdapter);
				getHeadIcon();
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				LogHelper.e("获取数据失败");

			}
		}, zid, mid);
		// getListdata();
	}

	// 加载头像
	@SuppressLint("NewApi")
	private void getHeadIcon() {
//		pathString = MyApplication.getInstance().getSpUtil().getHeadIcon();
		if (!pathString.isEmpty()) {
			// LogHelper.e(pathString);
			new AsyncTask<Void, Void, Bitmap>() {

				@Override
				protected Bitmap doInBackground(Void... arg0) {
					// TODO Auto-generated method stub
					mImageLoader = ImageLoader.getInstance();
					Bitmap bitmap = mImageLoader.loadImageSync(pathString,
							ImageLoaderHelper.getDisplayImageOptions());
					return bitmap;
				}

				@Override
				protected void onPostExecute(Bitmap result) {
					// TODO Auto-generated method stub
					super.onPostExecute(result);
					if (result == null) {
						user_icon.setImageResource(R.drawable.user_icon);
					} else {
						user_icon.setImageBitmap(ImageUtils.getRoundBitmap(
								LikeShowsActivity.this, result));
						result = null;
					}
				}
			}.execute();
		} else {
			@SuppressWarnings("static-access")
			Bitmap bitmap = new BitmapFactory().decodeResource(getResources(),
					R.drawable.user_icon);
			user_icon.setImageBitmap(ImageUtils.getRoundBitmap(
					LikeShowsActivity.this, bitmap));
			if (!bitmap.isRecycled()) {
				bitmap.recycle();
				bitmap = null;
			}
		}
	}

	private void fill() {

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

	public class onItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long arg3) {
			// TODO Auto-generated method stub
			@SuppressWarnings("unchecked")
			Map<String, String> dataMap = (Map<String, String>) adapter
					.getItem(position);
			// 获得选中项的HashMap对象
			String title = dataMap.get("itemTitle");
			String content = dataMap.get("itemContent");
			Toast.makeText(
					getApplicationContext(),
					"你选择了第" + position + "个Item，itemTitle的值是：" + title
							+ "itemContent的值是:" + content, Toast.LENGTH_SHORT)
					.show();
			// IntentUtils.startActivityForString(HomeWorkActivity.this,
			// HomeworkItemActivity.class, "id", dataMap.get("id"));
		}

	}

	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.focus:
			if (isRgihtShape) {
				LogHelper.e("点击关注：" + isRgihtShape);
				isRgihtShape = false;
				opAnimationView.right2add();
			} else {
				LogHelper.e("取消关注：" + isRgihtShape);
				isRgihtShape = true;
				opAnimationView.add2right();

			}
			break;
		}
	}
}
