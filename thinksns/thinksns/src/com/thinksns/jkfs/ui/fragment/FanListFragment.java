package com.thinksns.jkfs.ui.fragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.BaseListFragment;
import com.thinksns.jkfs.base.ThinkSNSApplication;
import com.thinksns.jkfs.bean.AccountBean;
import com.thinksns.jkfs.bean.UserFollowBean;
import com.thinksns.jkfs.constant.HttpConstant;
import com.thinksns.jkfs.ui.WeiboDetailActivity;
import com.thinksns.jkfs.ui.adapter.PeopleAdapter;
import com.thinksns.jkfs.ui.view.PullToRefreshListView;
import com.thinksns.jkfs.util.Utility;
import com.thinksns.jkfs.util.http.HttpMethod;
import com.thinksns.jkfs.util.http.HttpUtility;

/**
 * 微博列表，待完善.. 下拉刷新、加载更多仍存bug, fixing..
 * 
 * @author wangjia
 * 
 * 抄袭之的FANLISTFRAGMENT
 */
public class FanListFragment extends BaseListFragment {

	private ThinkSNSApplication application;
	private LinkedList<UserFollowBean> userfollows = new LinkedList<UserFollowBean>();
	private LinkedList<UserFollowBean> userfollow_all = new LinkedList<UserFollowBean>();
	private PeopleAdapter adapter;
	private AccountBean account;
	private int currentPage = 1;
	private int totalCount = 0;
	private String since_id = "";


	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				
				listView.onLoadMoreComplete();
				if (userfollows==null||userfollows.size() == 0) {
					Toast.makeText(getActivity(), "没有用户", Toast.LENGTH_SHORT)
							.show();
					break;
				}
				adapter.append(userfollows);
				userfollow_all.addAll(userfollows);
				currentPage = totalCount / 20 + 1;
				break;
			case 1:
				listView.onRefreshComplete();
				if (!listView.getLoadMoreStatus()) {
					listView.setLoadMoreEnable(true);
				}
				if (userfollows==null||userfollows.size() == 0) {
					Toast.makeText(getActivity(), "没有新用户", Toast.LENGTH_SHORT)
							.show();
					break;
				}
				if (!listView.getLoadMoreStatus() && totalCount == 20) {
					listView.setLoadMoreEnable(true);
				}
				adapter.insertToHead(userfollows);
				insertToHead(userfollows);
				Toast.makeText(getActivity(), "新增用户" + userfollows.size() + "个",
						Toast.LENGTH_SHORT).show();
				break;
			case 2:
				listView.onRefreshComplete();
				Toast.makeText(getActivity(), "网络未连接", Toast.LENGTH_SHORT)
						.show();
			}

		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);

		
		
		View view = mInflater.inflate(R.layout.people_list, null);
		listView = (PullToRefreshListView) view
				.findViewById(R.id.people_list_view);
		

		return view;
	}

	
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		application = (ThinkSNSApplication) this.getActivity()
				.getApplicationContext();
		account = application.getAccount(this.getActivity());
		
		
		listView.setListener(this);
		// listView.setLoadMoreEnable(false);
		adapter = new PeopleAdapter(getActivity(), mInflater, listView);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				
				//点击显示个人主页
				
			}

		});

	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		if (Utility.isConnected(getActivity())) {
			// 待添加超时判断


			new Thread() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Gson gson = new Gson();
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("app", "api");
					map.put("mod", "User");
					map.put("act", "user_followers"); 
					map.put("page", currentPage + "");
					map.put("oauth_token", account.getOauth_token());
					map.put("oauth_token_secret", account
							.getOauth_token_secret());
					String json = HttpUtility.getInstance().executeNormalTask(
							HttpMethod.Get, HttpConstant.THINKSNS_URL, map);
					Type listType = new TypeToken<LinkedList<UserFollowBean>>() {
					}.getType();
					userfollows = gson.fromJson(json, listType);
					if (userfollows!=null&&userfollows.size() > 0) {
						totalCount += userfollows.size();
					}
					mHandler.sendEmptyMessage(0);
					
				}
			}.start();
		} else {
			mHandler.sendEmptyMessage(2);
		}

	}

	@Override
	public void onRefresh() {
//		// TODO Auto-generated method stub
		if (Utility.isConnected(getActivity())) {

			// 待添加超时判断+新微博判断

			new Thread() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Gson gson = new Gson();
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("app", "api");
					map.put("mod", "User");
					map.put("act", "user_followers");
					if (!since_id.equals(""))
						map.put("max_id", since_id);
					map.put("oauth_token", account.getOauth_token());
					map.put("oauth_token_secret", account
							.getOauth_token_secret());
					String json = HttpUtility.getInstance().executeNormalTask(
							HttpMethod.Get, HttpConstant.THINKSNS_URL, map);
					Type listType = new TypeToken<LinkedList<UserFollowBean>>() {
					}.getType();
					userfollows = gson.fromJson(json, listType);
					if (userfollows!=null&&userfollows.size() > 0) {
//						since_id = userfollows.get(0).getFeed_id();
						Log.d("WEIBO SINCE ID", since_id);						
						totalCount += userfollows.size();
					}
					mHandler.sendEmptyMessage(1);
				}
			}.start();
		} else {
			mHandler.sendEmptyMessage(2);
		}

	}

	public void insertToHead(List<UserFollowBean> lists) {
		if (lists == null) {
			return;
		}
		for (int i = lists.size() - 1; i >= 0; --i) {
			userfollow_all.addFirst(lists.get(i));
		}
	}



}
