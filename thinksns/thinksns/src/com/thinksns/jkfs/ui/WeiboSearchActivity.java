package com.thinksns.jkfs.ui;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.BaseActivity;
import com.thinksns.jkfs.base.ThinkSNSApplication;
import com.thinksns.jkfs.bean.WeiboBean;
import com.thinksns.jkfs.constant.HttpConstant;
import com.thinksns.jkfs.ui.adapter.ListViewAdapter;
import com.thinksns.jkfs.ui.adapter.PeopleAdapter;
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

public class WeiboSearchActivity extends BaseActivity {

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
	// handler
	public static final int GET_WEIBOS = 0;
	public static final int CONNECT_WRONG = 1;

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
	private ArrayList<WeiboBean> weiboList;
	private WeiboAdapter weiboListViewAdapter;
	private PeopleAdapter userListViewAdapter;

	private int search_witch = SEARCH_WEIBO; // 查找标记
	private String weibo_max_id;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weibo_search);
		mContext = this;
		searchImage = (ImageView) findViewById(R.id.search_weibo_title_search_img);
		backImg = (ImageView) findViewById(R.id.search_weibo_title_back_img);
		choose_weibo = findViewById(R.id.search_weibo_choose_weibo);
		choose_user = findViewById(R.id.search_weibo_choose_user);
		editText = (EditText) findViewById(R.id.search_weibo_title_edit);

		mListView = (PullToRefreshListView) findViewById(R.id.search_weibo_listview);
		mInflater = getLayoutInflater();
		weiboListViewAdapter = new WeiboAdapter(mContext, mInflater, mListView);
		// 默认微博搜索
		mListView.setAdapter(weiboListViewAdapter);

		// 少一个用户的adapter

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case GET_WEIBOS:
					weiboList = (ArrayList<WeiboBean>) msg.obj;
					if (weiboList.size() == 0) {
						Toast.makeText(mContext, "找不到相关结果", Toast.LENGTH_SHORT)
								.show();
					} else {
						weiboListViewAdapter.update(weiboList);
						Log.i(TAG, "搜索了，奥");
						mListView.setSelection(0);
						mListView.setVisibility(View.VISIBLE);

					}
					break;
				case CONNECT_WRONG:
					Toast.makeText(mContext, "网络故障", Toast.LENGTH_SHORT).show();
					break;
				}
			}
		};

		choose_weibo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (search_witch != SEARCH_WEIBO) {
					search_witch = SEARCH_WEIBO;
					mListView.setVisibility(View.INVISIBLE);
					weiboListViewAdapter.clear();
					mListView.setAdapter(weiboListViewAdapter);
				}
			}
		});

		choose_user.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (search_witch != SEARCH_USER) {
					search_witch = SEARCH_USER;
					mListView.setVisibility(View.INVISIBLE);

					// weiboListViewAdapter.clear();
					// mListView.setAdapter(weiboListViewAdapter);
				}
			}
		});

		searchImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i(TAG, "搜索点击了");
				String keyWord = editText.getText().toString();
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
					switch (search_witch) {
					case SEARCH_WEIBO:
						getWeibosInThread("", keyWord);
						break;
					case SEARCH_USER:
						break;
					}
				}
			}
		});

		init();
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
	}

	/**
	 * 初始化下拉列表
	 */
	private void initListView() {
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, WeiboDetailActivity.class);
				intent.putExtra("weibo_detail", weiboList.get(position - 1));
				startActivity(intent);
			}
		});
		mListView.setLoadMoreEnable(true);
		mListView.setListener(new RefreshAndLoadMoreListener() {

			@Override
			public void onRefresh() {
				mListView.onRefreshComplete();
			}

			@Override
			public void onLoadMore() {
				// 加载更多还没写

			}
		});
	}

	// 获取用户
	private void getUsersInThread(final String max_id, final String keyWord) {

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

				if (!"".equals(max_id)) {
					map.put("max_id", max_id);
				}
				Log.i(TAG, "获取微博");
				jsonData = HttpUtility.getInstance().executeNormalTask(
						HttpMethod.Get, HttpConstant.THINKSNS_URL, map);

				// 将json转化成bean列表，handler出去
				handler.obtainMessage(WeiboSearchActivity.GET_WEIBOS,
						JSONToWeibos(jsonData)).sendToTarget();

			};
		}.start();
	}

	private ArrayList<WeiboBean> JSONToUsers(String jsonData) {

		ArrayList<WeiboBean> list = new ArrayList<WeiboBean>();
		try {
			Type listType = new TypeToken<ArrayList<WeiboBean>>() {
			}.getType();
			list = new Gson().fromJson(jsonData, listType);

			Log.i(TAG, "微博个数" + list.size());
			if (list.size() != 0) {
				Log.i(TAG, list.get(0).getUname());
				weibo_max_id = list.get(list.size() - 1).getId();
			}
		} catch (JsonSyntaxException e) {
			Log.i(TAG, "json微博出问题");
			handler.obtainMessage(CONNECT_WRONG).sendToTarget();
		}
		Log.i(TAG, "微博个数" + list.size());
		return list;
	}

	// 获取微博
	private void getWeibosInThread(final String max_id, final String keyWord) {

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

				if (!"".equals(max_id)) {
					map.put("max_id", max_id);
				}
				Log.i(TAG, "获取微博");
				jsonData = HttpUtility.getInstance().executeNormalTask(
						HttpMethod.Get, HttpConstant.THINKSNS_URL, map);

				// 将json转化成bean列表，handler出去
				handler.obtainMessage(WeiboSearchActivity.GET_WEIBOS,
						JSONToWeibos(jsonData)).sendToTarget();

			};
		}.start();
	}

	private ArrayList<WeiboBean> JSONToWeibos(String jsonData) {

		ArrayList<WeiboBean> list = new ArrayList<WeiboBean>();
		try {
			Type listType = new TypeToken<ArrayList<WeiboBean>>() {
			}.getType();
			list = new Gson().fromJson(jsonData, listType);

			Log.i(TAG, "微博个数" + list.size());
			if (list.size() != 0) {
				Log.i(TAG, list.get(0).getUname());
				weibo_max_id = list.get(list.size() - 1).getId();
			}
		} catch (JsonSyntaxException e) {
			Log.i(TAG, "json微博出问题");
			handler.obtainMessage(CONNECT_WRONG).sendToTarget();
		}
		Log.i(TAG, "微博个数" + list.size());
		return list;
	}
}
