package com.example.toolbar.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Top implements Parcelable {
	private String id;
	private String title;
	private String thumb;
	private String path;



	public Top(String id, String title, String thumb, String path) {
		super();
		this.id = id;
		this.title = title;
		this.thumb = thumb;
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

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(id);
		dest.writeString(title);
		dest.writeString(thumb);
		dest.writeString(path);
	}

	public static final Parcelable.Creator<Top> CREATOR = new Parcelable.Creator<Top>() {
		public Top createFromParcel(Parcel in) {
			return new Top(in);
		}

		public Top[] newArray(int size) {
			return new Top[size];
		}
	};
	
	private Top(Parcel in) {
		id = in.readString();
		title = in.readString();
		thumb = in.readString();
		path = in.readString();
	}

}
