package com.thinksns.jkfs.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.BaseFragment;

/**
 * 与我有关：at + 评论
 * 
 * @author wangjia
 * 
 */
public class AtAndCommentFragment extends BaseFragment {

    public static final String TAG="AtAndCommentFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.at_comment_fragment_layout,container,false);
    }

    @Override
	public void onLoadMore() {


	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub

	}

}
