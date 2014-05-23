package com.thinksns.jkfs.ui;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.ui.fragment.AboutMeFragment;
import com.thinksns.jkfs.ui.fragment.AtAndCommentFragment;
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
import android.view.View;

/**
 * MainFragmentActivity,待完善..
 * 
 * 调试时，只把MainFragmentActivity和MenuFragment里面
 * 自己的Fragment部分的注释取消，把其他人的Fragments都注释掉。
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
			//Fragment weiboMain = getWeiboMainFragment();
/*			Fragment atAndComment = getAtAndCommentFragment();
			Fragment collection = getCollectionFragment();
			Fragment chat = getChatFragment();
			Fragment channel = getChannelFragment();
			Fragment weiba = getWeibaFragment();
			Fragment setting = getSettingFragment();*/
			// 添加 Fragments, 设置 tag, 并 hide
			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
/*			if (!weiboMain.isAdded()) {
				transaction.add(R.id.content_frame, weiboMain,
						WeiboMainFragment.class.getName());
			}
*/
/*			if (!atAndComment.isAdded()) {
				transaction.add(R.id.content_frame, atAndComment,
						AtAndCommentFragment.class.getName());
				transaction.hide(atAndComment);
			}
			if (!collection.isAdded()) {
				transaction.add(R.id.content_frame, collection,
						CollectionFragment.class.getName());
				transaction.hide(collection);
			}
			if (!chat.isAdded()) {
				transaction.add(R.id.content_frame, chat,
						ChatFragment.class.getName());
				transaction.hide(chat);
			}
			if (!channel.isAdded()) {
				transaction.add(R.id.content_frame, channel,
						ChanelFragment.class.getName());
				transaction.hide(channel);
			}
			if (!weiba.isAdded()) {
				transaction.add(R.id.content_frame, weiba,
						WeibaFragment.class.getName());
				transaction.hide(weiba);
			}
			if (!setting.isAdded()) {
				transaction.add(R.id.content_frame, setting,
						SettingFragment.class.getName());
				transaction.hide(setting);
			}*/

			transaction.commit();

			FragmentTransaction menuTransation = getSupportFragmentManager()
					.beginTransaction();
			menuTransation.replace(R.id.menu_frame, new MenuFragment(),
					MenuFragment.class.getName());
			sm.showContent();
			menuTransation.commit();
		}

		// customize the SlidingMenu
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
	public void onBackPressed() {
		if (sm.isMenuShowing()) {
			sm.showContent();
		} else {
			super.onBackPressed();
		}
	}

	/* 获得侧滑菜单中的各个Fragment */

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
	public AtAndCommentFragment getAtAndCommentFragment() {
		AtAndCommentFragment fragment = ((AtAndCommentFragment) getSupportFragmentManager()
				.findFragmentByTag(AtAndCommentFragment.class.getName()));
		if (fragment == null) {
			fragment = new AtAndCommentFragment();
		}
		return fragment;
	}

	public CollectionFragment getCollectionFragment() {
		CollectionFragment fragment = ((CollectionFragment) getSupportFragmentManager()
				.findFragmentByTag(CollectionFragment.class.getName()));
		if (fragment == null) {
			fragment = new CollectionFragment();
		}
		return fragment;
	}

	public ChatFragment getChatFragment() {
		ChatFragment fragment = ((ChatFragment) getSupportFragmentManager()
				.findFragmentByTag(ChatFragment.class.getName()));
		if (fragment == null) {
			fragment = new ChatFragment();
		}
		return fragment;
	}

	public ChanelFragment getChannelFragment() {
		ChanelFragment fragment = ((ChanelFragment) getSupportFragmentManager()
				.findFragmentByTag(ChanelFragment.class.getName()));
		if (fragment == null) {
			fragment = new ChanelFragment();
		}
		return fragment;
	}

	public WeibaFragment getWeibaFragment() {
		WeibaFragment fragment = ((WeibaFragment) getSupportFragmentManager()
				.findFragmentByTag(WeibaFragment.class.getName()));
		if (fragment == null) {
			fragment = new WeibaFragment();
		}
		return fragment;
	}

	public SettingFragment getSettingFragment() {
		SettingFragment fragment = ((SettingFragment) getSupportFragmentManager()
				.findFragmentByTag(SettingFragment.class.getName()));
		if (fragment == null) {
			fragment = new SettingFragment();
		}
		return fragment;
	}

}
