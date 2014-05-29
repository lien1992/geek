package com.thinksns.jkfs.ui;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.BaseActivity;
import com.thinksns.jkfs.base.ThinkSNSApplication;
import com.thinksns.jkfs.bean.AccountBean;
import com.thinksns.jkfs.bean.UserFollowBean;
import com.thinksns.jkfs.constant.HttpConstant;
import com.thinksns.jkfs.ui.adapter.PeopleListAdapter;
import com.thinksns.jkfs.util.http.HttpMethod;
import com.thinksns.jkfs.util.http.HttpUtility;

/**
 * @author 邓思宇 用于在用户界面显示关注对象的LIST
 */

public class UserInfoFollowList extends BaseActivity implements
		OnScrollListener {
	private ListView listView;
	private int visibleLastIndex = 0; // 最后的可视项索引
	private int visibleItemCount; // 当前窗口可见项总数
	private PeopleListAdapter adapter;
	private View loadMoreView;
	private Button loadMoreButton;
	private Handler handler = new Handler();

	private ThinkSNSApplication application;
	private LinkedList<UserFollowBean> userfollows = new LinkedList<UserFollowBean>();
	private AccountBean account;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:

                adapter = new PeopleListAdapter(UserInfoFollowList.this,
                        userfollows);
                // 自动为id是list的ListView设置适配器
                listView.setAdapter(adapter);
              
				adapter.notifyDataSetChanged();

				break;

			}

		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_list);

		application = (ThinkSNSApplication) this.getApplicationContext();
		account = application.getAccount(this);

		loadMoreView = getLayoutInflater().inflate(R.layout.userinfo_loadmore,
				null);
		loadMoreButton = (Button) loadMoreView
				.findViewById(R.id.loadMoreButton);

		listView =(ListView)findViewById(R.id.userinfo_list); // 获取id是list的ListView
		listView.addFooterView(loadMoreView); // 设置列表底部视图

		initAdapter();

		
		listView.setOnScrollListener(this); // 添加滑动监听
	}

	/**
	 * 初始化适配器
	 */
	private void initAdapter() {

		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Gson gson = new Gson();
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("app", "api");
				map.put("mod", "User");
				map.put("act", "user_follow");
				map.put("oauth_token", account.getOauth_token());
				map.put("oauth_token_secret", account.getOauth_token_secret());
				String json = HttpUtility.getInstance().executeNormalTask(
						HttpMethod.Get, HttpConstant.THINKSNS_URL, map);
				Type listType = new TypeToken<LinkedList<UserFollowBean>>() {
				}.getType();
				userfollows = gson.fromJson(json, listType);
				mHandler.sendEmptyMessage(0);
				// adapter = new ListViewAdapter(UserInfoFollowList.this,
				// userfollows);

			}
		}.start();

	}

	/**
	 * 滑动时被调用
	 */
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.visibleItemCount = visibleItemCount;
		visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
	}

	/**
	 * 滑动状态改变时被调用
	 */
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		int itemsLastIndex = adapter.getCount() - 1; // 数据集最后一项的索引
		int lastIndex = itemsLastIndex + 1; // 加上底部的loadMoreView项
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& visibleLastIndex == lastIndex) {
			// 如果是自动加载,可以在这里放置异步加载数据的代码
			Log.i("LOADMORE", "loading...");
		}
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

				adapter.notifyDataSetChanged(); // 数据集变化后,通知adapter
				listView.setSelection(visibleLastIndex - visibleItemCount + 1); // 设置选中项
				loadMoreButton.setText("load more"); // 恢复按钮文字
			}
		}, 2000);
	}

	/**
	 * 模拟加载数据
	 */
	private void loadData() {
//		 int count = adapter.getCount();
//		 for (int i = count; i < count + 10; i++) {
//		 adapter.addItem();
//		 }
	}
}
