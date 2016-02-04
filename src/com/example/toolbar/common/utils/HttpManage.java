package com.example.toolbar.common.utils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


public class HttpManage {
	private static String address = "http://vroad.bbrtv.com/myradio/";// 目标服务端项目域名
	
	public static void getSeating(AsyncHttpResponseHandler responseHandler, String schoolID, String classID) {
		String url = address + "?d=pad&c=student&m=seat";
		RequestParams params = new RequestParams();
		params.put("schoolid", schoolID);// 临时调试参数2
		params.put("classname", classID);
		AsyncHttpClient httpClient = new AsyncHttpClient();
		httpClient.get(url, params, responseHandler);
	}

}
