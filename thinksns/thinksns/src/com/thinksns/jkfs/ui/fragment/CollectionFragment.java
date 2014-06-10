package com.thinksns.jkfs.ui.fragment;

import java.lang.reflect.Type;
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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.thinksns.jkfs.ui.MainFragmentActivity;
import com.thinksns.jkfs.ui.WeiboDetailActivity;
import com.thinksns.jkfs.ui.adapter.WeiboAdapter;
import com.thinksns.jkfs.ui.view.PullToRefreshListView;
import com.thinksns.jkfs.util.Utility;
import com.thinksns.jkfs.util.http.HttpMethod;
import com.thinksns.jkfs.util.http.HttpUtility;

public class CollectionFragment extends BaseListFragment {

	public static final String TAG = "CollectionFragment";
	private ThinkSNSApplication application;
	private ImageView back;
	private LinkedList<WeiboBean> weibos = new LinkedList<WeiboBean>();
	private LinkedList<WeiboBean> weibo_all = new LinkedList<WeiboBean>();
	private ProgressBar progressBar;
	private WeiboAdapter adapter;
	private AccountBean account;
	private int currentPage;
	private int totalCount;
	private String since_id = "";
	private boolean firstLoad = true;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				if (firstLoad) {
					progressBar.setVisibility(View.INVISIBLE);
				}
				listView.onRefreshComplete();
				Toast.makeText(getActivity(), "网络未连接", Toast.LENGTH_SHORT)
						.show();
				break;
			case 1:
				listView.onRefreshComplete();
				if (firstLoad) {
					progressBar.setVisibility(View.INVISIBLE);
					if (weibos == null || weibos.size() == 0) {
						Toast.makeText(getActivity(), "亲还未收藏过微博:(",
								Toast.LENGTH_SHORT).show();
						break;
					}
				}
				if (weibos == null || weibos.size() == 0) {
					Toast.makeText(getActivity(), "暂时没有新收藏:)",
							Toast.LENGTH_SHORT).show();
					break;
				}
				if (!listView.getLoadMoreStatus() && totalCount == 10) {
					listView.setLoadMoreEnable(true);
				}
				adapter.insertToHead(weibos);
				weibo_all.addAll(weibos);
				currentPage = totalCount / 10 + 1;
				break;
			case 2:
				listView.onLoadMoreComplete();
				if (weibos == null || weibos.size() == 0) {
					Toast.makeText(getActivity(),
							"没有更多收藏了，亲一共收藏了" + weibo_all.size() + "条微博:)",
							Toast.LENGTH_SHORT).show();
					break;
				}
				adapter.append(weibos);
				weibo_all.addAll(weibos);
				currentPage = totalCount / 20 + 1;
				break;
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		View view = mInflater
				.inflate(R.layout.collection_fragment_layout, null);
		back = (ImageView) view.findViewById(R.id.wb_fav_back);
		listView = (PullToRefreshListView) view
				.findViewById(R.id.fav_main_list_view);
		progressBar = (ProgressBar) view.findViewById(R.id.fav_progressbar);

		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		application = (ThinkSNSApplication) this.getActivity()
				.getApplicationContext();
		account = application.getAccount(this.getActivity());

		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				((MainFragmentActivity) getActivity()).getSlidingMenu()
						.toggle();
			}
		});
		listView.setListener(this);
		adapter = new WeiboAdapter(getActivity(), mInflater, listView);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						WeiboDetailActivity.class);
				Log.d("listview item pos", position + "");
				intent.putExtra("weibo_detail", weibo_all.get(position - 1));
				startActivity(intent);
			}

		});

		if (totalCount == 0)
			getWeibos();
	}

	private void getWeibos() {
		// TODO Auto-generated method stub
		if (Utility.isConnected(getActivity())) {

			new Thread() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Gson gson = new Gson();
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("app", "api");
					map.put("mod", "WeiboStatuses");
					map.put("act", "favorite_feed");
					if (!since_id.equals(""))
						map.put("since_id", since_id);
					map.put("count", "10");
					map.put("oauth_token", account.getOauth_token());
					map.put("oauth_token_secret", account
							.getOauth_token_secret());
					String json = HttpUtility.getInstance().executeNormalTask(
							HttpMethod.Get, HttpConstant.THINKSNS_URL, map);
					Type listType = new TypeToken<LinkedList<WeiboBean>>() {
					}.getType();
					weibos = gson.fromJson(json, listType);
					if (weibos != null && weibos.size() > 0) {
						since_id = weibos.get(0).getFeed_id();
						totalCount += weibos.size();
					}
					mHandler.sendEmptyMessage(1);
				}
			}.start();
		} else {
			mHandler.sendEmptyMessage(0);
		}
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		if (Utility.isConnected(getActivity())) {
			new Thread() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Gson gson = new Gson();
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("app", "api");
					map.put("mod", "WeiboStatuses");
					map.put("act", "favorite_feed");
					map.put("count", "10");
					map.put("page", currentPage + "");
					map.put("oauth_token", account.getOauth_token());
					map.put("oauth_token_secret", account
							.getOauth_token_secret());
					String json = HttpUtility.getInstance().executeNormalTask(
							HttpMethod.Get, HttpConstant.THINKSNS_URL, map);
					Type listType = new TypeToken<LinkedList<WeiboBean>>() {
					}.getType();
					weibos = gson.fromJson(json, listType);
					if (weibos != null && weibos.size() > 0) {
						totalCount += weibos.size();
					}
					mHandler.sendEmptyMessage(2);
				}
			}.start();
		} else {
			mHandler.sendEmptyMessage(0);
		}

	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		firstLoad = false;
		getWeibos();
	}

	public void insertToHead(List<WeiboBean> lists) {
		if (lists == null) {
			return;
		}
		for (int i = lists.size() - 1; i >= 0; --i) {
			weibo_all.addFirst(lists.get(i));
		}
	}
}
