package com.thinksns.jkfs.ui;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import android.R.interpolator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.ThinkSNSApplication;
import com.thinksns.jkfs.bean.AccountBean;
import com.thinksns.jkfs.bean.CommentBean;
import com.thinksns.jkfs.bean.UserInfoBean;
import com.thinksns.jkfs.bean.WeiboAttachBean;
import com.thinksns.jkfs.bean.WeiboBean;
import com.thinksns.jkfs.bean.WeiboRepostBean;
import com.thinksns.jkfs.constant.BaseConstant;
import com.thinksns.jkfs.constant.HttpConstant;
import com.thinksns.jkfs.ui.adapter.CommentAdapter;
import com.thinksns.jkfs.ui.fragment.CommentListFragment;
import com.thinksns.jkfs.ui.view.LinearLayoutForListView;
import com.thinksns.jkfs.ui.view.RoundAngleImageView;
import com.thinksns.jkfs.util.FaceDialog;
import com.thinksns.jkfs.util.Utility;
import com.thinksns.jkfs.util.FaceDialog.FaceSelect;
import com.thinksns.jkfs.util.http.HttpMethod;
import com.thinksns.jkfs.util.http.HttpUtility;

/**
 * 微博详细内容及评论、转发、收藏，待完成的部分（评论转发赞数目的更新）
 * 
 * @author wangjia
 * 
 */
public class WeiboDetailActivity extends Activity implements OnClickListener,
		FaceSelect {
	private ImageView back;
	private EditText comment_content;
	private ImageView repost;
	private ImageView favorite;
	private ImageView emotion;
	private ImageView send;
	private ImageView like;
	private ImageView comment;
	private ImageView repost_another;
	private TextView like_count;
	private TextView comment_count;
	private TextView repost_count;
	private TextView user_name;
	private RoundAngleImageView avatar;
	private TextView from;
	private TextView time;
	private TextView content;
	private ImageView pic;
	private LinearLayout repost_layout;
	private ImageView repost_pic;
	private TextView re_user_name;
	private TextView re_content;
	private View bottom;
	private LinearLayout header;
	private LinearLayout sticky_header;
	private ScrollView sv;
	private LinearLayoutForListView commentList;
	private List<WeiboAttachBean> attaches;
	private CommentAdapter adapter;
	private LinkedList<CommentBean> comments = new LinkedList<CommentBean>();
	private LinkedList<CommentBean> comment_all = new LinkedList<CommentBean>();
	private int currentPage;
	private int totalCount;
	private String since_id = "";
	private AccountBean account;
	private ThinkSNSApplication application;
	private WeiboBean weibo;
	private boolean isFavorite;
	private boolean isLike;
	private RotateAnimation rotateAnimation;
	private ProgressDialog sendProgress;
	private LinearLayout loadingComment;
	private ImageView loadImage;
	private TextView loadText;
	private LinearLayout loadMore;
	private TextView loadMoreText;

	private DisplayImageOptions options;

	int[] location = new int[2];
	int[] location2 = new int[2];

	private boolean isNoImageMode;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(WeiboDetailActivity.this, "网络未连接",
						Toast.LENGTH_SHORT).show();
				break;
			case 1:
				sendDialogDismiss();
				comment_content.setText("");
				Toast.makeText(WeiboDetailActivity.this, "评论成功",
						Toast.LENGTH_SHORT).show();
				loadImage.setVisibility(View.VISIBLE);
				loadText.setText("");
				loadingComment.setVisibility(View.VISIBLE);
				totalCount = 0;
				currentPage = 0;
				since_id = "";
				comment_all.clear();
				comment_count.setText((weibo.getComment_count() + 1) + "");
				getComments();
				/*
				 * WeiboDetailActivity.this.getSupportFragmentManager()
				 * .beginTransaction().replace( R.id.wb_detail_comment_layout,
				 * newCommentListFragment()).commit();
				 */
				break;
			case 2:
				sendDialogDismiss();
				Toast.makeText(WeiboDetailActivity.this, "出现意外，评论微博失败:(",
						Toast.LENGTH_SHORT).show();
				break;
			case 3:
				favorite.setImageDrawable(getResources().getDrawable(
						R.drawable.heart));
				favorite.invalidate();
				Toast.makeText(WeiboDetailActivity.this, "收藏微博成功",
						Toast.LENGTH_SHORT).show();
				break;
			case 4:
				/*
				 * Toast.makeText(WeiboDetailActivity.this, "出现意外，收藏微博失败:(",
				 * Toast.LENGTH_SHORT).show();
				 */
				break;
			case 5:
				favorite.setImageDrawable(getResources().getDrawable(
						R.drawable.favourite));
				favorite.invalidate();
				Toast.makeText(WeiboDetailActivity.this, "取消收藏成功",
						Toast.LENGTH_SHORT).show();
				break;
			case 6:
				/*
				 * Toast.makeText(WeiboDetailActivity.this, "出现意外，取消收藏失败:(",
				 * Toast.LENGTH_SHORT).show();
				 */
				break;
			case 7:
				int like_n = Integer.parseInt(like_count.getText().toString());
				like_count.setText((++like_n) + "");
				like.setImageDrawable(getResources().getDrawable(
						R.drawable.liked));
				like.invalidate();
				break;
			case 8:
				/*
				 * Toast.makeText(WeiboDetailActivity.this, "出现意外，赞失败了:(",
				 * Toast.LENGTH_SHORT).show(); // 存bug，待修复
				 */
				break;
			case 9:
				int dislike = Integer.parseInt(like_count.getText().toString());
				like_count.setText((--dislike) + "");
				like.setImageDrawable(getResources().getDrawable(
						R.drawable.like));
				like.invalidate();
				break;
			case 10:
				/*
				 * Toast.makeText(WeiboDetailActivity.this, "出现意外，取消赞失败了:(",
				 * Toast.LENGTH_SHORT).show();
				 */
				break;
			case 11:
				loadImage.setAnimation(null);
				if (weibo.getComment_count() > 0) {
					loadImage.setVisibility(View.GONE);
					loadText.setText("网络未连接，评论列表加载失败:(");
				} else if (weibo.getComment_count() == 0) {
					loadImage.setVisibility(View.GONE);
					loadText.setText("该微博还没有评论哦~");
				}
				break;
			case 12:
				if (comments == null || comments.size() == 0) {
					loadMoreText.setText("没有更多评论了 :)");
					loadMore.setClickable(false);
					break;
				}
				adapter.append(comments);
				commentList.bindLinearLayout();
				if (comments.size() != 10) {
					loadMoreText.setText("没有更多评论了 :)");
					loadMore.setClickable(false);
				} else
					loadMoreText.setText("加载更多");
				comment_all.addAll(comments);
				currentPage = totalCount / 10 + 1;
				break;
			case 13:
				if (comments == null || comments.size() == 0) {
					loadImage.setAnimation(null);
					loadImage.setVisibility(View.GONE);
					loadText.setText("该微博还没有评论哦~");
					break;
				}
				loadingComment.setVisibility(View.GONE);
				commentList.setVisibility(View.VISIBLE);
				adapter.clear();
				adapter.insertToHead(comments);
				commentList.bindLinearLayout();
				if (comments.size() < 10) {
					loadMoreText.setText("The End");
					loadMore.setClickable(false);
				} else if (comments.size() == 10) {
					loadMoreText.setText("加载更多");
					loadMore.setClickable(true);
				}
				for (int i = comments.size() - 1; i >= 0; --i) {
					comment_all.addFirst(comments.get(i));
				}
				currentPage = totalCount / 10 + 1;
				if (comment_all.size() > weibo.getComment_count())
					comment_count.setText(comment_all.size() + "");
				break;

			}
		}
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		application = (ThinkSNSApplication) getApplicationContext();
		account = application.getAccount(this);

		Intent intent = getIntent();
		weibo = intent.getParcelableExtra("weibo_detail");

		options = new DisplayImageOptions.Builder().showStubImage(
				R.drawable.ic_launcher).cacheInMemory().cacheOnDisc().build();

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		isNoImageMode = prefs.getBoolean(BaseConstant.NO_IMAGE_MODE_KEY, false);

		setContentView(R.layout.activity_weibodetail);
		initViews();
		initContents();
		sv.setOnTouchListener(new OnTouchListener() {
			private int lastY = 0;
			private int touchEventId = -9983761;
			@SuppressLint("HandlerLeak")
			Handler handler = new Handler() {
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					if (msg.what == touchEventId) {
						if (lastY != sv.getScrollY()) {
							handler.sendMessageDelayed(handler.obtainMessage(
									touchEventId, sv), 5);
							lastY = sv.getScrollY();
							header.getLocationOnScreen(location);
							sticky_header.getLocationOnScreen(location2);
							if (location[1] <= location2[1]) {
								sticky_header.setVisibility(View.VISIBLE);
							} else {
								sticky_header.setVisibility(View.GONE);
							}
						}
					}
				}
			};

			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					handler.sendMessageDelayed(handler.obtainMessage(
							touchEventId, v), 5);
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {
					handler.sendMessageDelayed(handler.obtainMessage(
							touchEventId, v), 5);
				}
				return false;
			}
		});

	}

	private void initViews() {
		// TODO Auto-generated method stub
		back = (ImageView) findViewById(R.id.wb_detail_back);
		back.setOnClickListener(this);
		repost = (ImageView) findViewById(R.id.wb_detail_repost);
		repost.setOnClickListener(this);
		favorite = (ImageView) findViewById(R.id.wb_detail_favorite);
		favorite.setOnClickListener(this);
		avatar = (RoundAngleImageView) findViewById(R.id.wb_detail_user_img);
		avatar.setOnClickListener(this);
		user_name = (TextView) findViewById(R.id.wb_detail_u_name);
		user_name.setOnClickListener(this);
		from = (TextView) findViewById(R.id.wb_detail_from);
		time = (TextView) findViewById(R.id.wb_detail_time);
		content = (TextView) findViewById(R.id.wb_detail_text);
		pic = (ImageView) findViewById(R.id.wb_detail_pic1);
		pic.setOnClickListener(this);
		repost_layout = (LinearLayout) findViewById(R.id.wb_detail_repost_layout);
		repost_pic = (ImageView) findViewById(R.id.re_detail_wb_pic1);
		repost_pic.setOnClickListener(this);
		re_user_name = (TextView) findViewById(R.id.re_detail_user_name);
		re_user_name.setOnClickListener(this);
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
		comment = (ImageView) findViewById(R.id.wb_detail_comment_list);
		comment.setOnClickListener(this);
		repost_another = (ImageView) findViewById(R.id.wb_detail_repost_another);
		repost_another.setOnClickListener(this);
		comment_content = (EditText) findViewById(R.id.wb_detail_edit_comment);
		bottom = (View) findViewById(R.id.wb_detail_bottom_layout_view);
		header = (LinearLayout) findViewById(R.id.wb_detail_comment_desc);
		sticky_header = (LinearLayout) findViewById(R.id.wb_detail_comment_desc_header);
		sv = (ScrollView) findViewById(R.id.wb_detail_scroll);
		commentList = (LinearLayoutForListView) findViewById(R.id.wb_detail_comment_list_view);
		adapter = new CommentAdapter(this, this.getLayoutInflater(), mHandler);
		commentList.setAdapter(adapter);
		loadingComment = (LinearLayout) findViewById(R.id.wb_detail_comment_loading);
		loadImage = (ImageView) findViewById(R.id.wb_detail_comment_load_img);
		rotateAnimation = new RotateAnimation(0.0f, +360.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotateAnimation.setRepeatCount(Animation.INFINITE);
		rotateAnimation.setInterpolator(this, interpolator.linear);
		rotateAnimation.setDuration(500);
		loadImage.startAnimation(rotateAnimation);
		loadText = (TextView) findViewById(R.id.wb_detail_comment_load_text);
		loadMore = (LinearLayout) findViewById(R.id.wb_detail_comment_load_more);
		loadMore.setOnClickListener(this);
		loadMoreText = (TextView) findViewById(R.id.wb_detail_comment_load_more_text);

	}

	private void initContents() {
		// TODO Auto-generated method stub
		user_name.setText(weibo.getUname());
		ImageLoader.getInstance().displayImage(weibo.getAvatar_small(), avatar,
				options);
		int where = Integer.parseInt(weibo.getFrom());
		switch (where) {
		case 0:
			from.setText("来自 网页");
			break;
		case 1:
			from.setText("来自 手机版");
			break;
		case 2:
			from.setText("来自 Android客户端");
			break;
		case 3:
			from.setText("来自 iPhone客户端");
			break;
		case 4:
			from.setText("来自 ipad客户端");
			break;
		}
		time.setText(weibo.getCtime());
		content.setText(weibo.getListViewSpannableString());
		content.setMovementMethod(LinkMovementMethod.getInstance());
		attaches = weibo.getAttach();
		if (weibo.getType().equals("repost")) {
			WeiboRepostBean weibo_repost = weibo.getTranspond_data();
			re_user_name.setText(weibo_repost.getUname());
			re_content.setText(weibo_repost.getListViewSpannableString());
			re_content.setMovementMethod(LinkMovementMethod.getInstance());
			if (weibo_repost.getType().equals("postimage") && !isNoImageMode) {
				ImageLoader.getInstance().displayImage(
						weibo_repost.getAttach().get(0).getAttach_middle(),
						repost_pic, options);
				repost_pic.setVisibility(View.VISIBLE);
				repost_pic.setFocusable(true);
			}
			repost_layout.setVisibility(View.VISIBLE);

		}
		if (weibo.getType().equals("postimage") && !isNoImageMode) {
			Log.d("weibo detail attach is null?", (weibo.getAttach() == null)
					+ "");
			Log.d("weibo attaches size", attaches.size() + "");
			ImageLoader.getInstance().displayImage(
					weibo.getAttach().get(0).getAttach_middle(), pic, options);
			pic.setVisibility(View.VISIBLE);
			pic.setFocusable(true);
		}
		like_count.setText(weibo.getDigg_count() + "");
		repost_count.setText(weibo.getRepost_count() + "");
		comment_count.setText(weibo.getComment_count() + "");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		/*
		 * this.getSupportFragmentManager().beginTransaction().replace(
		 * R.id.wb_detail_comment_layout, newCommentListFragment()) .commit();
		 */
		if (totalCount == 0)
			getComments();

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
			FaceDialog.release();
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
							map.put("source_id", weibo.getFeed_id() + "");
							map.put("source_app", "public");
							map.put("oauth_token", account.getOauth_token());
							map.put("oauth_token_secret", account
									.getOauth_token_secret());
							String result = HttpUtility.getInstance()
									.executeNormalTask(HttpMethod.Get,
											HttpConstant.THINKSNS_URL, map);
							if (result.equals("1")) {
								isFavorite = true;
								mHandler.sendEmptyMessage(3);
							} else if (result.equals("0")) {
								mHandler.sendEmptyMessage(4);
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
							map.put("source_id", weibo.getFeed_id() + "");
							map.put("source_app", "public");
							map.put("oauth_token", account.getOauth_token());
							map.put("oauth_token_secret", account
									.getOauth_token_secret());
							String result = HttpUtility.getInstance()
									.executeNormalTask(HttpMethod.Get,
											HttpConstant.THINKSNS_URL, map);
							if (result.equals("1")) {
								isFavorite = false;
								mHandler.sendEmptyMessage(5);

							} else if (result.equals("0")) {
								mHandler.sendEmptyMessage(6);
							}
						}
					}.start();
				}
			} else {
				mHandler.sendEmptyMessage(0);
			}
			break;
		case R.id.wb_detail_emotions:
			Utility.hideSoftInput(this);
			FaceDialog.showFaceDialog(this, bottom, bottom.getHeight(), this);
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
							map.put("row_id", weibo.getFeed_id() + "");
							map.put("table_name ", "feed");
							map.put("to_uid", account.getUid());
							map.put("oauth_token", account.getOauth_token());
							map.put("oauth_token_secret", account
									.getOauth_token_secret());
							String result = HttpUtility.getInstance()
									.executeNormalTask(HttpMethod.Get,
											HttpConstant.THINKSNS_URL, map);
							if (result.equals("1")) {
								mHandler.sendEmptyMessage(1);
							} else if (result.equals("0")) {
								mHandler.sendEmptyMessage(2);
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
							map.put("feed_id", weibo.getFeed_id());
							map.put("oauth_token", account.getOauth_token());
							map.put("oauth_token_secret", account
									.getOauth_token_secret());
							String result = HttpUtility.getInstance()
									.executeNormalTask(HttpMethod.Get,
											HttpConstant.THINKSNS_URL, map);
							if (result.equals("1")) {
								isLike = true;
								mHandler.sendEmptyMessage(7);
							} else if (result.equals("0")) {
								mHandler.sendEmptyMessage(8);
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
							map.put("feed_id", weibo.getFeed_id());
							map.put("oauth_token", account.getOauth_token());
							map.put("oauth_token_secret", account
									.getOauth_token_secret());
							String result = HttpUtility.getInstance()
									.executeNormalTask(HttpMethod.Get,
											HttpConstant.THINKSNS_URL, map);
							if (result.equals("1")) {
								isLike = false;
								mHandler.sendEmptyMessage(9);
							} else if (result.equals("0")) {
								mHandler.sendEmptyMessage(10);
							}
						}
					}.start();
				}
			} else {
				mHandler.sendEmptyMessage(0);
			}
			break;
		case R.id.wb_detail_comment_list:
			/*
			 * this.getSupportFragmentManager().beginTransaction().replace(
			 * R.id.wb_detail_comment_layout, newCommentListFragment())
			 * .commit();
			 */
			break;
		case R.id.wb_detail_comment_load_more:
			loadMoreComments();
			break;
		case R.id.wb_detail_repost_another:
			Intent in_r = new Intent(WeiboDetailActivity.this,
					RepostActivity.class);
			in_r.putExtra("repost", weibo);
			startActivity(in_r);
			FaceDialog.release();
			break;
		case R.id.wb_detail_user_img:
			if (Utility.isConnected(this)) {

				new Thread() {
					@Override
					public void run() {
						Gson gson = new Gson();
						HashMap<String, String> map = new HashMap<String, String>();
						map = new HashMap<String, String>();
						map.put("app", "api");
						map.put("mod", "User");
						map.put("act", "show");
						map.put("user_id", weibo.getUid());
						map.put("oauth_token", account.getOauth_token());
						map.put("oauth_token_secret", account
								.getOauth_token_secret());
						String json = HttpUtility.getInstance()
								.executeNormalTask(HttpMethod.Get,
										HttpConstant.THINKSNS_URL, map);
						if (json != null && !"".equals(json)) {
							UserInfoBean userinfo = gson.fromJson(json,
									UserInfoBean.class);
							if (userinfo != null) {
								Intent in_o = new Intent(
										WeiboDetailActivity.this,
										OtherInfoActivity.class);
								in_o.putExtra("FLAG", (userinfo.getUid()
										.equals(account.getUid()) ? "0" : "1"));
								in_o.putExtra("userinfo", userinfo.getUid());
								in_o.putExtra("following",
										userinfo.follow_state.getFollowing()
												+ "");
								startActivity(in_o);
							}
						}
					}
				}.start();
			} else
				mHandler.sendEmptyMessage(0);
			break;
		case R.id.wb_detail_u_name:
			if (Utility.isConnected(this)) {

				new Thread() {
					@Override
					public void run() {
						Gson gson = new Gson();
						HashMap<String, String> map = new HashMap<String, String>();
						map = new HashMap<String, String>();
						map.put("app", "api");
						map.put("mod", "User");
						map.put("act", "show");
						map.put("user_id", weibo.getUid());
						map.put("oauth_token", account.getOauth_token());
						map.put("oauth_token_secret", account
								.getOauth_token_secret());
						String json = HttpUtility.getInstance()
								.executeNormalTask(HttpMethod.Get,
										HttpConstant.THINKSNS_URL, map);
						if (json != null && !"".equals(json)) {
							UserInfoBean userinfo = gson.fromJson(json,
									UserInfoBean.class);
							if (userinfo != null) {
								Intent in_o = new Intent(
										WeiboDetailActivity.this,
										OtherInfoActivity.class);
								in_o.putExtra("FLAG", (userinfo.getUid()
										.equals(account.getUid()) ? "0" : "1"));
								in_o.putExtra("userinfo", userinfo.getUid());
								in_o.putExtra("following",
										userinfo.follow_state.getFollowing()
												+ "");
								startActivity(in_o);
							}
						}
					}
				}.start();
			} else
				mHandler.sendEmptyMessage(0);
			break;
		case R.id.re_detail_user_name:
			break;
		case R.id.wb_detail_pic1:
			if (weibo.getAttach().get(0).getAttach_url() != null
					&& !weibo.getAttach().get(0).getAttach_url().equals("")) {
				Intent in_browse = new Intent(WeiboDetailActivity.this,
						BrowseImageActivity.class);
				in_browse.putExtra("url", weibo.getAttach().get(0)
						.getAttach_url());
				startActivity(in_browse);
			}
			break;
		case R.id.re_detail_wb_pic1:
			if (weibo.getTranspond_data().getAttach().get(0).getAttach_url() != null
					&& !weibo.getTranspond_data().getAttach().get(0)
							.getAttach_url().equals("")) {
				Intent in_r_browse = new Intent(WeiboDetailActivity.this,
						BrowseImageActivity.class);
				in_r_browse.putExtra("url", weibo.getTranspond_data()
						.getAttach().get(0).getAttach_url());
				startActivity(in_r_browse);
			}
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		FaceDialog.release();
	}

	private void getComments() {
		// TODO Auto-generated method stub
		if (Utility.isConnected(this)) {
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
							Log.d("wj", "comment list count" + comments.size());
							since_id = comments.get(0).getComment_id();
							totalCount += comments.size();
						}
						mHandler.sendEmptyMessage(13);
					}
				}
			}.start();
		} else {
			mHandler.sendEmptyMessage(11);
		}

	}

	public void loadMoreComments() {
		// TODO Auto-generated method stub
		loadMoreText.setText("正在加载，请稍候..");
		if (Utility.isConnected(this)) {
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
					mHandler.sendEmptyMessage(12);
				}
			}.start();
		} else {
			loadMoreText.setText("网络未连接，单击重试");
		}
	}

	private CommentListFragment newCommentListFragment() {
		CommentListFragment comments = new CommentListFragment();
		Bundle bundle = new Bundle();
		bundle.putParcelable("account", account);
		bundle.putParcelable("weibo", weibo);
		comments.setArguments(bundle);
		return comments;
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

	@Override
	public void onFaceSelect(SpannableString spannableString) {
		// TODO Auto-generated method stub
		comment_content.append(spannableString);

	}

}
