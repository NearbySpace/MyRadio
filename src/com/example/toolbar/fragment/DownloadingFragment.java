package com.example.toolbar.fragment;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.strawberryradio.R;
import com.example.toolbar.bean.DownloadEntry;
import com.example.toolbar.common.utils.ImageLoaderHelper;
import com.example.toolbar.db.DBUtil;
import com.example.toolbar.db.SQLHelper;
import com.example.toolbar.download.DownloadManager;
import com.example.toolbar.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DownloadingFragment extends Fragment {
	private View view;
	private View headView;
	private ListView lv;
	private TextView tv_hint;

	private ImageView head_image;
	private TextView head_title;
	private TextView head_speed;
	private TextView head_percent;
	private TextView head_state;
	private ProgressBar head_pb;

	private List<DownloadEntry> list;
	private List<DownloadEntry> waitDownloadList;
	private DownloadEntry headDownloadEntry;
	private MyAdapter adapder;
	private ArrayList<DownloadEntry> mFileList = new ArrayList<DownloadEntry>();
	private String speed1 = 0 + "";
	private ImageLoader mImageLoader;
	private final String TAG = "DownloadingFragment";

	DownloadManager.DownloadStatusListener mDownloadStatusListener = new DownloadManager.SimpleDownloadStatusListener() {
		
		@Override
		public void onStart(DownloadEntry entry) {
			// TODO Auto-generated method stub
			super.onStart(entry);
			initHeadView();
		}

		@Override
		public void onProgress(DownloadEntry entry, int speed) {
			super.onProgress(entry, speed);
			speed1 = Utils.formatSize((float) speed);
			updateProgress(entry, speed1);
		}

		@Override
		public void onFinish(DownloadEntry entry) {
			// TODO Auto-generated method stub
			super.onFinish(entry);
			if (adapder == null)
				adapder = new MyAdapter();
			else
				adapder.notifyDataSetChanged();
		}

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mImageLoader = ImageLoader.getInstance();
		waitDownloadList = new ArrayList<DownloadEntry>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = LayoutInflater.from(getActivity()).inflate(
				R.layout.fragment_downloading, null);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		// initHeadView();
		lv = (ListView) view.findViewById(R.id.downloading_lv);
		tv_hint = (TextView) view.findViewById(R.id.downloading_fragment_tv);
		initData();
		if (list.size() != 0) {
			Log.i("DownloadingFragment", "list长度：" + list.size());
			tv_hint.setVisibility(View.GONE);
			adapder = new MyAdapter();
			lv.setAdapter(adapder);
		} else {
			tv_hint.setVisibility(View.VISIBLE);
			lv.setAdapter(new MyAdapter());
		}
		lv.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (lv.getChildAt(lv.getFirstVisiblePosition()) != null) {
					Log.i(TAG, "headView不是空");
					 initHeadView();
					DownloadManager.getInstance().addDownloadStatusListener(
							 mDownloadStatusListener);
				} else {
					Log.i(TAG, "headView是空");
				}
			}
		});
		// if(lv.getChildAt(lv.getFirstVisiblePosition()) != null){
		// initHeadView();
		// }else{
		// Log.i(TAG, "headView是空");
		// }
		 
	}

	private void initHeadView() {
		if (lv.getChildAt(lv.getFirstVisiblePosition()) != null) {
			Log.i(TAG, "initHeadView()----->headView不是空");
//			 initHeadView();
		} else {
			Log.i(TAG, "initHeadView()----->headView是空");
		}
		headView = lv.getChildAt(lv.getFirstVisiblePosition());
		head_image = (ImageView) headView.findViewById(R.id.downloading_iv);
		head_pb = (ProgressBar) headView.findViewById(R.id.download_pb_item);
		head_percent = (TextView) headView
				.findViewById(R.id.download_percent_item);
		head_speed = (TextView) headView.findViewById(R.id.download_speed_item);
		head_state = (TextView) headView.findViewById(R.id.download_wait_item);
		head_title = (TextView) headView.findViewById(R.id.download_title_item);
		head_pb.setVisibility(View.VISIBLE);
		head_state.setVisibility(View.GONE);
	}

	private void initData() {
		list = DownloadManager.getInstance().getDownloadQueue();
		// if (1 < list.size()) {
		// for (int i = 0; i < list.size(); i++) {
		// if (!list.get(i).getState().equals("1")) {
		// waitDownloadList.add(list.get(i));
		// }
		// }
		// }else{
		// waitDownloadList.clear();
		// }
		// if (headDownloadEntry != null) {
		// mImageLoader.displayImage(headDownloadEntry.getThumb(), head_image,
		// ImageLoaderHelper.getDisplayImageOptions());
		// head_title.setText(headDownloadEntry.getTitle());
		// }

		// list=new ArrayList<DownloadEntry>();
		// DBUtil db=DBUtil.getInstance(getActivity());
		// Cursor cursor=db.selectData(SQLHelper.TABLE_DOWNLOAD, null, null,
		// null, null, null, null);
		// while(cursor.moveToNext()){
		// DownloadEntry bean=new DownloadEntry();
		// bean.setId(cursor.getInt(cursor.getColumnIndex("id")));
		// bean.setTitle(cursor.getString(cursor.getColumnIndex("name")));
		// bean.setUrl(cursor.getString(cursor.getColumnIndex("url")));
		// bean.setStoragePath(cursor.getString(cursor.getColumnIndex("storage_path")));
		// bean.setState(cursor.getString(cursor.getColumnIndex("state")));
		// list.add(bean);
		// }

	}

	private void updateProgress(DownloadEntry entry, String speed) {
		head_pb.setProgress(entry.getFileProgress());
		head_percent.setText(entry.getFileProgress() + "%");
		head_speed.setText(speed);

	}

	@Override
	public void onDestroy() {
		DownloadManager.getInstance().removeDownloadStatusListener(
				mDownloadStatusListener);
		super.onDestroy();
	}

	class MyAdapter extends BaseAdapter {
		private DownloadEntry entry;
		Holder holder;

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		String state;

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.item_download, null);
				holder = new Holder();
				holder.image = (ImageView) convertView
						.findViewById(R.id.downloading_iv);
				holder.title = (TextView) convertView
						.findViewById(R.id.download_title_item);
				holder.speed = (TextView) convertView
						.findViewById(R.id.download_speed_item);
				holder.percent = (TextView) convertView
						.findViewById(R.id.download_percent_item);
				holder.pb = (ProgressBar) convertView
						.findViewById(R.id.download_pb_item);
				holder.state = (TextView) convertView
						.findViewById(R.id.download_wait_item);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
			entry = list.get(position);

			holder.title.setText(entry.getTitle());
			mImageLoader.displayImage(entry.getThumb(), holder.image,
					ImageLoaderHelper.getDisplayImageOptions());
			state = entry.getState();
			holder.pb.setVisibility(View.GONE);
			holder.state.setVisibility(View.VISIBLE);
			if (state.equals("4")) {
				holder.state.setText("下载失败");
			}
			// if(state.equals("1")){
			// holder.percent.setText(entry.getFileProgress()+"%");
			// holder.pb.setVisibility(View.VISIBLE);
			// holder.state.setVisibility(View.GONE);
			// holder.speed.setText(speed1);
			// holder.pb.setProgress(entry.getFileProgress());
			// }else{
			//
			// holder.pb.setVisibility(View.GONE);
			// holder.state.setVisibility(View.VISIBLE);
			// if(state.equals("4")){
			// holder.state.setText("下载失败");
			// }
			//
			// }
			return convertView;
		}

		class Holder {
			ImageView image;
			TextView title;
			TextView speed;
			TextView percent;
			TextView state;
			ProgressBar pb;
		}

	}

}
