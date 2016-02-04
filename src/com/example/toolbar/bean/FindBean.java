package com.example.toolbar.bean;

import java.util.ArrayList;

public class FindBean {
	public ArrayList<IconTop> radio_list;
	public ArrayList<ProgramClassify> type_list;

	public static class IconTop {
		public String avatar;
		public String id;
		public String nickname;
	}

	public static class ProgramClassify {
		public String id;
		public String title;
	}
	
	
}
