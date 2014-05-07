package com.thinksns.jkfs.support.util;

import android.app.Activity;
import android.app.Application;

public final class ThinkSNSApplication extends Application {
	private static ThinkSNSApplication globalContext = null;

	private Activity activity;

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
}