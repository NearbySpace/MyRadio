package com.example.toolbar.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Click_Ranking implements Parcelable {
	private String id;
	private String title;
	private String playtimes;
	private String thumb;
	private String mid;
	private String owner;
	private String path;
	/*
	 * "id": "1", "title": "\u8282\u76ee\u6d4b\u8bd5", "playtimes": "0",
	 * "thumb":
	 * "http:\/\/vroad.bbrtv.com\/cmradio\/uploads\/file\/20150701\/20150701050519_77228.jpg",
	 * "mid": "430", "owner": "\u97e6\u6c38\u658c" ,
	 */

	public Click_Ranking(String id, String title, String playtimes,
			String thumb, String mid, String owner, String path) {
		super();
		this.id = id;
		this.title = title;
		this.playtimes = playtimes;
		this.thumb = thumb;
		this.mid = mid;
		this.owner = owner;
		this.path = path;
	}
	
	public static final Parcelable.Creator<Click_Ranking> CREATOR = new Parcelable.Creator<Click_Ranking>() {
		public Click_Ranking createFromParcel(Parcel in) {
			return new Click_Ranking(in);
		}

		public Click_Ranking[] newArray(int size) {
			return new Click_Ranking[size];
		}
	};

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
		dest.writeString(playtimes);
		dest.writeString(thumb);
		dest.writeString(mid);
		dest.writeString(owner);
		dest.writeString(path);
	}
	
	private Click_Ranking(Parcel in) {
		id = in.readString();
		title = in.readString();
		playtimes = in.readString();
		thumb = in.readString();
		mid = in.readString();
		owner = in.readString();
		path = in.readString();
	}

}
