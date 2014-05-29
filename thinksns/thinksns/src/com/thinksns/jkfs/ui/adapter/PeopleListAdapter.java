package com.thinksns.jkfs.ui.adapter;


import java.util.LinkedList;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.bean.UserFollowBean;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PeopleListAdapter extends BaseAdapter {

	private LinkedList<UserFollowBean> uList;
	private LayoutInflater inflater;

	public PeopleListAdapter() {
	}
	
	public PeopleListAdapter(Context context, LinkedList<UserFollowBean> ulist) {
		this.uList = ulist;
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
			holder.button = (Button) convertView
					.findViewById(R.id.people_item_button);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
        ImageLoader.getInstance().displayImage(userfollow.getAvatar_small(),holder.avatar);

		holder.userName.setText(userfollow.getUname());
		holder.userText.setText("follower = "
				+ userfollow.follow_state.getFollower() + " following = "
				+ userfollow.follow_state.getFollowing());

		return convertView;
	}

	class ViewHolder {
		public ImageView avatar;
		public TextView userName;
		public TextView userText;
		public Button button;

	}

	
	
	 /**
	 * 添加列表项
	 *
	 * @param item
	 */
//	 public void addItem(UserFollowBean item) {
//		 uList.add(item);
//	 }


}
