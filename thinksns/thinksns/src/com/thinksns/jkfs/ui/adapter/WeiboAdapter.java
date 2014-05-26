package com.thinksns.jkfs.ui.adapter;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thinksns.jkfs.R;
import com.thinksns.jkfs.bean.WeiboBean;
import com.thinksns.jkfs.ui.view.PullToRefreshListView;
import com.thinksns.jkfs.util.common.ImageUtils;
import com.thinksns.jkfs.util.common.ImageUtils.ImageCallback;

/**
 * 微博适配器，适用于需要显示微博列表的所有位置。
 * 
 * 使用：加载新微博时，调用adapter.append(list)或insertToHead(list)
 * 
 * @author wangjia
 * 
 */
public class WeiboAdapter extends BaseAdapter {
	LinkedList<WeiboBean> wList = new LinkedList<WeiboBean>();

	Activity ctx;
	PullToRefreshListView lv;
	LayoutInflater in;

	// 追加微博到表尾
	public void append(List<WeiboBean> lists) {
		if (lists == null) {
			return;
		}
		wList.addAll(lists);
		notifyDataSetChanged();
	}

	// 将新微博加到表头
	public void insertToHead(List<WeiboBean> lists) {
		if (lists == null) {
			return;
		}
		for (int i = lists.size() - 1; i >= 0; --i) {
			wList.addFirst(lists.get(i));
		}
		notifyDataSetChanged();
	}

	// 更新微博列表，不要原来的数据
	public void update(List<WeiboBean> lists) {
		if (lists == null) {
			return;
		}
		wList.clear();
		for (int i = lists.size() - 1; i >= 0; --i) {
			wList.addFirst(lists.get(i));
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

	public WeiboAdapter(Activity context, LayoutInflater inflater,
			PullToRefreshListView listView) {
		ctx = context;
		in = inflater;
		lv = listView;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return wList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return wList.get(position);
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
		final WeiboBean weibo = wList.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = in.inflate(R.layout.main_weibo_listview_item, null);
			holder.avatar = (ImageView) convertView
					.findViewById(R.id.wb_user_img);
			holder.userName = (TextView) convertView
					.findViewById(R.id.wb_u_name);
			holder.content = (TextView) convertView.findViewById(R.id.wb_text);
			holder.time = (TextView) convertView.findViewById(R.id.wb_time);
			// 待定，如果微博含多张图片呢？
			/*
			 * holder.weibo_pic = (ImageView) convertView
			 * .findViewById(R.id.wb_pic);
			 */
			holder.repost_userName = (TextView) convertView
					.findViewById(R.id.re_user_name);
			holder.repost_content = (TextView) convertView
					.findViewById(R.id.re_wb_text);
			holder.repost = (LinearLayout) convertView
					.findViewById(R.id.wb_repost);
			holder.from = (TextView) convertView.findViewById(R.id.wb_from);
			holder.like_count = (TextView) convertView
					.findViewById(R.id.wb_like_count);
			holder.repost_count = (TextView) convertView
					.findViewById(R.id.wb_repost_count);
			holder.comment_count = (TextView) convertView
					.findViewById(R.id.wb_comment_count);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		ImageUtils.setThumbnailView(weibo.getAvatar_small(), holder.avatar,
				ctx, callback);
		holder.userName.setText(weibo.getUname());
		holder.content.setText(weibo.getContent());
		holder.time.setText(weibo.getCtime());
		int where = Integer.parseInt(weibo.getFrom());
		switch (where) {
		case 0:
			holder.from.setText("来自 网页");
			break;
		case 1:
			holder.from.setText("来自 手机版");
			break;
		case 2:
			holder.from.setText("来自 iphone客户端");
			break;
		case 3:
			holder.from.setText("来自 Android客户端");
			break;
		case 4:
			holder.from.setText("来自 ipad客户端");
			break;
		}
		holder.like_count.setText("0");// 待定，API中暂未找到赞的数目
		holder.repost_content.setText(weibo.getRepost_count() + "");
		holder.comment_count.setText(weibo.getComment_count() + "");
		if (weibo.getType().equals("repost")) {
			WeiboBean weibo_repost = weibo.getTranspond_data();
			// 这一行报错,在insert刷新之后只有两个，空指针
			holder.repost_userName.setText(weibo_repost.getUname());
			holder.repost_content.setText(weibo_repost.getContent() + "");
			holder.repost.setVisibility(View.VISIBLE);
		}
		if (weibo.getType().equals("postimage")) {
			// 图片微博，待定..
		}

		return convertView;
	}

	class ViewHolder {
		public ImageView avatar;
		public TextView userName;
		public TextView content;
		public TextView time;
		// public ImageView weibo_pic;
		public TextView repost_userName;
		public TextView repost_content;
		// public ImageView repost_weibo_pic;
		public LinearLayout repost;
		public TextView from;
		public TextView like_count;
		public TextView repost_count;
		public TextView comment_count;
	}

}
