package com.thinksns.jkfs.ui.adapter;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.thinksns.jkfs.ui.fragment.PostListFragment;
import com.thinksns.jkfs.ui.fragment.WeibaSearchListFragment;

public class WeibaSearchAdapter extends FragmentStatePagerAdapter {
	private PostListFragment postList;
	private WeibaSearchListFragment weibaList;
	public WeibaSearchAdapter(FragmentManager fm, Context mContext, Fragment fragmentForHide) {
		super(fm);
		// TODO Auto-generated constructor stub
		postList= new PostListFragment();
		weibaList=new WeibaSearchListFragment();
		postList.setContext(mContext, fragmentForHide);
		weibaList.setContext(mContext);
		fm.beginTransaction().hide(postList).hide(weibaList).commit();
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		switch(arg0){
		case 1:return postList;
		default:return weibaList;      
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 2;
	}
	

}