package com.example.toolbar.bean;

/**
 * [ { "id": "1", "title": "节目测试", "playtimes": "2", "thumb":
 * "http://vroad.bbrtv.com/cmradio/uploads/file/20150701/20150701050519_77228.jpg"
 * , "mid": "430", "owner": "张三" }, { "id": "2", "title": "test", "playtimes":
 * "2", "thumb":
 * "http://vroad.bbrtv.com/cmradio/uploads/file/20150702/20150702111703_66744.jpg"
 * , "mid": "430", "owner": "张三" } ]
 * 
 * */

public class Favorite {
	private String id;
	private String title;
	private String playtimes;
	private String thumb;
	private String mid;
	private String owner;
	private String path;
	private String list_type;

	public Favorite(String id, String title, String playtimes, String thumb,
			String mid, String owner,String path) {
		super();
		this.id = id;
		this.title = title;
		this.playtimes = playtimes;
		this.thumb = thumb;
		this.mid = mid;
		this.owner = owner;
		this.path=path;
	}
	
	

	public String getList_type() {
		return list_type;
	}



	public void setList_type(String list_type) {
		this.list_type = list_type;
	}



	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
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
