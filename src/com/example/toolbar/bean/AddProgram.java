package com.example.toolbar.bean;

import java.util.List;

/*
 * 添加频道
 *  {
 "id": "1",
 "title": "节目测试",
 "thumb": "http://vroad.bbrtv.com/cmradio/uploads/file/20150701/20150701050519_77228.jpg",
 "program_time": "12:23",
 "mid": "430",
 "owner": "韦永斌"
 }
 */

public class AddProgram {
	public List<AddProgramInfo> list;
	
	public static class AddProgramInfo{
		public String id;
		public String title;
		public String program_time;
		public String thumb;
		public String mid;
		public String owner;
		public String path;
		public boolean checkBox_status; 
	}
	


}
/*
 * "id": "2", "title": "test", "playtimes": "0", "thumb":
 * "http://vroad.bbrtv.com/cmradio/uploads/file/20150702/20150702111703_66744.jpg"
 * , "mid": "430", "owner": "韦永斌"
 */