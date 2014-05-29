package com.thinksns.jkfs.ui.fragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.BaseListFragment;
import com.thinksns.jkfs.base.ThinkSNSApplication;
import com.thinksns.jkfs.bean.AccountBean;
import com.thinksns.jkfs.bean.CommentBean;
import com.thinksns.jkfs.bean.WeiboBean;
import com.thinksns.jkfs.constant.HttpConstant;
import com.thinksns.jkfs.ui.MainFragmentActivity;
import com.thinksns.jkfs.ui.adapter.CommentAdapter;
import com.thinksns.jkfs.ui.adapter.WeiboAdapter;
import com.thinksns.jkfs.ui.view.PullToRefreshListView;
import com.thinksns.jkfs.util.Utility;
import com.thinksns.jkfs.util.http.HttpMethod;
import com.thinksns.jkfs.util.http.HttpUtility;

/**
 * 与我有关：评论（评论我的、我评论的) + at我的, 完善中..
 * 
 * @author wangjia
 * 
 */
public class AtAndCommentFragment extends BaseListFragment {

	public static final String TAG = "AtAndCommentFragment";
	private ThinkSNSApplication application;
	private AccountBean account;
	private PopupWindow popupWindow;
	private ListView lv_group;
	private List<String> groups;
	private ImageView back;
	private TextView comment;
	private TextView at;
	private WeiboAdapter at_adapter;
	private CommentAdapter comment_adapter;
	private int ctm_totalCount;
	private int ctm_currentPage;
	private String ctm_since_id = "";
	private LinkedList<CommentBean> ctm_comments = new LinkedList<CommentBean>();
	private LinkedList<CommentBean> ctm_comment_all = new LinkedList<CommentBean>();
	private int cbm_totalCount;
	private int cbm_currentPage;
	private String cbm_since_id = "";
	private LinkedList<CommentBean> cbm_comments = new LinkedList<CommentBean>();
	private LinkedList<CommentBean> cbm_comment_all = new LinkedList<CommentBean>();
	private int at_currentPage;
	private int at_totalCount;
	private String at_since_id = "";
	private LinkedList<WeiboBean> at_weibos = new LinkedList<WeiboBean>();
	private LinkedList<WeiboBean> at_weibo_all = new LinkedList<WeiboBean>();
	private boolean firstLoad = true;

	private int mode = 0;// 标识请求

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				listView.onRefreshComplete();
				Toast.makeText(getActivity(), "网络未连接", Toast.LENGTH_SHORT)
						.show();
				break;
			case 1:
				listView.onRefreshComplete();
				if (firstLoad)
					if (ctm_comments == null || ctm_comments.size() == 0) {
						Toast.makeText(getActivity(), "出现意外，评论加载失败:(",
								Toast.LENGTH_SHORT).show();
						break;
					}
				if (ctm_comments == null || ctm_comments.size() == 0) {
					Toast.makeText(getActivity(), "暂时没有新评论:)",
							Toast.LENGTH_SHORT).show();
					break;
				}
				if (!listView.getLoadMoreStatus() && ctm_totalCount == 10) {
					listView.setLoadMoreEnable(true);
				}
				comment_adapter.insertToHead(ctm_comments);
				for (int i = ctm_comments.size() - 1; i >= 0; --i) {
					ctm_comment_all.addFirst(ctm_comments.get(i));
				}

				if (!firstLoad)
					Toast.makeText(getActivity(),
							"新增评论" + ctm_comments.size() + "条",
							Toast.LENGTH_SHORT).show();
				ctm_currentPage = ctm_totalCount / 10 + 1;
				break;
			case 2:
				listView.onLoadMoreComplete();
				if (ctm_comments == null || ctm_comments.size() == 0) {
					Toast.makeText(getActivity(), "没有更多评论了亲:(",
							Toast.LENGTH_SHORT).show();
					break;
				}
				comment_adapter.append(ctm_comments);
				ctm_comment_all.addAll(ctm_comments);
				ctm_currentPage = ctm_totalCount / 10 + 1;
				break;
			case 3:
				listView.onRefreshComplete();
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
			case 4:
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
			case 5:
				listView.onRefreshComplete();
				if (at_weibos == null || at_weibos.size() == 0) {
					Toast.makeText(getActivity(), "暂时没有新@你的微博:)",
							Toast.LENGTH_SHORT).show();
					break;
				}
				if (!listView.getLoadMoreStatus() && at_totalCount == 10) {
					listView.setLoadMoreEnable(true);
				}
				at_adapter.insertToHead(at_weibos);
				for (int i = at_weibos.size() - 1; i >= 0; --i) {
					at_weibo_all.addFirst(at_weibos.get(i));
				}
				Toast.makeText(getActivity(),
						"新增@你的微博" + at_weibos.size() + "条", Toast.LENGTH_SHORT)
						.show();
				at_currentPage = at_totalCount / 10 + 1;
				break;
			case 6:
				listView.onLoadMoreComplete();
				if (at_weibos == null || at_weibos.size() == 0) {
					Toast.makeText(getActivity(), "没有更多微博了亲:(",
							Toast.LENGTH_SHORT).show();
					break;
				}
				at_adapter.append(at_weibos);
				at_weibo_all.addAll(at_weibos);
				at_currentPage = at_totalCount / 20 + 1;
				break;
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = mInflater
				.inflate(R.layout.at_comment_fragment_layout, null);
		back = (ImageView) view.findViewById(R.id.wb_at_back);
		comment = (TextView) view.findViewById(R.id.wb_comment_tab);
		at = (TextView) view.findViewById(R.id.wb_at_tab);
		listView = (PullToRefreshListView) view
				.findViewById(R.id.at_comment_main_list_view);
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

		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				((MainFragmentActivity) getActivity()).getSlidingMenu()
						.toggle();
			}
		});
		at.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				at.setTextColor(getResources().getColor(R.color.green));
				comment.setTextColor(getResources().getColor(R.color.grey));
				listView.setLoadMoreEnable(false);
				mode = 2;
				at_totalCount = 0;
				at_currentPage = 0;
				at_since_id = "";
				if (at_weibos.size() != 0)
					at_weibos.clear();
				if (at_weibo_all.size() != 0)
					at_weibo_all.clear();
				onRefresh();
			}
		});
		comment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				comment.setTextColor(getResources().getColor(R.color.green));
				at.setTextColor(getResources().getColor(R.color.grey));
				showWindow(v);
			}
		});
		getCommentsToMe();
	}

	private void getCommentsToMe() {
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
					map.put("act", "comments_to_me");
					if (!ctm_since_id.equals(""))
						map.put("since_id", ctm_since_id);
					map.put("count", "10");
					map.put("oauth_token", account.getOauth_token());
					map.put("oauth_token_secret", account
							.getOauth_token_secret());
					String json = HttpUtility.getInstance().executeNormalTask(
							HttpMethod.Get, HttpConstant.THINKSNS_URL, map);
					Type listType = new TypeToken<LinkedList<CommentBean>>() {
					}.getType();
					ctm_comments = gson.fromJson(json, listType);
					if (ctm_comments != null && ctm_comments.size() > 0) {
						ctm_since_id = ctm_comments.get(0).getId();
						ctm_totalCount += ctm_comments.size();
					}
					mHandler.sendEmptyMessage(1);
				}
			}.start();
		} else {
			mHandler.sendEmptyMessage(0);
		}
	}

	private void loadCommentsToMe() {
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
					map.put("act", "comments_to_me");
					map.put("page", ctm_currentPage + "");
					map.put("count", "10");
					map.put("oauth_token", account.getOauth_token());
					map.put("oauth_token_secret", account
							.getOauth_token_secret());
					String json = HttpUtility.getInstance().executeNormalTask(
							HttpMethod.Get, HttpConstant.THINKSNS_URL, map);
					Type listType = new TypeToken<LinkedList<CommentBean>>() {
					}.getType();
					ctm_comments = gson.fromJson(json, listType);
					if (ctm_comments != null && ctm_comments.size() > 0) {
						ctm_totalCount += ctm_comments.size();
					}
					mHandler.sendEmptyMessage(2);
				}
			}.start();
		} else {
			mHandler.sendEmptyMessage(0);
		}
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
					mHandler.sendEmptyMessage(3);
				}
			}.start();
		} else {
			mHandler.sendEmptyMessage(0);
		}

	}

	private void loadCommentsByMe() {
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
					mHandler.sendEmptyMessage(4);
				}
			}.start();
		} else {
			mHandler.sendEmptyMessage(0);
		}
	}

	private void getAtToMe() {
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
					mHandler.sendEmptyMessage(5);
				}
			}.start();
		} else {
			mHandler.sendEmptyMessage(0);
		}

	}

	private void loadAtToMe() {
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
					mHandler.sendEmptyMessage(6);

				}
			}.start();
		} else {
			mHandler.sendEmptyMessage(0);
		}
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		switch (mode) {
		case 0:
			loadCommentsToMe();
			break;
		case 1:
			loadCommentsByMe();
			break;
		case 2:
			loadAtToMe();
			break;
		}
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		switch (mode) {
		case 0:
			getCommentsToMe();
			firstLoad = false;
			break;
		case 1:
			getCommentsByMe();
			break;
		case 2:
			getAtToMe();
			break;
		}
	}

	private void showWindow(View parent) {
		if (popupWindow == null) {
			View view = mInflater.inflate(R.layout.at_comment_popup_listview,
					null);
			lv_group = (ListView) view
					.findViewById(R.id.comment_popup_listview);
			groups = new ArrayList<String>();
			groups.add("评论我的");
			groups.add("我评论的");

			GroupAdapter groupAdapter = new GroupAdapter(groups);
			lv_group.setAdapter(groupAdapter);
			popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
		}

		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);

		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		WindowManager windowManager = (WindowManager) getActivity()
				.getSystemService(Context.WINDOW_SERVICE);
		// 显示位置
		popupWindow.showAsDropDown(parent, 10, 0);
		lv_group.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				comment.setText(groups.get(position));
				mode = position;
				listView.setLoadMoreEnable(false);
				comment_adapter.clear();
				if (position == 0) {
					ctm_totalCount = 0;
					ctm_currentPage = 0;
					ctm_since_id = "";
					if (ctm_comments.size() != 0)
						ctm_comments.clear();
					if (ctm_comment_all.size() != 0)
						ctm_comment_all.clear();
				} else if (position == 1) {
					cbm_totalCount = 0;
					cbm_currentPage = 0;
					cbm_since_id = "";
					if (cbm_comments.size() != 0)
						cbm_comments.clear();
					if (cbm_comment_all.size() != 0)
						cbm_comment_all.clear();
				}
				onRefresh();

				if (popupWindow != null) {
					popupWindow.dismiss();
				}
			}
		});
	}

	class GroupAdapter extends BaseAdapter {

		private List<String> list;

		public GroupAdapter(List<String> list) {
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup viewGroup) {

			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.at_comment_popup_listview_item, null);
				holder = new ViewHolder();
				holder.groupItem = (TextView) convertView
						.findViewById(R.id.comment_popup_listview_item);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.groupItem.setText(list.get(position));
			return convertView;
		}

		class ViewHolder {
			TextView groupItem;
		}

	}
}