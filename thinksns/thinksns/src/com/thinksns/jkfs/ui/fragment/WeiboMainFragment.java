package com.thinksns.jkfs.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.BaseFragment;
import com.thinksns.jkfs.base.ThinkSNSApplication;
import com.thinksns.jkfs.bean.AccountBean;
import com.thinksns.jkfs.bean.WeiboBean;
import com.thinksns.jkfs.bean.WeiboListBean;
import com.thinksns.jkfs.util.http.HttpMethod;
import com.thinksns.jkfs.util.http.HttpUtility;

/**
 * 主微博Fragment (just test..)
 */
public class WeiboMainFragment extends BaseFragment {
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

				// 查看微博..

			}
		});
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub

		// 加载更多..
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub

		// 判断网络已连接..

		new Thread() {

			@Override
			public void run() {
				Map<String, String> map = new HashMap<String, String>();
				map.put("app", "api");
				map.put("mod", "User");
				map.put("act", "show");
				map.put("oauth_token", account.getOauth_token());
				map.put("oauth_token_secret", account.getOauth_token_secret());
				map.put("user_id", account.getUid());
				map.put("since_id", "");
				map.put("max_id", "");
				map.put("count", "");
				map.put("page", "");
				String json = HttpUtility.getInstance().executeNormalTask(
						HttpMethod.Get, "http://demo.thinksns.com/t3/", map);
				weiboList = new Gson().fromJson(json, WeiboListBean.class);
				mHandler.sendEmptyMessage(0);

				// 缓存微博进数据库..

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
			ViewHolder holder;
			WeiboBean weibo = wList.get(position);
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(
						R.layout.main_weibo_listview_item, null);
//				holder.userName = (TextView) convertView
//						.findViewById(R.id.textView1);
//				holder.content = (TextView) convertView
//						.findViewById(R.id.textView2);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.userName.setText(weibo.getUname());
			holder.content.setText(weibo.getContent());

			return convertView;
		}

	}

	class ViewHolder {
		public TextView userName;
		public TextView content;
		// 其他View..
	}
}
