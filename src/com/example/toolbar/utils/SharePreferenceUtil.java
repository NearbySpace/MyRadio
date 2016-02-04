package com.example.toolbar.utils;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferenceUtil {
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;

	private static final String MESSAGE_NOTIFY_KEY = "message_notify";
	private static final String MESSAGE_SOUND_KEY = "message_sound";
	private static final String SHOW_HEAD_KEY = "show_head";
	private static final String USER_ID_KEY = "user_id";// 用户id
	private static final String USER_NAME_KEY = "user_name";// 用户名
	private static final String USER_INTRO_KEY = "user_intro";// 用户介绍
	private static final String USER_NICKNAME_KEY = "user_nickname";// 用户昵称
	private static final String USER_EMAIL_KEY = "user_email";// 用户邮箱
	private static final String USER_PHONE_KEY = "user_phone";// 用户手机
	private static final String USER_SIGN_KEY = "user_sign";// 用户个性签名
	private static final String USER_HEADICON_KEY = "user_head_icon";// 用户头像
	private static final String USER_SAVEPASSWORD_KEY = "user_save_password";// 是否保存密码
	private static final String NOTIFY_TAG_KEY = "notify_tag";// 通知tag
	private static final String USER_ISLOGIN_KEY = "user_islogin";
	private static final String USER_IS_AUTO_LOGIN="user_is_auto_login";
	// myradio
	private static final String MYRADIO_MUSIC_TITLE_KEY = "myradio_music_title";// myrdio标题
	private static final String MYRADIO_MUSIC_IMAGE_KEY = "myradio_music_image";// myrdio正在播放的图标
	private static final String MYRADIO_MUSIC_ID_KEY = "myradio_music_id";// myrdio音乐id
	private static final String MYRADIO_MUSIC_URL_KEY = "myradio_music_url";// myradio的链接

	public SharePreferenceUtil(Context context, String file) {
		sp = context.getSharedPreferences(file, Context.MODE_PRIVATE);
		editor = sp.edit();
	}
	
	//清空数据
	public void clearData(){
		editor.clear().commit();
	}

	// 用户ID
	public void setUid(String userId) {
		editor.putString(USER_ID_KEY, userId);
		editor.commit();
	}

	public String getUid() {
		return sp.getString(USER_ID_KEY, "");
	}

	// 用户名
	public void setUserName(String username) {
		editor.putString(USER_NAME_KEY, username);
		editor.commit();
	}

	public String getUserName() {
		return sp.getString(USER_NAME_KEY, "");
	}
	
	//是否自动登录
	public void setIsAutoLogin(boolean isAutoLogin){
		editor.putBoolean(USER_IS_AUTO_LOGIN, isAutoLogin);
		editor.commit();
	}
	
	public boolean getIsAutoLogin(){
		return sp.getBoolean(USER_IS_AUTO_LOGIN, false);
		
	}

	// 是否登录了
	public boolean getisLogin() {
		return sp.getBoolean(USER_ISLOGIN_KEY, false);
	}

	public void setisLogin(boolean isLogin) {
		editor.putBoolean(USER_ISLOGIN_KEY, isLogin);
		editor.commit();
	}

	// 用户介绍
	public void setUserIntro(String username) {
		editor.putString(USER_INTRO_KEY, username);
		editor.commit();
	}

	public String getUserIntro() {
		return sp.getString(USER_INTRO_KEY, "");
	}

	// nick
	public void setNick(String nick) {
		editor.putString(USER_NICKNAME_KEY, nick);
		editor.commit();
	}

	public String getNick() {
		return sp.getString(USER_NICKNAME_KEY, "");
	}

	// email
	public void setEmail(String email) {
		editor.putString(USER_EMAIL_KEY, email);
		editor.commit();
	}

	public String getEmail() {
		return sp.getString(USER_EMAIL_KEY, "");
	}

	// phone
	public void setPhone(String phone) {
		editor.putString(USER_PHONE_KEY, phone);
		editor.commit();
	}

	public String setPhone() {
		return sp.getString(USER_PHONE_KEY, "");
	}

	// sign
	public void setSign(String sign) {
		editor.putString(USER_SIGN_KEY, sign);
		editor.commit();
	}

	public String getSign() {
		return sp.getString(USER_SIGN_KEY, "");
	}

	// 头像图标
	public String getHeadIcon() {
		return sp.getString(USER_HEADICON_KEY, "");
	}

	public void setHeadIcon(String icon) {
		editor.putString(USER_HEADICON_KEY, icon);
		editor.commit();
	}

	// 设置Tag
	public void setTag(String tag) {
		editor.putString(NOTIFY_TAG_KEY, tag);
		editor.commit();
	}

	public String getTag() {
		return sp.getString(NOTIFY_TAG_KEY, "");
	}

	// 是否通知
	public boolean getMsgNotify() {
		return sp.getBoolean(MESSAGE_NOTIFY_KEY, false);
	}

	public void setMsgNotify(boolean isChecked) {
		editor.putBoolean(MESSAGE_NOTIFY_KEY, isChecked);
		editor.commit();
	}

	// 是否记录密码
	public boolean getSavePassword() {
		return sp.getBoolean(USER_SAVEPASSWORD_KEY, false);
	}

	public void setSavePassword(boolean isSaved) {
		editor.putBoolean(USER_SAVEPASSWORD_KEY, isSaved);
		editor.commit();
	}

	public String getMyradioMusicTitle() {
		return sp.getString(MYRADIO_MUSIC_TITLE_KEY, "");
	}

	public void setMyradioMusicTitle(String title) {
		editor.putString(MYRADIO_MUSIC_TITLE_KEY, title);
		editor.commit();
	}

	public String getMyradioMusicImage() {
		return sp.getString(MYRADIO_MUSIC_IMAGE_KEY, "");
	}

	public void setMyradioMusicImage(String image) {
		editor.putString(MYRADIO_MUSIC_IMAGE_KEY, image);
		editor.commit();
	}

	public void setMyradioMusicID(String id) {
		editor.putString(MYRADIO_MUSIC_ID_KEY, id);
		editor.commit();
	}

	public String getMyradioMusicTD() {
		return sp.getString(MYRADIO_MUSIC_ID_KEY, "");
	}

	public void setMyradioMusicUrl(String url) {
		editor.putString(MYRADIO_MUSIC_URL_KEY, url);
		editor.commit();
	}

	public String getMyradioMusicUrl() {
		return sp.getString(MYRADIO_MUSIC_URL_KEY, "");
	}
	
	public void setDefaultProgram(String classifyID,String classifyName){
		editor.putString("classifyID", classifyID);
		editor.putString("classifyName", classifyName+"频道");
		editor.commit();
	}
	
	/**
	 * 保存main页播放的默认频道
	 * @return
	 */
	public Map<String,String> getDefaultProgram(){
		Map<String,String> map=new HashMap<String, String>();
		map.put("classifyID", sp.getString("classifyID", "3"));
		map.put("classifyName", sp.getString("classifyName", "音乐频道"));
		return map;
		
	}
	
	
	public void setPalyProgramInfo(String channelName,String programName,
			String host,String programID ,String url){
		if(channelName.contains("频道")){
			editor.putString("channelName", channelName);
		}else{
			editor.putString("channelName", channelName+"频道");
		}
		editor.putString("programName", programName);
		editor.putString("host", host);
		editor.putString("programID", programID);
		editor.putString("url", url);
		editor.commit();
	}
	
	public Map<String,String> getPalyProgramInfo(){
		Map<String,String> map=new HashMap<String, String>();
		map.put("channelName", sp.getString("channelName", "草莓电台"));
		map.put("programName", sp.getString("programName", "无名"));
		map.put("host", sp.getString("host", "无名"));
		map.put("programID", sp.getString("programID", "3"));
		map.put("url", sp.getString("url", null));
		return map;
	}

}
