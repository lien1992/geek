package com.thinksns.jkfs.ui.fragment;

import java.util.HashMap;
import java.util.Map;

import com.thinksns.jkfs.R;
import com.thinksns.jkfs.ui.MainFragmentActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * SlidingMenu左侧菜单。调试时自行将相关Fragment注释取消，git push前将自己的Fragment注释掉
 * 
 * @author wangjia
 * 
 */
public class MenuFragment extends Fragment implements OnClickListener {

    private String TAG="MenuFragment";
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
	private Map<Integer, Fragment> fragments = new HashMap<Integer, Fragment>();

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.d(TAG,"menuFramgnet onActivityCreated");
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        Log.d(TAG,"menuFramgnet onCreateView");
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
        Log.d(TAG,"menuFramgnet onViewCreated");
		super.onViewCreated(view, savedInstanceState);
		home.setOnClickListener(this);
		at.setOnClickListener(this);
		favorite.setOnClickListener(this);
		chat.setOnClickListener(this);
		channel.setOnClickListener(this);
		weiba.setOnClickListener(this);
		setting.setOnClickListener(this);
		logout.setOnClickListener(this);
		changeBackground(R.id.sm_home);

		// 替换头像、微博用户名..
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.sm_home:
			changeBackground(R.id.sm_home);
            WeiboMainFragment weiboMainFragment=new WeiboMainFragment();
            if(weiboMainFragment!=null)
            switchFragment(weiboMainFragment);
			break;
		case R.id.sm_at:
			changeBackground(R.id.sm_at);
            AtAndCommentFragment atAndCommentFragment=new AtAndCommentFragment();
            if(atAndCommentFragment!=null)
            switchFragment(atAndCommentFragment);
			break;
		case R.id.sm_favorite:
			changeBackground(R.id.sm_favorite);
            CollectionFragment collectionFragment=new CollectionFragment();
            if(collectionFragment!=null)
            switchFragment(collectionFragment);
			break;
		case R.id.sm_chat:
			changeBackground(R.id.sm_chat);
            ChatFragment chatFragment=new ChatFragment();
            if(chatFragment!=null)
            switchFragment(chatFragment);
			break;
		case R.id.sm_channel:
            ChannelFragment chanelFragment=new ChannelFragment();
            if(chanelFragment!=null)
            switchFragment(chanelFragment);
			changeBackground(R.id.sm_channel);
			break;
		case R.id.sm_weiba:
			changeBackground(R.id.sm_weiba);
            WeibaFragment weibaFragment=new WeibaFragment();
            if(weibaFragment!=null)
            switchFragment(weibaFragment);
			break;
		case R.id.sm_setting:
			changeBackground(R.id.sm_setting);
            SettingFragment settingFragment=new SettingFragment();
            if(settingFragment!=null)
            switchFragment(settingFragment);
			break;
		case R.id.sm_logout:
			changeBackground(R.id.sm_logout);
            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.quit_account)
                    .setMessage(R.string.quit_account_explanation).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getActivity().finish();
                }
            }).setNegativeButton("取消",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).show();
			break;
		}
	}

	private void changeBackground(int id) {

		home.setBackgroundResource(R.color.green_1);
		at.setBackgroundResource(R.color.green_1);
		favorite.setBackgroundResource(R.color.green_1);
		chat.setBackgroundResource(R.color.green_1);
		channel.setBackgroundResource(R.color.green_1);
		weiba.setBackgroundResource(R.color.green_1);
		setting.setBackgroundResource(R.color.green_1);
		logout.setBackgroundResource(R.color.green_1);
		switch (id) {
		case R.id.sm_home:
			home.setBackgroundResource(R.color.green_2);
			break;
		case R.id.sm_at:
			at.setBackgroundResource(R.color.green_2);
			break;
		case R.id.sm_favorite:
			favorite.setBackgroundResource(R.color.green_2);
			break;
		case R.id.sm_chat:
			chat.setBackgroundResource(R.color.green_2);
			break;
		case R.id.sm_channel:
			channel.setBackgroundResource(R.color.green_2);
			break;
		case R.id.sm_weiba:
			weiba.setBackgroundResource(R.color.green_2);
			break;
		case R.id.sm_setting:
			setting.setBackgroundResource(R.color.green_2);
			break;
		case R.id.sm_logout:
			logout.setBackgroundResource(R.color.green_2);
			break;
		}

	}

	// the meat of switching the above fragment
	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;

		if (getActivity() instanceof MainFragmentActivity) {
			MainFragmentActivity ra = (MainFragmentActivity) getActivity();
			ra.switchContent(fragment);
		}
	}
}
