package com.thinksns.jkfs.sina;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * SharedPreferences工具类
 * 
 */
public class PreferenceUtil {

	private static final String PREFERENCE_NAME = "thinksns_prefs";

	private static PreferenceUtil preferenceUtil;

	private SharedPreferences shareditorPreferences;

	private Editor editor;

	private PreferenceUtil(Context context) {
		init(context);
	}

	public void init(Context context) {
		if (shareditorPreferences == null || editor == null) {
			try {
				shareditorPreferences = context.getSharedPreferences(
						PREFERENCE_NAME, 0);
				editor = shareditorPreferences.edit();
			} catch (Exception e) {
			}
		}
	}

	public static PreferenceUtil getInstance(Context context) {
		if (preferenceUtil == null) {
			preferenceUtil = new PreferenceUtil(context);
		}
		return preferenceUtil;
	}

	public void saveLong(String key, long l) {
		editor.putLong(key, l);
		editor.commit();
	}

	public long getLong(String key, long defaultlong) {
		return shareditorPreferences.getLong(key, defaultlong);
	}

	public void saveBoolean(String key, boolean value) {
		editor.putBoolean(key, value);
		editor.commit();
	}

	public boolean getBoolean(String key, boolean defaultboolean) {
		return shareditorPreferences.getBoolean(key, defaultboolean);
	}

	public void saveInt(String key, int value) {
		if (editor != null) {
			editor.putInt(key, value);
			editor.commit();
		}
	}

	public int getInt(String key, int defaultInt) {
		return shareditorPreferences.getInt(key, defaultInt);
	}

	public String getString(String key, String defaultInt) {
		return shareditorPreferences.getString(key, defaultInt);
	}

	public String getString(Context context, String key, String defaultValue) {
		if (shareditorPreferences == null || editor == null) {
			shareditorPreferences = context.getSharedPreferences(
					PREFERENCE_NAME, 0);
			editor = shareditorPreferences.edit();
		}
		if (shareditorPreferences != null) {
			return shareditorPreferences.getString(key, defaultValue);
		}
		return defaultValue;
	}

	public void saveString(String key, String value) {
		editor.putString(key, value);
		editor.commit();
	}

	public void remove(String key) {
		editor.remove(key);
		editor.commit();
	}

	public void destroy() {
		shareditorPreferences = null;
		editor = null;
		preferenceUtil = null;
	}
}
