package com.thinksns.jkfs.ui;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.ui.fragment.MenuFragment;
import com.thinksns.jkfs.ui.fragment.WeiboMainFragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

public class MainFragmentActivity extends SlidingFragmentActivity {

	private SlidingMenu sm;
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
			FragmentTransaction menuTransation = getSupportFragmentManager()
					.beginTransaction();
			menuTransation.replace(R.id.menu_frame, new MenuFragment(),
					MenuFragment.class.getName());
			sm.showContent();
			menuTransation.commit();

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
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
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

}