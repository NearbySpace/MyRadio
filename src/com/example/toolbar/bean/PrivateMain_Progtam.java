package com.example.toolbar.bean;

public class PrivateMain_Progtam {
	private String id;
	private String title;
	private String program_ids;
	private String playtimes;
	private String thumb;
	private String program_num;
	
	public PrivateMain_Progtam(String title, String program_ids,
			String playtimes, String thumb, String program_num) {
		super();
		this.title = title;
		this.program_ids = program_ids;
		this.playtimes = playtimes;
		this.thumb = thumb;
		this.program_num = program_num;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getProgram_ids() {
		return program_ids;
	}
	public void setProgram_ids(String program_ids) {
		this.program_ids = program_ids;
	}
	public String getPlaytimes() {
		return playtimes;
	}
	public void setPlaytimes(String playtimes) {
		this.playtimes = playtimes;
	}
	public String getThumb() {
		return thumb;
	}
	public void setThumb(String thumb) {
		this.thumb = thumb;
	}
	public String getProgram_num() {
		return program_num;
	}
	public void setProgram_num(String program_num) {
		this.program_num = program_num;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

}
/**
 * "programme": [ { "title": "节目单添加测试", "program_ids": "1,2,4,6,7,8,9,10,11",
 * "playtimes": "0", "thumb":
 * "http://vroad.bbrtv.com/cmradio/uploads/file/20150724/20150724095638_31747.jpg"
 * , "program_num": 9 } ],
 */
