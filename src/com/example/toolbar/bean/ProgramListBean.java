package com.example.toolbar.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class ProgramListBean{
	/**
	 * 
	 */
	public ArrayList<ProgramListInfo> list;
	public ProgrammeInfo row;
	
	public static class ProgrammeInfo{
		/**
		 * 
		 */
		public String member_name;
		public String member_thumb;
		public String programme_comment;
		public String programme_dl;
		public String programme_fav;
		public String programme_name;
		public String programme_share;
		public String programme_thumb;
		public String member_id;
		public String is_favorite;
	}
	
	public static class ProgramListInfo{
		/**
		 * 
		 */
		public String addtime;
		public String id;	//后台数据库中该节目的排列顺序号
//		public String mid;
		public String nickname;
		public String program_time;
		public String program_id;
		public String programme_id;
		public String title;
		public String path;
		public String thumb;
		public String timespan;
		public String sort;
		public String type_id;
		public boolean checkBox_state;
		public boolean download_checkBox_state;//用于标记下载弹出框中CheckBox的状态
		public ArrayList<ProgramInfo> contentlist;
		
		public static class ProgramInfo{
			public String id;
			public String mid;
			public String owner;
			public String path;
			public String program_time;
			public String thumb;
			public String title;
		}
	}

	
}
