package com.thinksns.jkfs.ui;

import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.ThinkSNSApplication;
import com.thinksns.jkfs.constant.SettingsUtil;
import com.thinksns.jkfs.util.common.PreferencesUtils;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class WelcomeActivity extends Activity {

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) {

                boolean is_login= PreferencesUtils.getBoolean(WelcomeActivity.this, SettingsUtil.IS_LOGIN);
                if(is_login)
                    startActivity(new Intent(WelcomeActivity.this,MainFragmentActivity.class));
				else{
                    startActivity(new Intent(WelcomeActivity.this,
                            LoginActivity.class));
                }
				WelcomeActivity.this.finish();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);

        ThinkSNSApplication application=(ThinkSNSApplication)getApplication();
		super.onCreate(savedInstanceState);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.welcome));

	}

	protected void onResume() {
		super.onResume();
		mHandler.sendEmptyMessageDelayed(0, 2000);

	}

}
