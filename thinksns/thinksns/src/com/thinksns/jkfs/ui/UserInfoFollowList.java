package com.thinksns.jkfs.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.thinksns.jkfs.R;
import com.thinksns.jkfs.ui.fragment.FanListFragment;



/**
 * @author 邓思宇
 *	用于在用户界面显示微博LIST
 */
public class UserInfoFollowList extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userinfo_followlist);
		

	}
}
