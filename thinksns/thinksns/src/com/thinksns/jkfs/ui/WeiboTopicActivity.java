package com.thinksns.jkfs.ui;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.ThinkSNSApplication;
import com.thinksns.jkfs.bean.AccountBean;
import com.thinksns.jkfs.bean.WeiboBean;
import com.thinksns.jkfs.constant.HttpConstant;
import com.thinksns.jkfs.ui.adapter.WeiboAdapter;
import com.thinksns.jkfs.ui.view.PullToRefreshListView;
import com.thinksns.jkfs.ui.view.PullToRefreshListView.RefreshAndLoadMoreListener;
import com.thinksns.jkfs.util.Utility;
import com.thinksns.jkfs.util.http.HttpMethod;
import com.thinksns.jkfs.util.http.HttpUtility;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 查看某话题微博列表
 * 
 * @author wangjia
 * 
 */
public class WeiboTopicActivity extends Activity {
	private ImageView back;
	private TextView topic_name;
	private String topic;
	private AccountBean account;
	private ThinkSNSApplication application;
	private LinkedList<WeiboBean> weibos = new LinkedList<WeiboBean>();
	private LinkedList<WeiboBean> weibo_all = new LinkedList<WeiboBean>();
	private WeiboAdapter adapter;
	private int currentPage;
	private int totalCount;
	private String since_id = "";
	private boolean firstLoad = true;
	private PullToRefreshListView listView;
	private ProgressBar progress;
	private DisplayImageOptions options;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				if (firstLoad) {
					progress.setVisibility(View.INVISIBLE);
				}
				listView.onRefreshComplete();
				Toast.makeText(WeiboTopicActivity.this, "网络未连接",
						Toast.LENGTH_SHORT).show();
				break;
			case 1:
				listView.onRefreshComplete();
				if (firstLoad) {
					progress.setVisibility(View.INVISIBLE);
					if (weibos == null || weibos.size() == 0) {
						Toast.makeText(WeiboTopicActivity.this, "该话题还没有微博:(",
								Toast.LENGTH_SHORT).show();
						break;
					}
				}
				if (weibos == null || weibos.size() == 0) {
					Toast.makeText(WeiboTopicActivity.this, "暂时没有新微博:)",
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
					Toast.makeText(WeiboTopicActivity.this, "没有更多微博了亲:(",
							Toast.LENGTH_SHORT).show();
					break;
				}
				adapter.append(weibos);
				weibo_all.addAll(weibos);
				currentPage = totalCount / 10 + 1;
				break;
			}
		}
	};

	private RefreshAndLoadMoreListener listener = new RefreshAndLoadMoreListener() {

		@Override
		public void onLoadMore() {
			// TODO Auto-generated method stub
			if (Utility.isConnected(WeiboTopicActivity.this)) {
				new Thread() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Gson gson = new Gson();
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("app", "api");
						map.put("mod", "WeiboStatuses");
						map.put("act", "weibo_search_topic");
						map.put("key", topic);
						map.put("count", "10");
						map.put("page", currentPage + "");
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
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weibotopic);

		application = (ThinkSNSApplication) getApplicationContext();
		account = application.getAccount(this);

		Bundle extras = getIntent().getExtras();
		topic = extras.getString("topic");

		options = new DisplayImageOptions.Builder().showStubImage(
				R.drawable.ic_launcher).cacheInMemory().cacheOnDisc().build();
		topic_name = (TextView) findViewById(R.id.wb_topic_name);
		if (topic != null)
			topic_name.setText(topic);
		listView = (PullToRefreshListView) findViewById(R.id.wb_topic_list_view);
		listView.setListener(listener);
		adapter = new WeiboAdapter(this, getLayoutInflater(), listView);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(WeiboTopicActivity.this,
						WeiboDetailActivity.class);
				intent.putExtra("weibo_detail", weibo_all.get(position - 1));
				startActivity(intent);
			}

		});
		progress = (ProgressBar) findViewById(R.id.wb_topic_progressbar);
		back = (ImageView) findViewById(R.id.wb_topic_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}

		});

		if (totalCount == 0)
			getWeibos();

	}

	private void getWeibos() {
		// TODO Auto-generated method stub
		if (Utility.isConnected(this)) {

			new Thread() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Gson gson = new Gson();
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("app", "api");
					map.put("mod", "WeiboStatuses");
					map.put("act", "weibo_search_topic");
					map.put("key", topic);
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

}
