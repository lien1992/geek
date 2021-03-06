package com.thinksns.jkfs.base;

import java.io.File;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.thinksns.jkfs.bean.AccountBean;
import com.thinksns.jkfs.bean.UserInfoBean;
import com.thinksns.jkfs.constant.BaseConstant;
import com.thinksns.jkfs.constant.SettingsUtil;
import com.thinksns.jkfs.ui.fragment.MenuFragment;
import com.thinksns.jkfs.util.common.PreferencesUtils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;

public final class ThinkSNSApplication extends Application {
	private static ThinkSNSApplication globalContext = null;

	private Activity activity;
	private UserInfoBean user;
	private MenuFragment menu;

	private boolean isNoImageMode;// 无图模式

	private boolean isClearCache;

	private int image_quality;

	@Override
	public void onCreate() {
		super.onCreate();

		Log.d("XXX", "application is create");
		globalContext = this;

		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(this);
		setNoImageMode(settings.getBoolean(BaseConstant.NO_IMAGE_MODE_KEY,
				false));
		File cacheDir = StorageUtils.getCacheDirectory(getApplicationContext());
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).threadPoolSize(3).threadPriority(
				Thread.NORM_PRIORITY - 2)
		// 设置线程的优先级
				.denyCacheImageMultipleSizesInMemory()
				// 当同一个Uri获取不同大小的图片，缓存到内存时，只缓存一个。默认会缓存多个不同的大小的相同图片
				.memoryCache(new LruMemoryCache(2 * 1024 * 1024)).diskCache(
						new UnlimitedDiscCache(cacheDir)).tasksProcessingOrder(
						QueueProcessingType.LIFO)// 设置图片下载和显示的工作队列排序
				.writeDebugLogs().build();

		// Initialize ImageLoader with configuration
		ImageLoader.getInstance().init(config);

	}

	public boolean isLogin(Context context) {
		boolean is_login = PreferencesUtils.getBoolean(context,
				SettingsUtil.IS_LOGIN);
		return is_login;
	}

	public String getOauth_token(Context context) {
		if (isLogin(context)) {
			return PreferencesUtils
					.getString(context, SettingsUtil.OAUTH_TOKEN);
		}
		return null;
	}

	public String getUser_Id(Context context) {
		if (isLogin(context)) {
			return PreferencesUtils.getString(context, SettingsUtil.USER_ID);
		}
		return null;
	}

	public String getOauth_token_secret(Context context) {
		if (isLogin(context)) {
			return PreferencesUtils.getString(context,
					SettingsUtil.OAUTH_TOKEN_SECRET);
		}
		return null;
	}

	public static ThinkSNSApplication getInstance() {
		return globalContext;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public void setAccount(Context context, AccountBean account) {
		if (account == null)
			return;
		PreferencesUtils.putBoolean(context, SettingsUtil.IS_LOGIN, true);
		PreferencesUtils.putString(context, SettingsUtil.USER_ID, account
				.getUid());
		PreferencesUtils.putString(context, SettingsUtil.OAUTH_TOKEN, account
				.getOauth_token());
		PreferencesUtils.putString(context, SettingsUtil.OAUTH_TOKEN_SECRET,
				account.getOauth_token_secret());
	}

	public AccountBean getAccount(Context context) {
		if (isLogin(context)) {
			AccountBean accountBean = new AccountBean();
			accountBean.setUid(getUser_Id(context));
			accountBean.setOauth_token(getOauth_token(context));
			accountBean.setOauth_token_secret(getOauth_token_secret(context));
			return accountBean;
		}
		return null;
	}

	public boolean quitAccount(Context context) {

		if (isLogin(context)) {
			PreferencesUtils.putBoolean(context, SettingsUtil.IS_LOGIN, false);
			PreferencesUtils.removeKey(context, SettingsUtil.USER_ID);
			PreferencesUtils
					.removeKey(context, SettingsUtil.OAUTH_TOKEN_SECRET);
			PreferencesUtils.removeKey(context, SettingsUtil.OAUTH_TOKEN);
			return true;
		}
		return false;
	}

	public boolean isNewWork(Context context) {

		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		boolean isWifiConn = networkInfo.isConnected();
		networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		boolean isMobileConn = networkInfo.isConnected();
		if (!isWifiConn || isMobileConn)
			return false;
		return true;
	}

	public void setUser(UserInfoBean user) {
		this.user = user;
	}

	public UserInfoBean getUser() {
		return user;
	}

	public void setNoImageMode(boolean isNoImageMode) {
		this.isNoImageMode = isNoImageMode;
	}

	public boolean isNoImageMode() {
		return isNoImageMode;
	}

	public void setClearCache(boolean isClearCache) {
		this.isClearCache = isClearCache;
	}

	public boolean isClearCache() {
		return isClearCache;
	}

	public void setImage_quality(int image_quality) {
		this.image_quality = image_quality;
	}

	public int getImage_quality() {
		return image_quality;
	}

	public void setMenu(MenuFragment menu) {
		this.menu = menu;
	}

	public MenuFragment getMenu() {
		return menu;
	}
}