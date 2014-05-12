package com.thinksns.jkfs.sina;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
//import android.webkit.CookieManager;

public class SinaWeiboUtil {

	private static Context mContext;

	private static SinaWeiboUtil mInstantce;

	/** 微博 Web 授权类，提供登陆等功能 **/
	private static WeiboAuth mWeibo;

	/** 封装了 "access_token"，"expires_in"等，并提供了他们的管理功能 **/
	private static Oauth2AccessToken mAccessToken;

	/** 调用SSO授权 **/
	private static SsoHandler mSsoHandler;

	private WeiboListener listener;

	public SinaWeiboUtil() {
		mWeibo = new WeiboAuth(mContext, Sinas.APP_KEY, Sinas.REDIRECT_URL,
				Sinas.SCOPE);
	}

	public static SinaWeiboUtil getInstance(Context context) {
		mContext = context;
		if (mInstantce == null) {
			mInstantce = new SinaWeiboUtil();
		}
		return mInstantce;
	}

	/**
	 * 初始化新浪微博
	 * 
	 * @param l
	 *            授权是否过期回调方法
	 */
	public void initSinaWeibo(WeiboListener l) {
		String token = PreferenceUtil.getInstance(mContext).getString(
				Sinas.PREF_SINA_ACCESS_TOKEN, "");
		long expiresTime = PreferenceUtil.getInstance(mContext).getLong(
				Sinas.PREF_SINA_EXPIRES_TIME, 0);
		String uid = PreferenceUtil.getInstance(mContext).getString(
				Sinas.PREF_SINA_UID, "");
		String userName = PreferenceUtil.getInstance(mContext).getString(
				Sinas.PREF_SINA_USER_NAME, "");
		String remindIn = PreferenceUtil.getInstance(mContext).getString(
				Sinas.PREF_SINA_REMIND_IN, "");
		mAccessToken = new Oauth2AccessToken();
		mAccessToken.setToken(token);
		mAccessToken.setExpiresTime(expiresTime);

		if (mAccessToken.isSessionValid()) { // 判断是否已授权
			l.init(true);
		} else {
			l.init(false);
		}
	}

	/**
	 * SSO授权
	 * 
	 * @param l
	 */
	public void auth(WeiboListener l) {
		mSsoHandler = new SsoHandler((Activity) mContext, mWeibo);
		mSsoHandler.authorize(new AuthDialogListener());

		listener = l;
	}

    /**
     * 微博认证授权回调类。
     * 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用 {@link SsoHandler#authorizeCallBack} 后，
     *    该回调才会被执行。
     * 2. 非 SSO 授权时，当授权结束后，该回调就会被执行。
     * 当授权成功后，请保存该 access_token、expires_in、uid 等信息到 SharedPreferences 中。
     */
	class AuthDialogListener implements WeiboAuthListener {

		@Override
		public void onComplete(Bundle values) {
			String token = values.getString(Sinas.SINA_ACCESS_TOKEN);
			String uid = values.getString(Sinas.SINA_UID);
			String userName = values.getString(Sinas.SINA_USER_NAME);
			String expiresIn = values.getString(Sinas.SINA_EXPIRES_IN); 
			String remindIn = values.getString(Sinas.SINA_REMIND_IN);

			mAccessToken = new Oauth2AccessToken(token, expiresIn);
			if (mAccessToken.isSessionValid()) {
				PreferenceUtil.getInstance(mContext).saveString(
						Sinas.PREF_SINA_ACCESS_TOKEN, token);
				PreferenceUtil.getInstance(mContext).saveString(
						Sinas.PREF_SINA_UID, uid);
				PreferenceUtil.getInstance(mContext).saveLong(
						Sinas.PREF_SINA_EXPIRES_TIME,
						mAccessToken.getExpiresTime()); 
				PreferenceUtil.getInstance(mContext).saveString(
						Sinas.PREF_SINA_REMIND_IN, remindIn);
				PreferenceUtil.getInstance(mContext).saveString(
						Sinas.PREF_SINA_USER_NAME, userName);
				if (listener != null) {
					listener.onResult();
				}
			}
			//CookieManager cookieManager = CookieManager.getInstance();
			//cookieManager.removeAllCookie();
		}

		@Override
		public void onCancel() {
		}

		@Override
		public void onWeiboException(WeiboException e) {
		}
	}

	/**
	 * SSO授权回调函数, 当 SSO 授权 Activity 退出时，该函数被调用。
	 * 
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	public void authCallBack(int requestCode, int resultCode, Intent data) {
		if (mSsoHandler != null) {
			mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

	/**
	 * 检查是否已授权
	 * 
	 * @return true 已授权，false 未授权
	 */
	public boolean isAuth() {
		String token = PreferenceUtil.getInstance(mContext).getString(
				Sinas.PREF_SINA_ACCESS_TOKEN, "");
		if (TextUtils.isEmpty(token)) {
			return false;
		}
		return true;
	}
}
