package com.example.toolbar.dao;

import java.util.LinkedList;
import java.util.List;

import com.example.toolbar.bean.User;
import com.example.toolbar.db.UserSQLHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;



public class UserDao {
	private UserSQLHelper helper;

	public UserDao(Context context) {
		helper = new UserSQLHelper(context);
	}

	public User selectInfo(String userId) {
		User u = new User();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.rawQuery("select * from user where userId=?",
				new String[] { userId + "" });
		if (c.moveToFirst()) {
//			u.setUserId(c.getString(c.getColumnIndex("userid")));
			u.setNick(c.getString(c.getColumnIndex("nick")));
			u.setEmail(c.getString(c.getColumnIndex("email")));
			u.setTel(c.getString(c.getColumnIndex("tel")));
			u.setSgin(c.getString(c.getColumnIndex("sgin")));
		} else {
			return null;
		}
		return u;
	}

	public void addUser(List<User> list) {
		SQLiteDatabase db = helper.getWritableDatabase();
		for (User u : list) {
			db.execSQL(
					"insert into user (userId,nick,img,channelId,_group) values(?,?,?,?,?)",
					new Object[] { u.getUserId(),u.getEmail(), u.getNick(), u.getTel(),
							 u.getSgin() });
		}
		db.close();
	}

	public void addUser(User u) {
		if (selectInfo(u.getUserId()) != null) {
			update(u);
			return;
		}
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL(
				"insert into user (userId,nick,img,channelId,_group) values(?,?,?,?,?)",
				new Object[] { u.getUserId(),u.getEmail(), u.getNick(), u.getTel(),
						 u.getSgin()});
		db.close();

	}

	public User getUser(String userId) {
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor c = db.rawQuery("select * from user where userId=?",
				new String[] { userId });
		User u = new User();
		if (c.moveToNext()) {
			u.setUserId(c.getString(c.getColumnIndex("userId")));
			u.setNick(c.getString(c.getColumnIndex("nick")));
			u.setEmail(c.getString(c.getColumnIndex("email")));
			u.setTel(c.getString(c.getColumnIndex("tel")));
			u.setSgin(c.getString(c.getColumnIndex("sgin")));
		}
		return u;
	}

	public void updateUser(List<User> list) {
		if (list.size() > 0) {
			delete();
			addUser(list);
		}
	}

	public List<User> getUser() {
		SQLiteDatabase db = helper.getWritableDatabase();
		List<User> list = new LinkedList<User>();
		Cursor c = db.rawQuery("select * from user", null);
		while (c.moveToNext()) {
			User u = new User();
			u.setUserId(c.getString(c.getColumnIndex("userId")));
			u.setNick(c.getString(c.getColumnIndex("nick")));
			u.setEmail(c.getString(c.getColumnIndex("email")));
			u.setTel(c.getString(c.getColumnIndex("tel")));
			u.setSgin(c.getString(c.getColumnIndex("sgin")));
			list.add(u);
		}
		c.close();
		db.close();
		return list;
	}

	public void update(User u) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL(
				"update user set nick=?,img=?,_group=? where userId=?",
				new Object[] { u.getNick(), u.getEmail(), u.getTel(),
						u.getUserId() });
		db.close();
	}

	public User getLastUser() {
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor c = db.rawQuery("select * from user", null);
		User u = new User();
		while (c.moveToLast()) {
			u.setUserId(c.getString(c.getColumnIndex("userId")));
			u.setNick(c.getString(c.getColumnIndex("nick")));
			u.setEmail(c.getString(c.getColumnIndex("email")));
			u.setTel(c.getString(c.getColumnIndex("tel")));
			u.setSgin(c.getString(c.getColumnIndex("sgin")));
		}
		c.close();
		db.close();
		return u;
	}

	public void delUser(User u) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL("delete from user where userId=?",
				new Object[] { u.getUserId() });
		db.close();
	}

	public void delete() {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL("delete from user");
		db.close();
	}
}
