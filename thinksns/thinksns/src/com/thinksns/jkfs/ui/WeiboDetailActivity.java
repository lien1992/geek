package com.thinksns.jkfs.ui;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.BaseActivity;
import com.thinksns.jkfs.base.ThinkSNSApplication;
import com.thinksns.jkfs.bean.AccountBean;
import com.thinksns.jkfs.bean.CommentBean;
import com.thinksns.jkfs.bean.WeiboBean;
import com.thinksns.jkfs.constant.HttpConstant;
import com.thinksns.jkfs.ui.adapter.CommentAdapter;
import com.thinksns.jkfs.ui.view.PullToRefreshListView;
import com.thinksns.jkfs.ui.view.PullToRefreshListView.RefreshAndLoadMoreListener;
import com.thinksns.jkfs.util.Utility;
import com.thinksns.jkfs.util.common.ImageUtils;
import com.thinksns.jkfs.util.common.ImageUtils.ImageCallback;
import com.thinksns.jkfs.util.http.HttpMethod;
import com.thinksns.jkfs.util.http.HttpUtility;

/**
 * 微博详细内容及评论、转发、收藏，待完成的部分（添加评论表情、图片微博的显示、评论转发赞数目的更新）
 * 
 * @author wangjia
 * 
 */
public class WeiboDetailActivity extends BaseActivity implements
		OnClickListener {
	private RelativeLayout root;
	private ImageView back;
	private EditText comment_content;
	private ImageView repost;
	private ImageView favorite;
	private ImageView emotion;
	private ImageView send;
	private ImageView like;
	private TextView like_count;
	private TextView comment_count;
	private TextView repost_count;
	private TextView user_name;
	private ImageView avatar;
	private TextView from;
	private TextView time;
	private TextView content;
	private LinearLayout repost_layout;
	private TextView re_user_name;
	private TextView re_content;
	private PullToRefreshListView listView;
	private AccountBean account;
	private ThinkSNSApplication application;
	private WeiboBean weibo;
	private CommentAdapter adapter;
	private LinkedList<CommentBean> comments = new LinkedList<CommentBean>();
	private int currentPage = 1;
	private int totalCount = 0;
	private String since_id = "";
	private boolean isFavorite;
	private boolean isLike;
	private boolean firstLoad = true;
	private ProgressDialog sendProgress;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				listView.onRefreshComplete();
				Toast.makeText(WeiboDetailActivity.this, "网络未连接，评论列表加载失败:(",
						Toast.LENGTH_SHORT).show();
				break;
			case 1:
				listView.onRefreshComplete();
				if (comments == null || comments.size() == 0) {
					break;
				}
				if (!listView.getLoadMoreStatus() && totalCount == 10) {
					listView.setLoadMoreEnable(true);
				}
				adapter.insertToHead(comments);
				if (!firstLoad) {
					Toast.makeText(WeiboDetailActivity.this,
							"新增评论" + comments.size() + "条", Toast.LENGTH_SHORT)
							.show();
				}
				break;
			case 2:
				listView.onLoadMoreComplete();
				if (comments == null || comments.size() == 0) {
					Toast.makeText(WeiboDetailActivity.this, "没有新评论",
							Toast.LENGTH_SHORT).show();
					break;
				}
				adapter.append(comments);
				currentPage = totalCount / 10 + 1;
				break;
			case 3:
				sendDialogDismiss();
				Toast.makeText(WeiboDetailActivity.this, "评论成功",
						Toast.LENGTH_SHORT).show();
				break;
			case 4:
				sendDialogDismiss();
				Toast.makeText(WeiboDetailActivity.this, "出现意外，收藏微博失败:(",
						Toast.LENGTH_SHORT).show();
				break;
			case 5:
				Toast.makeText(WeiboDetailActivity.this, "收藏微博成功",
						Toast.LENGTH_SHORT).show();
				break;
			case 6:
				Toast.makeText(WeiboDetailActivity.this, "出现意外，收藏微博失败:(",
						Toast.LENGTH_SHORT).show();
				break;
			case 7:
				Toast.makeText(WeiboDetailActivity.this, "取消收藏成功",
						Toast.LENGTH_SHORT).show();
				break;
			case 8:
				Toast.makeText(WeiboDetailActivity.this, "出现意外，取消收藏失败:(",
						Toast.LENGTH_SHORT).show();
				break;
			case 9:
				// 赞成功
				break;
			case 10:
				Toast.makeText(WeiboDetailActivity.this, "出现意外，赞失败了:(",
						Toast.LENGTH_SHORT).show();
				break;
			case 11:
				// 取消赞成功
				break;
			case 12:
				Toast.makeText(WeiboDetailActivity.this, "出现意外，取消赞失败了:(",
						Toast.LENGTH_SHORT).show();
				break;

			}
		}
	};

	private RefreshAndLoadMoreListener listener = new RefreshAndLoadMoreListener() {

		@Override
		public void onLoadMore() {
			// TODO Auto-generated method stub
			if (Utility.isConnected(WeiboDetailActivity.this)) {
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
						String json = HttpUtility.getInstance()
								.executeNormalTask(HttpMethod.Get,
										HttpConstant.THINKSNS_URL, map);
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
			getComments();
			firstLoad = false;
		}

	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		application = (ThinkSNSApplication) getApplicationContext();
		account = application.getAccount(this);

		Intent intent = getIntent();
		weibo = intent.getParcelableExtra("weibo_detail");

		setContentView(R.layout.activity_weibodetail);
		initViews();
		initContents();
	}

	private void initViews() {
		// TODO Auto-generated method stub
		root = (RelativeLayout) findViewById(R.id.wb_detail_root);
		back = (ImageView) findViewById(R.id.wb_detail_back);
		back.setOnClickListener(this);
		repost = (ImageView) findViewById(R.id.wb_detail_repost);
		repost.setOnClickListener(this);
		favorite = (ImageView) findViewById(R.id.wb_detail_favorite);
		favorite.setOnClickListener(this);
		avatar = (ImageView) findViewById(R.id.wb_detail_user_img);
		user_name = (TextView) findViewById(R.id.wb_detail_u_name);
		from = (TextView) findViewById(R.id.wb_detail_from);
		time = (TextView) findViewById(R.id.wb_detail_time);
		content = (TextView) findViewById(R.id.wb_detail_text);
		repost_layout = (LinearLayout) findViewById(R.id.wb_detail_repost_layout);
		re_user_name = (TextView) findViewById(R.id.re_detail_user_name);
		re_content = (TextView) findViewById(R.id.re_detail_wb_text);
		like_count = (TextView) findViewById(R.id.wb_detail_like_count);
		repost_count = (TextView) findViewById(R.id.wb_detail_repost_count);
		comment_count = (TextView) findViewById(R.id.wb_detail_comment_count);
		emotion = (ImageView) findViewById(R.id.wb_detail_emotions);
		emotion.setOnClickListener(this);
		send = (ImageView) findViewById(R.id.wb_detail_comment_send);
		send.setOnClickListener(this);
		like = (ImageView) findViewById(R.id.wb_detail_like);
		like.setOnClickListener(this);
		comment_content = (EditText) findViewById(R.id.wb_detail_edit_comment);
		listView = (PullToRefreshListView) findViewById(R.id.wb_detail_comment_list_view);
		adapter = new CommentAdapter(this, LayoutInflater.from(this), listView);
		listView.setAdapter(adapter);
		listView.setListener(listener);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

			}

		});
	}

	private void initContents() {
		// TODO Auto-generated method stub
		user_name.setText(weibo.getUname());
		ImageUtils.setThumbnailView(weibo.getAvatar_small(), avatar, this,
				callback);
		int where = Integer.parseInt(weibo.getFrom());
		switch (where) {
		case 0:
			from.setText("来自 网页");
			break;
		case 1:
			from.setText("来自 手机版");
			break;
		case 2:
			from.setText("来自 iPhone客户端");
			break;
		case 3:
			from.setText("来自 Android客户端");
			break;
		case 4:
			from.setText("来自 ipad客户端");
			break;
		}
		time.setText(weibo.getCtime());
		content.setText(weibo.getContent());
		if (weibo.getType().equals("repost")) {
			WeiboBean weibo_repost = weibo.getTranspond_data();
			re_user_name.setText(weibo_repost.getUname());
			re_content.setText(weibo_repost.getContent());
			repost_layout.setVisibility(View.VISIBLE);
		}
		like_count.setText("0");
		repost_count.setText(weibo.getRepost_count() + "");
		comment_count.setText(weibo.getComment_count() + "");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (totalCount == 0) {
			getComments();
		}
	}

	private void getComments() {
		if (Utility.isConnected(WeiboDetailActivity.this)) {
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
					Type listType = new TypeToken<LinkedList<CommentBean>>() {
					}.getType();
					comments = gson.fromJson(json, listType);
					if (comments != null && comments.size() > 0) {
						Log.d("comment list count", comments.size() + "");
						since_id = comments.get(0).getId();
						totalCount += comments.size();
					}
					mHandler.sendEmptyMessage(1);
				}
			}.start();
		} else {
			mHandler.sendEmptyMessage(0);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.wb_detail_back:
			if (comment_content.getText().toString().trim().length() > 0) {
				AlertDialog.Builder builder = new Builder(this);
				builder.setTitle("提示");
				builder.setMessage("取消评论微博？");
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								finish();
							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						});
				builder.create().show();
			} else {
				finish();
			}
			break;
		case R.id.wb_detail_repost:
			Intent intent = new Intent(WeiboDetailActivity.this,
					RepostActivity.class);
			intent.putExtra("repost", weibo);
			startActivity(intent);
			break;
		case R.id.wb_detail_favorite:
			if (Utility.isConnected(WeiboDetailActivity.this)) {
				// 待添加超时判断
				if (!isFavorite) {
					new Thread() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Gson gson = new Gson();
							HashMap<String, String> map = new HashMap<String, String>();
							map.put("app", "api");
							map.put("mod", "WeiboStatuses");
							map.put("act", "favorite_create");
							map.put("source_table_name", "feed");
							map.put("source_id", weibo.getFeed_id());
							map.put("oauth_token", account.getOauth_token());
							map.put("oauth_token_secret", account
									.getOauth_token_secret());
							String result = HttpUtility.getInstance()
									.executeNormalTask(HttpMethod.Get,
											HttpConstant.THINKSNS_URL, map);
							if (result.equals("1")) {
								isFavorite = true;
								mHandler.sendEmptyMessage(5);
							} else if (result.equals("0")) {
								mHandler.sendEmptyMessage(6);
							}
						}
					}.start();
				} else {
					new Thread() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Gson gson = new Gson();
							HashMap<String, String> map = new HashMap<String, String>();
							map.put("app", "api");
							map.put("mod", "WeiboStatuses");
							map.put("act", "favorite_destroy");
							map.put("source_table_name", "feed");
							map.put("source_id", weibo.getFeed_id());
							map.put("oauth_token", account.getOauth_token());
							map.put("oauth_token_secret", account
									.getOauth_token_secret());
							String result = HttpUtility.getInstance()
									.executeNormalTask(HttpMethod.Get,
											HttpConstant.THINKSNS_URL, map);
							if (result.equals("1")) {
								isFavorite = false;
								mHandler.sendEmptyMessage(7);

							} else if (result.equals("0")) {
								mHandler.sendEmptyMessage(8);
							}
						}
					}.start();
				}
			} else {
				mHandler.sendEmptyMessage(0);
			}
			break;
		case R.id.wb_detail_emotions:

			break;
		case R.id.wb_detail_comment_send:
			if (comment_content.getText().toString().trim().length() == 0) {
				Toast.makeText(this, "请输入微博内容", Toast.LENGTH_SHORT).show();
			} else {

				if (Utility.isConnected(WeiboDetailActivity.this)) {
					// 待添加超时判断
					sendDialogShow();
					new Thread() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Gson gson = new Gson();
							HashMap<String, String> map = new HashMap<String, String>();
							map.put("app", "api");
							map.put("mod", "WeiboStatuses");
							map.put("act", "comment");
							map.put("content", comment_content.getText()
									.toString());
							map.put("row_id", weibo.getFeed_id());
							map.put("table_name ", "feed");
							map.put("to_uid", account.getUid());
							map.put("oauth_token", account.getOauth_token());
							map.put("oauth_token_secret", account
									.getOauth_token_secret());
							String result = HttpUtility.getInstance()
									.executeNormalTask(HttpMethod.Get,
											HttpConstant.THINKSNS_URL, map);
							Log.d("send weibo return what?", result);
							if (result.equals("1")) {
								mHandler.sendEmptyMessage(3);
							} else if (result.equals("0")) {
								mHandler.sendEmptyMessage(4);
							}
						}
					}.start();
				} else {
					mHandler.sendEmptyMessage(0);
				}
			}
			break;
		case R.id.wb_detail_like:
			if (Utility.isConnected(WeiboDetailActivity.this)) {
				// 待添加超时判断
				if (!isLike) {
					new Thread() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Gson gson = new Gson();
							HashMap<String, String> map = new HashMap<String, String>();
							map.put("app", "api");
							map.put("mod", "WeiboStatuses");
							map.put("act", "add_digg");
							//map.put("", weibo.getFeed_id());
							map.put("oauth_token", account.getOauth_token());
							map.put("oauth_token_secret", account
									.getOauth_token_secret());
							String result = HttpUtility.getInstance()
									.executeNormalTask(HttpMethod.Get,
											HttpConstant.THINKSNS_URL, map);
							if (result.equals("1")) {
								isLike = true;
								mHandler.sendEmptyMessage(9);
							} else if (result.equals("0")) {
								mHandler.sendEmptyMessage(10);
							}
						}
					}.start();

				} else {
					new Thread() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Gson gson = new Gson();
							HashMap<String, String> map = new HashMap<String, String>();
							map.put("app", "api");
							map.put("mod", "WeiboStatuses");
							map.put("act", "delete_digg");
							//map.put("", weibo.getFeed_id());
							map.put("oauth_token", account.getOauth_token());
							map.put("oauth_token_secret", account
									.getOauth_token_secret());
							String result = HttpUtility.getInstance()
									.executeNormalTask(HttpMethod.Get,
											HttpConstant.THINKSNS_URL, map);
							if (result.equals("1")) {
								isLike = false;
								mHandler.sendEmptyMessage(11);
							} else if (result.equals("0")) {
								mHandler.sendEmptyMessage(12);
							}
						}
					}.start();
				}
			} else {
				mHandler.sendEmptyMessage(0);
			}
			break;
		}
	}

	private void sendDialogShow() {
		if (sendProgress == null) {
			sendProgress = new ProgressDialog(this);
			sendProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			sendProgress.setMessage("正在发送..");
		}
		sendProgress.show();
	}

	private void sendDialogDismiss() {
		if (sendProgress != null && sendProgress.isShowing()) {
			sendProgress.dismiss();
		}
	}

	ImageCallback callback = new ImageCallback() {
		@Override
		public void loadImage(Bitmap bitmap, String imagePath) {
			// TODO Auto-generated method stub
			try {
				ImageView img = (ImageView) root.findViewWithTag(imagePath);
				img.setImageBitmap(bitmap);
			} catch (NullPointerException ex) {
				Log.e("error", "ImageView = null");
			}
		}
	};

}
