package com.example.toolbar.http;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class AsyncHttpClientUtils {
	/**
	 * get异步请求
	 * @param responseHandler
	 * @param url
	 */
	public static void getForAsyn(AsyncHttpResponseHandler responseHandler,String url){
		
		AsyncHttpClient httpClient = new AsyncHttpClient();
		httpClient.get(url, responseHandler);
	}
	
	/**
	 * post 异步请求
	 * @param responseHandler
	 * @param params
	 * @param url
	 */
	public static void  postForAsyn(AsyncHttpResponseHandler responseHandler,RequestParams params,String url){
		
		AsyncHttpClient httpClient = new AsyncHttpClient();
		httpClient.post(url,params,responseHandler);
	}

}
