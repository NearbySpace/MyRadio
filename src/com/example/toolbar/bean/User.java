package com.example.toolbar.bean;

import java.io.Serializable;

public class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String UserId;//
	private String email;
	private String nick;//
	private String tel;//
	private String sgin;
	@Override
	public String toString() {
		return "User [UserId=" + UserId + ", email=" + email + ", nick=" + nick
				+ ", tel=" + tel + ", sgin=" + sgin + "]";
	}
	public User(String userId, String email, String nick, String tel,
			String sgin) {
		super();
		UserId = userId;
		this.email = email;
		this.nick = nick;
		this.tel = tel;
		this.sgin = sgin;
	}
	public User() {
		// TODO Auto-generated constructor stub
	}
	public String getUserId() {
		return UserId;
	}
	public void setUserId(String userId) {
		UserId = userId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getSgin() {
		return sgin;
	}
	public void setSgin(String sgin) {
		this.sgin = sgin;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}


}
