package com.thinksns.jkfs.ui.fragment;

import java.util.HashMap;
import com.google.gson.Gson;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.ThinkSNSApplication;
import com.thinksns.jkfs.bean.AccountBean;
import com.thinksns.jkfs.bean.NotificationBean;
import com.thinksns.jkfs.bean.UserInfoBean;
import com.thinksns.jkfs.bean.UserInfoCountBean;
import com.thinksns.jkfs.constant.BaseConstant;
import com.thinksns.jkfs.constant.HttpConstant;
import com.thinksns.jkfs.ui.MainFragmentActivity;
import com.thinksns.jkfs.ui.SettingActivity;
import com.thinksns.jkfs.ui.view.RoundAngleImageView;
import com.thinksns.jkfs.util.Utility;
import com.thinksns.jkfs.util.http.HttpMethod;
import com.thinksns.jkfs.util.http.HttpUtility;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MenuFragment extends Fragment implements OnClickListener {

	public static final String TAG = "MenuFragment";

	private final int HTTP_GET_OK = 9;
	private final int HTTP_CHANGE_HEAD = 10;
	private static RoundAngleImageView avatar;
	private static TextView nick;
	private ImageView at_new;
	private ImageView chat_new;
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
	private static UserInfoBean userinfo;
	private String json;
	private NotificationBean comment_unread, at_unread, pm_unread;
	private static DbUtils db;
	private static DisplayImageOptions options;
	private ChangeHeadReceiver receiver;
	private Bitmap bitmap;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HTTP_GET_OK:

				ImageLoader.getInstance().displayImage(
						userinfo.getAvatar_original(), avatar, options);
				avatar.setImageBitmap(bitmap);
				avatar.invalidate();
				nick.setText(userinfo.getUname());
				try {
					UserInfoCountBean userInfoCount = userinfo.count_info;
					if (userInfoCount != null) {
						userInfoCount.setUser_info(userinfo);
						db.save(userInfoCount);
					}
				} catch (DbException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				application.setUser(userinfo);
				break;
			case HTTP_CHANGE_HEAD:
				avatar.setImageBitmap(bitmap);
				avatar.invalidate();
				try {
					UserInfoCountBean userInfoCount = userinfo.count_info;
					if (userInfoCount != null) {
						userInfoCount.setUser_info(userinfo);
						db.save(userInfoCount);
					}
				} catch (DbException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
		at_new = (ImageView) view.findViewById(R.id.sm_at_unread);
		chat_new = (ImageView) view.findViewById(R.id.sm_chat_unread);
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

		application = (ThinkSNSApplication) this.getActivity()
				.getApplicationContext();
		account = application.getAccount(this.getActivity());

		options = new DisplayImageOptions.Builder().showStubImage(
				R.drawable.ic_launcher).cacheInMemory().cacheOnDisc().build();

		IntentFilter filter = new IntentFilter();
		filter.addAction(BaseConstant.CHANGE_HEAD_BROADCAST);
		receiver = new ChangeHeadReceiver();
		getActivity().registerReceiver(receiver, filter);

		db = DbUtils.create(MenuFragment.this.getActivity());
		db.configDebug(true);
		UserInfoBean uib = null;
		try {
			uib = db.findFirst(Selector.from(UserInfoBean.class).orderBy("id",
					true));
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (uib != null && !Utility.isConnected(getActivity())) {
			ImageLoader.getInstance().displayImage(uib.getAvatar_original(),
					avatar, options);
			nick.setText(uib.getUname());
		} else {
			if (Utility.isConnected(getActivity()))
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
						map.put("oauth_token_secret", account
								.getOauth_token_secret());
						json = HttpUtility.getInstance().executeNormalTask(
								HttpMethod.Get, HttpConstant.THINKSNS_URL, map);
						if (json != null && !"".equals(json)) {
							Gson gson = new Gson();
							userinfo = gson.fromJson(json, UserInfoBean.class);
						}
						bitmap = ImageLoader.getInstance().loadImageSync(
								userinfo.getAvatar_big());
						if (bitmap != null)
							mHandler.sendEmptyMessage(HTTP_GET_OK);

					}
				}.start();
		}

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
					Log.d("wj", "at_unread.getCount()" + at_unread.getCount());
					atAndCommentFragment.setArguments(args);
					comment_unread = null;
					at_unread = null;
				}
				at_new.setVisibility(View.GONE);
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
			chat_new.setVisibility(View.GONE);
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
			/*
			 * SettingFragment settingFragment = new SettingFragment(); if
			 * (settingFragment != null) switchFragment(settingFragment);
			 */
			startActivity(new Intent(getActivity(), SettingActivity.class));
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
							try {
								db.dropTable(UserInfoBean.class);
							} catch (DbException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
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

	// 设置未读评论和at消息提醒
	public void setUnread(NotificationBean comment_unread,
			NotificationBean at_unread, NotificationBean pm_unread) {
		this.comment_unread = comment_unread;
		this.at_unread = at_unread;
		this.pm_unread = pm_unread;
		if (comment_unread.getCount() > 0 || at_unread.getCount() > 0) {
			at_new.setVisibility(View.VISIBLE);
		}
		if (pm_unread.getCount() > 0)
			chat_new.setVisibility(View.VISIBLE);
	}

	// 设置未读聊天消息提醒
	public void setUnreadChat() {
		chat_new.setVisibility(View.VISIBLE);
	}

	public void setUserInfo(UserInfoBean user) {
		userinfo = user;
		Log.d("wj", "url after:" + userinfo.getAvatar_big());
		new Thread() {
			@Override
			public void run() {
				bitmap = ImageLoader.getInstance().loadImageSync(
						userinfo.getAvatar_big());
				if (bitmap != null)
					mHandler.sendEmptyMessage(HTTP_CHANGE_HEAD);
			}
		}.start();

	}

	class ChangeHeadReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (Utility.isConnected(getActivity()))
				new Thread() {
					@Override
					public void run() {
						HashMap<String, String> map = new HashMap<String, String>();
						map = new HashMap<String, String>();
						map.put("app", "api");
						map.put("mod", "User");
						map.put("act", "show");
						map.put("user_id", account.getUid());
						map.put("oauth_token", account.getOauth_token());
						map.put("oauth_token_secret", account
								.getOauth_token_secret());
						json = HttpUtility.getInstance().executeNormalTask(
								HttpMethod.Get, HttpConstant.THINKSNS_URL, map);
						mHandler.sendEmptyMessage(HTTP_GET_OK);

					}
				}.start();
		}
	}

}
