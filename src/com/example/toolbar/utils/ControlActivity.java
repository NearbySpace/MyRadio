package com.example.toolbar.utils;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

/**
 * 保存Activity的类
 * 
 * @author huangjojo
 * 
 */
public class ControlActivity {
	private  List<Activity> list=new ArrayList<Activity>();
	private static ControlActivity instance;
	public static ControlActivity getInstance() {
	        if (null == instance) {
	            instance = new ControlActivity();
	        }
	        return instance;
	    }



	public void addActivity(Activity activity) {
//		if(list.contains(activity))return;
		list.add(activity);
	}
	
	public void removeActivity(Activity activity){
//		if(list.contains(activity))
		list.remove(activity);
	}

	public void removeAll() {
		for (int i = 0; i < list.size(); i++) {
			list.get(i).finish();
		}
		list.clear();
	}
	
	public void otherremoveAll(){
		

	}
	
}
