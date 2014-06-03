package com.thinksns.jkfs.ui.adapter;

import java.util.HashMap;
import java.util.LinkedList;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.bean.AccountBean;
import com.thinksns.jkfs.bean.UserFollowBean;
import com.thinksns.jkfs.constant.HttpConstant;
import com.thinksns.jkfs.ui.UserInfoFollowList;
import com.thinksns.jkfs.util.http.HttpMethod;
import com.thinksns.jkfs.util.http.HttpUtility;

/**
 * @author 邓思宇 关注人或粉丝列表的ADAPTER
 * 
 *         还未添加TAG
 * 
 */
public class PeopleListAdapter extends BaseAdapter {

	private LinkedList<UserFollowBean> uList;
	private LayoutInflater inflater;
	private AccountBean account;
	private String FOLLOW_DESTROY = "follow_destroy";
	private String FOLLOW_CREATE = "follow_create";

	public PeopleListAdapter(Context context, LinkedList<UserFollowBean> ulist,
			AccountBean account) {
		if (this.uList != null) {
			this.uList.clear();
		}
		this.uList = ulist;
		this.account = account;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return uList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return uList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	
	public UserFollowBean getUser(int position){
		
		UserFollowBean userfollow = uList.get(position);
		
		return userfollow;
		
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final ViewHolder holder;
		final UserFollowBean userfollow = uList.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.people_item, null);
			holder.avatar = (ImageView) convertView
					.findViewById(R.id.people_item_head);

			holder.userName = (TextView) convertView
					.findViewById(R.id.people_item_name);
			holder.userText = (TextView) convertView
					.findViewById(R.id.people_item_weibo);
			holder.userFo = (TextView) convertView
					.findViewById(R.id.people_item_fo);
			holder.button = (Button) convertView
					.findViewById(R.id.people_item_button);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		ImageLoader.getInstance().displayImage(userfollow.getAvatar_small(),
				holder.avatar);

		holder.userName.setText(userfollow.getUname());

		holder.userText.setText(userfollow.getUid());
		holder.userFo.setText("" + userfollow.follow_state.getFollowing());
		
		
		if (userfollow.follow_state.getFollowing() == 1) {
			holder.button.setText("取消关注");
		} else {
			holder.button.setText("关注");
		}

		holder.button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (userfollow.follow_state.getFollowing() == 1) {

					followif(userfollow.getUid().toString(), FOLLOW_DESTROY); // 点击取消关注

					userfollow.follow_state.setFollowing(0);

					holder.button.setText("关注");

				} else {

					followif(userfollow.getUid().toString(), FOLLOW_CREATE); // 点击再次关注

					userfollow.follow_state.setFollowing(1);

					holder.button.setText("取消关注");

				}

			}
		});

		return convertView;
	}

	class ViewHolder {
		public ImageView avatar;
		public TextView userName;
		public TextView userText;
		public TextView userFo;
		public Button button;

	}

	/**
	 * 添加列表项
	 * 
	 * @param item
	 */
	public void addItem(UserFollowBean item) {
		uList.add(item);
	}

	// 点击按钮 取消关注或再次关注
	private void followif(final String uid, final String act) {

		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub

				Gson gson = new Gson();
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("app", "api");
				map.put("mod", "User");
				map.put("act", act);
				map.put("oauth_token", account.getOauth_token());
				map.put("oauth_token_secret", account.getOauth_token_secret());
				map.put("user_id", uid);
				String json = HttpUtility.getInstance().executeNormalTask(
						HttpMethod.Post, HttpConstant.THINKSNS_URL, map);

			}
		}.start();

	}

}
