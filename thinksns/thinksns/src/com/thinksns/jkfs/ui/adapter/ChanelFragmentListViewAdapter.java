package com.thinksns.jkfs.ui.adapter;

import java.util.ArrayList;
import java.util.zip.Inflater;

import com.thinksns.jkfs.R;
import com.thinksns.jkfs.bean.WeiboBean;
import com.thinksns.jkfs.bean.WeiboListBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author zcc
 * @date 2014-05-19
 * 
 */
public class ChanelFragmentListViewAdapter extends BaseAdapter {
	ArrayList<WeiboBean> mlist;
	LayoutInflater mInflater;

	public ChanelFragmentListViewAdapter(ArrayList<WeiboBean> list,
			LayoutInflater inflater) {

		mlist = list;
		mInflater = inflater;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mlist.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.main_weibo_listview_item,
					null);
			viewHolder = new ViewHolder();
			viewHolder.nameTextView = (TextView) convertView
					.findViewById(R.id.textView1);
			viewHolder.weiboTextView = (TextView) convertView
					.findViewById(R.id.textView2);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.nameTextView.setText(mlist.get(position).getUname());
		viewHolder.weiboTextView.setText(mlist.get(position).getContent());
		return convertView;
	}

	class ViewHolder {
		TextView nameTextView;
		TextView weiboTextView;
	}

}
