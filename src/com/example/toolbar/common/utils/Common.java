package com.example.toolbar.common.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

/*
 * 常用工具类
 * 
 */

public class Common {

	// 获取通用列表数据
	public static List<Map<String, String>> strtolist(String data) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		if (data == null) {
			return list;
		}

		if (data.length() < 4) {
			return list;
		}
		if (!data.contains("[")) {
			return list;
		}

		if (!MyJsonUtils.isGetListSuccess(data)) {
			return list;
		}

		try {
			Gson gson = new Gson();
			Type type = new TypeToken<List<Map<String, String>>>() {
			}.getType();
			list = gson.fromJson(data, type);
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	// 获取通用列表数据
	@SuppressLint("NewApi")
	public static List<Map<String, String>> getList(String url) {
		String data = "";
		data = MyHttpUtils.getString(url);
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		if (data.isEmpty()) {
			return list;
		}
		
		try {
			Gson gson = new Gson();
			Type type = new TypeToken<List<Map<String, String>>>() {
			}.getType();
			list = gson.fromJson(data, type);
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}

		return list;
	}

	/*
	 * 
	 * 获取通用 详情
	 */
	public static Map<String, String> getDetail(String url) {
		Map<String, String> map = new HashMap<String, String>();
		if (url == null) {
			return map;
		}
		String content = MyHttpUtils.httpGet(url);
		Log.i("common", content);
		if (content == null) {
			return map;
		}

		if (!content.contains("{") || content.length() < 4) {
			return map;
		}

		try {
			Gson gson = new Gson();
			Type type = new TypeToken<Map<String, String>>() {
			}.getType();
			map = gson.fromJson(content, type);
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}

	// json字符 to map
	public static Map<String, Object> str2map(String content) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (content == null || content.length() < 4) {
			return map;
		}
		if (!content.contains("{")) {
			return map;
		}

		Gson gson = new Gson();
		Type type = new TypeToken<Map<String, Object>>() {
		}.getType();
		map = gson.fromJson(content, type);
		return map;
	}

	// json字符 to map
	public static Map<String, String> str3map(String content) {
		Map<String, String> map = new HashMap<String, String>();
		if (content == null || content.length() < 4) {
			return map;
		}
		if (!content.contains("{")) {
			return map;
		}

		Gson gson = new Gson();
		Type type = new TypeToken<Map<String, String>>() {
		}.getType();
		map = gson.fromJson(content, type);
		return map;
	}

	// 获取详细内容，从JSON 转到 Map
	public static Map<String, String> getMapContent(String url) {
		Map<String, String> map = new HashMap<String, String>();
		String content = MyHttpUtils.httpGet(url);
		if (content == null || content.length() < 4) {
			return map;
		}

		if (!content.contains("{")) {
			return map;
		}

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject = new JSONObject(content);
			map = new HashMap<String, String>();
			Iterator<String> iterator = jsonObject.keys();
			String key = null;
			String value = null;
			while (iterator.hasNext()) {
				key = iterator.next();
				value = jsonObject.getString(key);
				map.put(key, value);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			// return map;
		}

		return map;
	}

	// json格式字符串 转到 map格式 一维
	public static Map<String, String> str2mapstr(String content) {
		Map<String, String> map = new HashMap<String, String>();
		if (content == null || content.length() < 4) {
			return map;
		}
		if (!content.contains("{")) {
			return map;
		}

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject = new JSONObject(content);
			Iterator<String> iterator = jsonObject.keys();
			String key = null;
			String value = null;
			while (iterator.hasNext()) {
				key = iterator.next();
				value = jsonObject.getString(key);
				map.put(key, value);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		// Iterator<String> iterator = jsonObject.keys();
		// String key = null;
		// String value = null;
		// while (iterator.hasNext()) {
		// key = iterator.next();
		// try {
		// value = jsonObject.getString(key);
		// } catch (JSONException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// map.put(key, value);
		// }

		return map;
	}

	/**
	 * 通过url 获取图片
	 * 
	 * @param imgUrl
	 * @return Bitmap
	 */
	public static Bitmap getBitmapFromUrl(String imgUrl) {
		URL url;
		Bitmap bitmap = null;
		try {
			url = new URL(imgUrl);
			InputStream is = url.openConnection().getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			bitmap = BitmapFactory.decodeStream(bis);
			bis.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return bitmap;
	}

	/**
	 * 将json对象转换成Map
	 * 
	 * @param jsonObject
	 *            json对象
	 * @return Map对象
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> json2Map(JSONObject jsonObject)
			throws JSONException {
		Map<String, String> result = new HashMap<String, String>();
		Iterator<String> iterator = jsonObject.keys();
		String key = null;
		String value = null;
		while (iterator.hasNext()) {
			key = iterator.next();
			value = jsonObject.getString(key);
			result.put(key, value);
		}
		return result;
	}

	// 安装APK
	public static void installApk(Context context, String fileName) {
		File path = Environment.getExternalStorageDirectory();
		File newPath = new File(path.toString() + fileName);
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(newPath),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	// 秒转 时间 12:24:23
	public static String formatLongToTimeStr(int total) {
		String str = "";
		int hour = 0, minute = 0, second = 0;

		if (total > 60) {
			minute = total / 60;
		}
		second = total % 60;
		if (minute > 60) {
			hour = minute / 60;
			minute = minute % 60;
		}
		str = hour + ":" + minute + ":" + second;

		return str;
	}

	// 比较两个时间的大小
	public static int timeCompare(String time1, String time2) {
		int result = -2;

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			Date d1 = df.parse(time1);
			Date d2 = df.parse(time2);

			if ((d1.getTime() / 1000) > (d2.getTime() / 1000))
				result = 1;
			else if (d1.getTime() / 1000 == d2.getTime() / 1000)
				result = 0;
			else
				result = -1;
		} catch (Exception e) {
		}

		return result;
	}

	public static long lastClickTime;

	public static boolean isFastClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < 1200) {
			return true;
		}
		lastClickTime = time;
		return false;
	}

	// 获取手机分辨率
	public static DisplayMetrics getMetrics(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		return dm;
	}

	// 获取屏幕宽度
	public static int getDisplayWidth(Context context) {
		return ((Activity) context).getWindowManager().getDefaultDisplay()
				.getWidth();
	}

	// 获取屏幕高度
	public static int getDisplayHeight(Context context) {
		return ((Activity) context).getWindowManager().getDefaultDisplay()
				.getHeight();
	}

	public static void log(String str) {
		LogHelper.d(str);
	}

	// 同步请求 get
	// public static String httpGet(String url) {
	// String result = "";
	// HttpUtils http = new HttpUtils();
	// http.configCurrentHttpCacheExpiry(1000 * 2); // 缓存2秒
	// try {
	// ResponseStream responseStream = http.sendSync(HttpMethod.GET, url);
	// // int statusCode = responseStream.getStatusCode();
	// // Header[] headers =
	// // responseStream.getBaseResponse().getAllHeaders();
	// result = responseStream.readString();
	// } catch (Exception e) {
	// LogUtils.e(e.getMessage(), e);
	// }
	//
	// return result;
	// }
	//
	// // 同步请求 post
	// public static String httpPost(String url, RequestParams params) {
	// String result = "";
	// HttpUtils http = new HttpUtils();
	// http.configCurrentHttpCacheExpiry(1000 * 2); // 缓存2秒
	// try {
	// ResponseStream responseStream = http.sendSync(HttpMethod.POST, url,
	// params);
	// // int statusCode = responseStream.getStatusCode();
	// // Header[] headers =
	// // responseStream.getBaseResponse().getAllHeaders();
	// result = responseStream.readString();
	// } catch (Exception e) {
	// LogUtils.e(e.getMessage(), e);
	// }
	//
	// return result;
	// }

}
