package com.thinksns.jkfs.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.thinksns.jkfs.ui.fragment.AboutMeFragment;
import com.thinksns.jkfs.ui.fragment.WeiboListFragment;
import com.thinksns.jkfs.ui.fragment.WeiboListOfMineFragment;
import com.thinksns.jkfs.ui.fragment.WeiboMainFragment;

public class MainFragmentPagerAdapter extends FragmentStatePagerAdapter {

	private Fragment fragmentAtP0;
	private final FragmentManager fm;
	private WeiboMainFragment mainFragment;

	public MainFragmentPagerAdapter(FragmentActivity activity,
			WeiboMainFragment fragment) {
		super(activity.getSupportFragmentManager());
		fm = activity.getSupportFragmentManager();
		mainFragment = fragment;
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
		case 0:
			if (fragmentAtP0 == null) {
				fragmentAtP0 = new WeiboListFragment();
				mainFragment.setSwitchGroupListener(new SwitchGroupListener() {

					@Override
					public void onSwitch() {
						// TODO Auto-generated method stub
						fm.beginTransaction().remove(fragmentAtP0).commit();
						if (fragmentAtP0 instanceof WeiboListFragment)
							fragmentAtP0 = new WeiboListOfMineFragment();
						else
							fragmentAtP0 = new WeiboListFragment();
						notifyDataSetChanged();
					}

				});

			}
			return fragmentAtP0;
		case 1:
			return new AboutMeFragment();
		default:
			return new WeiboListFragment();
		}
	}

	@Override
	public int getItemPosition(Object object) {
		if (object instanceof WeiboListFragment
				&& fragmentAtP0 instanceof WeiboListOfMineFragment)
			return POSITION_NONE;
		if (object instanceof WeiboListOfMineFragment
				&& fragmentAtP0 instanceof WeiboListFragment)
			return POSITION_NONE;
		return POSITION_UNCHANGED;
	}

	@Override
	public int getCount() {
		return 2;
	}

	public interface SwitchGroupListener {
		public void onSwitch();
	}

}
