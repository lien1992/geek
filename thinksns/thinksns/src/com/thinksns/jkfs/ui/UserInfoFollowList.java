package com.thinksns.jkfs.ui;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.BaseActivity;
import com.thinksns.jkfs.base.ThinkSNSApplication;
import com.thinksns.jkfs.bean.AccountBean;
import com.thinksns.jkfs.bean.UserFollowBean;
import com.thinksns.jkfs.bean.UserInfoBean;
import com.thinksns.jkfs.constant.HttpConstant;
import com.thinksns.jkfs.ui.adapter.PeopleListAdapter;
import com.thinksns.jkfs.util.Utility;
import com.thinksns.jkfs.util.http.HttpMethod;
import com.thinksns.jkfs.util.http.HttpUtility;

/**
 * @author 邓思宇 用于在用户界面显示关注对象的LIST
 * 
 *         还未添加TAG
 * 
 */

public class UserInfoFollowList extends BaseActivity implements
		OnScrollListener {

	private ListView listView;
	private int visibleLastIndex = 0; // 最后的可视项索引
	private int visibleItemCount; // 当前窗口可见项总数
	private PeopleListAdapter adapter;
	private int ac = -1;// 判断当前打开的是那个ADAPTER
	private View loadMoreView;
	private Button loadMoreButton;
	private Handler handler = new Handler();
	private int currentPage;
	private ThinkSNSApplication application;
	private LinkedList<UserFollowBean> userfollows = new LinkedList<UserFollowBean>();// 提取全部的userfollowbean信息
	private LinkedList<UserFollowBean> userfollows2 = new LinkedList<UserFollowBean>();// 分页查看时候提取信息
	private AccountBean account;
	private int FLAG = 0;// 判断是打开自己的主页还是别人的主页 0为自己的
	private int FLAGG = 0;// 判断是打开关注人列表还是粉丝列表 0为关注人的
	private String uuid;

	public UserInfoFollowList() {
		super();
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				adapter = new PeopleListAdapter(UserInfoFollowList.this,
						userfollows, account);
				listView.setAdapter(adapter);
				currentPage = currentPage + 1;
				break;
			case 2:
				loadNew();
				currentPage = currentPage + 1;
				adapter.notifyDataSetChanged(); // 数据集变化后,通知adapter
				break;
			case 3:
				Toast toast = Toast.makeText(UserInfoFollowList.this, "网络未连接",
						Toast.LENGTH_SHORT);
				toast.show();
				break;
			}
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_list);
		currentPage = 1;
		Bundle extras = getIntent().getExtras();
		String flag = extras.getString("FLAG");
		String flagg = extras.getString("FLAGG");
		String uuuid = extras.getString("uuid");
		FLAG = Integer.parseInt(flag);
		FLAGG = Integer.parseInt(flagg);
		uuid = uuuid;

		application = (ThinkSNSApplication) this.getApplicationContext();
		account = application.getAccount(this);

		loadMoreView = getLayoutInflater().inflate(R.layout.userinfo_loadmore,
				null);
		loadMoreButton = (Button) loadMoreView
				.findViewById(R.id.loadMoreButton);

		listView = (ListView) findViewById(R.id.userinfo_list); // 获取id是list的ListView
		listView.addFooterView(loadMoreView); // 设置列表底部视图

		Button listhead = (Button) findViewById(R.id.list_head);
		if (FLAGG == 0) {
			listhead.setText("关注列表");
		} else if (FLAGG == 1) {
			listhead.setText("粉丝列表");
		}

		initAdapter();

		listView.setOnScrollListener(this); // 添加滑动监听

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				UserFollowBean userfollow = adapter.getUser(arg2);
				String fo = "" + userfollow.follow_state.getFollowing();
				Intent i = new Intent(UserInfoFollowList.this,
						OtherInfoActivity.class);
				i.putExtra("following", fo);
				i.putExtra("userinfo", userfollow);
				startActivity(i);

			}

		});

	}

	/**
	 * 初始化适配器
	 */
	private void initAdapter() {

		switch (FLAG) {
		case 0:

			if (FLAGG == 0) {
				if (Utility.isConnected(this)) {

					new Thread() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Gson gson = new Gson();
							HashMap<String, String> map = new HashMap<String, String>();
							map.put("app", "api");
							map.put("mod", "User");
							map.put("act", "user_following");
							map.put("oauth_token", account.getOauth_token());
							map.put("oauth_token_secret",
									account.getOauth_token_secret());
							String json = HttpUtility.getInstance()
									.executeNormalTask(HttpMethod.Get,
											HttpConstant.THINKSNS_URL, map);
							Type listType = new TypeToken<LinkedList<UserFollowBean>>() {
							}.getType();
							userfollows = gson.fromJson(json, listType);
							ac = ac + 1;
							mHandler.sendEmptyMessage(1);

						}
					}.start();
				} else {
					mHandler.sendEmptyMessage(3);
				}
			} else if (FLAGG == 1) {
				if (Utility.isConnected(this)) {

					new Thread() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Gson gson = new Gson();
							HashMap<String, String> map = new HashMap<String, String>();
							map.put("app", "api");
							map.put("mod", "User");
							map.put("act", "user_followers");
							map.put("oauth_token", account.getOauth_token());
							map.put("oauth_token_secret",
									account.getOauth_token_secret());
							String json = HttpUtility.getInstance()
									.executeNormalTask(HttpMethod.Get,
											HttpConstant.THINKSNS_URL, map);
							Type listType = new TypeToken<LinkedList<UserFollowBean>>() {
							}.getType();
							userfollows = gson.fromJson(json, listType);
							ac = ac + 1;
							mHandler.sendEmptyMessage(1);

						}
					}.start();
				} else {
					mHandler.sendEmptyMessage(3);
				}

			}
			break;
		case 1:

			if (FLAGG == 0) {
				if (Utility.isConnected(this)) {

					new Thread() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Gson gson = new Gson();
							HashMap<String, String> map = new HashMap<String, String>();
							map.put("app", "api");
							map.put("mod", "User");
							map.put("act", "user_following");
							map.put("user_id", uuid);
							map.put("oauth_token", account.getOauth_token());
							map.put("oauth_token_secret",
									account.getOauth_token_secret());
							String json = HttpUtility.getInstance()
									.executeNormalTask(HttpMethod.Get,
											HttpConstant.THINKSNS_URL, map);
							Type listType = new TypeToken<LinkedList<UserFollowBean>>() {
							}.getType();
							userfollows = gson.fromJson(json, listType);
							ac = ac + 1;
							mHandler.sendEmptyMessage(1);

						}
					}.start();
				} else {
					mHandler.sendEmptyMessage(3);
				}

			} else if (FLAGG == 1) {
				if (Utility.isConnected(this)) {

					new Thread() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Gson gson = new Gson();
							HashMap<String, String> map = new HashMap<String, String>();
							map.put("app", "api");
							map.put("mod", "User");
							map.put("act", "user_followers");
							map.put("user_id", uuid);
							map.put("oauth_token", account.getOauth_token());
							map.put("oauth_token_secret",
									account.getOauth_token_secret());
							String json = HttpUtility.getInstance()
									.executeNormalTask(HttpMethod.Get,
											HttpConstant.THINKSNS_URL, map);
							Type listType = new TypeToken<LinkedList<UserFollowBean>>() {
							}.getType();
							userfollows = gson.fromJson(json, listType);
							ac = ac + 1;
							mHandler.sendEmptyMessage(1);

						}
					}.start();
				} else {
					mHandler.sendEmptyMessage(3);
				}

			}
			break;

		}

	}

	/**
	 * 滑动时被调用
	 */
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
	}

	/**
	 * 滑动状态改变时被调用
	 */
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	/**
	 * 点击按钮事件
	 * 
	 * @param view
	 */
	public void loadMore(View view) {
		loadMoreButton.setText("loading..."); // 设置按钮文字loading
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {

				loadData();

				// adapter.notifyDataSetChanged(); // 数据集变化后,通知adapter
				listView.setSelection(visibleLastIndex - visibleItemCount + 1); // 设置选中项
				loadMoreButton.setText("load more"); // 恢复按钮文字
			}
		}, 2000);
	}

	/**
	 * 模拟加载数据
	 */
	private void loadData() {

		addMoreUserfollow();// 加载新的一页的userfollow

	}

	private void addMoreUserfollow() {

		switch (FLAG) {
		case 0:

			if (FLAGG == 0) {
				if (Utility.isConnected(this)) {

					new Thread() {
						@Override
						public void run() {

							userfollows2.clear();
							// TODO Auto-generated method stub
							Gson gson = new Gson();
							HashMap<String, String> map = new HashMap<String, String>();
							map.put("app", "api");
							map.put("mod", "User");
							map.put("page", currentPage + "");
							map.put("act", "user_following");
							map.put("oauth_token", account.getOauth_token());
							map.put("oauth_token_secret",
									account.getOauth_token_secret());
							String json = HttpUtility.getInstance()
									.executeNormalTask(HttpMethod.Get,
											HttpConstant.THINKSNS_URL, map);
							Type listType = new TypeToken<LinkedList<UserFollowBean>>() {
							}.getType();

							userfollows2 = gson.fromJson(json, listType);

							mHandler.sendEmptyMessage(2);

						}
					}.start();
				} else {
					mHandler.sendEmptyMessage(3);
				}
			} else if (FLAGG == 1) {
				if (Utility.isConnected(this)) {

					new Thread() {
						@Override
						public void run() {

							userfollows2.clear();
							// TODO Auto-generated method stub
							Gson gson = new Gson();
							HashMap<String, String> map = new HashMap<String, String>();
							map.put("app", "api");
							map.put("mod", "User");
							map.put("page", currentPage + "");
							map.put("act", "user_followers");
							map.put("oauth_token", account.getOauth_token());
							map.put("oauth_token_secret",
									account.getOauth_token_secret());
							String json = HttpUtility.getInstance()
									.executeNormalTask(HttpMethod.Get,
											HttpConstant.THINKSNS_URL, map);
							Type listType = new TypeToken<LinkedList<UserFollowBean>>() {
							}.getType();

							userfollows2 = gson.fromJson(json, listType);
							mHandler.sendEmptyMessage(2);

						}
					}.start();
				} else {
					mHandler.sendEmptyMessage(3);
				}

			}
			break;
		case 1:

			if (FLAGG == 0) {
				if (Utility.isConnected(this)) {

					new Thread() {
						@Override
						public void run() {

							userfollows2.clear();
							// TODO Auto-generated method stub
							Gson gson = new Gson();
							HashMap<String, String> map = new HashMap<String, String>();
							map.put("app", "api");
							map.put("mod", "User");
							map.put("page", currentPage + "");
							map.put("act", "user_following");
							map.put("user_id", uuid);
							map.put("oauth_token", account.getOauth_token());
							map.put("oauth_token_secret",
									account.getOauth_token_secret());
							String json = HttpUtility.getInstance()
									.executeNormalTask(HttpMethod.Get,
											HttpConstant.THINKSNS_URL, map);
							Type listType = new TypeToken<LinkedList<UserFollowBean>>() {
							}.getType();

							userfollows2 = gson.fromJson(json, listType);
							mHandler.sendEmptyMessage(2);

						}
					}.start();
				} else {
					mHandler.sendEmptyMessage(3);
				}

			} else if (FLAGG == 1) {

				if (Utility.isConnected(this)) {

					new Thread() {
						@Override
						public void run() {

							userfollows2.clear();
							// TODO Auto-generated method stub
							Gson gson = new Gson();
							HashMap<String, String> map = new HashMap<String, String>();
							map.put("app", "api");
							map.put("mod", "User");
							map.put("page", currentPage + "");
							map.put("act", "user_followers");
							map.put("user_id", uuid);
							map.put("oauth_token", account.getOauth_token());
							map.put("oauth_token_secret",
									account.getOauth_token_secret());
							String json = HttpUtility.getInstance()
									.executeNormalTask(HttpMethod.Get,
											HttpConstant.THINKSNS_URL, map);
							Type listType = new TypeToken<LinkedList<UserFollowBean>>() {
							}.getType();

							userfollows2 = gson.fromJson(json, listType);
							mHandler.sendEmptyMessage(2);

						}
					}.start();
				} else {
					mHandler.sendEmptyMessage(3);
				}

			}
			break;

		}

	}

	private void loadNew() {
		int nicount = userfollows2.size();// 测试判断剩下的数量
		if (nicount == 20) {

			for (int i = 0; i < 20; i++) {
				adapter.addItem(userfollows2.get(i));
			}

			Toast toast = Toast.makeText(this, "已加载" + nicount + "个人",
					Toast.LENGTH_SHORT);
			toast.show();

		} else if (nicount < 20 && nicount > 0) {

			for (int i = 0; i < nicount; i++) {
				adapter.addItem(userfollows2.get(i));
			}

			Toast toast = Toast.makeText(this, "已加载" + nicount + "个人",
					Toast.LENGTH_SHORT);
			toast.show();

		} else {
			Toast toast = Toast.makeText(this, "已没有更多的人", Toast.LENGTH_SHORT);
			toast.show();
		}

	}

}
