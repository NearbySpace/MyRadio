package com.example.toolbar.download;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.toolbar.bean.DownloadEntry;
import com.example.toolbar.db.DBUtil;
import com.example.toolbar.db.SQLHelper;
import com.example.toolbar.utils.ConfigUtils;
import com.example.toolbar.utils.ToastUtils;

public class DownloadManager {
	private static DownloadManager instance = null;
	public static DownloadManager getInstance() {
		if (instance == null) {
			instance = new DownloadManager();
		}
		return instance;
	}

	public interface DownloadStatusListener {
		void onCreate(DownloadEntry entry, boolean exists);

		void onStart(DownloadEntry entry);

		void onProgress(DownloadEntry entry, int speed);

		void onCancel(DownloadEntry entry);

		void onFinish(DownloadEntry entry);

		void onFailed(DownloadEntry entry);

		void onPause(DownloadEntry entry);

		void onBatchCreate(List<DownloadEntry> tasks);
	}

	public static class SimpleDownloadStatusListener implements
			DownloadManager.DownloadStatusListener {
		@Override
		public void onCreate(DownloadEntry entry, boolean exists) {
		}

		@Override
		public void onStart(DownloadEntry entry) {
		}

		@Override
		public void onProgress(DownloadEntry entry, int speed) {
		}

		@Override
		public void onCancel(DownloadEntry entry) {
		}

		@Override
		public void onFinish(DownloadEntry entry) {
		}

		@Override
		public void onFailed(DownloadEntry entry) {
		}

		@Override
		public void onPause(DownloadEntry entry) {

		}

		@Override
		public void onBatchCreate(List<DownloadEntry> tasks) {
		}
	}
	
	private ArrayList<DownloadManager.DownloadStatusListener> mDownloadStatusListeners = new ArrayList<DownloadManager.DownloadStatusListener>();
	public void addDownloadStatusListener(
			DownloadManager.DownloadStatusListener l) {
		mDownloadStatusListeners.add(l);
	}

	public void removeDownloadStatusListener(
			DownloadManager.DownloadStatusListener l) {
		mDownloadStatusListeners.remove(l);
	}

	public ArrayList<DownloadManager.DownloadStatusListener> getDownloadStatusListeners() {
		return mDownloadStatusListeners;
	}
	
	private LinkedList<DownloadEntry> mQueue = new LinkedList<DownloadEntry>();//下载队列

	public LinkedList<DownloadEntry> getDownloadQueue() {
		return mQueue;
	}
	
	public void initDownloadFile(){
		
	}
	
	public void beginDownloadFile(Context context,DownloadEntry downloadEntry,DBUtil db){
		Boolean isSame=false;
		for(int i=0;i<DownloadManager.getInstance().getDownloadQueue().size();i++){
			String url=DownloadManager.getInstance().getDownloadQueue().get(i).getUrl();
			if(url.equals(downloadEntry.getUrl())){
				isSame=true;
			}
		}
//		if(DownloadManager.getInstance().getDownloadQueue().contains(downloadEntry)){
//			
//		}
//		DownloadManager.getInstance().getDownloadQueue().contains(downloadEntry)
		if (!isSame) {
			DownloadManager.getInstance().getDownloadQueue()
			.addLast(downloadEntry);
			// 将该条数据插入数据库
//			ContentValues values = new ContentValues();
//			values.put("id", downloadEntry.getId());
//			values.put("name", downloadEntry.getTitle());
//			values.put("url", downloadEntry.getUrl());
//			values.put("storage_path", downloadEntry.getStoragePath());
			if (DownloadManager.getInstance().getDownloadQueue().size() == 1 ) {
				downloadEntry.setState(ConfigUtils.DownloadState_DOING);
				new DownloadTask(context,downloadEntry,db).execute();
//				values.put("state", ConfigUtils.DownloadState_DOING);
//				mQueue.add(downloadEntry);
//				Toast.makeText(context, "正在下载", 0).show();
				ToastUtils.showShort(context, "正在下载");
			} else {
//				Toast.makeText(context, "已加入下载队列，等待下载", 0).show();
				ToastUtils.showShort(context, "已加入下载队列，等待下载");
//				values.put("state", ConfigUtils.DownloadState_WAITTING);
				downloadEntry.setState(ConfigUtils.DownloadState_WAITTING);
			}
//			db.insertData(SQLHelper.TABLE_DOWNLOAD, values);
//			Log.i("AAAAA", "数据库插入了一条下载信息");
		}else{
//			Toast.makeText(context, "下载任务已经存在", 0).show();
			ToastUtils.showShort(context, "下载任务已经存在");
		}
	}
}
