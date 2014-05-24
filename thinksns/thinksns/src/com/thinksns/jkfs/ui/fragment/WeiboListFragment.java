package com.thinksns.jkfs.ui.fragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import com.thinksns.jkfs.bean.WeiboBean;
import com.thinksns.jkfs.constant.HttpConstant;
import com.thinksns.jkfs.ui.adapter.WeiboAdapter;
import com.thinksns.jkfs.ui.view.PullToRefreshListView;
import com.thinksns.jkfs.util.Utility;
import com.thinksns.jkfs.util.http.HttpMethod;
import com.thinksns.jkfs.util.http.HttpUtility;

/**
 * 微博列表，待完善.. 下拉刷新、加载更多仍存bug, fixing..
 * 
 * @author wangjia
 * 
 */
public class WeiboListFragment extends BaseListFragment {

	private ThinkSNSApplication application;
	// private WeiboListBean weiboList = new WeiboListBean();
	private List<WeiboBean> weibos = new ArrayList<WeiboBean>();
	private WeiboAdapter adapter;
	private AccountBean account;

	private int currentPage = 2;// 实验ing..
	private String since_id = "";// 实验ing..


	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				Log.d("WEIBO COUNT", weibos.size() + "");
				listView.onLoadMoreComplete();
				adapter.append(weibos);
				currentPage++;
				break;
			case 1:
				listView.onRefreshComplete();
				if (!listView.getLoadMoreStatus()) {
					listView.setLoadMoreEnable(true);
				}
				if (weibos == null) {
					Toast.makeText(getActivity(), "没有新微博", Toast.LENGTH_SHORT)
							.show();
					break;
				}
				adapter.insertToHead(weibos);
				Toast.makeText(getActivity(), "新增微博" + weibos.size() + "条",
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

		View view = mInflater.inflate(R.layout.main_weibo_list_fragment, null);
		listView = (PullToRefreshListView) view
				.findViewById(R.id.main_weibo_list_view);

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
		//listView.setLoadMoreEnable(true);
		adapter = new WeiboAdapter(getActivity(), mInflater, listView);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				// 点击某一条微博
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
					map.put("mod", "WeiboStatuses");
					map.put("act", "public_timeline");
					map.put("page", currentPage + "");
					map.put("oauth_token", account.getOauth_token());
					map.put("oauth_token_secret", account
							.getOauth_token_secret());
					String json = HttpUtility.getInstance().executeNormalTask(
							HttpMethod.Get, HttpConstant.THINKSNS_URL, map);
					Type listType = new TypeToken<ArrayList<WeiboBean>>() {
					}.getType();
					weibos = gson.fromJson(json, listType);
					mHandler.sendEmptyMessage(0);
				}
			}.start();
		} else {
			mHandler.sendEmptyMessage(2);
		}

	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		if (Utility.isConnected(getActivity())) {

			// 待添加超时判断+新微博判断

			new Thread() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Gson gson = new Gson();
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("app", "api");
					map.put("mod", "WeiboStatuses");
					map.put("act", "public_timeline");
					if (!since_id.equals(""))
						map.put("max_id", since_id);
					map.put("oauth_token", account.getOauth_token());
					map.put("oauth_token_secret", account
							.getOauth_token_secret());
					String json = HttpUtility.getInstance().executeNormalTask(
							HttpMethod.Get, HttpConstant.THINKSNS_URL, map);
					Type listType = new TypeToken<ArrayList<WeiboBean>>() {
					}.getType();
					weibos = gson.fromJson(json, listType);
					since_id = weibos.get(0).getFeed_id();
					Log.d("WEIBO SINCE ID", since_id);
					Log.d("WEIBO SINCE ID CONTENT", weibos.get(0).getContent());
					mHandler.sendEmptyMessage(1);
				}
			}.start();
		} else {
			mHandler.sendEmptyMessage(2);
		}

	}

}
