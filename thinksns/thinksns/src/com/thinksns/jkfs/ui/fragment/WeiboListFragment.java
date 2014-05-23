package com.thinksns.jkfs.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.BaseFragment;
import com.thinksns.jkfs.base.ThinkSNSApplication;
import com.thinksns.jkfs.bean.AccountBean;
import com.thinksns.jkfs.bean.WeiboBean;
import com.thinksns.jkfs.bean.WeiboListBean;

public class WeiboListFragment extends BaseFragment{
	
	private ThinkSNSApplication application;
	private WeiboListBean weiboList = new WeiboListBean();
	private WeiboAdapter adapter;
	private AccountBean account;
    private LayoutInflater mInflater;
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
/*		application = (ThinkSNSApplication) this.getActivity()
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
		});*/
        mInflater=inflater;
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		
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
				holder.userName = (TextView) convertView
						.findViewById(R.id.wb_u_name);
				holder.content = (TextView) convertView
						.findViewById(R.id.wb_text);

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
