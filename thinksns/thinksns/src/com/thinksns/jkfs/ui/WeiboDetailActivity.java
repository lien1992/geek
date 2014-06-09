package com.thinksns.jkfs.ui;

import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.ThinkSNSApplication;
import com.thinksns.jkfs.bean.AccountBean;
import com.thinksns.jkfs.bean.UserInfoBean;
import com.thinksns.jkfs.bean.WeiboAttachBean;
import com.thinksns.jkfs.bean.WeiboBean;
import com.thinksns.jkfs.bean.WeiboRepostBean;
import com.thinksns.jkfs.constant.BaseConstant;
import com.thinksns.jkfs.constant.HttpConstant;
import com.thinksns.jkfs.ui.fragment.CommentListFragment;
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
public class WeiboDetailActivity extends FragmentActivity implements
		OnClickListener, FaceSelect {
	private ImageView back;
	private EditText comment_content;
	private ImageView repost;
	private ImageView favorite;
	private ImageView emotion;
	private ImageView send;
	private ImageView like;
	private ImageView comments;
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
	private RelativeLayout bottom;
	private List<WeiboAttachBean> attaches;
	private AccountBean account;
	private ThinkSNSApplication application;
	private WeiboBean weibo;
	private boolean isFavorite;
	private boolean isLike;
	private ProgressDialog sendProgress;

	private DisplayImageOptions options;

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
				WeiboDetailActivity.this.getSupportFragmentManager()
						.beginTransaction().replace(
								R.id.wb_detail_comment_layout,
								newCommentListFragment()).commit();
				break;
			case 2:
				sendDialogDismiss();
				Toast.makeText(WeiboDetailActivity.this, "出现意外，收藏微博失败:(",
						Toast.LENGTH_SHORT).show();
				break;
			case 3:
				Toast.makeText(WeiboDetailActivity.this, "收藏微博成功",
						Toast.LENGTH_SHORT).show();
				break;
			case 4:
				Toast.makeText(WeiboDetailActivity.this, "出现意外，收藏微博失败:(",
						Toast.LENGTH_SHORT).show();
				break;
			case 5:
				Toast.makeText(WeiboDetailActivity.this, "取消收藏成功",
						Toast.LENGTH_SHORT).show();
				break;
			case 6:
				Toast.makeText(WeiboDetailActivity.this, "出现意外，取消收藏失败:(",
						Toast.LENGTH_SHORT).show();
				break;
			case 7:
				int like = Integer.parseInt(like_count.getText().toString());
				like_count.setText((++like) + "");
				break;
			case 8:
				Toast.makeText(WeiboDetailActivity.this, "出现意外，赞失败了:(",
						Toast.LENGTH_SHORT).show();
				break;
			case 9:
				int dislike = Integer.parseInt(like_count.getText().toString());
				like_count.setText((--dislike) + "");
				break;
			case 10:
				Toast.makeText(WeiboDetailActivity.this, "出现意外，取消赞失败了:(",
						Toast.LENGTH_SHORT).show();
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
		from = (TextView) findViewById(R.id.wb_detail_from);
		time = (TextView) findViewById(R.id.wb_detail_time);
		content = (TextView) findViewById(R.id.wb_detail_text);
		pic = (ImageView) findViewById(R.id.wb_detail_pic1);
		repost_layout = (LinearLayout) findViewById(R.id.wb_detail_repost_layout);
		repost_pic = (ImageView) findViewById(R.id.re_detail_wb_pic1);
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
		comments = (ImageView) findViewById(R.id.wb_detail_comment_list);
		comments.setOnClickListener(this);
		repost_another = (ImageView) findViewById(R.id.wb_detail_repost_another);
		repost_another.setOnClickListener(this);
		comment_content = (EditText) findViewById(R.id.wb_detail_edit_comment);
		bottom = (RelativeLayout) findViewById(R.id.wb_detail_bottom_layout);
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
		}
		like_count.setText(weibo.getDigg_count() + "");
		repost_count.setText(weibo.getRepost_count() + "");
		comment_count.setText(weibo.getComment_count() + "");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		this.getSupportFragmentManager().beginTransaction().replace(
				R.id.wb_detail_comment_layout, newCommentListFragment())
				.commit();
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
							Log.d("send weibo return what?", result);
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
			this.getSupportFragmentManager().beginTransaction().replace(
					R.id.wb_detail_comment_layout, newCommentListFragment())
					.commit();
			break;
		case R.id.wb_detail_repost_another:
			Intent in_r = new Intent(WeiboDetailActivity.this,
					RepostActivity.class);
			in_r.putExtra("repost", weibo);
			startActivity(in_r);
			FaceDialog.release();
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
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		FaceDialog.release();
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
