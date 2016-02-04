package com.example.toolbar.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class ProgramClassifyListBean implements Parcelable{
	public ArrayList<Info> list;
	
	ProgramClassifyListBean(Parcel source) {
		this.list=new ArrayList<Info>();
		source.readTypedList(list, Info.CREATOR);
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeTypedList(list);
		
	}
	
	public static final Parcelable.Creator<ProgramClassifyListBean> CREATOR=new Parcelable.Creator<ProgramClassifyListBean>(){

		@Override
		public ProgramClassifyListBean createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new ProgramClassifyListBean(source);
		}

		@Override
		public ProgramClassifyListBean[] newArray(int size) {
			// TODO Auto-generated method stub
			return new ProgramClassifyListBean[size];
		}
		
	};
	

	
	
	public static class Info implements Parcelable{
		public String id;
		public String mid;
		public String owner;
		public String path;
		public String program_time;
		public String thumb;
		public String title;
		
		Info(Parcel source) {
			id=source.readString();
			mid=source.readString();
			owner=source.readString();
			path=source.readString();
			program_time=source.readString();
			thumb=source.readString();
			title=source.readString();
		}
		
		@Override
		public int describeContents() {
			// TODO Auto-generated method stub
			return 0;
		}
		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(id);
			dest.writeString(mid);
			dest.writeString(owner);
			dest.writeString(path);
			dest.writeString(program_time);
			dest.writeString(thumb);
			dest.writeString(title);
		}
		
		public static final Parcelable.Creator<Info> CREATOR=new Parcelable.Creator<Info>(){

			
			@Override
			public Info createFromParcel(Parcel source) {
				// TODO Auto-generated method stub
				return new Info(source);
			}

			@Override
			public Info[] newArray(int size) {
				// TODO Auto-generated method stub
				return new Info[size];
			}
			
		};
		
		
	}

}
