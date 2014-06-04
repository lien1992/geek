package com.thinksns.jkfs.ui;

import com.thinksns.jkfs.R;
import com.thinksns.jkfs.constant.BaseConstant;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceClickListener;
import android.widget.Toast;

public class SettingActivity extends PreferenceActivity implements
		OnSharedPreferenceChangeListener, OnPreferenceClickListener {
	private CheckBoxPreference mCheckPreference;
	private Preference aboutUs;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.preferences);
		initPreferences();
	}

	private void initPreferences() {
		mCheckPreference = (CheckBoxPreference) findPreference(BaseConstant.NO_IMAGE_MODE_KEY);
		aboutUs = findPreference(BaseConstant.ABOUT_US_KEY);
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
}
