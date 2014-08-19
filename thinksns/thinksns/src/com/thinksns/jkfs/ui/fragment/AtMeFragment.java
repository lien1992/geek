package com.thinksns.jkfs.ui.fragment;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import android.R.interpolator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

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
import com.thinksns.jkfs.ui.adapter.WeiboAdapter;
import com.thinksns.jkfs.ui.view.PullToRefreshListView;
import com.thinksns.jkfs.util.Utility;
import com.thinksns.jkfs.util.http.HttpMethod;
import com.thinksns.jkfs.util.http.HttpUtility;

/**
 * at我的微博列表
 * 
 * @author wangjia
 * 
 */
public class AtMeFragment extends BaseListFragment {
	private ThinkSNSApplication application;
	private AccountBean account;
	private WeiboAdapter at_adapter;
	private ImageView loadImage;
	private int at_currentPage;
	private int at_totalCount;
	private String at_since_id = "";
	private LinkedList<WeiboBean> at_weibos = new LinkedList<WeiboBean>();
	private LinkedList<WeiboBean> at_weibo_all = new LinkedList<WeiboBean>();
	private boolean firstLoad = true;
	private boolean cacheMode = true;

	private DbUtils db;
	private List<WeiboBean> weibos_cache;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				if (firstLoad) {
					loadImage.setAnimation(null);
					loadImage.setVisibility(View.GONE);
				}
				listView.onRefreshComplete();
				Toast.makeText(getActivity(), "网络未连接", Toast.LENGTH_SHORT)
						.show();
				break;
			case 1:
				listView.onRefreshComplete();
				if (firstLoad) {
					loadImage.setAnimation(null);
					loadImage.setVisibility(View.GONE);
				}
				if (at_weibos == null || at_weibos.size() == 0) {
					Toast.makeText(getActivity(), "暂时没有新@你的微博:)",
							Toast.LENGTH_SHORT).show();
					break;
				}
				if (!listView.getLoadMoreStatus() && at_totalCount == 10) {
					listView.setLoadMoreEnable(true);
				}
				at_adapter.update(at_weibos);

				try {
					if (application.isClearCache()) {
						loadImage.setAnimation(null);
						loadImage.setVisibility(View.GONE);
						db = DbUtils.create(getActivity(), "thinksns2.db");
						db.configDebug(true);
						application.setClearCache(false);
					}
					for (int i = at_weibos.size() - 1; i >= 0; --i) {
						WeiboBean wb = at_weibos.get(i);
						wb.setContainsAt("t");
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
				for (int i = at_weibos.size() - 1; i >= 0; --i) {
					at_weibo_all.addFirst(at_weibos.get(i));
				}
				at_currentPage = at_totalCount / 10 + 1;
				break;
			case 2:
				listView.onLoadMoreComplete();
				if (at_weibos == null || at_weibos.size() == 0) {
					Toast.makeText(getActivity(), "没有更多微博了亲:(",
							Toast.LENGTH_SHORT).show();
					break;
				}
				at_adapter.append(at_weibos);
				at_weibo_all.addAll(at_weibos);
				at_currentPage = at_totalCount / 10 + 1;
				break;
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = mInflater.inflate(R.layout.main_weibo_list_fragment, null);
		listView = (PullToRefreshListView) view
				.findViewById(R.id.main_weibo_list_view);
		loadImage = (ImageView) view
				.findViewById(R.id.main_weibo_list_load_img);
		RotateAnimation rotateAnimation = new RotateAnimation(0.0f, +360.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotateAnimation.setRepeatCount(Animation.INFINITE);
		rotateAnimation.setInterpolator(getActivity(), interpolator.linear);
		rotateAnimation.setDuration(500);
		loadImage.startAnimation(rotateAnimation);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		application = (ThinkSNSApplication) this.getActivity()
				.getApplicationContext();
		account = application.getAccount(this.getActivity());

		db = DbUtils.create(getActivity(), "thinksns2.db");
		db.configDebug(true);

		listView.setListener(this);
		at_adapter = new WeiboAdapter(getActivity(), mInflater, listView);
		listView.setAdapter(at_adapter);
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
					intent.putExtra("weibo_detail", at_weibo_all
							.get(position - 1));
					startActivity(intent);
				}
			}

		});

		if (application.isClearCache()) {
			loadImage.setAnimation(null);
			loadImage.setVisibility(View.GONE);
			application.setClearCache(false);
			return;
		}

		try {
			weibos_cache = db.findAll(Selector.from(WeiboBean.class).where(
					"containsAt", "=", "t").limit(10).orderBy("id", true));
			if (weibos_cache != null && weibos_cache.size() > 0) {
				for (int i = 0; i < weibos_cache.size(); ++i) {
					WeiboBean wb = weibos_cache.get(i);
					if (wb.getType().equals("repost")) {
						WeiboRepostBean wrb = db.findFirst(Selector.from(
								WeiboRepostBean.class).where("originId", "=",
								wb.getId()));
						wb.setTranspond_data(wrb);
					}
				}
				at_adapter.insertToHead(weibos_cache);
				loadImage.setAnimation(null);
				loadImage.setVisibility(View.GONE);
			} else {
				getAtToMe();
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
			new Thread() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Gson gson = new Gson();
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("app", "api");
					map.put("mod", "WeiboStatuses");
					map.put("act", "mentions_feed");
					map.put("page", at_currentPage + "");
					map.put("count", "10");
					map.put("oauth_token", account.getOauth_token());
					map.put("oauth_token_secret", account
							.getOauth_token_secret());
					String json = HttpUtility.getInstance().executeNormalTask(
							HttpMethod.Get, HttpConstant.THINKSNS_URL, map);
					Type listType = new TypeToken<LinkedList<WeiboBean>>() {
					}.getType();
					at_weibos = gson.fromJson(json, listType);
					if (at_weibos != null && at_weibos.size() > 0) {
						at_totalCount += at_weibos.size();
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
		getAtToMe();
	}

	private void getAtToMe() {
		// TODO Auto-generated method stub
		if (Utility.isConnected(getActivity())) {
			cacheMode = false;
			new Thread() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Gson gson = new Gson();
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("app", "api");
					map.put("mod", "WeiboStatuses");
					map.put("act", "mentions_feed");
					if (!at_since_id.equals(""))
						map.put("since_id", at_since_id);
					map.put("count", "10");
					map.put("oauth_token", account.getOauth_token());
					map.put("oauth_token_secret", account
							.getOauth_token_secret());
					String json = HttpUtility.getInstance().executeNormalTask(
							HttpMethod.Get, HttpConstant.THINKSNS_URL, map);
					Type listType = new TypeToken<LinkedList<WeiboBean>>() {
					}.getType();
					at_weibos = gson.fromJson(json, listType);
					if (at_weibos != null && at_weibos.size() > 0) {
						at_since_id = at_weibos.get(0).getFeed_id();
						at_totalCount += at_weibos.size();
					}
					mHandler.sendEmptyMessage(1);
				}
			}.start();
		} else {
			mHandler.sendEmptyMessage(0);
		}

	}

}
