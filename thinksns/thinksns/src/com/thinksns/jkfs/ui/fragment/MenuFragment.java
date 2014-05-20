package com.thinksns.jkfs.ui.fragment;

import java.util.HashMap;
import java.util.Map;

import com.thinksns.jkfs.R;
import com.thinksns.jkfs.ui.MainFragmentActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * SlidingMenu左侧菜单,待完善..
 * 
 * @author wangjia
 * 
 */
public class MenuFragment extends Fragment implements OnClickListener {
	private ImageView avatar;
	private TextView nick;
	private LinearLayout home;
	private LinearLayout at;
	private LinearLayout favorite;
	private LinearLayout chat;
	private LinearLayout channel;
	private LinearLayout weiba;
	private LinearLayout setting;
	private LinearLayout logout;
	private Map<Integer, Object> fragments = new HashMap<Integer, Object>();

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		fragments.put(R.id.sm_home, ((MainFragmentActivity) getActivity())
				.getWeiboMainFragment());

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		final ScrollView view = (ScrollView) inflater.inflate(
				R.layout.slidingmenu_behind, container, false);
		avatar = (ImageView) view.findViewById(R.id.sm_behind_avatar);
		nick = (TextView) view.findViewById(R.id.sm_behind_nick);
		home = (LinearLayout) view.findViewById(R.id.sm_home);
		at = (LinearLayout) view.findViewById(R.id.sm_at);
		favorite = (LinearLayout) view.findViewById(R.id.sm_favorite);
		chat = (LinearLayout) view.findViewById(R.id.sm_chat);
		channel = (LinearLayout) view.findViewById(R.id.sm_channel);
		weiba = (LinearLayout) view.findViewById(R.id.sm_weiba);
		setting = (LinearLayout) view.findViewById(R.id.sm_setting);
		logout = (LinearLayout) view.findViewById(R.id.sm_logout);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		home.setOnClickListener(this);
		at.setOnClickListener(this);
		favorite.setOnClickListener(this);
		chat.setOnClickListener(this);
		channel.setOnClickListener(this);
		weiba.setOnClickListener(this);
		setting.setOnClickListener(this);
		logout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.sm_home:
			changeBackground(R.id.sm_home);
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			// 此处隐藏其他Fragment..
			WeiboMainFragment fragment = (WeiboMainFragment) fragments
					.get(R.id.sm_home);
			ft.show(fragment);
			ft.commit();
			break;
		case R.id.sm_at:
			changeBackground(R.id.sm_at);
			break;
		case R.id.sm_favorite:
			changeBackground(R.id.sm_favorite);
			break;
		case R.id.sm_chat:
			changeBackground(R.id.sm_chat);
			break;
		case R.id.sm_channel:
			changeBackground(R.id.sm_channel);
			break;
		case R.id.sm_weiba:
			changeBackground(R.id.sm_weiba);
			break;
		case R.id.sm_setting:
			changeBackground(R.id.sm_setting);
			break;
		case R.id.sm_logout:
			changeBackground(R.id.sm_logout);
			break;
		}
	}

	private void changeBackground(int id) {
		// TODO Auto-generated method stub
		home.setBackgroundResource(R.color.white);
		at.setBackgroundResource(R.color.white);
		favorite.setBackgroundResource(R.color.white);
		chat.setBackgroundResource(R.color.white);
		channel.setBackgroundResource(R.color.white);
		weiba.setBackgroundResource(R.color.white);
		setting.setBackgroundResource(R.color.white);
		logout.setBackgroundResource(R.color.white);
		switch (id) {
		case R.id.sm_home:
			home.setBackgroundResource(R.color.grey);
			break;
		case R.id.sm_at:
			at.setBackgroundResource(R.color.grey);
			break;
		case R.id.sm_favorite:
			favorite.setBackgroundResource(R.color.grey);
			break;
		case R.id.sm_chat:
			chat.setBackgroundResource(R.color.grey);
			break;
		case R.id.sm_channel:
			channel.setBackgroundResource(R.color.grey);
			break;
		case R.id.sm_weiba:
			weiba.setBackgroundResource(R.color.grey);
			break;
		case R.id.sm_setting:
			setting.setBackgroundResource(R.color.grey);
			break;
		case R.id.sm_logout:
			logout.setBackgroundResource(R.color.grey);
			break;
		}

	}

}
