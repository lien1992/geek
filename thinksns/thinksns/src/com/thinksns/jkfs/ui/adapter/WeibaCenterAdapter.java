package com.thinksns.jkfs.ui.adapter;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.thinksns.jkfs.base.ThinkSNSApplication;
import com.thinksns.jkfs.ui.fragment.PostListFragment;

public class WeibaCenterAdapter extends FragmentStatePagerAdapter {
	
	private static final String TAG = "WeibaCenterAdapter";
	private static final String APP = "api";
	private static final String MOD = "Weiba";
	private static final String POSTEDS = "posteds";
	private static final String COMMENTEDS = "commenteds";
	private static final String FAVORITE_LIST = "favorite_list";
	private static String OAUTH_TOKEN;
	private static String OAUTH_TOKEN_SECRECT;
	
	private Activity mContext;
	private Fragment fragmentForHide;
	
	public WeibaCenterAdapter(FragmentManager fm,Activity mContext,Fragment fragmentForHide) {
		super(fm);
		this.mContext = mContext;
		this.fragmentForHide=fragmentForHide;
		ThinkSNSApplication application = ThinkSNSApplication.getInstance();
		OAUTH_TOKEN = application.getOauth_token(mContext);
		OAUTH_TOKEN_SECRECT = application.getOauth_token_secret(mContext);
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		switch(arg0){
		case 0:
			final Map<String, String> posteds = new HashMap<String, String>();
			posteds.put("app", APP);
			posteds.put("mod", MOD);
			posteds.put("act", POSTEDS);
			posteds.put("oauth_token", OAUTH_TOKEN);
			posteds.put("oauth_token_secret", OAUTH_TOKEN_SECRECT);
			PostListFragment postedPostList = new PostListFragment();
			postedPostList.setContext(mContext, fragmentForHide);
			postedPostList.setAttribute(posteds, true);
			postedPostList.refresh();
			return  postedPostList;
		case 1:
			final Map<String, String> commenteds = new HashMap<String, String>();
			commenteds.put("app", APP);
			commenteds.put("mod", MOD);
			commenteds.put("act", COMMENTEDS);
			commenteds.put("oauth_token", OAUTH_TOKEN);
			commenteds.put("oauth_token_secret", OAUTH_TOKEN_SECRECT);
			PostListFragment commentedPostList = new PostListFragment();
			commentedPostList.setContext(mContext, fragmentForHide);
			commentedPostList.setAttribute(commenteds, true);
			commentedPostList.refresh();
			return  commentedPostList;
		case 2:
			final Map<String, String> favorite = new HashMap<String, String>();
			favorite.put("app", APP);
			favorite.put("mod", MOD);
			favorite.put("act", FAVORITE_LIST);
			favorite.put("oauth_token", OAUTH_TOKEN);
			favorite.put("oauth_token_secret", OAUTH_TOKEN_SECRECT);
			PostListFragment favoritePostList = new PostListFragment();
			favoritePostList.setContext(mContext, fragmentForHide);
			favoritePostList.setAttribute(favorite, true);
			favoritePostList.refresh();
			return  favoritePostList;	
		}
		return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 3;
	}


}
