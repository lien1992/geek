package com.thinksns.jkfs.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.BaseFragment;
import com.thinksns.jkfs.base.ThinkSNSApplication;
import com.thinksns.jkfs.bean.AccountBean;
import com.thinksns.jkfs.bean.WeiboBean;
import com.thinksns.jkfs.bean.WeiboListBean;
import com.thinksns.jkfs.constant.HttpConstant;
import com.thinksns.jkfs.ui.view.PullToRefreshListView;
import com.thinksns.jkfs.util.Utility;
import com.thinksns.jkfs.util.http.HttpMethod;
import com.thinksns.jkfs.util.http.HttpUtility;

public class WeiboListFragment extends BaseFragment {

	private ThinkSNSApplication application;
	private WeiboListBean weiboList = new WeiboListBean();
	private WeiboAdapter adapter;
	private AccountBean account;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				adapter.append(weiboList.getWeibos());
				break;
			}
			// case 1 ..
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);

		view = mInflater.inflate(R.layout.main_weibo_list_fragment, null);
		listView = (PullToRefreshListView) view
				.findViewById(R.id.main_weibo_list_view);

		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		application = (ThinkSNSApplication) this.getActivity()
				.getApplicationContext();
		account = application.getAccount(this.getActivity());

		listView.setListener(this);
		adapter = new WeiboAdapter();
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Gson gson = new Gson();
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("app", "api");
				map.put("mod", "WeiboStatuses");
				map.put("act", "public_timeline");
				// map.put("max_id", "");
				map.put("oauth_token", account.getOauth_token());
				map.put("oauth_token_secret", account.getOauth_token_secret());
				String json = HttpUtility.getInstance().executeNormalTask(
						HttpMethod.Get, HttpConstant.THINKSNS_URL, map);
				weiboList = gson.fromJson(json, WeiboListBean.class);
				mHandler.sendEmptyMessage(0);
			}
		}.start();

	}

	class WeiboAdapter extends BaseAdapter {
		List<WeiboBean> wList = new ArrayList<WeiboBean>();

		public void append(List<WeiboBean> lists) {
			if (lists == null) {
				return;
			}
			wList.addAll(lists);
			notifyDataSetChanged();
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
				convertView = mInflater.inflate(
						R.layout.main_weibo_listview_item, null);
				holder.avatar = (ImageView) convertView
						.findViewById(R.id.wb_user_img);
				holder.userName = (TextView) convertView
						.findViewById(R.id.wb_u_name);
				holder.content = (TextView) convertView
						.findViewById(R.id.wb_text);
				holder.time = (TextView) convertView.findViewById(R.id.wb_time);
				holder.weibo_pic = (ImageView) convertView
						.findViewById(R.id.wb_pic);
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
			new Thread() {
				@Override
				public void run() {
					Bitmap avatar = Utility.getBitmap(weibo.getAvatar_small());
					holder.avatar.setImageBitmap(avatar);
/*					if(weibo.getType().equals("postimage")){
						Bitmap wb_pic = Utility.getBitmap(weibo.getAttach().getUrl());
					}*/

				}
			}.start();

			holder.userName.setText(weibo.getUname());
			holder.content.setText(weibo.getContent());
			holder.time.setText(weibo.getCtime());
			holder.from.setText(weibo.getFrom());
			holder.like_count.setText("0");
			holder.repost_content.setText(weibo.getRepost_count());
			holder.comment_count.setText(weibo.getComment_count());
			if (weibo.getType().equals("repost")) {
				WeiboBean weibo_repost = weibo.getTranspond_data();
				holder.repost_userName.setText(weibo_repost.getUname());
				holder.repost_content.setText(weibo_repost.getContent());
				holder.repost.setVisibility(View.VISIBLE);
			}

			return convertView;
		}

	}

	class ViewHolder {
		public ImageView avatar;
		public TextView userName;
		public TextView content;
		public TextView time;
		public ImageView weibo_pic;
		public TextView repost_userName;
		public TextView repost_content;
		public LinearLayout repost;
		public TextView from;
		public TextView like_count;
		public TextView repost_count;
		public TextView comment_count;
	}

}
