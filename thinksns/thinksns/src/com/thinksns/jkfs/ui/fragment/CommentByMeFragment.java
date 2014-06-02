package com.thinksns.jkfs.ui.fragment;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
	private ProgressBar progressBar;
	private int cbm_totalCount;
	private int cbm_currentPage;
	private String cbm_since_id = "";
	private LinkedList<CommentBean> cbm_comments = new LinkedList<CommentBean>();
	private LinkedList<CommentBean> cbm_comment_all = new LinkedList<CommentBean>();
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
				for (int i = cbm_comments.size() - 1; i >= 0; --i) {
					cbm_comment_all.addFirst(cbm_comments.get(i));
				}
				Toast.makeText(getActivity(),
						"新增评论" + cbm_comments.size() + "条", Toast.LENGTH_SHORT)
						.show();
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
		progressBar = (ProgressBar) view
				.findViewById(R.id.main_weibo_progressbar);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		application = (ThinkSNSApplication) this.getActivity()
				.getApplicationContext();
		account = application.getAccount(this.getActivity());

		listView.setListener(this);
		comment_adapter = new CommentAdapter(getActivity(), mInflater, listView);
		listView.setAdapter(comment_adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

			}

		});
		if (cbm_totalCount == 0)
			getCommentsByMe();
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
						cbm_since_id = cbm_comments.get(0).getId();
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
