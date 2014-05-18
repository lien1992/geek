package com.thinksns.jkfs.sina;

public interface Sinas {

	public static final String CLIENT_ID = "client_id";
	public static final String RESPONSE_TYPE = "response_type";
	public static final String USER_REDIRECT_URL = "redirect_uri";

	public static String APP_KEY = "2309230490";
	public static String APP_SECRET = "f922227bb03acb5b47f6227758fb415a";
	public static String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
	public static String URL_USERS_SHOW = "https://api.weibo.com/2/users/show.json"; // 获取新浪微博用户资料

	// 支持传入多个scope权限，逗号分隔
	String SCOPE = "email,direct_messages_read,direct_messages_write,"
			+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
			+ "follow_app_official_microblog," + "invitation_write";

	/** 认证Code **/
	String SINA_CODE = "code";
	String SINA_ACCESS_TOKEN = "access_token";
	String SINA_EXPIRES_IN = "expires_in";
	String SINA_UID = "uid";
	String SINA_USER_NAME = "userName";
	String SINA_NAME = "name";
	String SINA_REMIND_IN = "remind_in";
	String SINA_DATE_PATTERN = "yyyy/MM/dd HH:mm:ss";
	String SINA_BASEURL = "https://api.weibo.com/oauth2/";
	String SINA_CLIENT_ID = "client_id";
	String SINA_CLIENT_SECRET = "client_secret";
	String SINA_GRANT_TYPE = "grant_type";
	String SINA_GRANT_TYPE_VALUE = "authorization_code";
	String SINA_REDIRECT_URI = "redirect_uri";

	// 新浪微博SharedPreferences
	String PREF_SINA_ACCESS_TOKEN = "SINA_ACCESS_TOKEN";
	String PREF_SINA_EXPIRES_TIME = "SINA_EXPIRES_TIME";
	String PREF_SINA_UID = "SINA_UID";
	String PREF_SINA_USER_NAME = "SINA_USER_NAME";
	String PREF_SINA_REMIND_IN = "SINA_REMIND_IN";

}
