package com.example.toolbar.common.utils;

import android.annotation.SuppressLint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;


public class MyJsonUtils {

	@SuppressLint("NewApi")
	public static boolean isPostSuccess(String json) {

		if (json == null) {
			return false;
		}
		if (json.isEmpty()) {
			return false;
		}

		if (json.contains("error_code")) {
			return false;
		} else {
			return true;
		}
	}

	@SuppressLint("NewApi")
	public static boolean isGetListSuccess(String listjson) {
		if (listjson == null) {
			return false;
		}
		if (listjson.isEmpty()) {
			return false;
		}
		if (listjson.contains("error_code")) {
			return false;
		} else {
			return true;
		}

	}

	@SuppressLint("NewApi")
	public static boolean isCheckStringState(String value) {
		if (value == null) {
			return false;
		}
		if (value.isEmpty()) {
			return false;
		}
		Map<String, String> map = Common.str2mapstr(value);

		if (map.containsKey("status") && map.get("status").equals("0")) {
			return true;
		} else {
			return false;
		}

	}

	public static boolean isCheckMapState(Map<String, String> value) {
		if (value == null) {
			return false;
		}
		if (value.isEmpty()) {
			return false;

		}
		if (value.containsKey("status") && value.get("status").equals("0")) {
			return true;
		} else {
			return false;
		}

	}

	
	@SuppressLint("NewApi")
	public static List<Map<String, String>> getListForJson(String key, String json_content){
		JSONObject jsonObject = null;
		List<Map<String, String>> dataList = new ArrayList<Map<String,String>>();
		//LogHelper.e("json_content:" + json_content);
		if (key.isEmpty() || json_content.isEmpty()) {
			return dataList;
		}
		try {
			jsonObject = new JSONObject(json_content);
			dataList = Common.strtolist(jsonObject.getString(key));			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dataList;
	}
	
	@SuppressLint("NewApi")
	public static Map<String, String> getMapForJson(String key, String json_content){
		JSONObject jsonObject = null;
		Map<String, String> dataMap = new HashMap<String, String>();
		//LogHelper.e("json_content:" + json_content);
		if (key.isEmpty() || json_content.isEmpty()) {
			return dataMap;
		}
		try {
			jsonObject = new JSONObject(json_content);
			dataMap = Common.str2mapstr(jsonObject.getString(key));
			return dataMap;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dataMap;
	}
	
	public static String getStringForJson(String key, String json_content){
		JSONObject jsonObject = null;
		String data = "";
		try {
			jsonObject = new JSONObject(json_content);
			data =jsonObject.getString(key);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
	
	public static String getArrayForJson(String key, String json_content){
		JSONObject jsonObject = null;
		String data = "";
		try {
			jsonObject = new JSONObject(json_content);
			data =jsonObject.getJSONArray(key).toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
	
	public static String getObjectForJson(String key, String json_content){
		JSONObject jsonObject = null;
		String data = "";
		try {
			jsonObject = new JSONObject(json_content);
			data =jsonObject.getJSONObject(key).toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
}
