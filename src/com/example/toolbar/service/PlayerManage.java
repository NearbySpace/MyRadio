package com.example.toolbar.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.toolbar.bean.PlayInfo;

public class PlayerManage {
	public static int position=0;
	private static PlayerManage instance=null;
	public static PlayerManage getInstance(){
		if(instance==null){
			instance=new PlayerManage();
		}
		return instance;
	}
	
	
	private List<PlayInfo> mList=new ArrayList<PlayInfo>();// 歌曲列表信息 
//	private List<PlayInfo> classifyList = new ArrayList<PlayInfo>();
	private Map<String,ArrayList<PlayInfo>> classifyMap =new HashMap<String, ArrayList<PlayInfo>>();
	public void addPlayInfo(PlayInfo info){
		mList.add(info);
	}
	
	public void clearList(){
		mList.clear();
	}
	
	public List<PlayInfo> getPlayInfos(){
		return mList;
	}
	
	public void addPlayInfoForClassifyList(ArrayList<PlayInfo> list,String classifyId){
		classifyMap.put(classifyId, list);
	}
	
	public void clearListForClassifyList(){
		classifyMap.clear();
	}
	
	public Map<String,ArrayList<PlayInfo>> getPlayInfosForClassifyList(){
		return classifyMap;
	}
	
}
