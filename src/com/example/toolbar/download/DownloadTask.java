package com.example.toolbar.download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.PublicKey;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.dolphinradio.R;
import com.example.toolbar.activity.MyDownLoadActivity;
import com.example.toolbar.bean.DownloadEntry;
import com.example.toolbar.db.DBUtil;
import com.example.toolbar.db.SQLHelper;
import com.example.toolbar.utils.ConfigUtils;
import com.example.toolbar.utils.ToastUtils;
import com.example.toolbar.utils.Utils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

public class DownloadTask extends AsyncTask<Void, Object, Integer>{
	private String url;
	private String storagePath;
	private String name;
	private String state;
	private NotificationManager mNotificationManager;
	private NotificationCompat.Builder builder;
	private Notification mNotification;
	private RemoteViews mRemoteViews ;
	private Context mContext;
	private DownloadEntry downloadEntry;
	private DBUtil db;
	private int fileSize;
	private int downloadedSize=0;//已下载量
	private int oldDownloadedSize=0;
	private int speed=0;
	private final int ID=2015115;
	private boolean isFinish=false;
	private static String TAG="DownloadTask";

	public DownloadTask(Context context,DownloadEntry downloadEntry,DBUtil db) {
		super();
		this.mContext=context;
		this.downloadEntry=downloadEntry;
		this.url = downloadEntry.getUrl();
		this.storagePath = downloadEntry.getStoragePath();
		this.name = downloadEntry.getTitle();
		this.state = downloadEntry.getState();
		this.db=db;
	}
	
	

	@Override
	protected void onPreExecute() {
		mNotificationManager=(NotificationManager) mContext.getSystemService(mContext.NOTIFICATION_SERVICE);
		builder=new NotificationCompat.Builder(mContext);
		builder.setSmallIcon(R.drawable.notification_download_icon);
		builder.setAutoCancel(true);
		builder.setTicker("正在下载");
		mRemoteViews=new RemoteViews(mContext.getPackageName(),
				R.layout.download_notification_layout);
		builder.setContent(mRemoteViews);
		mRemoteViews.setTextViewText(R.id.downloadText,downloadEntry.getTitle() );
		builder.setWhen(System.currentTimeMillis());
		Intent intent=new Intent(mContext,MyDownLoadActivity.class);
		intent.putExtra("downloading", true);
		PendingIntent contentIntent = PendingIntent.getActivity(mContext, 10,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(contentIntent);
		mNotification=builder.build();
		mNotificationManager.notify(ID, mNotification);
		super.onPreExecute();
	}



	@Override
	protected Integer doInBackground(Void... params) {
		int isSuccess=0;
		for (DownloadManager.DownloadStatusListener l : DownloadManager
				.getInstance().getDownloadStatusListeners()) {
			l.onStart(downloadEntry);
		}
		try {
			URL downloadurl=new URL(url);
			HttpURLConnection conn=(HttpURLConnection) downloadurl.openConnection();
			conn.setConnectTimeout(5*1000);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");	//设置客户端可以接受的返回数据类型
			conn.setRequestProperty("Accept-Language", "zh-CN");	//设置客户端使用的语言问中文
			conn.setRequestProperty("Referer", url); 	//设置请求的来源，便于对访问来源进行统计
			conn.setRequestProperty("Charset", "UTF-8");	//设置通信编码为UTF-8
			conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");	//客户端用户代理
			conn.setRequestProperty("Connection", "Keep-Alive");	//使用长连接
			conn.setRequestProperty("Accept-Encoding", "identity"); 
			int code = conn.getResponseCode();
			if(code == 200){
				
				fileSize=conn.getContentLength();
				Log.i(TAG, "fileSize--->"+fileSize);
				if(fileSize<=0) {
//					throw new Exception("unkonw file size");
					return isSuccess = 0;
				}
				InputStream is=conn.getInputStream();
				
				FileOutputStream fos=new FileOutputStream(storagePath);
				Log.i(TAG, "storagePath2--->"+storagePath);
				byte[] buffer = new byte[4*1024];	//设置本地数据缓存的大小为5k
				int offset = 0;	//设置每次读取的数据量
				//每一秒更新一次进度
				new Thread(new Runnable() {
					@Override
					public void run() {
						while(!isFinish){
							publishProgress(downloadedSize,speed);
							try {
								Thread.sleep(1000);
								speed=(downloadedSize - oldDownloadedSize);
								oldDownloadedSize=downloadedSize;
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						Log.i(TAG, "下载进度线程结束");
					}
				}).start();
				
				while((offset=is.read(buffer))!=-1){
					fos.write(buffer);
					downloadedSize+=offset;
				}
				this.isFinish=true;
				fos.flush();
				isSuccess=1;
				is.close();
				fos.close();
			}
			
		} catch (MalformedURLException e) {
			Log.i(TAG, "URL有误");
			e.printStackTrace();
		} catch (IOException e) {
			Log.i(TAG, "获取inputStream流失败");
			e.printStackTrace();
		} catch (Exception e) {
			Log.i(TAG, "获取文件长度不正确");
			e.printStackTrace();
		}
		return isSuccess;
	}
	
	
	
	@Override
	protected void onProgressUpdate(Object... values) {
		super.onProgressUpdate(values);
		int percent=(Integer)values[0]*100/fileSize;
		downloadEntry.setFileProgress(percent);
		mRemoteViews.setTextViewText(R.id.downloadSumText,percent+"%" );
		mRemoteViews.setProgressBar(R.id.downloadProgress, 100,
				percent, false);
		mNotificationManager.notify(ID, mNotification);
		for (DownloadManager.DownloadStatusListener l : DownloadManager
				.getInstance().getDownloadStatusListeners()) {
			l.onProgress(downloadEntry, (Integer) values[1]);
		}
		
	}



	@Override
	protected void onPostExecute(Integer result) {
		if(result==1){
//			Toast.makeText(mContext, "下载完成", Toast.LENGTH_LONG).show();
			mRemoteViews.setTextViewText(R.id.downloadSumText,"下载完成" );
			mRemoteViews.setProgressBar(R.id.downloadProgress, 100,
					100, false);
			Intent intent=new Intent(mContext,MyDownLoadActivity.class);
			intent.putExtra("downloading", false);
			PendingIntent contentIntent = PendingIntent.getActivity(mContext, 10,
					intent, PendingIntent.FLAG_UPDATE_CURRENT);
			builder.setContentIntent(contentIntent);
			mNotification=builder.build();
			mNotificationManager.notify(ID, mNotification);
			// 将该条数据插入已下载的数据库表
			ContentValues values = new ContentValues();
			if(downloadEntry.getProgram_id()!=null){
				values.put("program_id", downloadEntry.getProgram_id());
			}
			values.put("name", downloadEntry.getTitle());
			values.put("thumb", downloadEntry.getThumb());
			values.put("author", downloadEntry.getAuthor());
			values.put("storage_path", downloadEntry.getStoragePath());
			db.insertData(SQLHelper.TABLE_DOWNLOADED, values);
			if (!DownloadManager.getInstance().getDownloadQueue().isEmpty()){
				DownloadManager.getInstance().getDownloadQueue()
				.remove(downloadEntry);
//				db.deleteDownloadEntry(downloadEntry.getUrl());
				}
			//告诉DownlaodingFramgent,下载完毕更新数据更新
			downloadEntry.setFileProgress(100);
			for (DownloadManager.DownloadStatusListener l : DownloadManager
					.getInstance().getDownloadStatusListeners()) {
				l.onProgress(downloadEntry, 0);
				
				l.onFinish(downloadEntry);
			}
//			queueNext();
		}else{
			ToastUtils.showShort(mContext, "下载失败");
//			Toast.makeText(mContext, "下载失败", Toast.LENGTH_LONG).show();
			if (!DownloadManager.getInstance().getDownloadQueue().isEmpty()){
				DownloadManager.getInstance().getDownloadQueue()
				.remove(downloadEntry);
				}
		}
		queueNext();
		super.onPostExecute(result);
	}

//	public void Download(){
//		HttpUtils http=new HttpUtils();
//		HttpHandler<File> handler=http.download(
//				url, 
//				storagePath,
//				true, 
//				true, 
//				new RequestCallBack<File>(){
//			
//
//					@Override
//					public void onCancelled() {
//						// TODO Auto-generated method stub
//						super.onCancelled();
//					}
//
//					@Override
//					public void onLoading(long total, long current,
//							boolean isUploading) {
////						mNotificationManager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//						super.onLoading(total, current, isUploading);
//					}
//
//					@Override
//					public void onFailure(HttpException arg0, String arg1) {
//						Log.i("Download", "下载失败");
//						if (!DownloadManager.getInstance().getDownloadQueue().isEmpty()){
//							DownloadManager.getInstance().getDownloadQueue()
//							.remove(downloadEntry);
//							downloadEntry.setState(ConfigUtils.DownloadState_FAIL);
//							db.updateDownloadEntry(downloadEntry);
//						}
//						queueNext();
//					}
//
//					@Override
//					public void onSuccess(ResponseInfo<File> arg0) {
//						Log.i("Download", "下载成功");
//						Log.i(TAG, "name:"+downloadEntry.getTitle());
//						Log.i(TAG, "storage_path:"+downloadEntry.getStoragePath());
//						// 将该条数据插入已下载的数据库表
//						ContentValues values = new ContentValues();
////						values.put("id", downloadEntry.getId());
//						values.put("name", downloadEntry.getTitle());
//						values.put("thumb", downloadEntry.getThumb());
//						values.put("author", downloadEntry.getAuthor());
//						values.put("storage_path", downloadEntry.getStoragePath());
//						db.insertData(SQLHelper.TABLE_DOWNLOADED, values);
//						Log.i(TAG, "TABLE_DOWNLOADED------>条数据插入已下载的数据库表");
//						if (!DownloadManager.getInstance().getDownloadQueue().isEmpty()){
//							DownloadManager.getInstance().getDownloadQueue()
//							.remove(downloadEntry);
//							db.deleteDownloadEntry(downloadEntry.getUrl());
//							}
//						queueNext();
//					}
//			
//		} );
//	}
	
	private void queueNext() {
		if (!DownloadManager.getInstance().getDownloadQueue().isEmpty()) {
			final DownloadEntry downloadEntry = DownloadManager.getInstance()
					.getDownloadQueue().getFirst();
			if (downloadEntry != null
					&& !ConfigUtils.DownloadState_DOING.equals(downloadEntry.getState())) {
				downloadEntry.setState(ConfigUtils.DownloadState_DOING);
				new DownloadTask(mContext,downloadEntry,db).execute();
				
//				boolean isSuccess=db.updateDownloadEntry(downloadEntry);//更新数据库
//				if(isSuccess)Log.i("queueNext", "更新成功");else Log.i("queueNext", "更新失败");
			}
		} else {
//			for (DownloadManager.DownloadStatusListener l : DownloadManager
//					.getInstance().getDownloadStatusListeners()) {
//				l.onFinish(downloadEntry);
//			}
//			mNotificationManager.cancel(NOTIFY_ID);
			Toast.makeText(mContext, "已没有下载任务，全部下载完成", 0).show();
		}
	}

}
