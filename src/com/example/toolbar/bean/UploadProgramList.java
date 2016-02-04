package com.example.toolbar.bean;

import java.util.ArrayList;

public class UploadProgramList {
	public ArrayList<ProgramListInfo>  program_list;
	
	public static class ProgramListInfo{
		public ArrayList<ProgramInfo> type1;
		public ArrayList<ProgramClassifyInfo> type2;
		
		public static class ProgramInfo{
			public String time;
			public String id;
		}
		
		public static class ProgramClassifyInfo{
			public String time;
			public String id;
		}
	}
}
