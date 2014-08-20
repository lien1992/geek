package com.thinksns.jkfs.ui.fragment;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;

import android.R.interpolator;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.BaseListFragment;
import com.thinksns.jkfs.bean.AccountBean;
import com.thinksns.jkfs.bean.CommentBean;
import com.thinksns.jkfs.bean.WeiboBean;
import com.thinksns.jkfs.constant.HttpConstant;
import com.thinksns.jkfs.ui.adapter.CommentAdapter;
import com.thinksns.jkfs.ui.view.PullToRefreshListView;
import com.thinksns.jkfs.util.Utility;
import com.thinksns.jkfs.util.http.HttpMethod;
import com.thinksns.jkfs.util.http.HttpUtility;

/**
 * 评论列表
 * 
 * @author wangjia
 * 
 */
public class CommentListFragment extends BaseListFragment {
	private AccountBean account;
	private ImageView loadImage;
	private WeiboBean weibo;
	private CommentAdapter adapter;
	private LinkedList<CommentBean> comments = new LinkedList<CommentBean>();
	private LinkedList<CommentBean> comment_all = new LinkedList<CommentBean>();
	private int currentPage;
	private int totalCount;
	private String since_id = "";
	private boolean firstLoad = true;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				if (firstLoad) {
					loadImage.setAnimation(null);
					loadImage.setVisibility(View.GONE);
				}
				listView.onRefreshComplete();
				if (weibo.getComment_count() > 0)
					Toast.makeText(getActivity(), "网络未连接，评论列表加载失败:(",
							Toast.LENGTH_SHORT).show();
				break;
			case 1:
				listView.onRefreshComplete();
				if (firstLoad) {
					loadImage.setAnimation(null);
					loadImage.setVisibility(View.GONE);
					if (comments == null || comments.size() == 0) {
						break;
					}
				}
				if (comments == null || comments.size() == 0) {
					Toast.makeText(getActivity(), "暂时没有新评论:)",
							Toast.LENGTH_SHORT).show();
					break;
				}
				if (!listView.getLoadMoreStatus() && totalCount == 10) {
					listView.setLoadMoreEnable(true);
				}
				adapter.insertToHead(comments);
				for (int i = comments.size() - 1; i >= 0; --i) {
					comment_all.addFirst(comments.get(i));
				}
				Log.d("comments loaded", "true");
				if (!firstLoad)
					Toast.makeText(getActivity(),
							"新增评论" + comments.size() + "条", Toast.LENGTH_SHORT)
							.show();
				currentPage = totalCount / 10 + 1;
				break;
			case 2:
				listView.onLoadMoreComplete();
				if (comments == null || comments.size() == 0) {
					Toast.makeText(getActivity(), "没有新评论", Toast.LENGTH_SHORT)
							.show();
					break;
				}
				adapter.append(comments);
				comment_all.addAll(comments);
				currentPage = totalCount / 10 + 1;
				break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle bundle = this.getArguments();
		if (bundle != null) {
			this.account = bundle.getParcelable("account");
			this.weibo = bundle.getParcelable("weibo");
		}
	}

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
		rotateAnimation.setInterpolator(new LinearInterpolator());
		rotateAnimation.setDuration(500);
		loadImage.startAnimation(rotateAnimation);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		listView.setListener(this);
		adapter = new CommentAdapter(getActivity(), mInflater);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

			}

		});
		if (totalCount == 0)
			getComments();
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
					map.put("act", "comments");
					map.put("id", weibo.getFeed_id());
					map.put("count", "10");
					map.put("page", currentPage + "");
					map.put("oauth_token", account.getOauth_token());
					map.put("oauth_token_secret", account
							.getOauth_token_secret());
					String json = HttpUtility.getInstance().executeNormalTask(
							HttpMethod.Get, HttpConstant.THINKSNS_URL, map);
					Type listType = new TypeToken<LinkedList<CommentBean>>() {
					}.getType();
					comments = gson.fromJson(json, listType);
					if (comments != null && comments.size() > 0) {
						totalCount += comments.size();
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
		getComments();
	}

	private void getComments() {
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
					map.put("act", "comments");
					map.put("id", weibo.getFeed_id());
					if (!since_id.equals(""))
						map.put("since_id", since_id);
					map.put("count", "10");
					map.put("oauth_token", account.getOauth_token());
					map.put("oauth_token_secret", account
							.getOauth_token_secret());
					String json = HttpUtility.getInstance().executeNormalTask(
							HttpMethod.Get, HttpConstant.THINKSNS_URL, map);
					Log.d("comment list content", json);
					if (json != null && !"".equals(json)
							&& json.startsWith("[")) {
						Type listType = new TypeToken<LinkedList<CommentBean>>() {
						}.getType();
						comments = gson.fromJson(json, listType);
						if (comments != null && comments.size() > 0) {
							Log.d("comment list count", comments.size() + "");
							since_id = comments.get(0).getComment_id();
							totalCount += comments.size();
						}
						mHandler.sendEmptyMessage(1);
					}
				}
			}.start();
		} else {
			mHandler.sendEmptyMessage(0);
		}

	}
}
