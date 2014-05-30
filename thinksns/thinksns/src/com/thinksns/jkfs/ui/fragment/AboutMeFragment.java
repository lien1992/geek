package com.thinksns.jkfs.ui.fragment;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.ThinkSNSApplication;
import com.thinksns.jkfs.bean.AccountBean;
import com.thinksns.jkfs.bean.UserInfoBean;
import com.thinksns.jkfs.constant.HttpConstant;
import com.thinksns.jkfs.ui.ChangeUserInfo;
import com.thinksns.jkfs.ui.UserInfoFanList;
import com.thinksns.jkfs.ui.UserInfoFollowList;
import com.thinksns.jkfs.ui.UserInfoWeiboList;
import com.thinksns.jkfs.util.http.HttpMethod;
import com.thinksns.jkfs.util.http.HttpUtility;

/**
 * @author 邓思宇 我的主页列表显示信息 还未完成
 * 
 */
@SuppressLint({ "HandlerLeak", "ValidFragment" })
public class AboutMeFragment extends Fragment {

	public static final String TAG = "AboutMeFragment";
	// get the account
	private ThinkSNSApplication application;
	private AccountBean account;
	private UserInfoBean userinfo;
	private int FLAG = 0;// 判断是打开的主页还是其他人的页面 其他人页面设为1
	private String uuid;// 如果FLAG为1 会收到其他人的UID 存入此

	public AboutMeFragment() {
		super();
	}

	public AboutMeFragment(String i, String uid){
		this.FLAG = Integer.parseInt(i);
		this.uuid =uid;
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:

				ImageView head = (ImageView) getActivity().findViewById(
						R.id.m_head);
				TextView uname = (TextView) getActivity().findViewById(
						R.id.m_username);
				TextView address = (TextView) getActivity().findViewById(
						R.id.m_address);
				ImageView sex = (ImageView) getActivity().findViewById(
						R.id.m_sex);

				Button msex = (Button) getActivity().findViewById(R.id.m_sex1);
				Button mweibo = (Button) getActivity().findViewById(
						R.id.m_weibo);
				Button mfollow = (Button) getActivity().findViewById(
						R.id.m_follow);
				Button mfollowme = (Button) getActivity().findViewById(
						R.id.m_followme);
				Button muid = (Button) getActivity().findViewById(R.id.m_uid);
				Button muname = (Button) getActivity().findViewById(
						R.id.m_uname1);
				Button memail = (Button) getActivity().findViewById(
						R.id.m_email);
				Button maddress = (Button) getActivity().findViewById(
						R.id.m_add);

				uname.setText(userinfo.getUname());
				address.setText(userinfo.getLocation());
				msex.setText(userinfo.getSex());
				mweibo.setText(userinfo.count_info.getWeibo_count());
				mfollow.setText(userinfo.count_info.getFollowing_count());
				mfollowme.setText(userinfo.count_info.getFollower_count());
				muid.setText(userinfo.getUid());
				muname.setText(userinfo.getUname());
				memail.setText(userinfo.getEmail());

				String us = userinfo.getLocation();
				maddress.setText(us);

				ImageLoader.getInstance().displayImage(userinfo.getAvatar(),
						head);

				if (userinfo.getSex() == "男") {
					sex.setBackgroundResource(R.drawable.male);
				} else {
					sex.setBackgroundResource(R.drawable.female);
				}

				break;

			}

		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		return inflater.inflate(R.layout.aboutme_fragment, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {

		super.onViewCreated(view, savedInstanceState);

		application = (ThinkSNSApplication) this.getActivity()
				.getApplicationContext();
		account = application.getAccount(this.getActivity());

		openPage();

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		

//		String flag = getArguments().getString("FLAG"); 
//		String uuuid = getArguments().getString("uuid"); 
//	
//		FLAG = Integer.parseInt(flag);
//		uuid = uuuid;
		
		
		// 显示修改信息活动
		Button button = (Button) getActivity().findViewById(R.id.changeinfo);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// get the change info activity
				Intent intent = new Intent(getActivity(), ChangeUserInfo.class);
				startActivity(intent);

			}
		});

		// 显示微博LIST
		Button button2 = (Button) getActivity().findViewById(R.id.m_weibo);
		button2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// get the change info activity
				Intent intent = new Intent(getActivity(),
						UserInfoWeiboList.class);
				startActivity(intent);

			}
		});

		// 显示关注LIST
		Button button3 = (Button) getActivity().findViewById(R.id.m_follow);
		button3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// get the change info activity
				switch (FLAG) {
				case 0:

					Intent intent = new Intent(getActivity(),
							UserInfoFollowList.class);
					intent.putExtra("FLAG", "0");
					intent.putExtra("FLAGG", "0");
					intent.putExtra("uuid", "");
					startActivity(intent);

					break;
				case 1:

					// 前面的1表示打开的是其他人的页面 后面的0表示的是打开的是关注人页面

					Intent intent2 = new Intent(getActivity(),
							UserInfoFollowList.class);
					intent2.putExtra("FLAG", "1");
					intent2.putExtra("FLAGG", "0");
					intent2.putExtra("uuid", "");
					startActivity(intent2);
					break;
				}

			}
		});

		// 显示粉丝LIST
		Button button4 = (Button) getActivity().findViewById(R.id.m_followme);
		button4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (FLAG) {
				case 0:

					Intent intent = new Intent(getActivity(),
							UserInfoFollowList.class);
					intent.putExtra("FLAG", "0");
					intent.putExtra("FLAGG", "1");
					intent.putExtra("uuid", "");
					startActivity(intent);

					break;
				case 1:

					// 前面的1表示打开的是其他人的页面 后面的1表示的是打开的是粉丝人页面

					Intent intent2 = new Intent(getActivity(),
							UserInfoFollowList.class);
					intent2.putExtra("FLAG", "1");
					intent2.putExtra("FLAGG", "1");
					intent2.putExtra("uuid", "");
					startActivity(intent2);
					break;
				}
			}
		});
	}

	private void openPage() {
		switch (FLAG) {
		case 0:

			new Thread() {
				@Override
				public void run() {
					Gson gson = new Gson();
					HashMap<String, String> map = new HashMap<String, String>();
					map = new HashMap<String, String>();
					map.put("app", "api");
					map.put("mod", "User");
					map.put("act", "show");
					map.put("user_id", account.getUid());
					map.put("oauth_token", account.getOauth_token());
					map.put("oauth_token_secret",
							account.getOauth_token_secret());
					String json = HttpUtility.getInstance().executeNormalTask(
							HttpMethod.Get, HttpConstant.THINKSNS_URL, map);

					if (json != null && !"".equals(json)) {

						userinfo = gson.fromJson(json, UserInfoBean.class);
						mHandler.sendEmptyMessage(0);
					}

				}
			}.start();

			break;
		case 1:

			new Thread() {
				@Override
				public void run() {
					Gson gson = new Gson();
					HashMap<String, String> map = new HashMap<String, String>();
					map = new HashMap<String, String>();
					map.put("app", "api");
					map.put("mod", "User");
					map.put("act", "show");
					map.put("user_id", uuid);
					map.put("oauth_token", account.getOauth_token());
					map.put("oauth_token_secret",
							account.getOauth_token_secret());
					String json = HttpUtility.getInstance().executeNormalTask(
							HttpMethod.Get, HttpConstant.THINKSNS_URL, map);

					if (json != null && !"".equals(json)) {

						userinfo = gson.fromJson(json, UserInfoBean.class);
						mHandler.sendEmptyMessage(0);
					}

				}
			}.start();

			break;
		}
	}

}
