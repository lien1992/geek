package com.thinksns.jkfs.base;

import com.thinksns.jkfs.bean.AccountBean;
import com.thinksns.jkfs.constant.SettingsUtil;
import com.thinksns.jkfs.util.common.PreferencesUtils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public final class ThinkSNSApplication extends Application {
	private static ThinkSNSApplication globalContext = null;

	private Activity activity;

	@Override
	public void onCreate() {
		super.onCreate();
		globalContext = this;
	}

    public boolean isLogin(Context context) {
        boolean is_login = PreferencesUtils.getBoolean(context, SettingsUtil.IS_LOGIN);
        return is_login;
    }

    public String getOauth_token(Context context){
        if(isLogin(context)){
            return PreferencesUtils.getString(context,SettingsUtil.OAUTH_TOKEN);
        }
        return null;
    }

    public String getUser_Id(Context context){
        if(isLogin(context)){
            return PreferencesUtils.getString(context,SettingsUtil.USER_ID);
        }
        return null;
    }
    public String getOauth_token_secret(Context context){
        if(isLogin(context)){
            return PreferencesUtils.getString(context,SettingsUtil.OAUTH_TOKEN_SECRET);
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

	public void setAccount(Context context,AccountBean account) {
        if(account==null)
            return;
        PreferencesUtils.putBoolean(context, SettingsUtil.IS_LOGIN,true);
        PreferencesUtils.putString(context,SettingsUtil.USER_ID,account.getUid());
        PreferencesUtils.putString(context,SettingsUtil.OAUTH_TOKEN,account.getOauth_token());
        PreferencesUtils.putString(context,SettingsUtil.OAUTH_TOKEN_SECRET,account.getOauth_token_secret());
	}

	public AccountBean getAccount(Context context) {
        if(isLogin(context)){
            AccountBean accountBean=new AccountBean();
            accountBean.setUid(getUser_Id(context));
            accountBean.setOauth_token(getOauth_token(context));
            accountBean.setOauth_token_secret(getOauth_token_secret(context));
            return accountBean;
        }
		return null;
	}

    public boolean quitAccount(Context context){

        if(isLogin(context)){
            PreferencesUtils.putBoolean(context,SettingsUtil.IS_LOGIN,false);
            PreferencesUtils.removeKey(context,SettingsUtil.USER_ID);
            PreferencesUtils.removeKey(context,SettingsUtil.OAUTH_TOKEN_SECRET);
            PreferencesUtils.removeKey(context,SettingsUtil.OAUTH_TOKEN);
            return true;
        }
        return false;
    }

    public boolean isNewWork(Context context){

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean isWifiConn = networkInfo.isConnected();
        networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean isMobileConn = networkInfo.isConnected();
        if(!isWifiConn&&!isMobileConn)
            return false;
        return true;
    }
}