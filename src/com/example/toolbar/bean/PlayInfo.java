package com.example.toolbar.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class PlayInfo implements Parcelable{
	/*
	 * { "id": "418", "title": "《萨瓦迪卡》泰国小吃", "brief": "", "path":
	 * "http://vroad.bbrtv.com/myradio/uploads/file/20150412/20150412212744_35452.mp3"
	 * , "addtime": "1428845412", "uid": "218", "playtimes": "12", "zantimes":
	 * "0", "thumb":
	 * "http://vroad.bbrtv.com/myradio/uploads/file/20150412/20150412212946_32283.jpg"
	 * , "sortid": "100", "stars": "0.00"
	 */
	private String id;
	private String title;
	private String brief;
	private String path;
	private String addtime;
	private String uid;
	private String playtimes;
	private String zantimes;
	private String thumb;
	private String sortid;
	private String stars;
	private String owner;
	private String timespan;
	private String type_id;


	public PlayInfo(String id, String title, String brief, String path,
			String addtime, String uid, String playtimes, String zantimes,
			String thumb, String sortid, String stars,String owner,String timespan,
			String type_id) {
		super();
		this.id = id;
		this.title = title;
		this.brief = brief;
		this.path = path;
		this.addtime = addtime;
		this.uid = uid;
		this.playtimes = playtimes;
		this.zantimes = zantimes;
		this.thumb = thumb;
		this.sortid = sortid;
		this.stars = stars;
		this.owner=owner;
		this.timespan=timespan;
		this.type_id=type_id;
	}
	
	public String getType_id() {
		return type_id;
	}



	public void setType_id(String type_id) {
		this.type_id = type_id;
	}


	public String getTimespan() {
		return timespan;
	}



	public void setTimespan(String timespan) {
		this.timespan = timespan;
	}



	public String getOwner() {
		return owner;
	}



	public void setOwner(String owner) {
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

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getPlaytimes() {
		return playtimes;
	}

	public void setPlaytimes(String playtimes) {
		this.playtimes = playtimes;
	}

	public String getZantimes() {
		return zantimes;
	}

	public void setZantimes(String zantimes) {
		this.zantimes = zantimes;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public String getSortid() {
		return sortid;
	}

	public void setSortid(String sortid) {
		this.sortid = sortid;
	}

	public String getStars() {
		return stars;
	}

	public void setStars(String stars) {
		this.stars = stars;
	}
	
	PlayInfo(Parcel in){
		this.id=in.readString();
		this.title=in.readString();
		this.brief=in.readString();
		this.path=in.readString();
		this.addtime=in.readString();
		this.uid=in.readString();
		this.playtimes=in.readString();
		this.zantimes=in.readString();
		this.thumb=in.readString();
		this.sortid=in.readString();
		this.stars=in.readString();
		this.owner=in.readString();
		this.timespan=in.readString();
		this.type_id=in.readString();
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(title);
		dest.writeString(brief);
		dest.writeString(path);
		dest.writeString(addtime);
		dest.writeString(uid);
		dest.writeString(playtimes);
		dest.writeString(zantimes);
		dest.writeString(thumb);
		dest.writeString(sortid);
		dest.writeString(stars);
		dest.writeString(owner);
		dest.writeString(timespan);
		dest.writeString(type_id);
	}
	
	public static final Parcelable.Creator<PlayInfo> CREATOR
		 = new Parcelable.Creator<PlayInfo>(){

			@Override
			public PlayInfo createFromParcel(Parcel source) {
				// TODO Auto-generated method stub
				return new PlayInfo(source);
			}

			@Override
			public PlayInfo[] newArray(int size) {
				// TODO Auto-generated method stub
				return new PlayInfo[size];
			}
		
	};

}
