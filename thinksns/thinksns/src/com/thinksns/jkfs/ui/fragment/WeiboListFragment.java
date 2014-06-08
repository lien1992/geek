package com.thinksns.jkfs.ui.fragment;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
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
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.BaseListFragment;
import com.thinksns.jkfs.base.ThinkSNSApplication;
import com.thinksns.jkfs.bean.AccountBean;
import com.thinksns.jkfs.bean.WeiboAttachBean;
import com.thinksns.jkfs.bean.WeiboBean;
import com.thinksns.jkfs.bean.WeiboRepostAttachBean;
import com.thinksns.jkfs.bean.WeiboRepostBean;
import com.thinksns.jkfs.constant.HttpConstant;
import com.thinksns.jkfs.ui.WeiboDetailActivity;
import com.thinksns.jkfs.ui.WriteWeiboActivity;
import com.thinksns.jkfs.ui.adapter.WeiboAdapter;
import com.thinksns.jkfs.ui.view.PullToRefreshListView;
import com.thinksns.jkfs.util.Utility;
import com.thinksns.jkfs.util.http.HttpMethod;
import com.thinksns.jkfs.util.http.HttpUtility;

/**
 * 微博列表，后期改为用户关注的微博，目前暂显示公共微博 待完成的部分（新微博提醒、url等的显示）
 * 
 * @author wangjia
 * 
 */
public class WeiboListFragment extends BaseListFragment {

	private ThinkSNSApplication application;
	private LinkedList<WeiboBean> weibos = new LinkedList<WeiboBean>();
	private LinkedList<WeiboBean> weibo_all = new LinkedList<WeiboBean>();
	private WeiboAdapter adapter;
	private AccountBean account;
	private ProgressBar progressBar;
	private int currentPage;
	private int totalCount;
	private String since_id = "";
	private boolean firstLoad = true;
	private boolean cacheMode = true;

	private DbUtils db;
	private List<WeiboBean> weibos_cache;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				// Log.d("WEIBO COUNT", weibos.size() + "");
				listView.onLoadMoreComplete();
				if (weibos == null || weibos.size() == 0) {
					Toast.makeText(getActivity(), "没有更多微博了亲:(",
							Toast.LENGTH_SHORT).show();
					break;
				}
				adapter.append(weibos);
				weibo_all.addAll(weibos);
				currentPage = totalCount / 20 + 1;
				break;
			case 1:
				listView.onRefreshComplete();
				if (firstLoad) {
					progressBar.setVisibility(View.INVISIBLE);
					if (weibos == null || weibos.size() == 0) {
						Toast.makeText(getActivity(), "出现意外，微博加载失败:(",
								Toast.LENGTH_SHORT).show();
						break;
					}
				}
				if (weibos == null || weibos.size() == 0) {
					Toast.makeText(getActivity(), "暂时没有新微博:)",
							Toast.LENGTH_SHORT).show();
					break;
				}
				if (!listView.getLoadMoreStatus() && totalCount == 20) {
					listView.setLoadMoreEnable(true);
				}
				adapter.insertToHead(weibos);
				try {
					for (int i = weibos.size() - 1; i >= 0; --i) {
						WeiboBean wb = weibos.get(i);
						List<WeiboAttachBean> wabs = wb.getAttach();
						if (wabs != null)
							for (int j = 0; j < wabs.size(); ++j) {
								WeiboAttachBean wab = wabs.get(j);
								wab.weibo = wb;
								db.save(wab);
							}
						else {
							WeiboRepostBean wrb = wb.getTranspond_data();
							if (wrb != null) {
								wrb.weibo = wb;
								List<WeiboRepostAttachBean> wrabs = wrb
										.getAttach();
								if (wrabs != null)
									for (int j = 0; j < wrabs.size(); ++j) {
										WeiboRepostAttachBean wrab = wrabs
												.get(j);
										wrab.repost = wrb;
										db.save(wrab);
									}
								else
									db.save(wrb);
							} else
								db.save(wb);
						}
					}
				} catch (DbException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				insertToHead(weibos);
				Log.d("all weibos", weibo_all.size() + "");
				if (!firstLoad)
					Toast.makeText(getActivity(), "新增微博" + weibos.size() + "条",
							Toast.LENGTH_SHORT).show();
				currentPage = totalCount / 20 + 1;
				break;
			case 2:
				if (firstLoad) {
					progressBar.setVisibility(View.INVISIBLE);
				}
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
		progressBar = (ProgressBar) view
				.findViewById(R.id.main_weibo_progressbar);

		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		application = (ThinkSNSApplication) this.getActivity()
				.getApplicationContext();
		account = application.getAccount(this.getActivity());

		db = DbUtils.create(getActivity(), "thinksns2.db");
		// db.configAllowTransaction(true);
		db.configDebug(true);

		listView.setListener(this);
		adapter = new WeiboAdapter(getActivity(), mInflater, listView);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (cacheMode) {
					Intent intent = new Intent(getActivity(),
							WeiboDetailActivity.class);
					intent.putExtra("weibo_detail", weibos_cache
							.get(position - 1));
					startActivity(intent);

				} else {
					Intent intent = new Intent(getActivity(),
							WeiboDetailActivity.class);
					intent
							.putExtra("weibo_detail", weibo_all
									.get(position - 1));
					startActivity(intent);
				}
			}

		});
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						WriteWeiboActivity.class);
				startActivity(intent);
				return true;
			}

		});
		try {
			weibos_cache = db.findAll(Selector.from(WeiboBean.class).limit(20)
					.orderBy("id", true));
			if (weibos_cache != null && weibos_cache.size() > 0) {
				Log.d("wj", "weibos_cache.size():" + weibos_cache.size());
				for (int i = 0; i < weibos_cache.size(); ++i) {
					Log.d("wj", "attach.size(): "
							+ weibos_cache.get(i).getAttach().size()
							+ " weibo.type:" + weibos_cache.get(i).getType());
					WeiboBean wb = weibos_cache.get(i);
					if (wb.getType().equals("repost")) {
						Log.d("wj", "weibo");
						WeiboRepostBean wrb = db.findFirst(Selector.from(
								WeiboRepostBean.class).where("originId", "=",
								wb.getId()));
						wb.setTranspond_data(wrb);
					}

				}
				Log.d("wj", "weibo attach table size:"
						+ db.findAll(Selector.from(WeiboAttachBean.class))
								.size());
				Log.d("wj", "weibo repost table size:"
						+ db.findAll(Selector.from(WeiboRepostBean.class))
								.size());
				adapter.insertToHead(weibos_cache);
				progressBar.setVisibility(View.INVISIBLE);
			} else {
				getWeibos();
				cacheMode = false;
			}
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
					Type listType = new TypeToken<LinkedList<WeiboBean>>() {
					}.getType();
					weibos = gson.fromJson(json, listType);
					if (weibos != null && weibos.size() > 0) {
						totalCount += weibos.size();
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
		// TODO Auto-generated method stub
		firstLoad = false;
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
					map.put("act", "public_timeline");
					if (!since_id.equals(""))
						map.put("since_id", since_id);
					map.put("oauth_token", account.getOauth_token());
					map.put("oauth_token_secret", account
							.getOauth_token_secret());
					String json = HttpUtility.getInstance().executeNormalTask(
							HttpMethod.Get, HttpConstant.THINKSNS_URL, map);
					Type listType = new TypeToken<LinkedList<WeiboBean>>() {
					}.getType();
					weibos = gson.fromJson(json, listType); // bug待修复，可能返回string非array
					if (weibos != null && weibos.size() > 0) {
						since_id = weibos.get(0).getFeed_id();
						Log.d("WEIBO SINCE ID", since_id);
						Log.d("WEIBO SINCE ID CONTENT", weibos.get(0)
								.getContent());
						totalCount += weibos.size();
					}
					mHandler.sendEmptyMessage(1);
				}
			}.start();
		} else {
			mHandler.sendEmptyMessage(2);
		}
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
