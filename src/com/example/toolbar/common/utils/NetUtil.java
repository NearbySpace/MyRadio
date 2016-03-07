package com.example.toolbar.common.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class NetUtil {
	public static boolean isNetConnected(Context context) {
		boolean isNetConnected;
		// 获得网络连接服务
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connManager.getActiveNetworkInfo();
		if (info != null && info.isAvailable()) {
			// String name = info.getTypeName();
			// L.i("当前网络名称：" + name);
			isNetConnected = true;
		} else {
			Toast.makeText(context, "没有可用网络,请先连接网络！", 1).show();
			isNetConnected = false;
		}
		return isNetConnected;
	}
	
	//判断WIFI网络是否可用
		public boolean isWifiConnected(Context context) {  
		    if (context != null) {  
		        ConnectivityManager mConnectivityManager = (ConnectivityManager) context  
		                .getSystemService(Context.CONNECTIVITY_SERVICE);  
		        NetworkInfo mWiFiNetworkInfo = mConnectivityManager  
		                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);  
		        if (mWiFiNetworkInfo != null) {  
		            return mWiFiNetworkInfo.isAvailable();  
		        }  
		    }  
		    return false;  
		}
		
		//判断MOBILE网络是否可用
		public boolean isMobileConnected(Context context) {  
		    if (context != null) {  
		        ConnectivityManager mConnectivityManager = (ConnectivityManager) context  
		                .getSystemService(Context.CONNECTIVITY_SERVICE);  
		        NetworkInfo mMobileNetworkInfo = mConnectivityManager  
		                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);  
		        if (mMobileNetworkInfo != null) {  
		            return mMobileNetworkInfo.isAvailable();  
		        }  
		    }  
		    return false;  
		}
		
		
		/**
		 * 获取当前网络连接的类型信息	
		 * @param context
		 * @return  
		 */
		public static int getConnectedType(Context context) {  
		    if (context != null) {  
		        ConnectivityManager mConnectivityManager = (ConnectivityManager) context  
		                .getSystemService(Context.CONNECTIVITY_SERVICE);  
		        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();  
		        if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {  
		            return mNetworkInfo.getType();  
		        }  
		    }  
		    return -1;  
		}
}
