package com.thinksns.jkfs.ui.fragment;

import java.util.HashMap;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.ThinkSNSApplication;
import com.thinksns.jkfs.bean.AccountBean;
import com.thinksns.jkfs.bean.NotificationBean;
import com.thinksns.jkfs.bean.UserInfoBean;
import com.thinksns.jkfs.constant.HttpConstant;
import com.thinksns.jkfs.ui.MainFragmentActivity;
import com.thinksns.jkfs.ui.view.RoundAngleImageView;
import com.thinksns.jkfs.util.http.HttpMethod;
import com.thinksns.jkfs.util.http.HttpUtility;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MenuFragment extends Fragment implements OnClickListener {

	public static final String TAG = "MenuFragment";

	private final int HTTP_GET_OK = 9;
	private RoundAngleImageView avatar;
	private TextView nick;
	private LinearLayout home;
	private LinearLayout at;
	private LinearLayout favorite;
	private LinearLayout chat;
	private LinearLayout channel;
	private LinearLayout weiba;
	private LinearLayout setting;
	private LinearLayout logout;
	private ThinkSNSApplication application;
	private AccountBean account;
	private UserInfoBean userinfo;
	private String json;
	private NotificationBean comment_unread, at_unread;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HTTP_GET_OK:
				Log.d(TAG, "handler is 9");
				if (json != null && !"".equals(json)) {
					Gson gson = new Gson();
					userinfo = gson.fromJson(json, UserInfoBean.class);
					ImageLoader.getInstance().displayImage(
							userinfo.getAvatar(), avatar);
					nick.setText(userinfo.getUname());
					mHandler.sendEmptyMessage(0);
				}
				break;

			}

		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.d(TAG, "menuFramgnet onActivityCreated");

		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG, "menuFramgnet onCreateView");
		final ScrollView view = (ScrollView) inflater.inflate(
				R.layout.slidingmenu_behind, container, false);
		avatar = (RoundAngleImageView) view.findViewById(R.id.sm_behind_avatar);
		nick = (TextView) view.findViewById(R.id.sm_behind_nick);
		home = (LinearLayout) view.findViewById(R.id.sm_home);
		at = (LinearLayout) view.findViewById(R.id.sm_at);
		favorite = (LinearLayout) view.findViewById(R.id.sm_favorite);
		chat = (LinearLayout) view.findViewById(R.id.sm_chat);
		channel = (LinearLayout) view.findViewById(R.id.sm_channel);
		weiba = (LinearLayout) view.findViewById(R.id.sm_weiba);
		setting = (LinearLayout) view.findViewById(R.id.sm_setting);
		logout = (LinearLayout) view.findViewById(R.id.sm_logout);

		application = (ThinkSNSApplication) this.getActivity()
				.getApplicationContext();
		account = application.getAccount(this.getActivity());

		new Thread() {
			@Override
			public void run() {
				Log.d(TAG, "run");
				HashMap<String, String> map = new HashMap<String, String>();
				map = new HashMap<String, String>();
				map.put("app", "api");
				map.put("mod", "User");
				map.put("act", "show");
				map.put("user_id", account.getUid());
				map.put("oauth_token", account.getOauth_token());
				map.put("oauth_token_secret", account.getOauth_token_secret());
				json = HttpUtility.getInstance().executeNormalTask(
						HttpMethod.Get, HttpConstant.THINKSNS_URL, map);
				mHandler.sendEmptyMessage(HTTP_GET_OK);

			}
		}.start();

		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		Log.d(TAG, "menuFramgnet onViewCreated");
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

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.sm_home:
			changeBackground(R.id.sm_home);
			WeiboMainFragment weiboMainFragment = new WeiboMainFragment();
			if (weiboMainFragment != null)
				switchFragment(weiboMainFragment);
			break;
		case R.id.sm_at:
			changeBackground(R.id.sm_at);
			AtAndCommentFragment atAndCommentFragment = new AtAndCommentFragment();
			if (atAndCommentFragment != null) {
				if (comment_unread != null || at_unread != null) {
					Bundle args = new Bundle();
					args.putParcelable("comment_unread", comment_unread);
					Log.d("wj", "comment_unread.getCount()"
							+ comment_unread.getCount());
					args.putParcelable("at_unread", at_unread);
					Log.d("wj", "at_unread.getCount()"
							+ at_unread.getCount());
					atAndCommentFragment.setArguments(args);
					comment_unread = null;
					at_unread = null;
				}
				switchFragment(atAndCommentFragment);
			}
			break;
		case R.id.sm_favorite:
			changeBackground(R.id.sm_favorite);
			CollectionFragment collectionFragment = new CollectionFragment();
			if (collectionFragment != null)
				switchFragment(collectionFragment);
			break;
		case R.id.sm_chat:
			changeBackground(R.id.sm_chat);
			ChatFragment chatFragment = new ChatFragment();
			if (chatFragment != null)
				switchFragment(chatFragment);
			break;
		case R.id.sm_channel:
			ChannelFragment chanelFragment = new ChannelFragment();
			if (chanelFragment != null)
				switchFragment(chanelFragment);
			changeBackground(R.id.sm_channel);
			break;
		case R.id.sm_weiba:
			changeBackground(R.id.sm_weiba);
			WeibaFragment weibaFragment = new WeibaFragment();
			if (weibaFragment != null)
				switchFragment(weibaFragment);
			break;
		case R.id.sm_setting:
			changeBackground(R.id.sm_setting);
			SettingFragment settingFragment = new SettingFragment();
			if (settingFragment != null)
				switchFragment(settingFragment);
			break;
		case R.id.sm_logout:
			changeBackground(R.id.sm_logout);
			new AlertDialog.Builder(getActivity()).setTitle(
					R.string.quit_account).setMessage(
					R.string.quit_account_explanation).setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							application.quitAccount(getActivity());
							getActivity().finish();
						}
					}).setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
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

	public void setUnread(NotificationBean comment_unread,
			NotificationBean at_unread) {
		this.comment_unread = comment_unread;
		this.at_unread = at_unread;
	}
}
