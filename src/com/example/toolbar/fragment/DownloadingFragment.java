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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.strawberryradio.R;
import com.example.toolbar.bean.DownloadEntry;
import com.example.toolbar.db.DBUtil;
import com.example.toolbar.db.SQLHelper;
import com.example.toolbar.download.DownloadManager;
import com.example.toolbar.utils.Utils;

public class DownloadingFragment extends Fragment {
	private View view;
	private ListView lv;
	private TextView tv_hint;
	private List<DownloadEntry> list;
	private MyAdapter adapder;
	private ArrayList<DownloadEntry> mFileList = new ArrayList<DownloadEntry>();
	private String speed1=0+"";
	private final String TAG="DownloadingFragment";
	
	DownloadManager.DownloadStatusListener mDownloadStatusListener = new DownloadManager.SimpleDownloadStatusListener(){

		@Override
		public void onProgress(DownloadEntry entry, int speed) {
			super.onProgress(entry, speed);
			speed1=Utils.formatSize((float)speed);
			if(adapder!=null)  adapder.notifyDataSetChanged();
		}

		@Override
		public void onFinish(DownloadEntry entry) {
			// TODO Auto-generated method stub
			super.onFinish(entry);
			if(adapder==null) adapder=new MyAdapter();
			else adapder.notifyDataSetChanged();
		}
		
		
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view=LayoutInflater.from(getActivity()).inflate(R.layout.fragment_downloading, null);
		DownloadManager.getInstance().addDownloadStatusListener(
				mDownloadStatusListener);
		return view;
	}
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		lv=(ListView) view.findViewById(R.id.downloading_lv);
		tv_hint=(TextView) view.findViewById(R.id.downloading_fragment_tv);
		initData();
		if(list.size()!=0){
			Log.i("DownloadingFragment", "list长度："+list.size());
			tv_hint.setVisibility(View.GONE);
			adapder=new MyAdapter();
			lv.setAdapter(adapder);
		}else{
			tv_hint.setVisibility(View.VISIBLE);
			lv.setAdapter(new MyAdapter());
		}
		
	}
	
	
	
	private void initData() {
		list=DownloadManager.getInstance().getDownloadQueue();
		
//		list=new ArrayList<DownloadEntry>();
//		DBUtil db=DBUtil.getInstance(getActivity());
//		Cursor cursor=db.selectData(SQLHelper.TABLE_DOWNLOAD, null, null, null, null, null, null);
//		while(cursor.moveToNext()){
//			DownloadEntry bean=new DownloadEntry();
//			bean.setId(cursor.getInt(cursor.getColumnIndex("id")));
//			bean.setTitle(cursor.getString(cursor.getColumnIndex("name")));
//			bean.setUrl(cursor.getString(cursor.getColumnIndex("url")));
//			bean.setStoragePath(cursor.getString(cursor.getColumnIndex("storage_path")));
//			bean.setState(cursor.getString(cursor.getColumnIndex("state")));
//			list.add(bean);
//		}
		
	}
	
	@Override
	public void onDestroy() {
		DownloadManager.getInstance().removeDownloadStatusListener(
				mDownloadStatusListener);
		super.onDestroy();
	}

	class MyAdapter extends BaseAdapter{
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
			if(convertView==null){
				convertView=LayoutInflater.from(getActivity()).inflate(R.layout.item_download, null);
				holder=new Holder();
				holder.title=(TextView) convertView.findViewById(R.id.download_title_item);
				holder.speed=(TextView) convertView.findViewById(R.id.download_speed_item);
				holder.percent=(TextView) convertView.findViewById(R.id.download_percent_item);
				holder.pb=(ProgressBar) convertView.findViewById(R.id.download_pb_item);
				holder.state=(TextView) convertView.findViewById(R.id.download_wait_item);
				convertView.setTag(holder);
			}else{
				holder=(Holder) convertView.getTag();
			}
			holder.title.setText(list.get(position).getTitle());
			state=list.get(position).getState();
			if(state.equals("1")){
				holder.percent.setText(list.get(position).getFileProgress()+"%");
				holder.pb.setVisibility(View.VISIBLE);
				holder.state.setVisibility(View.GONE);
				holder.speed.setText(speed1);
				holder.pb.setProgress(list.get(position).getFileProgress());
			}else{
				holder.pb.setVisibility(View.GONE);
				holder.state.setVisibility(View.VISIBLE);
				if(state.equals("4")){
					holder.state.setText("下载失败");
				}
				
			}
			return convertView;
		}
		
		class Holder{
			TextView title;
			TextView speed;
			TextView percent;
			TextView state;
			ProgressBar pb;
		}
		
	}

}
