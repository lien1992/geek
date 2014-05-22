package com.thinksns.jkfs.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;

import com.thinksns.jkfs.ui.fragment.AboutMeFragment;
import com.thinksns.jkfs.ui.fragment.WeiboListFragment;

/**
 * 仍待修改..
 * 
 */
public class MainFragmentPagerAdapter extends FragmentPagerAdapter {

	public MainFragmentPagerAdapter(FragmentActivity activity) {
		super(activity.getSupportFragmentManager());
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
		case 1:
			return new WeiboListFragment();
		case 2:
			return new AboutMeFragment();
		default:
			return new WeiboListFragment();
		}
	}

	@Override
	public int getCount() {
		return 2;
	}

}