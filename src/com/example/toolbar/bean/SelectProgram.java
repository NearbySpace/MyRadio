package com.example.toolbar.bean;

/*
 * 
 *  {
 "id": "1",
 "title": "情感",
 "thumb": "",
 "program_count": "4"
 }
 */
public class SelectProgram {
	private String id;
	private String title;
	private String thumb;
	private String program_count;

	public SelectProgram(String id, String title, String thumb,
			String program_count) {
		super();
		this.id = id;
		this.title = title;
		this.thumb = thumb;
		this.program_count = program_count;
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

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public String getProgram_count() {
		return program_count;
	}

	public void setProgram_count(String program_count) {
		this.program_count = program_count;
	}

}
