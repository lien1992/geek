package com.thinksns.jkfs.ui;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.ui.fragment.AboutMeFragment;
import com.thinksns.jkfs.ui.fragment.ChanelFragment;
import com.thinksns.jkfs.ui.fragment.ChatFragment;
import com.thinksns.jkfs.ui.fragment.CollectionFragment;
import com.thinksns.jkfs.ui.fragment.MenuFragment;
import com.thinksns.jkfs.ui.fragment.SettingFragment;
import com.thinksns.jkfs.ui.fragment.WeibaFragment;
import com.thinksns.jkfs.ui.fragment.WeiboMainFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;

/**
 * MainFragmentActivity,待完善..
 * 
 * @author wangjia
 * 
 */
public class MainFragmentActivity extends SlidingFragmentActivity {

	private SlidingMenu sm;
    public static final int[] FragmentId={1,2,3,4,5,6,7,8};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        int a[]=new int[2];
        int[] b=new int[2];
		setContentView(R.layout.content_frame);

		// check if the content frame contains the menu frame
		if (findViewById(R.id.menu_frame) == null) {
			setBehindContentView(R.layout.menu_frame);
			getSlidingMenu().setSlidingEnabled(true);
			getSlidingMenu().setMode(SlidingMenu.LEFT);
			getSlidingMenu()
					.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
			// show home as up so we can toggle
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		} else {
			// add a dummy view
			View v = new View(this);
			setBehindContentView(v);
			getSlidingMenu().setSlidingEnabled(false);
			getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}

		if (savedInstanceState == null) {
			Fragment weiboMain = getWeiboMainFragment();
			// 待添加其他Fragment..
			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			if (!weiboMain.isAdded()) {
				transaction.add(R.id.content_frame, weiboMain,
						WeiboMainFragment.class.getName());
				transaction.hide(weiboMain);
			}
			transaction.commit();

			FragmentTransaction menuTransation = getSupportFragmentManager()
					.beginTransaction();
			menuTransation.replace(R.id.menu_frame, new MenuFragment(),
					MenuFragment.class.getName());
			getSlidingMenu().showContent();
			menuTransation.commit();
		}

		// customize the SlidingMenu
		sm = getSlidingMenu();
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.slidingmenu_shadow);
		sm.setBehindScrollScale(0.25f);
		sm.setFadeDegree(0.25f);

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
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			toggle();
		}
		return super.onOptionsItemSelected(item);
	}

	public WeiboMainFragment getWeiboMainFragment() {
		WeiboMainFragment fragment = ((WeiboMainFragment) getSupportFragmentManager()
				.findFragmentByTag(WeiboMainFragment.class.getName()));
		if (fragment == null) {
			fragment = new WeiboMainFragment();
		}
		return fragment;
	}


    public void switchContent(int fragmentId) {
        FragmentManager f=getSupportFragmentManager();
        FragmentTransaction ft=f.beginTransaction();
        switch (fragmentId){
            case 1:
                WeiboMainFragment weibo=new WeiboMainFragment();
                ft.replace(R.id.content_frame,weibo);
                ft.commit();
                break;
            case 2:
                AboutMeFragment aboutme=new AboutMeFragment();
                ft.replace(R.id.content_frame,aboutme);
                ft.commit();
                break;
            case 3:
                CollectionFragment collection =new CollectionFragment();
                ft.replace(R.id.content_frame,collection);
                ft.commit();
                break;
            case 4:
                ChatFragment chat=new ChatFragment();
                ft.replace(R.id.content_frame,chat);
                ft.commit();
                break;
            case 5:
                ChanelFragment chanel=new ChanelFragment();
                ft.replace(R.id.content_frame,chanel);
                ft.commit();
                break;
            case 6:
                WeibaFragment weiba=new WeibaFragment();
                ft.replace(R.id.content_frame,weiba);
                ft.commit();
                break;
            case 7:
                SettingFragment setting=new SettingFragment();
                ft.replace(R.id.content_frame,setting);
                ft.commit();
                break;
        }
    }
}
