package com.example.toolbar.bean;

import java.util.List;

/**
 * 个人主页
 * 
 * @author Administrator
 * 
 */
public class PrivateMain {
	/*
	 * { "avatar": "", "username": "jhz", "nickname": "壬辰水", "level": "0",
	 * "backgroundpic": "", "foo_playbill_num": 0, "programme": [ { "title":
	 * "节目单添加测试", "program_ids": "1,2,4,6,7,8,9,10,11", "playtimes": "0", "thumb":
	 * "http://vroad.bbrtv.com/cmradio/uploads/file/20150724/20150724095638_31747.jpg"
	 * , "program_num": 9 } ], "is_favorite": 0, "favorite_num": 1, "fans_num": 0,
	 * "playbill_num": 1 }
	 */
	private String avatar;
	private String username;
	private String nickname;
	private String level;
	private String backgroundpic;
	private String foo_playbill_num;
	private List<PrivateMain_Progtam> programme;
	private String is_favorite;
	private String favorite_num;
	private String fans_num;
	private String playbill_num;

	public PrivateMain(String avatar, String username, String nickname,
			String level, String backgroundpic, String foo_playbill_num,
			List<PrivateMain_Progtam> programme, String is_favorite,
			String favorite_num, String fans_num, String playbill_num) {
		super();
		this.avatar = avatar;
		this.username = username;
		this.nickname = nickname;
		this.level = level;
		this.backgroundpic = backgroundpic;
		this.foo_playbill_num = foo_playbill_num;
		this.programme = programme;
		this.is_favorite = is_favorite;
		this.favorite_num = favorite_num;
		this.fans_num = fans_num;
		this.playbill_num = playbill_num;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getBackgroundpic() {
		return backgroundpic;
	}

	public void setBackgroundpic(String backgroundpic) {
		this.backgroundpic = backgroundpic;
	}

	public String getFoo_playbill_num() {
		return foo_playbill_num;
	}

	public void setFoo_playbill_num(String foo_playbill_num) {
		this.foo_playbill_num = foo_playbill_num;
	}

	public List<PrivateMain_Progtam> getProgramme() {
		return programme;
	}

	public void setProgramme(List<PrivateMain_Progtam> programme) {
		this.programme = programme;
	}

	public String getIs_favorite() {
		return is_favorite;
	}

	public void setIs_favorite(String is_favorite) {
		this.is_favorite = is_favorite;
	}

	public String getFavorite_num() {
		return favorite_num;
	}

	public void setFavorite_num(String favorite_num) {
		this.favorite_num = favorite_num;
	}

	public String getFans_num() {
		return fans_num;
	}

	public void setFans_num(String fans_num) {
		this.fans_num = fans_num;
	}

	public String getPlaybill_num() {
		return playbill_num;
	}

	public void setPlaybill_num(String playbill_num) {
		this.playbill_num = playbill_num;
	}

}
