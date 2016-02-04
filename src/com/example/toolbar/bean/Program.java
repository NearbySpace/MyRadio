package com.example.toolbar.bean;

public class Program {
	private String id;
	private String title;
	private String playtimes;
	private String thumb;
	private String mid;
	private String owner;

	public Program(String id, String title, String playtimes, String thumb,
			String mid, String owner) {
		super();
		this.id = id;
		this.title = title;
		this.playtimes = playtimes;
		this.thumb = thumb;
		this.mid = mid;
		this.owner = owner;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	

}
/*
 * "id": "2", "title": "test", "playtimes": "0", "thumb":
 * "http://vroad.bbrtv.com/cmradio/uploads/file/20150702/20150702111703_66744.jpg"
 * , "mid": "430", "owner": "韦永斌"
 */