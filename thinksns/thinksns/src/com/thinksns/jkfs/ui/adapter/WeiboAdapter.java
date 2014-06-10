package com.thinksns.jkfs.ui.adapter;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.bean.WeiboBean;
import com.thinksns.jkfs.bean.WeiboRepostBean;
import com.thinksns.jkfs.constant.BaseConstant;
import com.thinksns.jkfs.ui.view.PullToRefreshListView;
import com.thinksns.jkfs.ui.view.RoundAngleImageView;

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

	private boolean isNoImageMode;

	private DisplayImageOptions options;

	// 追加微博到表尾
	public void append(List<WeiboBean> lists) {
		if (lists == null) {
			return;
		}
		wList.addAll(lists);
		Log.d("adapter all weibos", wList.size() + "");
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

	public void clear() {
		wList.clear();
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

	public WeiboAdapter(Activity context, LayoutInflater inflater,
			PullToRefreshListView listView) {
		ctx = context;
		in = inflater;
		lv = listView;
		options = new DisplayImageOptions.Builder().showStubImage(
				R.drawable.ic_launcher).cacheInMemory().cacheOnDisc().build();
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(ctx);
		isNoImageMode = prefs.getBoolean(BaseConstant.NO_IMAGE_MODE_KEY, false);

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
			holder.avatar = (RoundAngleImageView) convertView
					.findViewById(R.id.wb_user_img);
			holder.userName = (TextView) convertView
					.findViewById(R.id.wb_u_name);
			holder.content = (TextView) convertView.findViewById(R.id.wb_text);
			holder.time = (TextView) convertView.findViewById(R.id.wb_time);
			// 待添加多张图
			holder.weibo_pic = (ImageView) convertView
					.findViewById(R.id.wb_pic1);
			holder.weibo_pics = (LinearLayout) convertView
					.findViewById(R.id.wb_pics);
			holder.repost_userName = (TextView) convertView
					.findViewById(R.id.re_user_name);
			holder.repost_content = (TextView) convertView
					.findViewById(R.id.re_wb_text);
			// 待添加多张图
			holder.repost_weibo_pic = (ImageView) convertView
					.findViewById(R.id.re_wb_pic1);
			holder.repost_pics = (LinearLayout) convertView
					.findViewById(R.id.re_pics);
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
		ImageLoader.getInstance().displayImage(weibo.getAvatar_small(),
				holder.avatar, options);
		holder.userName.setText(weibo.getUname());
		if (!TextUtils.isEmpty(weibo.getContent()))
			holder.content.setText(weibo.getListViewSpannableString());
		else
			holder.content.setVisibility(View.GONE);
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
			holder.from.setText("来自 iPhone客户端");
			break;
		case 3:
			holder.from.setText("来自 Android客户端");
			break;
		case 4:
			holder.from.setText("来自 ipad客户端");
			break;
		}
		holder.like_count.setText(weibo.getDigg_count() + "");
		holder.repost_content.setText(weibo.getRepost_count() + "");
		holder.comment_count.setText(weibo.getComment_count() + "");

		if (weibo.getType().equals("repost")) {
			holder.weibo_pics.setVisibility(View.GONE);
			WeiboRepostBean weibo_repost = weibo.getTranspond_data();
			holder.repost_userName.setText(weibo_repost.getUname());
			if (!TextUtils.isEmpty(weibo_repost.getContent()))
				holder.repost_content.setText(weibo_repost
						.getListViewSpannableString());
			else
				holder.repost_content.setVisibility(View.GONE);
			if (weibo_repost.getType().equals("postimage")) {
				if (!isNoImageMode) {
					ImageLoader.getInstance().displayImage(
							weibo_repost.getAttach().get(0).getAttach_middle(),
							holder.repost_weibo_pic, options);
					holder.repost_pics.setVisibility(View.VISIBLE);
				}
			} else {
				holder.repost_pics.setVisibility(View.GONE);
			}
			holder.repost.setVisibility(View.VISIBLE);
		} else if (weibo.getType().equals("weiba_repost")) {
			// 转发微吧

		} else {
			if (weibo.getType().equals("postimage")) {
				if (!isNoImageMode && weibo.getAttach().size() > 0) {
					ImageLoader.getInstance().displayImage(
							weibo.getAttach().get(0).getAttach_middle(),
							holder.weibo_pic, options);
					holder.weibo_pics.setVisibility(View.VISIBLE);
				}
			} else {
				holder.weibo_pics.setVisibility(View.GONE);
			}
			holder.repost.setVisibility(View.GONE);
			holder.repost_pics.setVisibility(View.GONE);
		}
		return convertView;
	}

	static class ViewHolder {
		public RoundAngleImageView avatar;
		public TextView userName;
		public TextView content;
		public TextView time;
		public ImageView weibo_pic;
		public LinearLayout weibo_pics;
		public TextView repost_userName;
		public TextView repost_content;
		public ImageView repost_weibo_pic;
		public LinearLayout repost;
		public LinearLayout repost_pics;
		public TextView from;
		public TextView like_count;
		public TextView repost_count;
		public TextView comment_count;
	}

}
