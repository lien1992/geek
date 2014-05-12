package com.thinksns.jkfs.ui;

import com.thinksns.jkfs.R;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Window;
import android.view.WindowManager;

public class WelcomeActivity extends Activity {

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) {
				startActivity(new Intent(WelcomeActivity.this,
						LoginActivity.class));
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
		getWindow().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.welcome));

	}

	protected void onResume() {
		super.onResume();
		mHandler.sendEmptyMessageDelayed(0, 2000);

	}

}
