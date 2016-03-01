package com.example.toolbar.bean;

import java.util.ArrayList;

public class PickerBean {
	public ArrayList<Classify> list;
	
	public static class Classify{
		public String id;
		public String thumb;
		public String title;
		public String content;
	}
}
