package com.thinksns.jkfs.ui;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.BaseActivity;
import com.thinksns.jkfs.base.ThinkSNSApplication;
import com.thinksns.jkfs.bean.UserFollowBean;
import com.thinksns.jkfs.bean.WeiboBean;
import com.thinksns.jkfs.constant.HttpConstant;
import com.thinksns.jkfs.ui.adapter.PeopleListAdapter;
import com.thinksns.jkfs.ui.adapter.WeiboAdapter;
import com.thinksns.jkfs.ui.fragment.ChannelFragment;
import com.thinksns.jkfs.ui.view.PullToRefreshListView;
import com.thinksns.jkfs.ui.view.PullToRefreshListView.RefreshAndLoadMoreListener;
import com.thinksns.jkfs.util.http.HttpMethod;
import com.thinksns.jkfs.util.http.HttpUtility;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class SearchActivity extends BaseActivity {

	public static final String TAG = "zcc";
	public static final int SEARCH_WEIBO = 0;
	public static final int SEARCH_USER = 1;

	// thinksnsAPI用的
	static private final String APP = "api";
	static private final String MOD = "WeiboStatuses";
	static private final String ACT_GET_WEIBO = "weibo_search_weibo";
	static private final String ACT_GET_USER = "weibo_search_user";
	static private String OAUTH_TOKEN;
	static private String OAUTH_TOKEN_SECRECT;

	static private int onLoadMoreFlag = 0; // 1代表加载更多
	static private int onSearchFlag = 0; // 1代表加载更多
	static private int page = 0;// 0是没有使用

	// handler
	public static final int GET_WEIBOS = 0;
	public static final int CONNECT_WRONG = 1;
	public static final int GET_USERS = 2;
	public static final int LOAD_MORE_USER = 3;
	public static final int LOAD_MORE_WEIBO = 4;

	private Activity mContext;
	private ImageView searchImage;
	private ImageView backImg;
	private TextView titleName;
	private View choose_weibo;
	private View choose_user;
	private EditText editText;

	private PullToRefreshListView mListView;
	private LayoutInflater mInflater;
	private String jsonData;
	private Handler handler;
	private LinkedList<WeiboBean> weiboList;
	private WeiboAdapter weiboListViewAdapter;
	private PeopleListAdapter userListViewAdapter;
	private LinkedList<UserFollowBean> userList;

	private int search_witch = SEARCH_WEIBO; // 查找标记
	private String keyWord;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		mContext = this;
		searchImage = (ImageView) findViewById(R.id.search_title_search_img);
		backImg = (ImageView) findViewById(R.id.search_title_back_img);
		choose_weibo = findViewById(R.id.search_choose_weibo);
		choose_user = findViewById(R.id.search_choose_user);
		editText = (EditText) findViewById(R.id.search_title_edit);

		mListView = (PullToRefreshListView) findViewById(R.id.search_listview);
		mInflater = getLayoutInflater();
		weiboList = new LinkedList<WeiboBean>();
		weiboListViewAdapter = new WeiboAdapter(mContext, mInflater, mListView);
		userList = new LinkedList<UserFollowBean>();
		userListViewAdapter = new PeopleListAdapter(mContext, userList,
				ThinkSNSApplication.getInstance().getAccount(mContext));
		// 默认微博搜索
		mListView.setAdapter(weiboListViewAdapter);

		// 少一个用户的adapter

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case GET_WEIBOS:
					if (((LinkedList<WeiboBean>) msg.obj).size() == 0) {
						Toast.makeText(mContext, "找不到相关结果", Toast.LENGTH_SHORT)
								.show();
						page = 0;
					} else {
						weiboList = (LinkedList<WeiboBean>) msg.obj;
						weiboListViewAdapter.update(weiboList);
						Log.i(TAG, "搜索了，奥");
						mListView.setSelection(0);
						mListView.setVisibility(View.VISIBLE);
					}
					onSearchFlag = 0;
					break;
				case CONNECT_WRONG:
					Toast.makeText(mContext, "网络故障", Toast.LENGTH_SHORT).show();
					break;
				case GET_USERS:
					if (((LinkedList<UserFollowBean>) msg.obj).size() == 0) {
						Toast.makeText(mContext, "找不到相关结果", Toast.LENGTH_SHORT)
								.show();
						page = 0;
					} else {
						userList = (LinkedList<UserFollowBean>) msg.obj;
						userListViewAdapter.update(userList);
						Log.i(TAG, "搜索了，奥");
						mListView.setSelection(0);
						mListView.setVisibility(View.VISIBLE);
					}
					onSearchFlag = 0;
					break;
				case LOAD_MORE_USER:
					if (((LinkedList<UserFollowBean>) msg.obj).size() == 0) {
						mListView.onLoadMoreComplete();
						Log.i(TAG, "没更多了");
						Toast.makeText(mContext, "没有更多结果", Toast.LENGTH_SHORT)
								.show();
					} else {
						userList = append(userList,
								(LinkedList<UserFollowBean>) msg.obj);
						userListViewAdapter.update(userList);
						Log.i(TAG, "加载更多了");

					}
					// 加载完成
					onLoadMoreFlag = 0;
					break;
				case LOAD_MORE_WEIBO:
					if (((LinkedList<WeiboBean>) msg.obj).size() == 0) {
						mListView.onLoadMoreComplete();
						Log.i(TAG, "没更多了");
						Toast.makeText(mContext, "没有更多结果", Toast.LENGTH_SHORT)
								.show();
					} else {
						weiboList = append(weiboList,
								(LinkedList<WeiboBean>) msg.obj);
						weiboListViewAdapter.update(weiboList);
						Log.i(TAG, "加载更多了");

					}
					// 加载完成
					onLoadMoreFlag = 0;
					break;
				}
			}
		};

		choose_weibo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (search_witch != SEARCH_WEIBO) {
					Log.i(TAG, "切换了" + search_witch);
					search_witch = SEARCH_WEIBO;
					mListView.setVisibility(View.INVISIBLE);
					weiboListViewAdapter.clear();
					mListView.setAdapter(weiboListViewAdapter);
					changeOnItemClick();
				}

			}
		});

		choose_user.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (search_witch != SEARCH_USER) {
					Log.i(TAG, "切换了" + search_witch);
					search_witch = SEARCH_USER;
					mListView.setVisibility(View.INVISIBLE);
					userListViewAdapter.clear();
					mListView.setAdapter(userListViewAdapter);
					changeOnItemClick();
				}
			}
		});

		searchImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i(TAG, "搜索点击了");
				keyWord = editText.getText().toString();
				Log.i(TAG, "输入框里的东西是" + keyWord);
				while (keyWord.endsWith(" ")) {
					keyWord = keyWord.substring(0, keyWord.length() - 1);
				}
				while (keyWord.startsWith(" ")) {
					keyWord = keyWord.substring(1);
				}
				Log.i(TAG, "整理后的东西是" + keyWord);

				if ("".equals(keyWord)) {
					Toast.makeText(mContext, "关键字为空", Toast.LENGTH_SHORT)
							.show();
					return;
				} else {
					onSearchFlag = 1;
					page = 1;
					switch (search_witch) {
					case SEARCH_WEIBO:
						getWeibosInThread(1, keyWord);
						break;
					case SEARCH_USER:
						getUsersInThread(1, keyWord);
						break;
					}
				}
			}
		});
		backImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mContext.finish();
			}
		});

		init();
	}

	private void changeOnItemClick() {
		if (search_witch == SEARCH_WEIBO) {

			mListView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					Log.i(TAG, "微博点击");
					Intent intent = new Intent(mContext,
							WeiboDetailActivity.class);
					intent.putExtra("weibo_detail", weiboList.get(position - 1));
					startActivity(intent);
				}
			});
		} else {
			mListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					// Log.i(TAG, "yonghu点击");
					// TextView tx = (TextView) arg1
					// .findViewById(R.id.people_item_weibo);// 传送数据 USER
					// // ID
					// TextView ts = (TextView) arg1
					// .findViewById(R.id.people_item_fo);// 传送数据 FOLLOWING
					// String uuid = tx.getText().toString();
					// String fo = ts.getText().toString();
					//
					// Intent i = new Intent(mContext, OtherInfoActivity.class);
					// i.putExtra("uuid", uuid);
					// Log.i(TAG, "__" + uuid + "uuid");
					// i.putExtra("following", fo);
					// startActivity(i);
					UserFollowBean userfollow = userListViewAdapter
							.getUser(arg2);
					String fo = "" + userfollow.follow_state.getFollowing();
					Intent i = new Intent(mContext, OtherInfoActivity.class);
					i.putExtra("following", fo);
					i.putExtra("userinfo", userfollow);
					startActivity(i);
				}

			});
		}
	}

	/**
	 * 初始化
	 */
	private void init() {
		ThinkSNSApplication application = ThinkSNSApplication.getInstance();
		OAUTH_TOKEN = application.getOauth_token(mContext);
		OAUTH_TOKEN_SECRECT = application.getOauth_token_secret(mContext);
		// 下拉列表初始化
		initListView();
		changeOnItemClick();
	}

	/**
	 * 初始化下拉列表
	 */
	private void initListView() {

		mListView.setLoadMoreEnable(true);
		mListView.setListener(new RefreshAndLoadMoreListener() {

			@Override
			public void onRefresh() {
				mListView.onRefreshComplete();
			}

			@Override
			public void onLoadMore() {
				// 开始加载
				onLoadMoreFlag = 1;
				page += 1;
				if (search_witch == SEARCH_WEIBO) {
					getWeibosInThread(page, keyWord);
				} else {
					getUsersInThread(page, keyWord);
				}
			}
		});
	}

	// 获取用户
	private void getUsersInThread(final int page, final String keyWord) {

		new Thread() {
			public void run() {
				final Map<String, String> map = new HashMap<String, String>();
				map.put("app", APP);
				map.put("oauth_token", OAUTH_TOKEN);
				map.put("oauth_token_secret", OAUTH_TOKEN_SECRECT);
				map.put("mod", MOD);
				map.put("act", ACT_GET_USER);
				map.put("count", "20");
				map.put("key", keyWord);

				if (onLoadMoreFlag == 1) {
					map.put("page", page + "");
				}
				Log.i(TAG, "获取用户");
				jsonData = HttpUtility.getInstance().executeNormalTask(
						HttpMethod.Get, HttpConstant.THINKSNS_URL, map);
				if (onSearchFlag == 1) {
					// 将json转化成bean列表，handler出去
					handler.obtainMessage(SearchActivity.GET_USERS,
							JSONToUsers(jsonData)).sendToTarget();
				}
				if (onLoadMoreFlag == 1) {
					handler.obtainMessage(SearchActivity.LOAD_MORE_USER,
							JSONToUsers(jsonData)).sendToTarget();
				}
			};
		}.start();
	}

	private LinkedList<UserFollowBean> JSONToUsers(String jsonData) {

		LinkedList<UserFollowBean> list = new LinkedList<UserFollowBean>();
		try {
			Type listType = new TypeToken<LinkedList<UserFollowBean>>() {
			}.getType();
			list = new Gson().fromJson(jsonData, listType);

			if (list != null && list.size() != 0) {
				Log.i(TAG, list.get(0).getUname());
			}
		} catch (JsonSyntaxException e) {
			Log.i(TAG, "json用户出问题");
			handler.obtainMessage(CONNECT_WRONG).sendToTarget();
		}
		Log.i(TAG, "用户个数" + list.size());
		return list;
	}

	// 获取微博
	private void getWeibosInThread(final int page, final String keyWord) {

		new Thread() {
			public void run() {
				final Map<String, String> map = new HashMap<String, String>();
				map.put("app", APP);
				map.put("oauth_token", OAUTH_TOKEN);
				map.put("oauth_token_secret", OAUTH_TOKEN_SECRECT);
				map.put("mod", MOD);
				map.put("act", ACT_GET_WEIBO);
				map.put("count", "20");
				map.put("key", keyWord);

				if (onLoadMoreFlag == 1) {
					Log.i(TAG, "page" + page);
					map.put("page", page + "");
				}
				Log.i(TAG, "获取微博");
				jsonData = HttpUtility.getInstance().executeNormalTask(
						HttpMethod.Get, HttpConstant.THINKSNS_URL, map);
				if (onSearchFlag == 1) {
					// 将json转化成bean列表，handler出去
					handler.obtainMessage(SearchActivity.GET_WEIBOS,
							JSONToWeibos(jsonData)).sendToTarget();
				}
				if (onLoadMoreFlag == 1) {

					Log.i(TAG, "微博加载更多。。。");
					handler.obtainMessage(SearchActivity.LOAD_MORE_WEIBO,
							JSONToWeibos(jsonData)).sendToTarget();
				}
			};
		}.start();
	}

	private LinkedList<WeiboBean> JSONToWeibos(String jsonData) {

		LinkedList<WeiboBean> list = new LinkedList<WeiboBean>();
		try {
			Type listType = new TypeToken<LinkedList<WeiboBean>>() {
			}.getType();
			list = new Gson().fromJson(jsonData, listType);

			if (list != null && list.size() != 0) {
				Log.i(TAG, list.get(0).getUname());
			}
		} catch (JsonSyntaxException e) {
			Log.i(TAG, "json微博出问题");
			handler.obtainMessage(CONNECT_WRONG).sendToTarget();
		}
		Log.i(TAG, "微博个数" + list.size());
		return list;
	}

	// 加到表头
	public LinkedList append(LinkedList mList, LinkedList list) {
		if (list == null) {
			return mList;
		}
		mList.addAll(list);
		return mList;
	}
}
