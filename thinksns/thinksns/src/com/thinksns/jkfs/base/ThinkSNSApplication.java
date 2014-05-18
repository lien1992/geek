package com.thinksns.jkfs.base;

import com.thinksns.jkfs.bean.AccountBean;

import android.app.Activity;
import android.app.Application;

public final class ThinkSNSApplication extends Application {
	private static ThinkSNSApplication globalContext = null;

	private Activity activity;
	private AccountBean account; // 已登录用户账户

	@Override
	public void onCreate() {
		super.onCreate();
		globalContext = this;
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

	public void setAccount(AccountBean account) {
		this.account = account;
	}

	public AccountBean getAccount() {
		return account;
	}
}