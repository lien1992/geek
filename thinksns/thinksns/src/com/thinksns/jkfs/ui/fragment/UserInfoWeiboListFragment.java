package com.thinksns.jkfs.ui.fragment;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import android.R.interpolator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.BaseListFragment;
import com.thinksns.jkfs.base.ThinkSNSApplication;
import com.thinksns.jkfs.bean.AccountBean;
import com.thinksns.jkfs.bean.UserInfoBean;
import com.thinksns.jkfs.bean.WeiboBean;
import com.thinksns.jkfs.constant.HttpConstant;
import com.thinksns.jkfs.ui.WeiboDetailActivity;
import com.thinksns.jkfs.ui.WriteWeiboActivity;
import com.thinksns.jkfs.ui.adapter.WeiboAdapter;
import com.thinksns.jkfs.ui.view.PullToRefreshListView;
import com.thinksns.jkfs.util.Utility;
import com.thinksns.jkfs.util.http.HttpMethod;
import com.thinksns.jkfs.util.http.HttpUtility;

public class UserInfoWeiboListFragment extends BaseListFragment {
	private ThinkSNSApplication application;
	private LinkedList<WeiboBean> weibos = new LinkedList<WeiboBean>();
	private LinkedList<WeiboBean> weibo_all = new LinkedList<WeiboBean>();
	private WeiboAdapter adapter;
	private AccountBean account;
	private ImageView loadImage;
	private int currentPage;
	private int totalCount;
	private String since_id = "";
	private boolean firstLoad = true;
	private UserInfoBean userInfo;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
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
					loadImage.setAnimation(null);
					loadImage.setVisibility(View.GONE);
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
				insertToHead(weibos);
				Log.d("all weibos", weibo_all.size() + "");
				if (!firstLoad)
					Toast.makeText(getActivity(), "新增微博" + weibos.size() + "条",
							Toast.LENGTH_SHORT).show();
				currentPage = totalCount / 20 + 1;
				break;
			case 2:
				if (firstLoad) {
					loadImage.setAnimation(null);
					loadImage.setVisibility(View.GONE);
				}
				listView.onRefreshComplete();
				Toast.makeText(getActivity(), "网络未连接", Toast.LENGTH_SHORT)
						.show();
				break;
			case 3:
				adapter.update(weibo_all);
				adapter.notifyDataSetChanged();
				Toast.makeText(getActivity(), "删除微博成功", Toast.LENGTH_SHORT)
						.show();
				break;
			}

		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle args = this.getArguments();
		if (args != null) {
			userInfo = args.getParcelable("uinfo");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
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
		rotateAnimation.setInterpolator(new LinearInterpolator());
		rotateAnimation.setDuration(500);
		loadImage.startAnimation(rotateAnimation);

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
		adapter = new WeiboAdapter(getActivity(), mInflater, listView);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						WeiboDetailActivity.class);
				intent.putExtra("weibo_detail", weibo_all.get(position - 1));
				startActivity(intent);
			}

		});
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO Auto-generated method stub
				if (userInfo.getUid().equals(account.getUid())) {

					AlertDialog.Builder builder = new AlertDialog.Builder(
							getActivity()).setTitle("删除此微博").setPositiveButton(
							"确定", new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									String id = weibo_all.get(position - 1)
											.getFeed_id();
									delWeibo(id);

									weibo_all.remove(position - 1);

								}
							}).setNegativeButton("取消", null);

					builder.create();
					builder.show();

				}
				return true;
			}

		});

		if (totalCount == 0)
			getWeibos();
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
					map.put("act", "user_timeline");
					map.put("user_id", userInfo.getUid());
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
			if (userInfo != null)

				new Thread() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Gson gson = new Gson();
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("app", "api");
						map.put("mod", "WeiboStatuses");
						map.put("act", "user_timeline");
						map.put("user_id", userInfo.getUid());
						Log.d("wj", "userInfo.getUid()" + userInfo.getUid());
						if (!since_id.equals(""))
							map.put("since_id", since_id);
						map.put("oauth_token", account.getOauth_token());
						map.put("oauth_token_secret", account
								.getOauth_token_secret());
						String json = HttpUtility.getInstance()
								.executeNormalTask(HttpMethod.Get,
										HttpConstant.THINKSNS_URL, map);
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

	public void delWeibo(final String id) {
		if (Utility.isConnected(getActivity())) {

			new Thread() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Gson gson = new Gson();
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("app", "api");
					map.put("mod", "WeiboStatuses");
					map.put("act", "destroy");
					map.put("id", id);
					map.put("oauth_token", account.getOauth_token());
					map.put("oauth_token_secret", account
							.getOauth_token_secret());
					String json = HttpUtility.getInstance().executeNormalTask(
							HttpMethod.Post, HttpConstant.THINKSNS_URL, map);
					mHandler.sendEmptyMessage(3);
				}
			}.start();
		} else {
			mHandler.sendEmptyMessage(2);
		}

	}
}
