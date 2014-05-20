package com.thinksns.jkfs.ui.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;

import com.thinksns.jkfs.R;
import com.thinksns.jkfs.ui.fragment.AboutMeFragment;
import com.thinksns.jkfs.ui.fragment.ChanelFragment;
import com.thinksns.jkfs.ui.fragment.ChatFragment;
import com.thinksns.jkfs.ui.fragment.CollectionFragment;
import com.thinksns.jkfs.ui.fragment.HomeFragment;
import com.thinksns.jkfs.ui.fragment.WeibaFragment;

/**
 * 仍待修改..
 * 
 */
public class MainFragmentPagerAdapter extends FragmentPagerAdapter {
	private Activity mActivity;
	protected static final String[] CONTENT = new String[] { "主页", "聊天",
			"与我有关", "收藏", "频道", "微吧" };
	protected static final int[] ICONS = new int[] { R.drawable.home_selector,
			R.drawable.chat_selector, R.drawable.at_selector,
			R.drawable.collection_selector, R.drawable.chanel_selector,
			R.drawable.weiba_selector, };

	private int mCount = CONTENT.length;

	public MainFragmentPagerAdapter(FragmentActivity activity) {
		super(activity.getSupportFragmentManager());
		this.mActivity = activity;
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
		case 1:
			return new ChatFragment();
		case 2:
			return new AboutMeFragment();
		case 3:
			return new CollectionFragment();
		case 4:
			return new ChanelFragment();
		case 5:
			return new WeibaFragment();
		default:
			return new ChatFragment();
		}
	}

	@Override
	public int getCount() {
		return mCount;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return MainFragmentPagerAdapter.CONTENT[position % CONTENT.length];
	}

	public void setCount(int count) {
		if (count > 0 && count <= 10) {
			mCount = count;
			notifyDataSetChanged();
		}
	}
}