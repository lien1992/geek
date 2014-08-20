package com.thinksns.jkfs.ui.fragment;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import android.R.interpolator;
import android.os.Bundle;
import android.os.Handler;
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
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.BaseListFragment;
import com.thinksns.jkfs.base.ThinkSNSApplication;
import com.thinksns.jkfs.bean.AccountBean;
import com.thinksns.jkfs.bean.CommentBean;
import com.thinksns.jkfs.constant.HttpConstant;
import com.thinksns.jkfs.ui.adapter.CommentAdapter;
import com.thinksns.jkfs.ui.view.PullToRefreshListView;
import com.thinksns.jkfs.util.Utility;
import com.thinksns.jkfs.util.http.HttpMethod;
import com.thinksns.jkfs.util.http.HttpUtility;

/**
 * 我评论的
 * 
 * @author wangjia
 * 
 */
public class CommentByMeFragment extends BaseListFragment {
	private ThinkSNSApplication application;
	private AccountBean account;
	private CommentAdapter comment_adapter;
	private ImageView loadImage;
	private int cbm_totalCount;
	private int cbm_currentPage;
	private String cbm_since_id = "";
	private LinkedList<CommentBean> cbm_comments = new LinkedList<CommentBean>();
	private LinkedList<CommentBean> cbm_comment_all = new LinkedList<CommentBean>();
	private boolean firstLoad = true;

	private DbUtils db;
	private List<CommentBean> comments_cache;

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
				if (cbm_comments == null || cbm_comments.size() == 0) {
					Toast.makeText(getActivity(), "暂时没有新评论:)",
							Toast.LENGTH_SHORT).show();
					break;
				}
				if (!listView.getLoadMoreStatus() && cbm_totalCount == 10) {
					listView.setLoadMoreEnable(true);
				}
				comment_adapter.insertToHead(cbm_comments);

				try {
					if (application.isClearCache()) {
						loadImage.setAnimation(null);
						loadImage.setVisibility(View.GONE);
						db = DbUtils.create(getActivity(), "thinksns2.db");
						db.configDebug(true);
						application.setClearCache(false);
					}
					for (int i = cbm_comments.size() - 1; i >= 0; --i) {
						CommentBean cb = cbm_comments.get(i);
						cb.setComment_type("by");
						db.save(cb);
					}
				} catch (DbException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				for (int i = cbm_comments.size() - 1; i >= 0; --i) {
					cbm_comment_all.addFirst(cbm_comments.get(i));
				}
				cbm_currentPage = cbm_totalCount / 10 + 1;
				break;
			case 2:
				listView.onLoadMoreComplete();
				if (cbm_comments == null || cbm_comments.size() == 0) {
					Toast.makeText(getActivity(), "没有更多评论了亲:(",
							Toast.LENGTH_SHORT).show();
					break;
				}
				comment_adapter.append(cbm_comments);
				cbm_comment_all.addAll(cbm_comments);
				cbm_currentPage = cbm_totalCount / 10 + 1;
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
		rotateAnimation.setInterpolator(new LinearInterpolator());
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
		listView.setDividerHeight(0);
		comment_adapter = new CommentAdapter(getActivity(), mInflater);
		listView.setAdapter(comment_adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

			}

		});

		if (application.isClearCache()) {
			loadImage.setAnimation(null);
			loadImage.setVisibility(View.GONE);
			application.setClearCache(false);
			return;
		}

		try {
			comments_cache = db.findAll(Selector.from(CommentBean.class).where(
					"comment_type", "=", "by").limit(10).orderBy("id", true));
			if (comments_cache != null && comments_cache.size() > 0) {
				comment_adapter.insertToHead(comments_cache);
				loadImage.setAnimation(null);
				loadImage.setVisibility(View.GONE);
			} else {
				getCommentsByMe();
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
					map.put("act", "comments_by_me");
					map.put("page", cbm_currentPage + "");
					map.put("count", "10");
					map.put("oauth_token", account.getOauth_token());
					map.put("oauth_token_secret", account
							.getOauth_token_secret());
					String json = HttpUtility.getInstance().executeNormalTask(
							HttpMethod.Get, HttpConstant.THINKSNS_URL, map);
					Type listType = new TypeToken<LinkedList<CommentBean>>() {
					}.getType();
					cbm_comments = gson.fromJson(json, listType);
					if (cbm_comments != null && cbm_comments.size() > 0) {
						cbm_totalCount += cbm_comments.size();
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
		getCommentsByMe();
	}

	private void getCommentsByMe() {
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
					map.put("act", "comments_by_me");
					if (!cbm_since_id.equals(""))
						map.put("since_id", cbm_since_id);
					map.put("count", "10");
					map.put("oauth_token", account.getOauth_token());
					map.put("oauth_token_secret", account
							.getOauth_token_secret());
					String json = HttpUtility.getInstance().executeNormalTask(
							HttpMethod.Get, HttpConstant.THINKSNS_URL, map);
					Type listType = new TypeToken<LinkedList<CommentBean>>() {
					}.getType();
					cbm_comments = gson.fromJson(json, listType);
					if (cbm_comments != null && cbm_comments.size() > 0) {
						cbm_since_id = cbm_comments.get(0).getComment_id();
						cbm_totalCount += cbm_comments.size();
					}
					mHandler.sendEmptyMessage(1);
				}
			}.start();
		} else {
			mHandler.sendEmptyMessage(0);
		}

	}

}
