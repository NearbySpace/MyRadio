package com.example.toolbar.bean;

import java.io.Serializable;

public class DownloadedBean implements Serializable {
	private String programId;
	private String thumb;
	private String name;
	private String size;
	private String author;
	private String storagePath;
	//用于标记CheckBox的状态，防止listview的item复用的时候checkbox的状态混乱
	private Boolean checked_state = false; 
	
	
	public Boolean getChecked_state() {
		return checked_state;
	}
	public void setChecked_state(Boolean checked_state) {
		this.checked_state = checked_state;
	}
	public String getProgramId() {
		return programId;
	}
	public void setProgramId(String programId) {
		this.programId = programId;
	}
	public String getThumb() {
		return thumb;
	}
	public void setThumb(String thumb) {
		this.thumb = thumb;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getStoragePath() {
		return storagePath;
	}
	public void setStoragePath(String storagePath) {
		this.storagePath = storagePath;
	}
	
}
