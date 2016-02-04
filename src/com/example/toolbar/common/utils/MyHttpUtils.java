package com.example.toolbar.common.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.ResponseStream;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;

public class MyHttpUtils {

	public static final String UTF_8 = "UTF-8";
	public static final String DESC = "descend";
	public static final String ASC = "ascend";

	private final static int TIMEOUT_CONNECTION = 20000; // 20秒
	private final static int TIMEOUT_SOCKET = 20000;
	private final static int RETRY_TIME = 3;
	
	public final static int DOWN_STATE_START = 10;
	public final static int DOWN_STATE_LOADDING = 11;
	public final static int DOWN_STATE_SUCCESS = 12;
	public final static int DOWN_STATE_FAILURE = 13;
	
	private static Map<String, String> dataMap;
	
	private static HttpHandler httpHandler;

	private static HttpClient getHttpClient() {
		HttpClient httpClient = new HttpClient();
		// 设置 HttpClient 接收 Cookie,用与浏览器一样的策略
		httpClient.getParams().setCookiePolicy(
				CookiePolicy.BROWSER_COMPATIBILITY);
		// 设置 默认的超时重试处理策略
		httpClient.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());
		// 设置 连接超时时间
		httpClient.getHttpConnectionManager().getParams()
				.setConnectionTimeout(TIMEOUT_CONNECTION);
		// 设置 读数据超时时间
		httpClient.getHttpConnectionManager().getParams()
				.setSoTimeout(TIMEOUT_SOCKET);
		// 设置 字符集
		httpClient.getParams().setContentCharset(UTF_8);
		return httpClient;
	}

	/**
	 * get请求URL
	 * 
	 * @param url
	 * @throws AppException
	 */
	@SuppressLint("NewApi")
	public static String getString(String url) {

		HttpClient httpClient = null;
		GetMethod httpGet = null;
		if (url == null || url.isEmpty()) {
			return "";
		}
		String responseBody = "";
		int time = 0;
		do {
			try {
				httpClient = getHttpClient();
				httpGet = new GetMethod(url);
				int statusCode = httpClient.executeMethod(httpGet);
				if (statusCode != HttpStatus.SC_OK) {
					LogHelper.d("" + statusCode);
					return "";
				}
				responseBody = httpGet.getResponseBodyAsString();
				// System.out.println("XMLDATA=====>"+responseBody);
				break;
			} catch (HttpException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生致命的异常，可能是协议不对或者返回的内容有问题
				e.printStackTrace();
				LogHelper.d("" + e.toString());
			} catch (IOException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生网络异常
				e.printStackTrace();
				LogHelper.d("" + e.toString());
			} finally {
				// 释放连接
				if (httpGet != null) {
					httpGet.releaseConnection();
				}
				httpClient = null;
			}
		} while (time < RETRY_TIME);

		responseBody = responseBody.replaceAll("\\p{Cntrl}", "");

		return responseBody;
	}

	/**
	 * 获取网络图片
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getNetBitmap(String url) {
		HttpClient httpClient = null;
		GetMethod httpGet = null;
		Bitmap bitmap = null;
		int time = 0;
		do {
			try {
				httpClient = getHttpClient();
				httpGet = new GetMethod(url);
				int statusCode = httpClient.executeMethod(httpGet);
				if (statusCode != HttpStatus.SC_OK) {
					LogHelper.d("" + statusCode);
				}
				BitmapFactory.Options opt = new BitmapFactory.Options();
				opt.inPreferredConfig = Bitmap.Config.RGB_565;
				opt.inPurgeable = true;
				opt.inInputShareable = true;
				InputStream inStream = httpGet.getResponseBodyAsStream();
				bitmap = BitmapFactory.decodeStream(inStream, null, opt);
				inStream.close();
				break;
			} catch (HttpException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生致命的异常，可能是协议不对或者返回的内容有问题
				e.printStackTrace();
			} catch (IOException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生网络异常
				e.printStackTrace();
			} finally {
				// 释放连接
				httpGet.releaseConnection();
				httpClient = null;
			}
		} while (time < RETRY_TIME);
		return bitmap;
	}

	/**
	 * 公用post方法
	 * 
	 * @param url
	 * @param params
	 * @param files
	 * @throws AppException
	 */
	@SuppressLint("NewApi")
	public static String httpPost(String url, Map<String, Object> params,
			Map<String, File> files) {
		if (url == null || url.isEmpty()) {
			return "";
		}

		HttpClient httpClient = getHttpClient();
		if (httpClient == null) {
			return "";
		}
		PostMethod httpPost = null;
		try {
			httpPost = new PostMethod(url);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("httpUtils", "PostMethod_Error");
			return "";
		}

		// post表单参数处理
		int length = (params == null ? 0 : params.size())
				+ (files == null ? 0 : files.size());
		Part[] parts = new Part[length];
		int i = 0;
		if (params != null)
			for (String name : params.keySet()) {
				parts[i++] = new StringPart(name, String.valueOf(params
						.get(name)), UTF_8);
				// System.out.println("post_key==> "+name+"    value==>"+String.valueOf(params.get(name)));
			}
		if (files != null)
			for (String file : files.keySet()) {
				try {
					parts[i++] = new FilePart(file, files.get(file));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				// System.out.println("post_key_file==> "+file);
			}

		String responseBody = "";
		int time = 0;
		do {
			try {
				httpPost.setRequestEntity(new MultipartRequestEntity(parts,
						httpPost.getParams()));
				int statusCode = httpClient.executeMethod(httpPost);
				if (statusCode != HttpStatus.SC_OK) {
					System.out.println(statusCode);
					return "";
				} else if (statusCode == HttpStatus.SC_OK) {

				}
				responseBody = httpPost.getResponseBodyAsString();
				// System.out.println("XMLDATA=====>"+responseBody);
				break;
			} catch (HttpException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生致命的异常，可能是协议不对或者返回的内容有问题
				e.printStackTrace();

			} catch (IOException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生网络异常
				e.printStackTrace();

			} finally {
				// 释放连接
				httpPost.releaseConnection();
				httpClient = null;
			}
		} while (time < RETRY_TIME);

		return responseBody;
	}

	// 同步请求 get ---xutils框架
	public static String httpGet(String url) {
		String result = "";
		com.lidroid.xutils.HttpUtils http = new com.lidroid.xutils.HttpUtils(
				15 * 1000);// 设置链接超时
		http.configCurrentHttpCacheExpiry(1000 * 4); // 缓存2秒
		http.configTimeout(20 * 1000);/* 从连接池中取连接的超时时间 */
		http.configSoTimeout(20 * 1000);/* 请求超时 */
		try {
			ResponseStream responseStream = http.sendSync(HttpMethod.GET, url);
			// int statusCode = responseStream.getStatusCode();
			// Header[] headers =
			// responseStream.getBaseResponse().getAllHeaders();
			result = responseStream.readString();
		} catch (Exception e) {
			LogUtils.e(e.getMessage(), e);
		}

		return result;
	}

	// 同步请求 post---xutils框架
	public static String httpPost(String url, RequestParams params) {
		String result = "";
		com.lidroid.xutils.HttpUtils http = new com.lidroid.xutils.HttpUtils(
				15 * 1000);
		http.configCurrentHttpCacheExpiry(1000 * 4); // 缓存2秒
		http.configTimeout(20 * 1000);/* 从连接池中取连接的超时时间 */
		http.configSoTimeout(20 * 1000);/* 请求超时 */
		try {
			ResponseStream responseStream = http.sendSync(HttpMethod.POST, url,
					params);
			result = responseStream.readString();
			LogHelper.e(responseStream.getStatusCode() + "");
			// http.configRequestThreadPoolSize(Runtime.getRuntime().availableProcessors());
		} catch (Exception e) {
			LogUtils.e(e.getMessage(), e);
		}

		return result;
	}
	/**
	 * 异步调用Http
	 * @param url
	 * @param handler 回调信息 what != 0 代表失败
	 */
	public static void httpGetAsync(String url, final Handler handler) {

		com.lidroid.xutils.HttpUtils http = new com.lidroid.xutils.HttpUtils(
				15 * 1000);
		http.configCurrentHttpCacheExpiry(1000 * 4); // 缓存4秒
		http.configTimeout(20 * 1000);/* 从连接池中取连接的超时时间 */
		http.configSoTimeout(20 * 1000);/* 请求超时 */
		http.configRequestThreadPoolSize(10);// 设置线程数
		http.send(HttpMethod.GET, url, new RequestCallBack<String>() {

			@Override
			public void onFailure(
					com.lidroid.xutils.exception.HttpException error, String msg) {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(1);
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				// TODO Auto-generated method stub
				Message msg = new Message();
				msg.what = 0;
				msg.obj = responseInfo.result;
				handler.sendMessage(msg);
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				// TODO Auto-generated method stub
				super.onLoading(total, current, isUploading);
				// handler.sendMessage(msg)
			}
		});
	}
	
	/**
	 * 下载框架
	 * @param url 链接
	 * @param target 目标地址
	 * @param handler 回调
	 */
	public static void httpDownload(String url, String target,
			final Handler handler) {

		com.lidroid.xutils.HttpUtils http = new com.lidroid.xutils.HttpUtils();
		// http.configCurrentHttpCacheExpiry(1000 * 4); // 缓存4秒
		// http.configTimeout(20 * 1000);/* 从连接池中取连接的超时时间 */
		// http.configSoTimeout(20 * 1000);/* 请求超时 */
		// http.configRequestThreadPoolSize(10);// 设置线程数
		
		dataMap = new HashMap<String, String>();
		httpHandler  = http.download(url,//链接
				target, //目标地址
				true,// 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
				false,// 如果从请求返回信息中获取到文件名，是否下载完成后自动重命名。
				new RequestCallBack<File>() {

			@Override
			public void onStart() {
				LogHelper.e("conn...");
				handler.sendEmptyMessage(DOWN_STATE_START);
			}

			@Override
			public void onSuccess(ResponseInfo<File> responseInfo) {
				// TODO Auto-generated method stub
				Message message = new Message();
				message.what = DOWN_STATE_SUCCESS;
				message.obj = responseInfo.result.getPath();
				handler.sendMessage(message);
				LogHelper.e("success");
			}

			@Override
			public void onFailure(
					com.lidroid.xutils.exception.HttpException error, String msg) {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(DOWN_STATE_FAILURE);
				LogHelper.e("error" + msg);
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				// TODO Auto-generated method stub
				LogHelper.e("onLoading..." + total +"|"+ current);
				super.onLoading(total, current, isUploading);
				Message message = new Message();
				message.what = DOWN_STATE_LOADDING;
				dataMap.put("total", total + "");
				dataMap.put("current", current + "");
				message.obj = dataMap;
				handler.sendMessageDelayed(message, 1000);
			}
		});		
		
	}
	
	public static void cancelDownload(){
		if (httpHandler != null) {
			httpHandler.cancel();
		}
	}

}
