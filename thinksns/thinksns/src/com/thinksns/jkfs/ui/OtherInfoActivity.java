package com.thinksns.jkfs.ui;

import java.util.HashMap;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.ThinkSNSApplication;
import com.thinksns.jkfs.bean.AccountBean;
import com.thinksns.jkfs.bean.UserInfoBean;
import com.thinksns.jkfs.constant.HttpConstant;
import com.thinksns.jkfs.ui.fragment.AboutMeFragment;
import com.thinksns.jkfs.util.Utility;
import com.thinksns.jkfs.util.http.HttpMethod;
import com.thinksns.jkfs.util.http.HttpUtility;

public class OtherInfoActivity extends FragmentActivity {
	private ImageView back;
	private ProgressBar progressBar;
	private AccountBean account;
	private UserInfoBean userinfo;
	private String uid;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				progressBar.setVisibility(View.INVISIBLE);
				Toast.makeText(OtherInfoActivity.this, "网络未连接",
						Toast.LENGTH_SHORT).show();
				break;
			case 1:
				progressBar.setVisibility(View.INVISIBLE);
				android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
				android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();
				AboutMeFragment fragment = new AboutMeFragment("1", uid,
						userinfo.follow_state.getFollowing() + "");
				fragmentTransaction.replace(R.id.other_page_layout, fragment);
				fragmentTransaction.commit();
				break;
			case 2:
				progressBar.setVisibility(View.INVISIBLE);
				Toast.makeText(OtherInfoActivity.this, "出现意外，用户主页加载失败:(",
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userinfo_otherpage);
		account = ((ThinkSNSApplication) getApplicationContext())
				.getAccount(this);

		back = (ImageView) findViewById(R.id.user_page_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}

		});
		progressBar = (ProgressBar) findViewById(R.id.user_page_progressbar);

		Bundle extras = getIntent().getExtras();
		String user = extras.getString("userinfo");
		String fo = extras.getString("following");
		uid = extras.getString("uid");

		if (user != null && fo != null) {
			android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
			android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			AboutMeFragment fragment = new AboutMeFragment("1", user, fo);
			fragmentTransaction.replace(R.id.other_page_layout, fragment);
			fragmentTransaction.commit();
			progressBar.setVisibility(View.INVISIBLE);
		} else if (uid != null)
			getUserInfo(uid);
		else
			mHandler.sendEmptyMessage(2);

	}

	private void getUserInfo(final String uid) {
		// TODO Auto-generated method stub
		if (Utility.isConnected(this)) {

			new Thread() {
				@Override
				public void run() {
					Gson gson = new Gson();
					HashMap<String, String> map = new HashMap<String, String>();
					map = new HashMap<String, String>();
					map.put("app", "api");
					map.put("mod", "User");
					map.put("act", "show");
					map.put("user_id", uid);
					map.put("oauth_token", account.getOauth_token());
					map.put("oauth_token_secret", account
							.getOauth_token_secret());
					String json = HttpUtility.getInstance().executeNormalTask(
							HttpMethod.Get, HttpConstant.THINKSNS_URL, map);
					if (json != null && !"".equals(json)
							&& json.startsWith("{")) {
						userinfo = gson.fromJson(json, UserInfoBean.class);
						if (userinfo != null)
							mHandler.sendEmptyMessage(1);
						else
							mHandler.sendEmptyMessage(2);
					}
				}
			}.start();
		} else
			mHandler.sendEmptyMessage(0);

	}

}
