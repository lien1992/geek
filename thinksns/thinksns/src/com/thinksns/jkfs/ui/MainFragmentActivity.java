package com.thinksns.jkfs.ui;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.ThinkSNSApplication;
import com.thinksns.jkfs.bean.NotificationBean;
import com.thinksns.jkfs.ui.fragment.MenuFragment;
import com.thinksns.jkfs.ui.fragment.WeiboMainFragment;
import com.thinksns.jkfs.util.GetMsgService;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;

public class MainFragmentActivity extends SlidingFragmentActivity {

	private SlidingMenu sm;
	private AlarmManager alarm;
	private NewMsgReceiver receiver;
	private static MenuFragment menu;
	private String TAG = "MainFragmentActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_frame);
		setSlidingActionBarEnabled(true);

		sm = getSlidingMenu();
		// check if the content frame contains the menu frame
		if (findViewById(R.id.menu_frame) == null) {
			setBehindContentView(R.layout.menu_frame);
			sm.setSlidingEnabled(true);
			sm.setMode(SlidingMenu.LEFT);
			sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

		} else {
			// add a dummy view
			View v = new View(this);
			setBehindContentView(v);
			sm.setSlidingEnabled(false);
			sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}

		if (savedInstanceState == null) {
			Fragment weiboMain = new WeiboMainFragment();
			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			transaction.replace(R.id.content_frame, weiboMain,
					WeiboMainFragment.class.getName());
			transaction.commit();
			menu = new MenuFragment();
			FragmentTransaction menuTransation = getSupportFragmentManager()
					.beginTransaction();
			menuTransation.replace(R.id.menu_frame, menu, MenuFragment.class
					.getName());
			sm.showContent();
			menuTransation.commit();
			ThinkSNSApplication.getInstance().setMenu(menu);

			// customize the SlidingMenu
			sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
			sm.setShadowWidthRes(R.dimen.shadow_width);
			sm.setShadowDrawable(R.drawable.slidingmenu_shadow);
			sm.setBehindScrollScale(0.25f);
			sm.setFadeDegree(0.25f);
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(this, GetMsgService.class);
		PendingIntent pendingIntent = PendingIntent.getService(this, 2014,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		long triggerAtTime = SystemClock.elapsedRealtime();
		// 每10s请求一次服务器获取未读消息数目
		alarm.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, triggerAtTime,
				10 * 1000, pendingIntent);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(this, GetMsgService.class);
		PendingIntent pendingIntent = PendingIntent.getService(this, 2014,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		alarm.cancel(pendingIntent);
	}

	@Override
	public void onBackPressed() {
		if (sm.isMenuShowing()) {
			sm.showContent();
		} else {
			super.onBackPressed();
		}
	}

	public void switchContent(Fragment fragment) {
		FragmentManager f = getSupportFragmentManager();
		FragmentTransaction ft = f.beginTransaction();
		ft.replace(R.id.content_frame, fragment).commit();
		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			public void run() {
				getSlidingMenu().showContent();
			}
		}, 50);
	}

	public static class NewMsgReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			NotificationBean comment_unread = intent
					.getParcelableExtra("comment");
			NotificationBean at_unread = intent.getParcelableExtra("atme");
			NotificationBean pm_unread = intent.getParcelableExtra("pm");

			Log.d("wj", "NotificationBean comment count:"
					+ comment_unread.getCount());
			Log.d("wj", "NotificationBean at count" + at_unread.getCount());
			menu.setUnread(comment_unread, at_unread, pm_unread);
		}

	}

}