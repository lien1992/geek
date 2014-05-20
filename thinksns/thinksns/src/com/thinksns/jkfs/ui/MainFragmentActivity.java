package com.thinksns.jkfs.ui;

import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.ThinkSNSApplication;
import com.thinksns.jkfs.ui.fragment.AboutMeFragment;
import com.thinksns.jkfs.ui.fragment.ChanelFragment;
import com.thinksns.jkfs.ui.fragment.ChatFragment;
import com.thinksns.jkfs.ui.fragment.CollectionFragment;
import com.thinksns.jkfs.ui.fragment.HomeFragment;
import com.thinksns.jkfs.ui.fragment.MenuFragmentList;
import com.thinksns.jkfs.ui.fragment.SettingFragment;
import com.thinksns.jkfs.ui.fragment.WeibaFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

public class MainFragmentActivity extends SlidingFragmentActivity {

	private Fragment mContent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Think SNS");

		setContentView(R.layout.responsive_content_frame);

		// check if the content frame contains the menu frame
		if (findViewById(R.id.menu_frame) == null) {
			setBehindContentView(R.layout.menu_frame);
			getSlidingMenu().setSlidingEnabled(true);
			getSlidingMenu()
					.setTouchModeAbove(
							com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.TOUCHMODE_FULLSCREEN);
			// show home as up so we can toggle
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		} else {
			// add a dummy view
			View v = new View(this);
			setBehindContentView(v);
			getSlidingMenu().setSlidingEnabled(false);
			getSlidingMenu()
					.setTouchModeAbove(
							com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.TOUCHMODE_NONE);
		}

		// set the Above View Fragment
		if (savedInstanceState != null)
			mContent = getSupportFragmentManager().getFragment(
					savedInstanceState, "mContent");
		if (mContent == null)
			mContent = new HomeFragment();
		getSupportFragmentManager().beginTransaction().replace(
				R.id.content_frame, mContent).commit();

		// set the Behind View Fragment
		getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame,
				new MenuFragmentList()).commit();

		// customize the SlidingMenu
		com.jeremyfeinstein.slidingmenu.lib.SlidingMenu sm = getSlidingMenu();
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindScrollScale(0.25f);
		sm.setFadeDegree(0.25f);

		// // show the explanation dialog
		// if (savedInstanceState == null)
		// new AlertDialog.Builder(this)
		// .setTitle(R.string.what_is_this)
		// .setMessage(R.string.responsive_explanation)
		// .show();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			toggle();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mContent", mContent);
	}

	public void switchContent(int fragment_id) {
		Log.d("MOSL", fragment_id + "");
		switch (fragment_id) {

		case 1:
			mContent = new HomeFragment();
			break;
		case 2:
			mContent = new AboutMeFragment();
			break;
		case 3:
			mContent = new ChatFragment();
			break;
		case 4:
			mContent = new ChanelFragment();
			break;
		case 5:
			mContent = new WeibaFragment();
			break;
		case 6:
			mContent = new CollectionFragment();
			break;
		case 7:
			mContent = new SettingFragment();
			break;
		default:
			quitTheNumber();

		}
		getSupportFragmentManager().beginTransaction().replace(
				R.id.content_frame, mContent).commit();
		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			public void run() {
				getSlidingMenu().showContent();
			}
		}, 50);
	}

	private void quitTheNumber() {
		new AlertDialog.Builder(this).setPositiveButton("退出",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						ThinkSNSApplication application = ThinkSNSApplication
								.getInstance();
						application.quitAccount(MainFragmentActivity.this);
						MainFragmentActivity.this.finish();
					}
				}).setTitle(R.string.quit_account).setMessage(
				R.string.quit_account_explanation).show();
	}

	// public void onBirdPressed(int pos) {
	// Intent intent =new Intent(this,);
	// startActivity(intent);
	// }

}
