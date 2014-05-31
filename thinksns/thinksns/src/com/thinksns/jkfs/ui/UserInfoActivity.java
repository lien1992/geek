package com.thinksns.jkfs.ui;

import java.lang.reflect.Field;
import java.util.HashMap;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.ThinkSNSApplication;
import com.thinksns.jkfs.bean.AccountBean;
import com.thinksns.jkfs.bean.UserInfoBean;
import com.thinksns.jkfs.constant.HttpConstant;
import com.thinksns.jkfs.util.http.HttpMethod;
import com.thinksns.jkfs.util.http.HttpUtility;

/**
 * @author 邓思宇 个人主页列表 只有框架 无功能
 * 
 */
@SuppressWarnings("deprecation")
public class UserInfoActivity extends TabActivity {

	private TabHost tabHost;
	private TabWidget tabWidget;
	Field mBottomLeftStrip;
	Field mBottomRightStrip;
	private UserInfoBean userinfo;

	private ThinkSNSApplication application;
	private AccountBean account;

	
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:

				ImageView head = (ImageView) findViewById(
						R.id.s_head);
				TextView uname = (TextView)findViewById(
						R.id.s_username);
				TextView address = (TextView) findViewById(
						R.id.s_address);
				ImageView sex = (ImageView) findViewById(
						R.id.s_sex);

				

				uname.setText(userinfo.getUname());
				address.setText(userinfo.getLocation());
			
				ImageLoader.getInstance().displayImage(userinfo.getAvatar(),
						head);

				if (userinfo.getSex() == "男") {
					sex.setBackgroundResource(R.drawable.male);
				} else {
					sex.setBackgroundResource(R.drawable.female);
				}

				break;

			}

		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_userinfo);

		application = (ThinkSNSApplication) this.getApplicationContext();
		account = application.getAccount(this);

		// 获得信息 改写VIEW
//		ImageView head = (ImageView) findViewById(R.id.s_head);
//		ImageView sex = (ImageView) findViewById(R.id.s_sex);
//		TextView username = (TextView) findViewById(R.id.s_username);
//		TextView add = (TextView) findViewById(R.id.s_address);
//		Button but1 = (Button) findViewById(R.id.s_follow);
		
		Bundle extras = getIntent().getExtras();
		String uuid = extras.getString("uuid");
		openUserPage(uuid);

		// 创建TABHOST
		TabHost tabHost = getTabHost();
		TabHost.TabSpec spec;
		Intent intent;
		Intent intent2;
		intent = new Intent().setClass(this, OtherInfoActivity.class);
		intent2 = new Intent().setClass(this, OtherInfoActivity.class);

		spec = tabHost.newTabSpec("1").setIndicator("微博" // add one tab
		) // ***need to change to a list xml
				.setContent(intent);
		tabHost.addTab(spec);

		spec = tabHost.newTabSpec("2").setIndicator("关注").setContent(intent2);
		tabHost.addTab(spec);

		spec = tabHost.newTabSpec("3").setIndicator("粉丝").setContent(intent2);
		tabHost.addTab(spec);

		spec = tabHost.newTabSpec("4").setIndicator("详细资料").setContent(intent);
		tabHost.addTab(spec);

		makeupTab();// 设置TABHOST背景图片

		tabHost.setCurrentTab(0);// 当前显示的TAB

	}

	// 接受到得到的UID 获得JSON信息

	private void openUserPage(final String uid) {

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
				map.put("oauth_token_secret", account.getOauth_token_secret());
				String json = HttpUtility.getInstance().executeNormalTask(
						HttpMethod.Get, HttpConstant.THINKSNS_URL, map);

				if (json != null && !"".equals(json)) {

					userinfo = gson.fromJson(json, UserInfoBean.class);
					mHandler.sendEmptyMessage(0);
				}

			}
		}.start();

	}

	// 设置TABHOST背景图片的方法
	@SuppressWarnings("deprecation")
	public TabHost makeupTab() {
		if (this.tabHost == null) {
			tabHost = (TabHost) findViewById(android.R.id.tabhost);
			tabWidget = (TabWidget) findViewById(android.R.id.tabs);
			tabHost.setup();
			tabHost.bringToFront();

			if (Integer.valueOf(Build.VERSION.SDK) <= 7) {
				try {
					mBottomLeftStrip = tabWidget.getClass().getDeclaredField(
							"mBottomLeftStrip");
					mBottomRightStrip = tabWidget.getClass().getDeclaredField(
							"mBottomRightStrip");
					if (!mBottomLeftStrip.isAccessible()) {
						mBottomLeftStrip.setAccessible(true);
					}
					if (!mBottomRightStrip.isAccessible()) {
						mBottomRightStrip.setAccessible(true);
					}
					mBottomLeftStrip.set(tabWidget,
							getResources().getDrawable(R.drawable.linee));
					mBottomRightStrip.set(tabWidget, getResources()
							.getDrawable(R.drawable.linee));

				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				try {
					mBottomLeftStrip = tabWidget.getClass().getDeclaredField(
							"mLeftStrip");
					mBottomRightStrip = tabWidget.getClass().getDeclaredField(
							"mRightStrip");
					if (!mBottomLeftStrip.isAccessible()) {
						mBottomLeftStrip.setAccessible(true);
					}
					if (!mBottomRightStrip.isAccessible()) {
						mBottomRightStrip.setAccessible(true);
					}
					mBottomLeftStrip.set(tabWidget,
							getResources().getDrawable(R.drawable.linee));
					mBottomRightStrip.set(tabWidget, getResources()
							.getDrawable(R.drawable.linee));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			for (int i = 0; i < tabWidget.getChildCount(); i++) {

				final TextView tv = (TextView) tabWidget.getChildAt(i)
						.findViewById(android.R.id.title);
				tv.setTextColor(Color.BLACK);
				View vvv = tabWidget.getChildAt(i);
				if (tabHost.getCurrentTab() == i) {
					vvv.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.focus));
				} else {
					vvv.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.unfocus));
				}
			}

			tabHost.setOnTabChangedListener(new OnTabChangeListener() {

				@Override
				public void onTabChanged(String tabId) {
					// TODO Auto-generated method stub
					for (int i = 0; i < tabWidget.getChildCount(); i++) {
						View vvv = tabWidget.getChildAt(i);
						if (tabHost.getCurrentTab() == i) {
							vvv.setBackgroundDrawable(getResources()
									.getDrawable(R.drawable.focus));
						} else {
							vvv.setBackgroundDrawable(getResources()
									.getDrawable(R.drawable.unfocus));
						}
					}
				}
			});

		} else {
			return tabHost;
		}
		return null;
	}

}
