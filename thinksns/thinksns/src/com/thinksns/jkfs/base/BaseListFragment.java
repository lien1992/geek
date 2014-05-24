package com.thinksns.jkfs.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thinksns.jkfs.ui.view.PullToRefreshListView;
import com.thinksns.jkfs.ui.view.PullToRefreshListView.RefreshAndLoadMoreListener;

/**
 * 带有下拉刷新ListView的Fragment基类
 * 
 * 
 */
public abstract class BaseListFragment extends Fragment implements
		RefreshAndLoadMoreListener {
	protected LayoutInflater mInflater;
	protected PullToRefreshListView listView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mInflater = inflater;
		return super.onCreateView(inflater, container, savedInstanceState);
	}
}
