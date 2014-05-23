package com.thinksns.jkfs.base;

import com.thinksns.jkfs.ui.view.PullToRefreshListView;
import com.thinksns.jkfs.ui.view.PullToRefreshListView.RefreshAndLoadMoreListener;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mosl on 14-5-11. 带有下拉刷新ListView的Fragment基类
 */
public abstract class BaseFragment extends Fragment implements
		RefreshAndLoadMoreListener {



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//mInflater = inflater;
		return super.onCreateView(inflater, container, savedInstanceState);
	}

}
