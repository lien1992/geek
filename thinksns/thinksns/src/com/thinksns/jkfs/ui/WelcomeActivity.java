package com.thinksns.jkfs.ui;

import com.thinksns.jkfs.R;
import com.thinksns.jkfs.constant.SettingsUtil;
import com.thinksns.jkfs.ui.view.Shimmer;
import com.thinksns.jkfs.ui.view.ShimmerTextView;
import com.thinksns.jkfs.util.common.PreferencesUtils;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Window;
import android.view.WindowManager;

public class WelcomeActivity extends Activity {

	private ShimmerTextView tv;
	private Shimmer shimmer;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) {

				boolean is_login = PreferencesUtils.getBoolean(
						WelcomeActivity.this, SettingsUtil.IS_LOGIN);
				if (is_login)
					startActivity(new Intent(WelcomeActivity.this,
							MainFragmentActivity.class));
				else {
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

		super.onCreate(savedInstanceState);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		/*
		 * getWindow().setBackgroundDrawable(
		 * getResources().getDrawable(R.drawable.welcome));
		 */
		setContentView(R.layout.activity_welcome);

		tv = (ShimmerTextView) findViewById(R.id.thinksns_name);
		shimmer = new Shimmer();
		shimmer.start(tv);

	}

	protected void onResume() {
		super.onResume();
		mHandler.sendEmptyMessageDelayed(0, 2000);

	}

}
