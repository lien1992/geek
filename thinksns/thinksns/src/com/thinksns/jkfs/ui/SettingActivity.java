package com.thinksns.jkfs.ui;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.DbUtils.DbUpgradeListener;
import com.lidroid.xutils.exception.DbException;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.ThinkSNSApplication;
import com.thinksns.jkfs.constant.BaseConstant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceClickListener;
import android.widget.Toast;

public class SettingActivity extends PreferenceActivity implements
		OnSharedPreferenceChangeListener, OnPreferenceClickListener {
	private CheckBoxPreference mCheckPreference;
	private Preference aboutUs;
	private Preference clearCache;
	private ThinkSNSApplication application;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		initPreferences();
		application = (ThinkSNSApplication) getApplicationContext();
	}

	private void initPreferences() {
		mCheckPreference = (CheckBoxPreference) findPreference(BaseConstant.NO_IMAGE_MODE_KEY);
		aboutUs = findPreference(BaseConstant.ABOUT_US_KEY);
		clearCache = findPreference(BaseConstant.CLEAR_CACHE_KEY);
	}

	protected void onResume() {
		super.onResume();
		SharedPreferences sharedPreferences = getPreferenceScreen()
				.getSharedPreferences();
		sharedPreferences.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		// TODO Auto-generated method stub
		if (aboutUs == preference)
			Toast.makeText(this, "click", Toast.LENGTH_SHORT).show();
		return false;
	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		// TODO Auto-generated method stub
		if (aboutUs == preference)
			startActivity(new Intent(this, AboutUsActivity.class));
		if (clearCache == preference) {
			DbUtils db = DbUtils.create(this, "thinksns2.db", 10,
					new DbUpgradeListener() {

						@Override
						public void onUpgrade(DbUtils db, int oldVersion,
								int newVersion) {
							// TODO Auto-generated method stub
							try {
								if (!application.isClearCache()) {
									db.dropDb();
									application.setClearCache(true);
									Toast.makeText(SettingActivity.this,
											"缓存已清除", Toast.LENGTH_SHORT).show();

								}
							} catch (DbException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}

					});

		}
		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}
}
