package com.example.toolbar.download;

import android.content.Context;
import android.widget.Toast;

import com.example.toolbar.bean.DownloadEntry;
import com.example.toolbar.bean.PlayInfo;
import com.example.toolbar.common.utils.FileUtils;
import com.example.toolbar.db.DBUtil;
import com.example.toolbar.utils.ConfigUtils;

public class DownloadUtils {

	/**
	 * 初始化下载对象，并开始下载
	 * @param context
	 * @param playInfo
	 */
	public static void downloadProgram(Context context,PlayInfo playInfo) {
		// String state = ConfigUtils.DownloadState_WAITTING;
		String url = playInfo.getPath();
		String name = playInfo.getTitle();
		int end = url.lastIndexOf(".");
		String format = url.substring(end);// 文件的格式
		String completeName = name + format;
		// 判断节目是否已经被下载过
		boolean isSame = FileUtils.checkFileExists(ConfigUtils.SDDownloadPath
				+ completeName);
		if (isSame) {
			Toast.makeText(context, "已经缓存", 0).show();
			return;
		}
		// 检查数据库看是否有正在下载的任务
		// DBUtil db = DBUtil.getInstance(this);
		// Cursor cursor = db.selectData(SQLHelper.TABLE_DOWNLOAD, null, null,
		// null, null, null, null);
		// int count = cursor.getCount();
		//
		// cursor.close();
		String thumb = playInfo.getThumb();
		String path = ConfigUtils.getDownloadPath(context) + completeName;

		// 初始化下载对象
		DownloadEntry downloadEntry = new DownloadEntry();
		downloadEntry.setProgram_id(playInfo.getId());
		downloadEntry.setAuthor("无名");
		downloadEntry.setThumb(thumb);
		downloadEntry.setStoragePath(path);
		downloadEntry.setUrl(url);
		downloadEntry.setTitle(name);
		// 开始下载
		DownloadManager.getInstance().beginDownloadFile(context,
				downloadEntry, DBUtil.getInstance(context));

	}
}
