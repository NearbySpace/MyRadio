package com.example.toolbar.http;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;

import android.content.Context;

import com.example.toolbar.common.utils.LogHelper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 网络访问汇总
 * 
 * @author fylder
 * 
 */
public class HttpManage {

	public static String address = "http://vroad.bbrtv.com/cmradio/index.php";// 目标服务端项目域名
	public static String mainDataUrl = address + "?d=android&c=program&m=index";//首页数据
	public static String mainUserDataUrl = address + "?d=android&c=member&m=detail";//用户数据
	public static String visitStatisticsUrl = address + "?d=android&c=api&m=base_save";//客户端统计
	public static String mainPickerDataUrl = address + "?d=android&c=program&m=homepage_type";//首页滑轮数据
	public static String favoriteDelUrl = address + "?d=android&c=program&m=program_fav_del";//取消节目收藏
	public static String favoriteDelUrls = address+"?d=android&c=program&m=programme_fav_del";//取消节目单收藏
	public static String favoriteAddUrl = address + "?d=android&c=program&m=program_fav_add";//节目收藏
	public static String favoriteListAddUrl = address + "?d=android&c=program&m=programme_fav_add";//节目单收藏
	//节目单的节目列表排序
	public static String programListSortUrl = address + "?d=android&c=program&m=programme_sort";
	//节目单提交编辑结果的url
	public static String editProgrammeUrl = address + "?d=android&c=program&m=edit_programme";
	public static String programmeDataUrl = address +"?d=android&c=program&m=programme_detail";//节目单数据
	public static String findDataUrl = address+"?d=android&c=program&m=find";//发现页数据
	//按类型取得节目列表
	public static String addProgramListUrl = address + "?d=android&c=program&m=get_list_by_type";
	public static String alreadyPlayedListUrl = address + "?d=android&c=member&m=mydata";//已播放列表
	public static String changePassworkUrl = address + "?d=android&c=member&m=password_save";//更改密码
	public static String personDataUrl = address + "?d=android&c=member&m=homepage";//个人主页
	public static String loginUrl = address + "?d=android&c=member&m=check_login";//登录
	public static String myFavoriteUrl = address + "?d=android&c=member&m=mydata";//我的收藏
	public static String registUrl = address + "?d=android&c=member&m=regist";//注册
	public static String selectProgramUrl = address + "?d=android&c=program&m=type";//获得全部节目类型
	public static String userSuggstionUrl = address + "?d=android&c=api&m=feedback";//建议与反馈
	public static String myProgrammeUrl = address + "?d=android&c=program&m=my_programme";//我的节目单
	public static String delProgrammeUrl = address + "?d=android&c=program&m=programme_del";//删除节目单
	//在节目单内添加节目
	public static String addProgramInProgrammeUrl = address + "?d=android&c=program&m=add_more_programme";
	public static String buildProgramUrl = address + "?d=android&c=program&m=add_programme";//创建节目单
	/**
	 * 如果5秒后，数据还没有显示出来，就从新加载数据
	 */
	public static void freshenData(){
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				
			}
		}, 5000);
	}
	
	public interface OnCallBack{
		public void onSuccess(byte[] arg2);
		public void onFailure(byte[] arg2, Throwable arg3);
	}
	
	/**
	 * 二次封装网络连接库
	 * @param url
	 * @param paramsMap 参数集，如果无参数可设为null
	 * @param requestType 请求方法（get或post）0表示post，1表示get
	 * @param callBack 
	 */
	public static void getNetData(String url,Map<String,Object> paramsMap,
			int requestType,final OnCallBack callBack){
		AsyncHttpClient httpClient = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		if(paramsMap != null){
			for(String key : paramsMap.keySet()){
				params.put(key, paramsMap.get(key));
			}
		}
		
		AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				callBack.onSuccess(arg2);
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				callBack.onFailure(arg2,arg3);
			}
		};
		
		switch (requestType) {
		case 0: //post请求
			httpClient.post(url,params, responseHandler);
			break;
		case 1://get请求
			httpClient.get(url,params, responseHandler);
			break;

		default:
			break;
		}
	}
//	/**
//	 * 获取首页数据
//	 */
//	public static void getMainData(AsyncHttpResponseHandler responseHandler) {
//		String url = address + "?d=android&c=program&m=index";
//
//		AsyncHttpClient httpClient = new AsyncHttpClient();
//		// LogHelper.e("URL："+url);
//		httpClient.get(url, responseHandler);
//	}
//	
//	/**
//	 * 获取首页用户会员数据
//	 * uid 用户id
//	 */
//	public static void getMainUserData(String uid,AsyncHttpResponseHandler responseHandler) {
//		String url = address + "?d=android&c=member&m=detail";
//
//		AsyncHttpClient httpClient = new AsyncHttpClient();
//		RequestParams params = new RequestParams();
//		params.put("uid", uid);
//		httpClient.get(url,params, responseHandler);
//	}
//	
//	/**
//	 * 获得节目单的数据
//	 * @param id
//	 */
//	public static void getProgramListData(String id,AsyncHttpResponseHandler responseHandler){
//		String url=address+"?d=android&c=program&m=programme_detail";
//		RequestParams params = new RequestParams();
//		params.put("programme_id", id);
//		AsyncHttpClient httpClient = new AsyncHttpClient();
//		httpClient.get(url, params, responseHandler);
//	}
	
	/**
	 * 获取搜索结果
	 * @param keyword
	 * @param responseHandler
	 */
	public static void getSearchData(String keyword,AsyncHttpResponseHandler responseHandler){
		String url=address+"?d=android&c=program&m=search";
		RequestParams params = new RequestParams();
		params.put("keyword", keyword);
		AsyncHttpClient httpClient = new AsyncHttpClient();
		httpClient.get(url, params, responseHandler);
	}
	
	
//	/**
//	 * 获取发现页的数据
//	 * @param responseHandler
//	 */
//	public static void getFindData(AsyncHttpResponseHandler responseHandler){
//		String url=address+"?d=android&c=program&m=find";
//		AsyncHttpClient httpClient = new AsyncHttpClient();
//		httpClient.get(url, responseHandler);
//	}
//	/**
//	 * 获取自定义节目列表入口
//	 */
//	public static void getMyProgram(
//			AsyncHttpResponseHandler responseHandler, int page, String mid) {
//		String url = address + "?d=android&c=program&m=my_programme";
//		RequestParams params = new RequestParams();
//		params.put("page", page);
//		params.put("mid", mid);
//		AsyncHttpClient httpClient = new AsyncHttpClient();
//		httpClient.get(url, params, responseHandler);
//	}
	
//	/**
//	 * 删除我的节目单
//	 * @param programme_id
//	 * @param mid
//	 * @param responseHandler
//	 */
//	public static void deleteMyProgram(String programme_id,String mid,AsyncHttpResponseHandler responseHandler){
//		String url = address + "?d=android&c=program&m=programme_del";
//		RequestParams params = new RequestParams();
//		params.put("programme_id", programme_id);
//		params.put("mid", mid);
//		AsyncHttpClient httpClient = new AsyncHttpClient();
//		httpClient.get(url, params, responseHandler);
//	}
	
//	/**
//	 * 节目单编辑接口
//	 * @param mid
//	 * @param programme_id
//	 * @param program_ids
//	 * @param responseHandler
//	 */
//	public static void editProgramme(String mid,String programme_id,String program_ids,
//			AsyncHttpResponseHandler responseHandler){
//		String url = address + "?d=android&c=program&m=edit_programme";
//		RequestParams params = new RequestParams();
//		params.put("mid", mid);
//		params.put("programme_id", programme_id);
//		params.put("program_ids", program_ids);
//		AsyncHttpClient httpClient = new AsyncHttpClient();
//		httpClient.post(url, params, responseHandler);
//	}
	
//	/**
//	 * 删除节目单中的节目
//	 * @param programme_id
//	 * @param program_id 如果要删除多个节目，可用逗号隔开，program_id=1,32,75
//	 * @param responseHandler
//	 */
//	public static void deleteProgramInProgramList(String programme_id,String program_id,
//			AsyncHttpResponseHandler responseHandler){
//			String url = address + "?d=android&c=program&m=programme_pro_del";
//			RequestParams params = new RequestParams();
//			params.put("programme_id", programme_id);
//			params.put("program_id", program_id);
//			AsyncHttpClient httpClient = new AsyncHttpClient();
//			httpClient.get(url, params, responseHandler);
//	}
	
//	/**
//	 * 节目单中的节目重新排序的接口
//	 * @param programme_id
//	 * @param program_ids
//	 * @param responseHandler
//	 */
//	public static void programListSort(String programme_id, String program_ids,
//			AsyncHttpResponseHandler responseHandler){
//		String url = address + "?d=android&c=program&m=programme_sort";
//		RequestParams params = new RequestParams();
//		params.put("programme_id", programme_id);
//		params.put("program_ids", program_ids);
//		AsyncHttpClient httpClient = new AsyncHttpClient();
//		httpClient.get(url, params, responseHandler);
//	}
	
//	/**
//	 * 按类型获取节目列表数据
//	 * @param page
//	 * @param type_id
//	 * @param responseHandler
//	 */
//	public static void getProgramClassifyListData(int page,String type_id,AsyncHttpResponseHandler responseHandler){
//		String url = address + "?d=android&c=program&m=get_list_by_type";
//		RequestParams params = new RequestParams();
//		params.put("page", page);
//		params.put("type_id", type_id);
//		AsyncHttpClient httpClient = new AsyncHttpClient();
//		httpClient.get(url, params, responseHandler);
//	}
	
//	/**
//	 * 获取首页的滑轮PickerView的数据
//	 */
//	public static void getNewMainPickerData(AsyncHttpResponseHandler responseHandler){
//		String url ="http://vroad.bbrtv.com/cmradio/index.php?d=android&c=program&m=homepage_type";
//		AsyncHttpClient httpClient = new AsyncHttpClient();
//		httpClient.get(url, responseHandler);
//	}
	
//	/*
//	 * 获取节目数据
//	 */
//	public static void getProgram(AsyncHttpResponseHandler responseHandler) {
//		String url = address + "?d=android&c=program&m=play_lists";
//
//		AsyncHttpClient httpClient = new AsyncHttpClient();
//
//		httpClient.get(url, responseHandler);
//	}

//	/*
//	 * 登录
//	 */
//	public static void getLogin(AsyncHttpResponseHandler responseHandler,
//			String userName, String passWord) {
//		String url = address + "?d=android&c=member&m=check_login";
//		RequestParams params = new RequestParams();
//		params.put("username", userName);
//		params.put("password", passWord);
//		LogHelper.e("用户-密码:" + userName + passWord + "-----"
//				+ params.toString());
//		AsyncHttpClient httpClient = new AsyncHttpClient();
//		httpClient.post(url, params, responseHandler);
//	}

//	/**
//	 * 获取个人主页数据入口
//	 */
//	public static void getPrivateData(AsyncHttpResponseHandler responseHandler,
//			String zid, String mid) {
//		String url = address + "?d=android&c=member&m=homepage";
//		RequestParams params = new RequestParams();
//		params.put("zid", zid);
//		params.put("mid", mid);
//		AsyncHttpClient httpClient = new AsyncHttpClient();
//		httpClient.get(url, params, responseHandler);
//	}

//	/**
//	 * 获取节目选择入口
//	 */
//	public static void getSelectProgram(
//			AsyncHttpResponseHandler responseHandler, String page) {
//		String url = address + "?d=android&c=program&m=type";
//		RequestParams params = new RequestParams();
//		params.put("page", page);
//		AsyncHttpClient httpClient = new AsyncHttpClient();
//		httpClient.get(url, params, responseHandler);
//	}
//	/**
//	 * 获取添加节目入口
//	 */
//	public static void getAddProgramlist(
//			AsyncHttpResponseHandler responseHandler, String typeId ) {
//		String url = address + "?d=android&c=program&m=get_list_by_type";
//		RequestParams params = new RequestParams();
//		params.put("type_id", typeId );
//		AsyncHttpClient httpClient = new AsyncHttpClient();
//		httpClient.get(url, params, responseHandler);
//	}
	
//	/*
//	 * 上传选中节目
//	 */
//	public static void sendProgram(AsyncHttpResponseHandler responseHandler,String mid,
//			String program_ids, String title) {
//		String url = address + "?d=android&c=program&m=add_programme";
//		RequestParams params = new RequestParams();
//		params.put("mid", mid);
////		params.put("program_ids", program_ids);
//		params.put("title", title);
//		params.put("program_ids", program_ids);
//		AsyncHttpClient httpClient = new AsyncHttpClient();
//		httpClient.post(url, params, responseHandler);
//	}
	
	
//	/**
//	 * 在节目单内添加节目
//	 * @param mid
//	 * @param programme_id
//	 * @param program_ids
//	 * @param responseHandler
//	 */
//	public static void addProgramInProgramme(String mid,String programme_id,String program_ids,
//			AsyncHttpResponseHandler responseHandler){
//		String url = address + "?d=android&c=program&m=add_more_programme";
//		RequestParams params = new RequestParams();
//		params.put("mid", mid);
//		params.put("programme_id", programme_id);
//		params.put("program_ids", program_ids);
//		AsyncHttpClient httpClient = new AsyncHttpClient();
//		httpClient.post(url, params, responseHandler);
//	}
	
	
//	/**
//	 * 添加节目
//	 */
//	public static void AddProgram(
//			AsyncHttpResponseHandler responseHandler, String title , String uid) {
//		String url = address + "?d=android&c=program&m=add_channel";
//		RequestParams params = new RequestParams();
//		params.put("title ", title );
//		params.put("uid", uid);
//		AsyncHttpClient httpClient = new AsyncHttpClient();
//		httpClient.get(url, params, responseHandler);
//	}
	
	/**
	 * 下载节目
	 * @param responseHandler
	 * @param program_id
	 */
	public static void downloadProgram(AsyncHttpResponseHandler responseHandler,
			String program_id){
		String url=address+"?d=android&c=program&m=program_dl";
		RequestParams params = new RequestParams();
		params.put("program_id", program_id);
		AsyncHttpClient httpClient = new AsyncHttpClient();
		httpClient.get(url, params, responseHandler);
	}

	
	/**
	 * 获取播放数据入口
	 */
	public static void getPlaydata(AsyncHttpResponseHandler responseHandler
			) {
		String url = "http://vroad.bbrtv.com/myradio/index.php?c=audio&m=audio_time&uid=173&page=1";
		AsyncHttpClient httpClient = new AsyncHttpClient();
		httpClient.get(url,  responseHandler);
	}
	
//	/**
//	 * http://vroad.bbrtv.com/cmradio/index.php?d=android&c=member&m=mydata
//	 * 获取我的收藏入口
//	 */
//	public static void getMyFavorite(AsyncHttpResponseHandler responseHandler,
//			String uid ,int type, int page) {
//		String url = address + "?d=android&c=member&m=mydata";
//		RequestParams params = new RequestParams();
//		params.put("uid", uid);
//		params.put("type", type);
//		params.put("page", page);
//		AsyncHttpClient httpClient = new AsyncHttpClient();
//		httpClient.get(url, params, responseHandler);
//	}

	
//	/**
//	 * 添加收藏节目
//	 * @param mid
//	 * @param program_id
//	 * @param type_id
//	 * @param responseHandler
//	 */
//	public static void FavoriteAdd(String mid, String program_id,String type_id,AsyncHttpResponseHandler responseHandler){
//		String url = address + "?d=android&c=program&m=program_fav_add";
//		RequestParams params = new RequestParams();
//		params.put("mid", mid);
//		params.put("program_id", program_id);
//		params.put("type_id", type_id);
//		AsyncHttpClient httpClient = new AsyncHttpClient();
//		httpClient.post(url, params, responseHandler);
//	}
	
//	/**
//	 * 添加收藏节目单
//	 * @param programme_id  节目单id
//	 * @param mid   用户id
//	 * @param type_id  类型
//	 * @param responseHandler
//	 */
//	public static void FavoriteListAdd(String programme_id,String mid,String type_id,AsyncHttpResponseHandler responseHandler){
//		String url = address + "?d=android&c=program&m=programme_fav_add";
//		RequestParams params = new RequestParams();
//		params.put("mid", mid);
//		params.put("programme_id", programme_id);
//		params.put("type_id", type_id);
//		AsyncHttpClient httpClient = new AsyncHttpClient();
//		httpClient.post(url, params, responseHandler);
//	}
	
//	/**
//	 * 取消收藏节目单
//	 * @param programme_id 节目单id
//	 * @param mid 用户id
//	 * @param type_id
//	 * @param responseHandler
//	 */
//	public static void FavoriteListDel(String programme_id,String mid,String type_id,AsyncHttpResponseHandler responseHandler){
//		String url = address + "?d=android&c=program&m=programme_fav_del";
//		RequestParams params = new RequestParams();
//		params.put("mid", mid);
//		params.put("programme_id", programme_id);
//		params.put("type_id", type_id);
//		AsyncHttpClient httpClient = new AsyncHttpClient();
//		httpClient.get(url, params, responseHandler);
//	}
	
//	/**
//	 * 删除我的收藏
//	 * http://vroad.bbrtv.com/cmradio/index.php?d=android&c=program&m=program_fav_del 
//	 */
//	public static void FavoriteDel(AsyncHttpResponseHandler responseHandler,
//			String mid, String program_id,String type_id,String list_type) {
//		String url = address + "?d=android&c=program&m=program_fav_del";
//		String urls=address+"?d=android&c=program&m=programme_fav_del";
//		RequestParams params = new RequestParams();
//		params.put("mid", mid);
//		params.put("type_id", type_id);
//		AsyncHttpClient httpClient = new AsyncHttpClient();
//		if(list_type.equals("1")){
//			params.put("program_id", program_id);
//			httpClient.get(url, params, responseHandler);
//		}else{
//			params.put("programme_id", program_id);
//			httpClient.get(urls, params, responseHandler);
//		}
//		
//	}
	
//	/**
//	 * 取得注册的返回信息
//	 * @param username
//	 * @param password
//	 * @param nickname
//	 * @param tel
//	 * @param email
//	 * @param IDcard
//	 * @param responseHandler
//	 */
//	public static void getRegistResult(String username,String password,String nickname,String tel,
//			String email,String IDcard,AsyncHttpResponseHandler responseHandler){
//		String url = address + "?d=android&c=member&m=regist";
//		RequestParams params = new RequestParams();
//		params.put("username", username);
//		LogHelper.e("用户名："+username+"-----密码："+password);
//		params.put("password", password);
//		params.put("nickname", nickname);
//		if(tel.length()!=0){
//			params.put("tel", tel);
//		}
//		params.put("email", email);
//		if(IDcard.length()!=0){
//			params.put("IDcard", IDcard);
//		}
//		AsyncHttpClient httpClient = new AsyncHttpClient();
//		httpClient.post(url, params, responseHandler);
//	}
//
//	/**
//	 * 更改密码
//	 * @param responseHandler
//	 * @param old_password
//	 * @param new_password
//	 */
//	public static void changePasswork(AsyncHttpResponseHandler responseHandler,
//			String uid,String old_password,String new_password){
//		String url = address + "?d=android&c=member&m=password_save";
//		RequestParams params = new RequestParams();
//		params.put("uid", uid);
//		params.put("old_password", old_password);
//		params.put("new_password", new_password);
//		AsyncHttpClient httpClient = new AsyncHttpClient();
//		httpClient.post(url, params, responseHandler);
//	}
//	
//	
//	
//	/**
//	 * 意见反馈接口
//	 * @param uid
//	 * @param content
//	 * @param responseHandler
//	 */
//	public static void userSuggestion(String mid,String content,AsyncHttpResponseHandler responseHandler){
//		String url = address + "?d=android&c=api&m=feedback";
//		RequestParams params = new RequestParams();
//		params.put("mid", mid);
//		params.put("content", content);
//		AsyncHttpClient httpClient = new AsyncHttpClient();
//		httpClient.post(url, params, responseHandler);
//	}
//	
//	/**
//	 * 访问统计
//	 * @param uid  用户id  如果是首次安装则为0
//	 * @param city 所在城市名称
//	 * @param district  城区
//	 * @param lnglat 经纬度 ，格式如：108.34534,22.83012
//	 * @param version 客户端版本号
//	 * @param OSVersion  手机系统版本
//	 * @param PhoneModel 手机型号
//	 * @param PhoneBrand 手机品牌
//	 * @param PhoneOS 手机操作系统
//	 * @param isfirst 是否首次安装
//	 */
//	public static void visitStatistics(String mid,String city,String district
//			,String lnglat,String version,String OSVersion,String PhoneModel
//			,String PhoneBrand,String PhoneOS,String isfirst,AsyncHttpResponseHandler responseHandler){
////		http://vroad.bbrtv.com/cmradio/index.php?d=android&c=api&m=base_save
//		String url = address + "?d=android&c=api&m=base_save";
//		RequestParams params = new RequestParams();
//		params.put("mid", mid);
//		params.put("city", city);
//		params.put("district", district);
//		params.put("lnglat", lnglat);
//		params.put("version", version);
//		params.put("os_version", OSVersion);
//		params.put("phone_model", PhoneModel);
//		params.put("phone_brand", PhoneBrand);
//		params.put("phone_os", PhoneOS);
//		params.put("isfirst", isfirst);
//		AsyncHttpClient httpClient = new AsyncHttpClient();
//		httpClient.post(url, params, responseHandler);
//	}
	
	/**
	 * 获取课程表入口
	 * 
	 * @param responseHandler
	 * @param schoolID
	 * @param classID
	 */
	public static void getCourseTable(AsyncHttpResponseHandler responseHandler,
			String schoolID, String classID) {
		String url = address + "?d=pad&c=timetable&m=lists";
		RequestParams params = new RequestParams();
		params.put("schoolid", schoolID);
		params.put("classname", classID);
		AsyncHttpClient httpClient = new AsyncHttpClient();
		httpClient.get(url, params, responseHandler);
	}

	/**
	 * 获取值日表入口
	 * 
	 * @param responseHandler
	 * @param schoolID
	 * @param classID
	 */
	public static void getDuty(AsyncHttpResponseHandler responseHandler,
			String schoolID, String classID) {
		String url = address + "?d=pad&c=student&m=on_duty";
		RequestParams params = new RequestParams();
		params.put("schoolid", schoolID);// 临时调试参数2
		params.put("classname", classID);
		AsyncHttpClient httpClient = new AsyncHttpClient();
		httpClient.get(url, params, responseHandler);
	}

	/**
	 * 获取座位表入口
	 * 
	 * @param responseHandler
	 * @param schoolID
	 * @param classID
	 */
	public static void getSeating(AsyncHttpResponseHandler responseHandler,
			String schoolID, String classID) {
		String url = address + "?d=pad&c=student&m=seat";
		RequestParams params = new RequestParams();
		params.put("schoolid", schoolID);// 临时调试参数2
		params.put("classname", classID);
		AsyncHttpClient httpClient = new AsyncHttpClient();
		httpClient.get(url, params, responseHandler);
	}

	/**
	 * 获取资讯入口
	 * 
	 * @param responseHandler
	 * @param schoolID
	 * @param classID
	 */
	public static void getNews(AsyncHttpResponseHandler responseHandler,
			String schoolID, String classID) {
		String url = address + "?d=pad&c=news&m=index&catid=2";
		RequestParams params = new RequestParams();
		params.put("schoolid", schoolID);
		params.put("classname", classID);
		AsyncHttpClient httpClient = new AsyncHttpClient();
		httpClient.get(url, params, responseHandler);
	}

	/**
	 * 获取版本信息
	 * @param responseHandler
	 * @param schoolID
	 * @param classID
	 */
	public static void getVersion(AsyncHttpResponseHandler responseHandler) {
		String url = "http://vroad.bbrtv.com/cmradio/index.php?d=android&c=api&m=android_version";
		AsyncHttpClient httpClient = new AsyncHttpClient();
		httpClient.get(url, responseHandler);
	}

	/**
	 * 获取天气
	 * 
	 * @param responseHandler
	 */
	public static void getWeather(AsyncHttpResponseHandler responseHandler) {
		String url = "http://apistore.baidu.com/microservice/weather";
		RequestParams params = new RequestParams();
		params.put("cityid", "101300101");

		AsyncHttpClient httpClient = new AsyncHttpClient();
		httpClient.get(url, params, responseHandler);
	}

}
