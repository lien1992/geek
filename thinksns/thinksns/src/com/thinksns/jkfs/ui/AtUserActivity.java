package com.thinksns.jkfs.ui;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.ThinkSNSApplication;
import com.thinksns.jkfs.bean.AccountBean;
import com.thinksns.jkfs.bean.UserInfoBean;
import com.thinksns.jkfs.constant.HttpConstant;
import com.thinksns.jkfs.ui.view.PullToRefreshListView;
import com.thinksns.jkfs.ui.view.PullToRefreshListView.RefreshAndLoadMoreListener;
import com.thinksns.jkfs.util.Utility;
import com.thinksns.jkfs.util.http.HttpMethod;
import com.thinksns.jkfs.util.http.HttpUtility;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 用户好友列表
 * 
 * @author wangjia
 * 
 */
public class AtUserActivity extends Activity implements OnClickListener {
	private ImageView back;
	private RelativeLayout back_to_write;
	private AccountBean account;
	private ThinkSNSApplication application;
	private AtUserAdapter adapter;
	private LinkedList<UserInfoBean> friends = new LinkedList<UserInfoBean>();
	private LinkedList<UserInfoBean> friends_all = new LinkedList<UserInfoBean>();
	private int currentPage;
	private int totalCount;
	private String since_id = "";
	private ProgressBar progress;
	private PullToRefreshListView listView;
	private LayoutInflater inflater;
	private DisplayImageOptions options;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				listView.onLoadMoreComplete();
				if (friends == null || friends.size() == 0) {
					Toast.makeText(AtUserActivity.this, "没有更多好友了亲:(",
							Toast.LENGTH_SHORT).show();
					break;
				}
				friends_all.addAll(friends);
				currentPage = totalCount / 10 + 1;
				break;
			case 1:
				listView.onRefreshComplete();
				progress.setVisibility(View.INVISIBLE);

				if (friends == null || friends.size() == 0)
					break;
				if (!listView.getLoadMoreStatus() && totalCount == 10) {
					listView.setLoadMoreEnable(true);
				}
				insertToHead(friends);
				currentPage = totalCount / 10 + 1;
				break;
			case 2:
				progress.setVisibility(View.INVISIBLE);
				listView.onRefreshComplete();
				Toast
						.makeText(AtUserActivity.this, "网络未连接",
								Toast.LENGTH_SHORT).show();
			}

		}
	};

	private RefreshAndLoadMoreListener listener = new RefreshAndLoadMoreListener() {

		@Override
		public void onLoadMore() {
			// TODO Auto-generated method stub
			if (Utility.isConnected(AtUserActivity.this)) {
				new Thread() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Gson gson = new Gson();
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("app", "api");
						map.put("mod", "User");
						map.put("act", "user_friends");
						map.put("count", "10");
						map.put("page", currentPage + "");
						map.put("oauth_token", account.getOauth_token());
						map.put("oauth_token_secret", account
								.getOauth_token_secret());
						String json = HttpUtility.getInstance()
								.executeNormalTask(HttpMethod.Get,
										HttpConstant.THINKSNS_URL, map);
						Type listType = new TypeToken<LinkedList<UserInfoBean>>() {
						}.getType();
						friends = gson.fromJson(json, listType);
						if (friends != null && friends.size() > 0) {
							totalCount += friends.size();
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
			getFriends();
		}

	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_atuser);

		application = (ThinkSNSApplication) getApplicationContext();
		account = application.getAccount(this);
		inflater = getLayoutInflater();
		options = new DisplayImageOptions.Builder().showStubImage(
				R.drawable.ic_launcher).cacheInMemory().cacheOnDisc().build();

		initViews();

		if (totalCount == 0)
			getFriends();
	}

	private void initViews() {
		// TODO Auto-generated method stub
		back = (ImageView) findViewById(R.id.wb_at_users_back);
		back.setOnClickListener(this);
		back_to_write = (RelativeLayout) findViewById(R.id.wb_at_users_bottom);
		back_to_write.setOnClickListener(this);
		progress = (ProgressBar) findViewById(R.id.wb_at_users_progressbar);
		listView = (PullToRefreshListView) findViewById(R.id.wb_at_users_list_view);
		listView.setListener(listener);
		adapter = new AtUserAdapter();
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				String uname = friends_all.get(arg2-1).getUname();
				Intent intent = new Intent();
				intent.putExtra("at_name", "@" + uname + " ");
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		});

	}

	private void getFriends() {
		if (Utility.isConnected(AtUserActivity.this)) {
			new Thread() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Gson gson = new Gson();
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("app", "api");
					map.put("mod", "User");
					map.put("act", "user_friends");
					map.put("count", "10");
					if (!since_id.equals(""))
						map.put("since_id", since_id);
					map.put("oauth_token", account.getOauth_token());
					map.put("oauth_token_secret", account
							.getOauth_token_secret());
					String json = HttpUtility.getInstance().executeNormalTask(
							HttpMethod.Get, HttpConstant.THINKSNS_URL, map);
					Type listType = new TypeToken<LinkedList<UserInfoBean>>() {
					}.getType();
					friends = gson.fromJson(json, listType);
					if (friends != null && friends.size() > 0) {
						since_id = friends.get(0).getUid();
						totalCount += friends.size();
					}
					mHandler.sendEmptyMessage(1);
				}
			}.start();
		} else {
			mHandler.sendEmptyMessage(2);
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.wb_at_users_back:
			finish();
			break;
		case R.id.wb_at_users_bottom:
			finish();
			break;

		}
	}

	class AtUserAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return friends_all.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return friends_all.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			UserInfoBean user = friends_all.get(position);
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.at_user_listview_item,
						null);
				holder.avatar = (ImageView) convertView
						.findViewById(R.id.at_user_item_avatar);
				holder.userName = (TextView) convertView
						.findViewById(R.id.at_user_item_u_name);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			ImageLoader.getInstance().displayImage(user.getAvatar_small(),
					holder.avatar, options);
			holder.userName.setText(user.getUname());

			return convertView;
		}

	}

	class ViewHolder {
		ImageView avatar;
		TextView userName;
	}

	public void insertToHead(List<UserInfoBean> lists) {
		if (lists == null) {
			return;
		}
		for (int i = lists.size() - 1; i >= 0; --i) {
			friends_all.addFirst(lists.get(i));
		}
	}

}
