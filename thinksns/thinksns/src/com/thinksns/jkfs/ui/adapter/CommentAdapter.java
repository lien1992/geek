package com.thinksns.jkfs.ui.adapter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.ThinkSNSApplication;
import com.thinksns.jkfs.bean.CommentBean;
import com.thinksns.jkfs.bean.UserInfoBean;
import com.thinksns.jkfs.constant.HttpConstant;
import com.thinksns.jkfs.ui.OtherInfoActivity;
import com.thinksns.jkfs.ui.WeiboDetailActivity;
import com.thinksns.jkfs.ui.view.RoundAngleImageView;
import com.thinksns.jkfs.util.Utility;
import com.thinksns.jkfs.util.http.HttpMethod;
import com.thinksns.jkfs.util.http.HttpUtility;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 微博评论适配器，适配微博详情底部的评论列表
 * 
 * @author wangjia
 * 
 */
public class CommentAdapter extends BaseAdapter {
	LinkedList<CommentBean> cList = new LinkedList<CommentBean>();

	Activity ctx;
	LayoutInflater in;
	Handler handler;

	private DisplayImageOptions options;

	public void append(List<CommentBean> lists) {
		if (lists == null) {
			return;
		}
		cList.addAll(lists);
		notifyDataSetChanged();
	}

	public void insertToHead(List<CommentBean> lists) {
		if (lists == null) {
			return;
		}
		for (int i = lists.size() - 1; i >= 0; --i) {
			cList.addFirst(lists.get(i));
		}
		notifyDataSetChanged();
	}

	public void clear() {
		cList.clear();
		// notifyDataSetChanged();
	}

	public CommentAdapter(Activity context, LayoutInflater inflater, Handler h) {
		ctx = context;
		in = inflater;
		handler = h;

		options = new DisplayImageOptions.Builder().showStubImage(
				R.drawable.ic_launcher).cacheInMemory().cacheOnDisc().build();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return cList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return cList.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		final CommentBean comment = cList.get(position);
		Log.d("wj", "comment adapter item is null?" + (comment == null));
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = in.inflate(R.layout.main_weibo_comment_listview_item,
					null);
			holder.avatar = (RoundAngleImageView) convertView
					.findViewById(R.id.wb_cmt_user_img);
			holder.userName = (TextView) convertView
					.findViewById(R.id.wb_cmt_u_name);
			holder.content = (TextView) convertView
					.findViewById(R.id.wb_cmt_text);
			holder.time = (TextView) convertView.findViewById(R.id.wb_cmt_time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Log.d("comment.getUser().getAvatar_small()", comment.getUser_info()
				.getAvatar_small());

		ImageLoader.getInstance().displayImage(
				comment.getUser_info().getAvatar_small(), holder.avatar,
				options);
		holder.avatar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (Utility.isConnected(ctx)) {

					new Thread() {
						@Override
						public void run() {
							Gson gson = new Gson();
							HashMap<String, String> map = new HashMap<String, String>();
							map = new HashMap<String, String>();
							map.put("app", "api");
							map.put("mod", "User");
							map.put("act", "show");
							map.put("user_id", comment.getUid());
							map.put("oauth_token", ThinkSNSApplication
									.getInstance().getOauth_token(ctx));
							map.put("oauth_token_secret", ThinkSNSApplication
									.getInstance().getOauth_token_secret(ctx));
							String json = HttpUtility.getInstance()
									.executeNormalTask(HttpMethod.Get,
											HttpConstant.THINKSNS_URL, map);
							if (json != null && !"".equals(json)) {
								UserInfoBean userinfo = gson.fromJson(json,
										UserInfoBean.class);
								if (userinfo != null) {
									Intent in_o = new Intent(ctx,
											OtherInfoActivity.class);
									in_o.putExtra("FLAG", userinfo.getUid()
											.equals(
													ThinkSNSApplication
															.getInstance()
															.getAccount(ctx)
															.getUid()) ? 0 : 1);
									in_o
											.putExtra("userinfo", userinfo
													.getUid());
									in_o.putExtra("following",
											userinfo.follow_state
													.getFollowing()
													+ "");
									ctx.startActivity(in_o);
								}
							}
						}
					}.start();
				} else
					handler.sendEmptyMessage(0);
			}

		});
		holder.userName.setText(comment.getUser_info().getUname());
		holder.userName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (Utility.isConnected(ctx)) {

					new Thread() {
						@Override
						public void run() {
							Gson gson = new Gson();
							HashMap<String, String> map = new HashMap<String, String>();
							map = new HashMap<String, String>();
							map.put("app", "api");
							map.put("mod", "User");
							map.put("act", "show");
							map.put("user_id", comment.getUid());
							map.put("oauth_token", ThinkSNSApplication
									.getInstance().getOauth_token(ctx));
							map.put("oauth_token_secret", ThinkSNSApplication
									.getInstance().getOauth_token_secret(ctx));
							String json = HttpUtility.getInstance()
									.executeNormalTask(HttpMethod.Get,
											HttpConstant.THINKSNS_URL, map);
							if (json != null && !"".equals(json)) {
								UserInfoBean userinfo = gson.fromJson(json,
										UserInfoBean.class);
								if (userinfo != null) {
									Intent in_o = new Intent(ctx,
											OtherInfoActivity.class);
									in_o.putExtra("FLAG", userinfo.getUid()
											.equals(
													ThinkSNSApplication
															.getInstance()
															.getAccount(ctx)
															.getUid()) ? 0 : 1);
									in_o
											.putExtra("userinfo", userinfo
													.getUid());
									in_o.putExtra("following",
											userinfo.follow_state
													.getFollowing()
													+ "");
									ctx.startActivity(in_o);
								}
							}
						}
					}.start();
				} else
					handler.sendEmptyMessage(0);
			}
		});
		holder.content.setText(comment.getListViewSpannableString());
		holder.time.setText(comment.getCtime());

		return convertView;
	}

	static class ViewHolder {
		public RoundAngleImageView avatar;
		public TextView userName;
		public TextView content;
		public TextView time;

	}

}
