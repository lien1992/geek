package com.thinksns.jkfs.ui.adapter;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thinksns.jkfs.R;
import com.thinksns.jkfs.bean.UserFollowBean;
import com.thinksns.jkfs.bean.UserInfoBean;
import com.thinksns.jkfs.bean.UserFollowBean;

import com.thinksns.jkfs.ui.view.PullToRefreshListView;
import com.thinksns.jkfs.util.common.ImageUtils;
import com.thinksns.jkfs.util.common.ImageUtils.ImageCallback;



/**
 * @author 邓思宇
 * 关注或者粉丝列表的Adapter
 *
 *	抄袭userfollowadapter
 *
 */
public class PeopleAdapter extends BaseAdapter {
	
	LinkedList<UserFollowBean> uList = new LinkedList<UserFollowBean>();

	Activity ctx;
	PullToRefreshListView lv;
	LayoutInflater in;

	// 追加到表尾
	public void append(List<UserFollowBean> lists) {
		if (lists == null) {
			return;
		}
		uList.addAll(lists);
		notifyDataSetChanged();
	}

	// 将新微博加到表头
	public void insertToHead(List<UserFollowBean> lists) {
		if (lists == null) {
			return;
		}
		for (int i = lists.size() - 1; i >= 0; --i) {
			uList.addFirst(lists.get(i));
		}
		notifyDataSetChanged();
	}

	// 更新微博列表，不要原来的数据
	public void update(List<UserFollowBean> lists) {
		if (lists == null) {
			return;
		}
		uList.clear();
		for (int i = lists.size() - 1; i >= 0; --i) {
			uList.addFirst(lists.get(i));
		}
		notifyDataSetChanged();
	}

	// 图片加载回调
	ImageCallback callback = new ImageCallback() {
		@Override
		public void loadImage(Bitmap bitmap, String imagePath) {
			// TODO Auto-generated method stub
			try {
				ImageView img = (ImageView) lv.findViewWithTag(imagePath);
				img.setImageBitmap(bitmap);
			} catch (NullPointerException ex) {
				Log.e("error", "ImageView = null");
			}
		}
	};

	public PeopleAdapter(Activity context, LayoutInflater inflater,
			PullToRefreshListView listView) {
		ctx = context;
		in = inflater;
		lv = listView;

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
		// TODO Auto-generated method stub
		final ViewHolder holder;
		final UserFollowBean userfollow = uList.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = in.inflate(R.layout.people_item, null);
			holder.avatar = (ImageView) convertView
					.findViewById(R.id.people_item_head);
			holder.userName = (TextView) convertView
					.findViewById(R.id.people_item_name);
			holder.userText= (TextView) convertView
					.findViewById(R.id.people_item_weibo);
			holder.button = (Button) convertView
					.findViewById(R.id.people_item_button);


			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		ImageUtils.setThumbnailView(userfollow.getAvatar_small(), holder.avatar,
				ctx, callback);
		holder.userName.setText(userfollow.getUname());
		holder.userText.setText("follower = "+userfollow.follow_state.getFollower()+" following = "+userfollow.follow_state.getFollowing());

		return convertView;
	}

	class ViewHolder {
		public ImageView avatar;
		public TextView userName;
		public TextView userText;
		public Button button;
	
	}

}
