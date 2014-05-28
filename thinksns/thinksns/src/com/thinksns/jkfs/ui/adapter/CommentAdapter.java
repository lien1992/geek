package com.thinksns.jkfs.ui.adapter;

import java.util.LinkedList;
import java.util.List;

import com.thinksns.jkfs.R;
import com.thinksns.jkfs.bean.CommentBean;
import com.thinksns.jkfs.ui.view.PullToRefreshListView;
import com.thinksns.jkfs.util.common.ImageUtils;
import com.thinksns.jkfs.util.common.ImageUtils.ImageCallback;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
	PullToRefreshListView lv;
	LayoutInflater in;

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

	public CommentAdapter(Activity context, LayoutInflater inflater,
			PullToRefreshListView listView) {
		ctx = context;
		in = inflater;
		lv = listView;
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
		Log.d("comment adapter item is null?", (comment == null) + "");
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = in.inflate(R.layout.main_weibo_comment_listview_item,
					null);
			Log.d("in is null ?", (in == null) + "");
			holder.avatar = (ImageView) convertView
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
		Log.d("comment.getUser().getAvatar_small()", comment.getUser()
				.getAvatar_small());

		ImageUtils.setThumbnailView(comment.getUser().getAvatar_small(),
				holder.avatar, ctx, callback);
		holder.userName.setText(comment.getUser().getUname());
		holder.content.setText(comment.getContent());
		holder.time.setText(comment.getTime());

		return convertView;
	}

	static class ViewHolder {
		public ImageView avatar;
		public TextView userName;
		public TextView content;
		public TextView time;

	}

}