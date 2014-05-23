package com.thinksns.jkfs.ui.fragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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

/**
 * 微博列表，待完善..
 * 
 * @author wangjia
 * 
 */
public class WeiboListFragment extends BaseFragment {

	private ThinkSNSApplication application;
	private WeiboListBean weiboList = new WeiboListBean();
	private List<WeiboBean> weibos = new ArrayList<WeiboBean>();
	private WeiboAdapter adapter;
	private AccountBean account;
    private LayoutInflater mInflater;
    private PullToRefreshListView listView;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				adapter.append(weibos);
				break;
			case 1:
				//
				break;
			case 2:
				Toast.makeText(getActivity(), "网络未连接", Toast.LENGTH_SHORT)
						.show();
				listView.onRefreshComplete();
			}

		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);

		View view = mInflater.inflate(R.layout.main_weibo_list_fragment, null);
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
		if (Utility.isConnected(getActivity())) {
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
					map.put("oauth_token_secret", account
							.getOauth_token_secret());
					String json = HttpUtility.getInstance().executeNormalTask(
							HttpMethod.Get, HttpConstant.THINKSNS_URL, map);
					Type listType = new TypeToken<ArrayList<WeiboBean>>() {
					}.getType();
					weibos = gson.fromJson(json, listType);
					mHandler.sendEmptyMessage(0);
				}
			}.start();
		} else {
			mHandler.sendEmptyMessage(2);
		}

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

			/*
			 * Bitmap avatar = Utility.getBitmap(weibo.getAvatar_small());
			 * holder.avatar.setImageBitmap(avatar);
			 * 
			 * if(weibo.getType().equals("postimage")){ Bitmap wb_pic =
			 * Utility.getBitmap(weibo.getAttach().getUrl()); }
			 */
			// 在handler中更新
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
